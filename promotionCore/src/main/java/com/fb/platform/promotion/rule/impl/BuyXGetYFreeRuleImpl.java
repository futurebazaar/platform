/**
 * 
 */
package com.fb.platform.promotion.rule.impl;

import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.RuleConfiguration;
import com.fb.platform.promotion.rule.RuleRequest;
import com.fb.platform.promotion.rule.RuleResponse;
import com.fb.platform.promotion.to.Product;

/**
 * @author vinayak
 *
 */
public class BuyXGetYFreeRuleImpl implements PromotionRule {

	private Product xProduct;
	private Product yProduct;
	
	@Override
	public void init(RuleConfiguration ruleConfig) {
		//String xProductId = ruleConfig.getConfigItemValue("xProd");
		//String yProductId = ruleConfig.getConfigItemValue("yProd");
	}

	@Override
	public boolean isApplicable(RuleRequest request) {
		return false;
	}

	@Override
	public RuleResponse execute(RuleRequest request) {
		return null;
	}
}
