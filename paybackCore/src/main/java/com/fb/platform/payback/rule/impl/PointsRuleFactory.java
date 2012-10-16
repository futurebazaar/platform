package com.fb.platform.payback.rule.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.fb.platform.payback.cache.ListCacheAccess;
import com.fb.platform.payback.dao.ListDao;
import com.fb.platform.payback.rule.BurnPointsRuleEnum;
import com.fb.platform.payback.rule.PointsRule;
import com.fb.platform.payback.rule.EarnPointsRuleEnum;
import com.fb.platform.payback.rule.RuleConfiguration;

public class PointsRuleFactory {
	
		@Autowired
		private static ListDao listDao;
		
		public void setListDao(ListDao listDao){
			PointsRuleFactory.listDao = listDao;
		}
		
		@Autowired
		private static ListCacheAccess listCacheAccess;
		
		public void setListCacheAccess(ListCacheAccess listCacheAccess){
			PointsRuleFactory.listCacheAccess = listCacheAccess;
		}
		
		
		private static PointsRule getRule(EarnPointsRuleEnum ruleName){
			PointsRule rule = null;
			switch(ruleName){
			
				case BUY_WORTH_X_EARN_Y_BONUS_POINTS:
					rule = new BuyWorthXEarnYBonusPoints();
				
					break;
				
				case EARN_X_POINTS_ON_Y_DAY:
					rule = new EarnXPointsOnYDay();
					break;
					
				case BUY_PRODUCT_X_EARN_Y_POINTS:
					rule = new BuyProductXEarnYPoints();
					break;
					
				case ENTER_LOYALTY_CARD_EARN_X_POINTS:
					rule = new EnterLoyaltyCardEarnXPoints();
					break;
				
				case EARN_X_POINTS_ON_Y_PAYMENT_MODE:
					rule = new EarnXPointsOnYPaymentMode();
					break;
					
				case BUY_DOD_EARN_Y_POINTS:
					rule = new BuyHeroDealEarnYPoints();
					((BuyHeroDealEarnYPoints) rule).setListDao(listDao);
					((BuyHeroDealEarnYPoints) rule).setListCacheAccess(listCacheAccess);
					break;
					
				default:
					throw new IllegalArgumentException("Unkown RulesEnum object found : " + ruleName);
			
			}
			
			return rule;
		}
		
		public static PointsRule createRule(BurnPointsRuleEnum ruleName, RuleConfiguration ruleConfig, String clientName){
			PointsRule rule = null;
			
			switch(ruleName){
			
				case PURCHASE_ORDER_BURN_X_POINTS:
					rule = new PurchaseOrderBurnXPoints();
					rule.init(ruleConfig);
					break;
					
				default:
					throw new IllegalArgumentException("Unkown RulesEnum object found : " + ruleName);
			
			}
			
			return rule;
		}
		
		public static PointsRule createRule(EarnPointsRuleEnum ruleName, RuleConfiguration ruleConfig, String clientName){
			PointsRule rule = getRule(ruleName);
			rule.init(ruleConfig);
			return rule;
		}

}
