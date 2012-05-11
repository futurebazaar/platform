package com.fb.platform.promotion.admin.service.impl;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.platform.promotion.admin.dao.PromotionAdminDao;
import com.fb.platform.promotion.admin.service.PromotionAdminService;
import com.fb.platform.promotion.admin.to.PromotionTO;
import com.fb.platform.promotion.admin.to.RuleConfigItemTO;
import com.fb.platform.promotion.admin.to.SearchPromotionOrderBy;
import com.fb.platform.promotion.admin.to.SearchPromotionOrderByOrder;
import com.fb.platform.promotion.dao.RuleDao;
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
	public int createPromotion(PromotionTO promotionTO) {
		int isActive = promotionTO.isActive() ? 1 : 0;
		int ruleId = ruleDao.getRuleId(promotionTO.getRuleName());
		int promotionId = 0;
		
		promotionId = promotionAdminDao.createPromotion(promotionTO.getPromotionName(), 
				promotionTO.getDescription(), 
				ruleId, 
				promotionTO.getValidFrom(), 
				promotionTO.getValidTill(),
				isActive);
		if(promotionId > 0) {
			promotionAdminDao.createPromotionLimitConfig(	promotionId, 
					promotionTO.getMaxUses(), 
					promotionTO.getMaxAmount(), 
					promotionTO.getMaxUsesPerUser(), 
					promotionTO.getMaxAmountPerUser());
			
			for(RuleConfigItemTO ruleConfigItemTO : promotionTO.getConfigItems()) {
				promotionAdminDao.createPromotionRuleConfig(ruleConfigItemTO.getRuleConfigName(), 
						ruleConfigItemTO.getRuleConfigValue(), 
						promotionId, 
						ruleId);
			}
			
		}
		
		return promotionId;
		
	}
	
	@Override
	public boolean updatePromotion(PromotionTO promotionTO) {
		int isActive = promotionTO.isActive() ? 1 : 0;
		boolean updateSuccesfull = false;
		int ruleId = ruleDao.getRuleId(promotionTO.getRuleName());
		
		int promotionUpdated = promotionAdminDao.updatePromotion(promotionTO.getPromotionId(), 
				promotionTO.getPromotionName(), 
				promotionTO.getDescription(), 
				promotionTO.getValidFrom(), 
				promotionTO.getValidTill(), 
				isActive,
				ruleId);
		
		updateSuccesfull = (promotionUpdated == 1) ? true : false;
		 
		if(updateSuccesfull) {
			promotionAdminDao.updatePromotionLimitConfig(promotionTO.getPromotionId(), 
					promotionTO.getMaxUses(), 
					promotionTO.getMaxAmount(), 
					promotionTO.getMaxUsesPerUser(), 
					promotionTO.getMaxAmountPerUser());
			
			int rowsDeleted = promotionAdminDao.deletePromotionRuleConfig(promotionTO.getPromotionId());
			
			for(RuleConfigItemTO ruleConfigItemTO : promotionTO.getConfigItems()) {
				if(updateSuccesfull) {
					int promotionRuleUpdated = promotionAdminDao.createPromotionRuleConfig(ruleConfigItemTO.getRuleConfigName(), 
							ruleConfigItemTO.getRuleConfigValue(), 
							promotionTO.getPromotionId(), 
							ruleId);
					updateSuccesfull = (promotionRuleUpdated == 1) ? true : false;
				} else {
					break;
				}
			}
		}
		
		
		return updateSuccesfull;
		
	}
	
	@Override
	public List<PromotionTO> searchPromotion(String promotionName, DateTime validFrom, DateTime validTill, int isActive, SearchPromotionOrderBy orderBy,
			SearchPromotionOrderByOrder order, int startRecord, int batchSize) {
		List<PromotionTO> promotionsList = promotionAdminDao.searchPromotion(promotionName, validFrom, validTill, isActive, orderBy, order, startRecord, batchSize);
		return promotionsList;
	}
	@Override
	public int getPromotionCount(String promotionName, DateTime validFrom, DateTime validTill, int isActive) {
		int promotionsCount = promotionAdminDao.getPromotionCount(promotionName, validFrom, validTill, isActive);
		return promotionsCount;
	}
	
	@Override
	public PromotionTO viewPromotion(int promotionId) {
		PromotionTO promotionCompleteView = promotionAdminDao.viewPromotion(promotionId);
		if(promotionCompleteView != null) {
			int couponCount = promotionAdminDao.getCouponCount(promotionId);
			promotionCompleteView.setCouponCount(couponCount);
		}
		return promotionCompleteView;
	}
	
	public void setRuleDao(RuleDao ruleDao) {
		this.ruleDao = ruleDao;
	}
	
	public void setPromotionAdminDao(PromotionAdminDao promotionAdminDao) {
		this.promotionAdminDao = promotionAdminDao;
	}

}
