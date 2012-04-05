/**
 * 
 */
package com.fb.platform.promotion.rule.impl;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.RuleConfigConstants;
import com.fb.platform.promotion.rule.RuleConfiguration;
import com.fb.platform.promotion.to.OrderRequest;
import com.fb.commons.to.Money;

/**
 * @author keith
 *
 */
public class BuyWorthXGetYPercentOffRuleImpl implements PromotionRule, Serializable {

	private Money minOrderValue;
	private BigDecimal discountPercentage;
	private Money maxDiscountPerUse;
	
	@Override
	public void init(RuleConfiguration ruleConfig) {
		minOrderValue = new Money(BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue(RuleConfigConstants.MIN_ORDER_VALUE))));
		discountPercentage = BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue(RuleConfigConstants.DISCOUNT_PERCENTAGE)));
		maxDiscountPerUse = new Money (BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue(RuleConfigConstants.MAX_DISCOUNT_CEIL_IN_VALUE))));
	}

	@Override
	public boolean isApplicable(OrderRequest request) {
		Money orderValue = new Money(request.getOrderValue());
		if(orderValue.gteq(minOrderValue)){
			return true;
		}
		return false;
	}

	@Override
	public Money execute(OrderRequest request) {
		Money orderVal = new Money(request.getOrderValue());
		Money discountAmount = (orderVal.times(discountPercentage.doubleValue())).div(100); 
		if(discountAmount.gteq(maxDiscountPerUse)){
			return maxDiscountPerUse;
		}
		else{
			return discountAmount;
		}
	}
}
