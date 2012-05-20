package com.fb.platform.payback.rule.impl;

import com.fb.platform.payback.rule.BurnPointsRuleEnum;
import com.fb.platform.payback.rule.PointsRule;
import com.fb.platform.payback.rule.EarnPointsRuleEnum;
import com.fb.platform.payback.rule.RuleConfiguration;

public class PointsRuleFactory {

		public static PointsRule createRule(EarnPointsRuleEnum ruleName, RuleConfiguration ruleConfig){
			PointsRule rule = null;
			
			switch(ruleName){
			
				case BUY_WORTH_X_EARN_Y_BONUS_POINTS:
					rule = new BuyWorthXEarnYBonusPoints();
					rule.init(ruleConfig);
					break;
				
				case EARN_X_BONUS_POINTS_ON_Y_DAY:
					rule = new EarnXPointsOnYDay();
					rule.init(ruleConfig);
					break;
					
				case BUY_PRODUCT_X_EARN_Y_POINTS:
					rule = new BuyProductXEarnYPoints();
					rule.init(ruleConfig);
					break;
					
				case ENTER_LOYALTY_CARD_EARN_X_POINTS:
					rule = new EnterLoyaltyCardEarnXPoints();
					rule.init(ruleConfig);
					break;
					
				default:
					throw new IllegalArgumentException("Unkown RulesEnum object found : " + ruleName);
			
			}
			
			return rule;
		}
		
		public static PointsRule createRule(BurnPointsRuleEnum ruleName, RuleConfiguration ruleConfig){
			PointsRule rule = null;
			
			switch(ruleName){
			
				case CANCEL_ORDER_REVERSE_X_BURN_POINTS:
					rule = new CancelOrderReverseXBurnPoints();
					rule.init(ruleConfig);
					break;
					
				default:
					throw new IllegalArgumentException("Unkown RulesEnum object found : " + ruleName);
			
			}
			
			return rule;
		}
}
