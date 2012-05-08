package com.fb.platform.promotion.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fb.platform.promotion.admin.dao.PromotionAdminDao;
import com.fb.platform.promotion.admin.service.PromotionAdminService;
import com.fb.platform.promotion.dao.RuleDao;
import com.fb.platform.promotion.model.Promotion;
import com.fb.platform.promotion.rule.RuleConfigItem;
import com.fb.platform.promotion.rule.RuleConfiguration;
import com.fb.platform.promotion.rule.RulesEnum;

/**
 * 
 * @author neha
 *
 */
public class PromotionAdminServiceImpl implements PromotionAdminService {

	@Autowired
	private RuleDao ruleDao = null;
	
	@Autowired
	private PromotionAdminDao promotionAdminDao = null;
	
	@Override
	public List<RulesEnum> getAllPromotionRules() {
		List<RulesEnum> promotionRulesList = ruleDao.getAllPromotionRules();
		return promotionRulesList;
	}
	
	@Override
	public int createPromotion(Promotion promotion, RulesEnum rulesEnum, RuleConfiguration ruleConfiguration) {
		int isActive = promotion.isActive() ? 1 : 0;
		int promotionId = promotionAdminDao.createPromotion(promotion.getName(), 
															promotion.getDescription(), 
															rulesEnum, 
															promotion.getDates().getValidFrom(), 
															promotion.getDates().getValidTill(),
															isActive);
		if(promotionId > 0) {
			promotionAdminDao.createPromotionLimitConfig(	promotionId, 
															promotion.getLimitsConfig().getMaxUses(), 
															promotion.getLimitsConfig().getMaxAmount(), 
															promotion.getLimitsConfig().getMaxUsesPerUser(), 
															promotion.getLimitsConfig().getMaxAmountPerUser());
			
			int ruleId = ruleDao.getRuleId(rulesEnum.toString());
			for(RuleConfigItem ruleConfigItem : ruleConfiguration.getConfigItems()) {
				promotionAdminDao.createPromotionRuleConfig(ruleConfigItem.getKey(), 
						ruleConfigItem.getValue(), 
						promotionId, 
						ruleId);
			}
			
		}
		
		return promotionId;
		
	}
	
	public void setRuleDao(RuleDao ruleDao) {
		this.ruleDao = ruleDao;
	}
	
	public void setPromotionAdminDao(PromotionAdminDao promotionAdminDao) {
		this.promotionAdminDao = promotionAdminDao;
	}

}
