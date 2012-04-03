/**
 * 
 */
package com.fb.platform.promotion.rule.impl;

import java.math.BigDecimal;

import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.RuleConfiguration;
import com.fb.platform.promotion.to.OrderRequest;
import com.fb.commons.to.Money;

/**
 * @author vinayak
 *
 */
public class BuyWorthXGetYRsOffRuleImpl implements PromotionRule {

	private Money minOrderValue;
	private Money fixedRsOff;
	
	@Override
	public void init(RuleConfiguration ruleConfig) {
		minOrderValue = new Money(BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue("MIN_ORDER_VALUE"))));
		fixedRsOff = new Money (BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue("FIXED_DISCOUNT_RS_OFF"))));
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
		return fixedRsOff;
	}
}
