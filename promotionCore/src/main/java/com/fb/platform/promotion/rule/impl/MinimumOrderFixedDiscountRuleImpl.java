/**
 * 
 */
package com.fb.platform.promotion.rule.impl;

import java.math.BigDecimal;

import com.fb.platform.promotion.rule.OrderRuleRequest;
import com.fb.platform.promotion.rule.OrderRuleResponse;
import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.RuleConfiguration;
import com.fb.platform.promotion.rule.RuleRequest;
import com.fb.commons.to.Money;

/**
 * @author vinayak
 *
 */
public class MinimumOrderFixedDiscountRuleImpl implements PromotionRule {

	private Money minOrderValue;
	private Money fixedRsOff;
	
	@Override
	public void init(RuleConfiguration ruleConfig) {
		minOrderValue = new Money(BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue("minOrderVal"))));
		fixedRsOff = new Money (BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue("fixedRsOff"))));
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
		Money calcDiscValue = ruleReq.getOrderValue().minus(fixedRsOff);
		ruleResp.setDiscountValue(calcDiscValue);
		return ruleResp;
	}
}
