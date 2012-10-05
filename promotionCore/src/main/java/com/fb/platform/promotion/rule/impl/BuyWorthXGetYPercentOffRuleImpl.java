/**
 * 
 */
package com.fb.platform.promotion.rule.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.model.OrderDiscount;
import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.RulesEnum;
import com.fb.platform.promotion.rule.config.RuleConfigDescriptorEnum;
import com.fb.platform.promotion.rule.config.RuleConfigItemDescriptor;
import com.fb.platform.promotion.rule.config.RuleConfiguration;
import com.fb.platform.promotion.rule.config.data.BuyWorthXGetYPercentOffRuleData;
import com.fb.platform.promotion.rule.metadata.RuleConfigMetadata;
import com.fb.platform.promotion.to.OrderRequest;
import com.fb.platform.promotion.to.PromotionStatusEnum;
import com.fb.platform.promotion.util.ListUtil;

/**
 * @author keith
 *
 */
public class BuyWorthXGetYPercentOffRuleImpl implements PromotionRule {

	private static transient Log log = LogFactory.getLog(BuyWorthXGetYPercentOffRuleImpl.class);

	BuyWorthXGetYPercentOffRuleData data = null;
	
	@Override
	public void init(RuleConfiguration ruleConfig) {

		data = (BuyWorthXGetYPercentOffRuleData) RulesEnum.BUY_WORTH_X_GET_Y_PERCENT_OFF.getRuleData(ruleConfig);
		
	}

	@Override
	public PromotionStatusEnum isApplicable(OrderRequest request,int userId,boolean isCouponCommitted) {
		if (log.isDebugEnabled()) {
			log.debug("Checking if BuyWorthXGetYPercentOffRuleImpl applies on order : " + request.getOrderId());
		}
		if (ListUtil.isValidList(data.getClientList()) && !request.isValidClient(data.getClientList())) {
			return PromotionStatusEnum.INVALID_CLIENT;
		}
		if (ListUtil.isValidList(data.getIncludeCategoryList()) && !request.isAnyProductInCategory(data.getIncludeCategoryList())) {
			return PromotionStatusEnum.CATEGORY_MISMATCH;
		}
		if (ListUtil.isValidList(data.getExcludeCategoryList()) && request.isAnyProductInCategory(data.getExcludeCategoryList())) {
			return PromotionStatusEnum.CATEGORY_MISMATCH;
		}
		if (ListUtil.isValidList(data.getBrands()) && !request.isAnyProductInBrand(data.getBrands())) {
			return PromotionStatusEnum.BRAND_MISMATCH;
		}
		if (ListUtil.isValidList(data.getProductIds()) && !request.hasProduct(data.getProductIds())) {
			return PromotionStatusEnum.PRODUCT_NOT_PRESENT;
		}
		Money orderValue = request.getOrderValue(data.getBrands(), data.getIncludeCategoryList(), data.getExcludeCategoryList(), data.getProductIds());
		if(data.getMinOrderValue() !=null && orderValue.lt(data.getMinOrderValue())) {
			return PromotionStatusEnum.LESS_ORDER_AMOUNT;
		}
		return PromotionStatusEnum.SUCCESS;
	}

	@Override
	public OrderDiscount execute(OrderDiscount orderDiscount) {
		OrderRequest request = orderDiscount.getOrderRequest();
		Set<Integer> productSet = null;
		if(log.isDebugEnabled()) {
			log.debug("Executing BuyWorthXGetYPercentOffRuleImpl on order : " + request.getOrderId());
		}
		Money orderVal = request.getOrderValue(data.getBrands(), data.getIncludeCategoryList(), data.getExcludeCategoryList(), data.getProductIds());
		Money discountCalculated = (orderVal.times(data.getDiscountPercentage().doubleValue())).div(100);
		Money finalDiscountAmount = new Money(new BigDecimal(0));
		if(data.getMaxDiscountPerUse() != null && discountCalculated.gt(data.getMaxDiscountPerUse())) {
			log.info("Maximum discount is less than the calculated discount on this order. Max Discount = "+data.getMaxDiscountPerUse() +" and Discount calculated = "+discountCalculated);
			finalDiscountAmount = finalDiscountAmount.plus(data.getMaxDiscountPerUse());
		}
		else {
			log.info("Discount amount calculated is = "+discountCalculated);
			finalDiscountAmount = finalDiscountAmount.plus(discountCalculated);
		}
		
		orderDiscount.setOrderDiscountValue(finalDiscountAmount.getAmount());
		if(ListUtil.isValidList(data.getProductIds())) {
			productSet = new HashSet<Integer>(data.getProductIds());
		}
		return orderDiscount.distributeDiscountOnOrder(orderDiscount,data.getBrands(),data.getIncludeCategoryList(),data.getExcludeCategoryList(), productSet);
	}
	
	@Override
	public RuleConfigMetadata getRuleConfigMetadata() {
		return RulesEnum.BUY_WORTH_X_GET_Y_PERCENT_OFF.getMetaData();
	}
	
	
	@Override
	public List<RuleConfigItemDescriptor> getRuleConfigs() {
		List<RuleConfigItemDescriptor> ruleConfigs = new ArrayList<RuleConfigItemDescriptor>();
		
		ruleConfigs.add(new RuleConfigItemDescriptor(RuleConfigDescriptorEnum.CLIENT_LIST, false));
		ruleConfigs.add(new RuleConfigItemDescriptor(RuleConfigDescriptorEnum.CATEGORY_INCLUDE_LIST, false));
		ruleConfigs.add(new RuleConfigItemDescriptor(RuleConfigDescriptorEnum.CATEGORY_EXCLUDE_LIST, false));
		ruleConfigs.add(new RuleConfigItemDescriptor(RuleConfigDescriptorEnum.BRAND_LIST, false));
		ruleConfigs.add(new RuleConfigItemDescriptor(RuleConfigDescriptorEnum.MIN_ORDER_VALUE, false));
		ruleConfigs.add(new RuleConfigItemDescriptor(RuleConfigDescriptorEnum.DISCOUNT_PERCENTAGE, true));
		ruleConfigs.add(new RuleConfigItemDescriptor(RuleConfigDescriptorEnum.MAX_DISCOUNT_CEIL_IN_VALUE, true));
		ruleConfigs.add(new RuleConfigItemDescriptor(RuleConfigDescriptorEnum.PRODUCT_ID, false));
		
		return ruleConfigs;
	}

}
