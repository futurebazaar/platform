/**
 * 
 */
package com.fb.platform.promotion.rule.impl;

import java.math.BigDecimal;

import com.fb.platform.promotion.rule.OrderRuleRequest;
import com.fb.platform.promotion.rule.OrderRuleResponse;
import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.RuleConfigConstants;
import com.fb.platform.promotion.rule.RuleConfiguration;
import com.fb.platform.promotion.rule.RuleRequest;
import com.fb.commons.to.Money;

/**
 * @author vinayak
 *
 */
public class BuyWorthXGetYPercentOffRuleImpl implements PromotionRule {

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
	public boolean isApplicable(RuleRequest request) {
		Money orderValue = ((OrderRuleRequest)request).getOrderValue();
		if(orderValue.gteq(minOrderValue)){
			return true;
		}
		return false;
	}

	@Override
	public OrderRuleResponse execute(RuleRequest request) {
		OrderRuleResponse ruleResp = new OrderRuleResponse();
		OrderRuleRequest ruleReq = (OrderRuleRequest) request;
		Money orderVal = ruleReq.getOrderValue();
		Money discountAmount = (orderVal.times(discountPercentage.doubleValue())).div(100); 
		Money calcDiscValue = ruleReq.getOrderValue().minus(discountAmount);
		if(calcDiscValue.gteq(maxDiscountPerUse)){
			ruleResp.setDiscountValue(maxDiscountPerUse);
		}
		else{
			ruleResp.setDiscountValue(calcDiscValue);
		}
		return ruleResp;
	}
}
