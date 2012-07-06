/**
 * 
 */
package com.fb.platform.promotion.rule.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.exception.MandatoryDataMissingException;
import com.fb.platform.promotion.model.OrderDiscount;
import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.RulesEnum;
import com.fb.platform.promotion.rule.config.RuleConfigDescriptorEnum;
import com.fb.platform.promotion.rule.config.RuleConfigItemDescriptor;
import com.fb.platform.promotion.rule.config.RuleConfiguration;
import com.fb.platform.promotion.rule.config.data.CategoryBasedVariablePercentOffRuleData;
import com.fb.platform.promotion.rule.config.type.CategoryDiscountPair;
import com.fb.platform.promotion.rule.metadata.RuleConfigMetadata;
import com.fb.platform.promotion.to.OrderRequest;
import com.fb.platform.promotion.to.PromotionStatusEnum;
import com.fb.platform.promotion.util.ListUtil;

/**
 * @author keith
 *
 */
public class CategoryBasedVariablePercentOffRuleImpl implements PromotionRule, Serializable {

	private static transient Log log = LogFactory.getLog(CategoryBasedVariablePercentOffRuleImpl.class);

	CategoryBasedVariablePercentOffRuleData data = null;
	
	@Override
	public void init(RuleConfiguration ruleConfig) throws MandatoryDataMissingException {

		data = (CategoryBasedVariablePercentOffRuleData) RulesEnum.CATEGORY_BASED_VARIABLE_PERCENT_OFF.getRuleData(ruleConfig);
		
	}

	@Override
	public PromotionStatusEnum isApplicable(OrderRequest request,int userId,boolean isCouponCommitted) {
		if (log.isDebugEnabled()) {
			log.debug("Checking if CategoryBasedVariablePercentOffRuleImpl applies on order : " + request.getOrderId());
		}
		if (ListUtil.isValidList(data.getClientList()) && !request.isValidClient(data.getClientList())) {
			return PromotionStatusEnum.INVALID_CLIENT;
		}
		Money orderValue = request.getOrderValueForRelevantProducts(null, data.getCategoryDiscountPairs().getAllCategoryList(), null);
		if(data.getMinOrderValue() !=null && orderValue.lt(data.getMinOrderValue())){
			return PromotionStatusEnum.LESS_ORDER_AMOUNT;
		}
		return PromotionStatusEnum.SUCCESS;
	}

	@Override
	public OrderDiscount execute(OrderDiscount orderDiscount) {
		OrderRequest request = orderDiscount.getOrderRequest();
		if(log.isDebugEnabled()) {
			log.debug("Executing BuyWorthXGetYPercentOffRuleImpl on order : " + request.getOrderId());
		}
		Money finalDiscountAmount = new Money(new BigDecimal(0));
		// TODO : change logic to loop thru CatDiscMap
		for(CategoryDiscountPair pair : data.getCategoryDiscountPairs().getCategoryDiscountMap()) {
			Money orderVal = request.getOrderValueForRelevantProducts(null, pair.getCategoryList(), null);
			Money discountCalculated = (orderVal.times(pair.getPercent().doubleValue())).div(100);
			finalDiscountAmount = finalDiscountAmount.plus(discountCalculated);
			orderDiscount.setOrderDiscountValue(discountCalculated.getAmount());
			orderDiscount.distributeDiscountOnOrder(orderDiscount,null,pair.getCategoryList(),null);
		}
		//[1,2]= 5 : [3-8]=10 : [9,10]=15 : [11,12]=12 : [13,14]=12 : [15,16]=18
//		Money orderVal = request.getOrderValueForRelevantProducts(null, data.getCategoryDiscountPairs().getAllCategoryList(), null);
//		Money discountCalculated = (orderVal.times(data.getCategoryDiscountPairs().getCategoryDiscountMap().get(0).getPercent().doubleValue())).div(100);
//		Money finalDiscountAmount = new Money(new BigDecimal(0));
//		if(data.getMaxDiscountPerUse() != null && discountCalculated.gt(data.getMaxDiscountPerUse())){
//			log.info("Maximum discount is less than the calculated discount on this order. Max Discount = "+data.getMaxDiscountPerUse() +" and Discount calculated = "+discountCalculated);
//			finalDiscountAmount = finalDiscountAmount.plus(data.getMaxDiscountPerUse());
//		}
//		else{
//			log.info("Discount amount calculated is the final discount on order. Discount calculated = "+discountCalculated);
//			finalDiscountAmount = finalDiscountAmount.plus(discountCalculated);
//		}
		
		orderDiscount.setOrderDiscountValue(finalDiscountAmount.getAmount());
		return orderDiscount;
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
		ruleConfigs.add(new RuleConfigItemDescriptor(RuleConfigDescriptorEnum.MAX_DISCOUNT_CEIL_IN_VALUE, false));
		
		return ruleConfigs;
	}

}