/**
 * 
 */
package com.fb.platform.promotion.rule.impl;

import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.RuleConfiguration;
import com.fb.platform.promotion.to.PromotionRequest;

/**
 * @author vinayak
 *
 */
public class BuyXGetYFreeRuleImpl implements PromotionRule {

	@Override
	public void init(RuleConfiguration ruleConfig) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isApplicable(PromotionRequest request) {
		return false;
	}

	@Override
	public Object execute(PromotionRequest request) {
		return null;
	}
}
