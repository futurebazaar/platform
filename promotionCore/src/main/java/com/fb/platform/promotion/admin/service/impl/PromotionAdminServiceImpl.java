package com.fb.platform.promotion.admin.service.impl;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.fb.commons.PlatformException;
import com.fb.platform.promotion.admin.dao.CouponAdminDao;
import com.fb.platform.promotion.admin.dao.PromotionAdminDao;
import com.fb.platform.promotion.admin.service.PromotionAdminService;
import com.fb.platform.promotion.admin.to.PromotionTO;
import com.fb.platform.promotion.admin.to.RuleConfigItemTO;
import com.fb.platform.promotion.dao.RuleDao;
import com.fb.platform.promotion.model.coupon.Coupon;
import com.fb.platform.promotion.model.coupon.CouponLimitsConfig;
import com.fb.platform.promotion.model.coupon.CouponType;
import com.fb.platform.promotion.rule.RulesEnum;
import com.fb.platform.promotion.service.CouponAlreadyAssignedToUserException;
import com.fb.platform.promotion.service.CouponNotFoundException;
import com.fb.platform.promotion.service.InvalidCouponTypeException;
import com.fb.platform.promotion.service.PromotionService;
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
	public List<PromotionTO> searchPromotion(String promotionName, DateTime validFrom, DateTime validTill, int startRecord, int batchSize) {
		List<PromotionTO> promotionsList = promotionAdminDao.searchPromotion(promotionName, validFrom, validTill, startRecord, batchSize);
		return promotionsList;
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

	@Override
	public void assignCouponToUser(String couponCode, int userId, int overriddenUserLimit) 
			throws CouponNotFoundException, CouponAlreadyAssignedToUserException, InvalidCouponTypeException {

		try {
			Coupon coupon = couponAdminDao.loadCouponWithoutConfig(couponCode);
			if (coupon == null) {
				throw new CouponNotFoundException("Assigning invalid CouponCode to user. CouponCode : " + couponCode + ". UserId : "  + userId);
			}

			if (!(coupon.getType() == CouponType.PRE_ISSUE)) {
				throw new InvalidCouponTypeException("Only PRE_ISSUE coupons can be assigned to a user. CouponCode : " + couponCode + ". UserId : "  + userId + ". CouponType : " + coupon.getType());
			}

			couponAdminDao.assignToUser(userId, couponCode, overriddenUserLimit);

		} catch (DataAccessException e) {
			throw new PlatformException("Exception while assigning Coupon to user. CouponCode : " + couponCode + ". UserId : "  + userId, e);
		}
	}
}
