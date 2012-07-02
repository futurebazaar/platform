/**
 * 
 */
package com.fb.platform.promotion.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fb.platform.promotion.dao.OrderDao;
import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.RulesEnum;
import com.fb.platform.promotion.rule.config.RuleConfigItemDescriptor;
import com.fb.platform.promotion.rule.config.RuleConfiguration;
import com.fb.platform.promotion.rule.impl.BuyWorthXGetYPercentOffRuleImpl;
import com.fb.platform.promotion.rule.impl.BuyWorthXGetYRsOffRuleImpl;
import com.fb.platform.promotion.rule.impl.BuyXBrandGetYRsOffOnZProductRuleImpl;
import com.fb.platform.promotion.rule.impl.BuyXGetYFreeRuleImpl;
import com.fb.platform.promotion.rule.impl.BuyXQuantityGetVariablePercentOffRuleImpl;
import com.fb.platform.promotion.rule.impl.FirstPurchaseBuyWorthXGetYRsOffRuleImpl;

/**
 * @author vinayak
 *
 */
public class PromotionRuleFactory {
	

	@Autowired
	private static OrderDao orderDao = null;
	
	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	private static PromotionRule getRule(RulesEnum ruleName) {
		PromotionRule rule = null;

		switch (ruleName) {

		case BUY_X_GET_Y_FREE:
			rule = new BuyXGetYFreeRuleImpl();
			break;

		case BUY_WORTH_X_GET_Y_RS_OFF:
			rule = new BuyWorthXGetYRsOffRuleImpl();
			break;
			
		case BUY_WORTH_X_GET_Y_PERCENT_OFF:
			rule = new BuyWorthXGetYPercentOffRuleImpl();
			break;
		
		case BUY_X_BRAND_GET_Y_RS_OFF_ON_Z_PRODUCT:
			rule = new BuyXBrandGetYRsOffOnZProductRuleImpl();
			break;
			
		case FIRST_PURCHASE_BUY_WORTH_X_GET_Y_RS_OFF:
			rule = new FirstPurchaseBuyWorthXGetYRsOffRuleImpl();
			((FirstPurchaseBuyWorthXGetYRsOffRuleImpl)rule).setOrderDao(orderDao);
			break;
			
		case BUY_X_QUANTITY_GET_VARIABLE_PERCENT_OFF:
			rule = new BuyXQuantityGetVariablePercentOffRuleImpl();
			break;
			
		default:
			throw new IllegalArgumentException("Unkown RulesEnum object found : " + ruleName);
		}

		return rule;
	}
	
	public static List<RuleConfigItemDescriptor> getRuleConfig(RulesEnum ruleName) {
		PromotionRule rule = getRule(ruleName);
		List<RuleConfigItemDescriptor> ruleConfigs = rule.getRuleConfigs();
		return ruleConfigs;
	}
	
	public static PromotionRule createRule(RulesEnum ruleName, RuleConfiguration ruleConfig) {
		PromotionRule rule = getRule(ruleName);
		rule.init(ruleConfig);
		return rule;
	}
}
