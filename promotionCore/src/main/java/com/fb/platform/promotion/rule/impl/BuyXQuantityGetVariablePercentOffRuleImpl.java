/**
 * 
 */
package com.fb.platform.promotion.rule.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.PlatformException;
import com.fb.commons.to.Money;
import com.fb.platform.promotion.model.OrderDiscount;
import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.RuleConfigDescriptorEnum;
import com.fb.platform.promotion.rule.RuleConfigDescriptorItem;
import com.fb.platform.promotion.rule.RuleConfiguration;
import com.fb.platform.promotion.to.OrderItem;
import com.fb.platform.promotion.to.OrderRequest;
import com.fb.platform.promotion.to.PromotionStatusEnum;
import com.fb.platform.promotion.util.ListUtil;
import com.fb.platform.promotion.util.StringToIntegerList;

/**
 * @author keith
 *
 */
public class BuyXQuantityGetVariablePercentOffRuleImpl implements PromotionRule, Serializable {

	private static transient Log log = LogFactory.getLog(BuyXQuantityGetVariablePercentOffRuleImpl.class);

	private Money minOrderValue = null;
	private Money maxDiscountPerUse = null;
	private List<Integer> clientList = null;
	private List<Integer> includeCategoryList = null;
	private List<Integer> excludeCategoryList = null;
	private List<Integer> brands;
	private QuantityDiscountMapper quantityDiscountMap = null;	// "1=5,2=15,3=20"
	
	@Override
	public void init(RuleConfiguration ruleConfig) {

		quantityDiscountMap = new QuantityDiscountMapper(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.VARIABLE_DISCOUNT_PERCENTAGE.name()));
		if (ruleConfig.isConfigItemPresent(RuleConfigDescriptorEnum.MAX_DISCOUNT_CEIL_IN_VALUE.name())) {
			maxDiscountPerUse = new Money (BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.MAX_DISCOUNT_CEIL_IN_VALUE.name()))));
		}

		if (ruleConfig.isConfigItemPresent(RuleConfigDescriptorEnum.CATEGORY_INCLUDE_LIST.name())) {
			StrTokenizer strTokCategories = new StrTokenizer(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.CATEGORY_INCLUDE_LIST.name()),",");
			includeCategoryList = StringToIntegerList.convert((List<String>)strTokCategories.getTokenList());
			log.info("includeCategoryList = "+ includeCategoryList);
		}
		
		if (ruleConfig.isConfigItemPresent(RuleConfigDescriptorEnum.CATEGORY_EXCLUDE_LIST.name())) {
			StrTokenizer strTokCategories = new StrTokenizer(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.CATEGORY_EXCLUDE_LIST.name()),",");
			excludeCategoryList = StringToIntegerList.convert((List<String>)strTokCategories.getTokenList());
			log.info("excludeCategoryList = "+ excludeCategoryList);
		}
		
		if (ruleConfig.isConfigItemPresent(RuleConfigDescriptorEnum.CLIENT_LIST.name())) {
			StrTokenizer strTokClients = new StrTokenizer(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.CLIENT_LIST.name()),",");
			clientList = StringToIntegerList.convert((List<String>)strTokClients.getTokenList());
			log.info("clientList = "+ clientList);
		}
		
		if (ruleConfig.isConfigItemPresent(RuleConfigDescriptorEnum.MIN_ORDER_VALUE.name())) {
			minOrderValue = new Money(BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.MIN_ORDER_VALUE.name()))));
			log.info("minOrderValue : " + minOrderValue.toString());
		}
		else {
			log.warn("Minimum Order Value not specified for this rule");
		}
		
		if (ruleConfig.isConfigItemPresent(RuleConfigDescriptorEnum.BRAND_LIST.name())) {
			StrTokenizer strTokCategories = new StrTokenizer(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.BRAND_LIST.name()),",");
			brands = StringToIntegerList.convert((List<String>)strTokCategories.getTokenList());
			log.info("brandsList = "+ brands);
		}
	}

	@Override
	public PromotionStatusEnum isApplicable(OrderRequest request,int userId,boolean isCouponCommitted) {
		if (log.isDebugEnabled()) {
			log.debug("Checking if BuyXQuantityGetVariablePercentOffRuleImpl applies on order : " + request.getOrderId());
		}
		if (ListUtil.isValidList(clientList) && !request.isValidClient(clientList)) {
			return PromotionStatusEnum.INVALID_CLIENT;
		}
		if(ListUtil.isValidList(includeCategoryList) && !request.isAnyProductInCategory(includeCategoryList)) {
			return PromotionStatusEnum.CATEGORY_MISMATCH;
		}
		if(ListUtil.isValidList(excludeCategoryList) && request.isAnyProductInCategory(excludeCategoryList)){
			return PromotionStatusEnum.CATEGORY_MISMATCH;
		}
		if(ListUtil.isValidList(brands) && !request.isAnyProductInBrand(brands)){
			return PromotionStatusEnum.BRAND_MISMATCH;
		}
		Money orderValue = request.getOrderValueForRelevantProducts(brands, includeCategoryList, excludeCategoryList);
		if(minOrderValue != null && orderValue.lt(minOrderValue)) {
			return PromotionStatusEnum.LESS_ORDER_AMOUNT;
		}
		int relevantProductQuantity = request.getOrderQuantityForRelevantProducts(brands, includeCategoryList, excludeCategoryList);
		if(!quantityDiscountMap.isQuantityApplicable(relevantProductQuantity)) {
			return PromotionStatusEnum.NOT_APPLICABLE;
		}
		return PromotionStatusEnum.SUCCESS;
	}

	@Override
	public OrderDiscount execute(OrderDiscount orderDiscount) {
		OrderRequest request = orderDiscount.getOrderRequest();
		if(log.isDebugEnabled()) {
			log.debug("Executing BuyXQuantityGetVariablePercentOffRuleImpl on order : " + request.getOrderId());
		}
		int relevantProductQuantity = request.getOrderQuantityForRelevantProducts(brands, includeCategoryList, excludeCategoryList);
		log.info("Relevant Product Quantity = " + relevantProductQuantity);
		BigDecimal discountPercentage = quantityDiscountMap.getDiscount(relevantProductQuantity);
		log.info("Relevant Percent Discount = " + discountPercentage);
		Money orderVal = request.getOrderValueForRelevantProducts(brands, includeCategoryList, excludeCategoryList);
		log.info("Discount Relevant on Order Value = " + orderVal.toString());
		Money discountCalculated = (orderVal.times(discountPercentage.doubleValue())).div(100);
		log.info("Calculated Discount on Order Value = " + discountCalculated.toString());
		Money finalDiscountAmount = new Money(BigDecimal.ZERO);
		if(maxDiscountPerUse!=null && discountCalculated.gt(maxDiscountPerUse)){
			log.info("Maximum discount is less than the calculated discount on this order. Max Discount = "+maxDiscountPerUse +" and Discount calculated = "+discountCalculated);
			finalDiscountAmount = finalDiscountAmount.plus(maxDiscountPerUse);
		}
		else{
			log.info("Discount amount calculated is the final discount on order. Discount calculated = "+discountCalculated);
			finalDiscountAmount = finalDiscountAmount.plus(discountCalculated);
		}
		
		orderDiscount.setTotalOrderDiscount(finalDiscountAmount.getAmount());
		return orderDiscount.distributeDiscountOnOrder(orderDiscount,this.brands,this.includeCategoryList,this.excludeCategoryList);
	}

	@Override
	public List<RuleConfigDescriptorItem> getRuleConfigs() {
		List<RuleConfigDescriptorItem> ruleConfigs = new ArrayList<RuleConfigDescriptorItem>();
		
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.CLIENT_LIST, false));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.CATEGORY_INCLUDE_LIST, false));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.CATEGORY_EXCLUDE_LIST, false));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.BRAND_LIST, false));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.MIN_ORDER_VALUE, false));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.VARIABLE_DISCOUNT_PERCENTAGE, true));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.MAX_DISCOUNT_CEIL_IN_VALUE, false));
		
		return ruleConfigs;
	}
	
}
