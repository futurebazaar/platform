/**
 * 
 */
package com.fb.platform.promotion.util;

import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.RuleConfiguration;
import com.fb.platform.promotion.rule.RulesEnum;
import com.fb.platform.promotion.rule.impl.BuyXGetYFreeRuleImpl;
import com.fb.platform.promotion.rule.impl.BuyWorthXGetYRsOffRuleImpl;
import com.fb.platform.promotion.rule.impl.BuyWorthXGetYPercentOffRuleImpl;
import com.fb.platform.promotion.rule.impl.BuyWorthXGetYRsOffOnZCategoryRuleImpl;

/**
 * @author vinayak
 *
 */
public class PromotionRuleFactory {

	public static PromotionRule createRule(RulesEnum ruleName, RuleConfiguration ruleConfig) {
		PromotionRule rule = null;

		switch (ruleName) {

		case BUY_X_GET_Y_FREE:
			rule = new BuyXGetYFreeRuleImpl();
			rule.init(ruleConfig);
			break;

		case BUY_WORTH_X_GET_Y_RS_OFF:
			rule = new BuyWorthXGetYRsOffRuleImpl();
			rule.init(ruleConfig);
			break;
			
		case BUY_WORTH_X_GET_Y_PERCENT_OFF:
			rule = new BuyWorthXGetYPercentOffRuleImpl();
			rule.init(ruleConfig);
			break;
			
		case BUY_WORTH_X_GET_Y_RS_OFF_ON_Z_CATEGORY:
			rule = new BuyWorthXGetYRsOffOnZCategoryRuleImpl();
			rule.init(ruleConfig);
			break;
			
		default:
			throw new IllegalArgumentException("Unkown RulesEnum object found : " + ruleName);
		}

		return rule;
	}
}
