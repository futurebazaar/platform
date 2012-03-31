/**
 * 
 */
package com.fb.platform.promotion.rule.impl;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.RuleConfiguration;
import com.fb.platform.promotion.to.OrderRequest;
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
	public boolean isApplicable(OrderRequest request) {
		return false;
	}

	@Override
	public Money execute(OrderRequest request) {
		return null;
	}
}
