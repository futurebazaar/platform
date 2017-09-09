package com.fb.platform.payback.rule.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.fb.platform.payback.cache.ListCacheAccess;
import com.fb.platform.payback.dao.ListDao;
import com.fb.platform.payback.rule.BurnPointsRuleEnum;
import com.fb.platform.payback.rule.PointsRule;
import com.fb.platform.payback.rule.EarnPointsRuleEnum;
import com.fb.platform.payback.rule.RuleConfiguration;
import com.fb.platform.payback.util.PointsUtil;

public class PointsRuleFactory {
	
		@Autowired
		private static ListDao listDao;
		
		public void setListDao(ListDao listDao){
			this.listDao = listDao;
		}
		
		@Autowired
		private static ListCacheAccess listCacheAccess;
		
		public void setListCacheAccess(ListCacheAccess listCacheAccess){
			this.listCacheAccess = listCacheAccess;
		}
		
		@Autowired
		private static PointsUtil pointsUtil;
		
		public void setPointsUtil(PointsUtil pointsUtil){
			this.pointsUtil = pointsUtil;
		}
		
		public static PointsRule createRule(EarnPointsRuleEnum ruleName, RuleConfiguration ruleConfig){
			PointsRule rule = null;
			switch(ruleName){
			
				case BUY_WORTH_X_EARN_Y_BONUS_POINTS:
					rule = new BuyWorthXEarnYBonusPoints();
					rule.setPointsUtil(pointsUtil);
					rule.init(ruleConfig);
					break;
				
				case EARN_X_POINTS_ON_Y_DAY:
					rule = new EarnXPointsOnYDay();
					rule.setPointsUtil(pointsUtil);
					rule.init(ruleConfig);
					break;
					
				case BUY_PRODUCT_X_EARN_Y_POINTS:
					rule = new BuyProductXEarnYPoints();
					rule.setPointsUtil(pointsUtil);
					rule.init(ruleConfig);
					break;
					
				case ENTER_LOYALTY_CARD_EARN_X_POINTS:
					rule = new EnterLoyaltyCardEarnXPoints();
					rule.setPointsUtil(pointsUtil);
					rule.init(ruleConfig);
					break;
				
				case EARN_X_POINTS_ON_Y_PAYMENT_MODE:
					rule = new EarnXPointsOnYPaymentMode();
					rule.setPointsUtil(pointsUtil);
					rule.init(ruleConfig);
					break;
					
				case BUY_DOD_EARN_Y_POINTS:
					rule = new BuyHeroDealEarnYPoints();
					((BuyHeroDealEarnYPoints) rule).setListDao(listDao);
					((BuyHeroDealEarnYPoints) rule).setListCacheAccess(listCacheAccess);
					rule.setPointsUtil(pointsUtil);
					rule.init(ruleConfig);
					break;

				case EARN_X_POINTS_ON_Y_CATEGORY_FOR_Z_PAYMENT_MODE:
					rule = new EarnXPointsOnYCategoryForZPaymentMode();
					rule.setPointsUtil(pointsUtil);
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
			
				case PURCHASE_ORDER_BURN_X_POINTS:
					rule = new PurchaseOrderBurnXPoints();
					rule.setPointsUtil(pointsUtil);
					rule.init(ruleConfig);
					break;
					
				default:
					throw new IllegalArgumentException("Unkown RulesEnum object found : " + ruleName);
			
			}
			
			return rule;
		}

}
