/**
 * 
 */
package com.fb.platform.promotion.rule.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.RuleConfigDescriptorItem;
import com.fb.platform.promotion.rule.RuleConfigDescriptorEnum;
import com.fb.platform.promotion.rule.RuleConfiguration;
import com.fb.platform.promotion.to.OrderRequest;
import com.fb.platform.promotion.to.PromotionStatusEnum;
import com.fb.platform.promotion.util.StringToIntegerList;

/**
 * @author keith
 *
 */
public class BuyWorthXGetYPercentOffOnZCategoryRuleImpl implements PromotionRule, Serializable {

	private static transient Log log = LogFactory.getLog(BuyWorthXGetYPercentOffOnZCategoryRuleImpl.class);
	private Money minOrderValue;
	private BigDecimal discountPercentage;
	private List<Integer> categories;
	private List<Integer> clientList;
	private Money maxDiscountPerUse;
	
	@Override
	public void init(RuleConfiguration ruleConfig) {
		minOrderValue = new Money(BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.MIN_ORDER_VALUE.name()))));
		discountPercentage = BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.DISCOUNT_PERCENTAGE.name())));
		StrTokenizer strTokCategories = new StrTokenizer(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.CATEGORY_LIST.name()),",");
		categories = StringToIntegerList.convert((List<String>)strTokCategories.getTokenList());
		StrTokenizer strTokClients = new StrTokenizer(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.CLIENT_LIST.name()),",");
		clientList = StringToIntegerList.convert((List<String>)strTokClients.getTokenList());
		maxDiscountPerUse = new Money (BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.MAX_DISCOUNT_CEIL_IN_VALUE.name()))));
	}

	@Override
	public PromotionStatusEnum isApplicable(OrderRequest request,int userId,boolean isCouponCommitted) {
		if(log.isDebugEnabled()) {
			log.debug("Checking if BuyWorthXGetYPercentOffOnZCategoryRuleImpl applies on order : " + request.getOrderId());
		}
		Money orderValue = new Money(request.getOrderValue());
		if(request.isValidClient(clientList)){
			if(orderValue.gteq(minOrderValue)){
				if(request.isAllProductsInCategory(categories)){
					return PromotionStatusEnum.SUCCESS;
				}
				return PromotionStatusEnum.CATEGORY_MISMATCH;
			}
			return PromotionStatusEnum.LESS_ORDER_AMOUNT;
		}
		return PromotionStatusEnum.INVALID_CLIENT;
	}

	@Override
	public Money execute(OrderRequest request) {
		if(log.isDebugEnabled()) {
			log.debug("Executing BuyWorthXGetYPercentOffOnZCategoryRuleImpl on order : " + request.getOrderId());
		}
		Money orderVal = new Money(request.getOrderValue());
		Money discountAmount = (orderVal.times(discountPercentage.doubleValue())).div(100); 
		if(discountAmount.gteq(maxDiscountPerUse)){
			return maxDiscountPerUse;
		}
		else{
			return discountAmount;
		}
	}
	
	@Override
	public List<RuleConfigDescriptorItem> getRuleConfigs() {
		List<RuleConfigDescriptorItem> ruleConfigs = new ArrayList<RuleConfigDescriptorItem>();
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.CATEGORY_LIST, true));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.BRAND_LIST, true));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.MIN_ORDER_VALUE, true));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.CATEGORY_INCLUDE_LIST, true));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.CATEGORY_EXCLUDE_LIST, true));
		
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.DISCOUNT_PERCENTAGE, true));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.CLIENT_LIST, true));
		ruleConfigs.add(new RuleConfigDescriptorItem(RuleConfigDescriptorEnum.MAX_DISCOUNT_CEIL_IN_VALUE, true));
		return ruleConfigs;
	}
}
