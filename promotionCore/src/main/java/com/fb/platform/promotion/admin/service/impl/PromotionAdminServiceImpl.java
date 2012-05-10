package com.fb.platform.promotion.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fb.platform.promotion.admin.dao.CouponAdminDao;
import com.fb.platform.promotion.admin.dao.PromotionAdminDao;
import com.fb.platform.promotion.admin.service.PromotionAdminService;
import com.fb.platform.promotion.dao.RuleDao;
import com.fb.platform.promotion.model.coupon.CouponLimitsConfig;
import com.fb.platform.promotion.model.coupon.CouponType;
import com.fb.platform.promotion.rule.RulesEnum;
import com.fb.platform.promotion.service.PromotionService;
import com.fb.platform.promotion.to.PromotionTO;
import com.fb.platform.promotion.to.RuleConfigItemTO;
import com.fb.platform.promotion.util.CouponCodeCreator;

/**
 * 
 * @author neha
 *
 */
public class PromotionAdminServiceImpl implements PromotionAdminService {

	@Autowired
	private RuleDao ruleDao = null;

	@Autowired
	private CouponAdminDao couponAdminDao = null;

	@Autowired
	private PromotionAdminDao promotionAdminDao = null;

	@Autowired
	private PromotionService promotionService = null;

	@Autowired
	private CouponCodeCreator couponCodeCreator = null;

	private static final int COUPON_GENERATION_BATCH_SIZE = 1000;

	@Override
	public List<RulesEnum> getAllPromotionRules() {
		List<RulesEnum> promotionRulesList = ruleDao.getAllPromotionRules();
		return promotionRulesList;
	}
	
	@Override
	public int createPromotion(PromotionTO promotionTO) {
		int isActive = promotionTO.isActive() ? 1 : 0;
		int ruleId = ruleDao.getRuleId(promotionTO.getRulesEnum().toString());
		
		int promotionId = promotionAdminDao.createPromotion(promotionTO.getName(), 
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
				promotionAdminDao.createPromotionRuleConfig(ruleConfigItemTO.getName(), 
						ruleConfigItemTO.getValue(), 
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

	@Override
	public List<String> createCoupons(int count, int length, String startsWith, String endsWith, int promotionId, CouponType type, CouponLimitsConfig limits) {

		//see if the promotion is valid, if not this will throw promotion not found exception
		promotionService.getPromotion(promotionId);

		couponCodeCreator.init(count, length, startsWith, endsWith, COUPON_GENERATION_BATCH_SIZE);

		while (couponCodeCreator.nextBatchAvailable()) {
			List<String> batchOfCouponCodes = couponCodeCreator.getNextBatch();
			createCouponsInBatch(batchOfCouponCodes, promotionId, type, limits);
		}

		return couponCodeCreator.getGeneratedCoupons();
	}

	private void createCouponsInBatch(List<String> batchOfCoupons, int promotionId, CouponType type, CouponLimitsConfig limits) {
		
	}

	public void setCouponAdminDao(CouponAdminDao couponAdminDao) {
		this.couponAdminDao = couponAdminDao;
	}

	public void setPromotionService(PromotionService promotionService) {
		this.promotionService = promotionService;
	}

	public void setCouponCodeCreator(CouponCodeCreator couponCodeCreator) {
		this.couponCodeCreator = couponCodeCreator;
	}

}
