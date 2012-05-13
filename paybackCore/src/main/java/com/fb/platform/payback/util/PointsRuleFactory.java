package com.fb.platform.payback.util;

import com.fb.platform.payback.rule.PointsRule;
import com.fb.platform.payback.rule.EarnPointsRuleEnum;
import com.fb.platform.payback.rule.RuleConfiguration;
import com.fb.platform.payback.rule.impl.BuyProductXEarnYPoints;
import com.fb.platform.payback.rule.impl.BuyWorthXEarnYPoints;
import com.fb.platform.payback.rule.impl.EarnXBonusPointsOnYDay;

public class PointsRuleFactory {

		public static PointsRule createRule(EarnPointsRuleEnum ruleName, RuleConfiguration ruleConfig){
			PointsRule rule = null;
			
			switch(ruleName){
			
				case BUY_WORTH_X_EARN_Y_POINTS:
					rule = new BuyWorthXEarnYPoints();
					rule.init(ruleConfig);
					break;
				
				case EARN_X_BONUS_POINTS_ON_Y_DAY:
					rule = new EarnXBonusPointsOnYDay();
					rule.init(ruleConfig);
					break;
					
				case BUY_PRODUCT_X_EARN_Y_POINTS:
					rule = new BuyProductXEarnYPoints();
					rule.init(ruleConfig);
					break;
					
				case WRITE_REVIEW_FOR_PRODUCT_X_EARN_Y_POINTS:
					//rule.init(ruleConfig);
					break;
				
				default:
					throw new IllegalArgumentException("Unkown RulesEnum object found : " + ruleName);
			
			}
			
			return rule;
		}
}
