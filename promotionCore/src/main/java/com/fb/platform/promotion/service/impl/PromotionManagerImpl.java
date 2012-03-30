/**
 * 
 */
package com.fb.platform.promotion.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.platform.auth.AuthenticationService;
import com.fb.platform.auth.AuthenticationTO;
import com.fb.platform.promotion.cache.CouponCacheAccess;
import com.fb.platform.promotion.cache.PromotionCacheAccess;
import com.fb.platform.promotion.dao.CouponDao;
import com.fb.platform.promotion.dao.PromotionDao;
import com.fb.platform.promotion.model.GlobalPromotionUses;
import com.fb.platform.promotion.model.Promotion;
import com.fb.platform.promotion.model.UserPromotionUses;
import com.fb.platform.promotion.model.coupon.Coupon;
import com.fb.platform.promotion.model.coupon.CouponType;
import com.fb.platform.promotion.model.coupon.GlobalCouponUses;
import com.fb.platform.promotion.model.coupon.UserCouponUses;
import com.fb.platform.promotion.service.PromotionManager;
import com.fb.platform.promotion.to.CommitCouponRequest;
import com.fb.platform.promotion.to.CommitCouponResponse;
import com.fb.platform.promotion.to.CommitCouponStatusEnum;
import com.fb.platform.promotion.to.CouponRequest;
import com.fb.platform.promotion.to.CouponResponse;
import com.fb.platform.promotion.to.CouponResponseStatusEnum;

/**
 * @author vinayak
 *
 */
public class PromotionManagerImpl implements PromotionManager {

	private static Log logger = LogFactory.getLog(PromotionManagerImpl.class);

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private CouponCacheAccess couponCache = null;

	@Autowired
	private PromotionCacheAccess promotionCache = null;

	@Autowired
	private CouponDao couponDao = null;

	@Autowired
	private PromotionDao promotionDao = null;

	@Override
	public CouponResponse applyCoupon(CouponRequest request) {
		CouponResponse response = new CouponResponse();

		if (request == null || StringUtils.isBlank(request.getSessionToken())) {
			response.setCouponStatus(CouponResponseStatusEnum.NO_SESSION);
			return response;
		}

		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			//invalid session token
			response.setCouponStatus(CouponResponseStatusEnum.NO_SESSION);
			return response;
		}

		int userId = authentication.getUserID();

		//find the coupon.
		Coupon coupon = getCoupon(request.getCouponCode());
		if (coupon == null) {
			//we dont recognise this coupon code, bye bye
			response.setCouponStatus(CouponResponseStatusEnum.INVALID_COUPON_CODE);
			return response;
		}

		//find the associated promotion
		Promotion promotion = getPromotion(coupon.getPromotionId());
		if (promotion == null) {
			//problem.
			logger.error("No Promotion Found for Coupon code : " + coupon.getCode());
			response.setCouponStatus(CouponResponseStatusEnum.INTERNAL_ERROR);
			return response;
		}

		boolean withinCouponUsesLimits = validateCouponUses(coupon, userId);
		if (!withinCouponUsesLimits) {
			logger.warn("Coupon exceeded limit. Coupon code : " + coupon.getCode());
			response.setCouponStatus(CouponResponseStatusEnum.NUMBER_OF_USES_EXCEEDED);
			return response;
		}

		boolean withinPromotionUsesLimits = validatePromotionUses(promotion, userId);
		if (!withinPromotionUsesLimits) {
			logger.warn("Coupon exceeded Promotions limit. Coupon code : " + coupon.getCode());
			response.setCouponStatus(CouponResponseStatusEnum.NUMBER_OF_USES_EXCEEDED);
			return response;
		}

		//check if the promotion is applicable on this request.
		boolean applicable = promotion.isApplicable(request);
		if (!applicable) {
			logger.warn("Coupon code used when not applicable. Coupon code : " + coupon.getCode());
			response.setCouponStatus(CouponResponseStatusEnum.NOT_APPLICABLE);
			return response;
		}
		//return promotion.apply(request);
		return null;
	}

	@Override
	public CommitCouponResponse commitCouponUse(CommitCouponRequest request) {
		CommitCouponResponse response = new CommitCouponResponse();

		if (request == null || StringUtils.isBlank(request.getSessionToken())) {
			response.setCommitCouponStatus(CommitCouponStatusEnum.NO_SESSION);
			return response;
		}

		if (StringUtils.isBlank(request.getCouponCode())) {
			response.setCommitCouponStatus(CommitCouponStatusEnum.INVALID_COUPON_CODE);
			return response;
		}

		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			//invalid session token
			response.setCommitCouponStatus(CommitCouponStatusEnum.NO_SESSION);
			return response;
		}

		int userId = authentication.getUserID();

		//find the coupon.
		Coupon coupon = getCoupon(request.getCouponCode());
		if (coupon == null) {
			//we dont recognise this coupon code, bye bye
			response.setCommitCouponStatus(CommitCouponStatusEnum.INVALID_COUPON_CODE);
			return response;
		}

		//find the associated promotion
		Promotion promotion = getPromotion(coupon.getPromotionId());
		if (promotion == null) {
			//problem.
			logger.error("No Promotion Found for Coupon code : " + coupon.getCode());
			response.setCommitCouponStatus(CommitCouponStatusEnum.INTERNAL_ERROR);
			return response;
		}

		boolean globalCuponUpdateSuccess = couponDao.updateGlobalUses(coupon.getId(), request.getDiscountValue());
		if (!globalCuponUpdateSuccess) {
			logger.error("Unable to update the global uses for coupon code : " + coupon.getCode());
			response.setCommitCouponStatus(CommitCouponStatusEnum.INTERNAL_ERROR);
			return response;
		}

		boolean userCuponUpdateStatus = couponDao.updateUserUses(coupon.getId(), userId, request.getDiscountValue());
		if (!userCuponUpdateStatus) {
			logger.error("Unable to update the user uses for coupon code : " + coupon.getCode());
			response.setCommitCouponStatus(CommitCouponStatusEnum.INTERNAL_ERROR);
			return response;
		}

		boolean globalPromotionUpdateSuccess = promotionDao.updateGlobalUses(promotion.getId(), request.getDiscountValue());
		if (!globalPromotionUpdateSuccess) {
			logger.error("Unable to update global promotion uses for Coupon code : " + coupon.getCode());
			response.setCommitCouponStatus(CommitCouponStatusEnum.INTERNAL_ERROR);
			return response;
		}

		boolean userPromotionUpdateStatus = promotionDao.updateUserUses(promotion.getId(), userId, request.getDiscountValue());
		if (!userPromotionUpdateStatus) {
			logger.error("Unable to update user promotion uses for Coupon code : " + coupon.getCode());
			response.setCommitCouponStatus(CommitCouponStatusEnum.INTERNAL_ERROR);
			return response;
		}

		response.setCommitCouponStatus(CommitCouponStatusEnum.SUCCESS);
		return response;
	}

	private boolean validatePromotionUses(Promotion promotion, int userId) {
		GlobalPromotionUses globalUses = promotionDao.loadGlobalUses(promotion.getId());
		UserPromotionUses userUses = promotionDao.loadUserUses(promotion.getId(), userId);
		return promotion.isWithinLimits(globalUses, userUses);
	}

	private boolean validateCouponUses(Coupon coupon, int userId) {
		GlobalCouponUses globalUses = couponDao.loadGlobalUses(coupon.getId());
		UserCouponUses userUses = couponDao.loadUserUses(coupon.getId(), userId);

		return coupon.isWithinLimits(globalUses, userUses);
	}

	private Coupon getCoupon(String couponCode) {
		//check if we have coupon cached.
		Coupon coupon = couponCache.get(couponCode);
		if (coupon == null) {
			//load it using dao
			coupon = couponDao.load(couponCode);

			if (coupon != null) {
				cacheCoupon(couponCode, coupon);
			}
		}

		return coupon;
	}

	private void cacheCoupon(String couponCode, Coupon coupon) {
		//cache the global coupon
		if (coupon.getType() == CouponType.GLOBAL) {
			try {
				couponCache.lock(couponCode);
				if (couponCache.get(couponCode) == null) {
					couponCache.put(couponCode, coupon);
				}
			} finally {
				couponCache.unlock(couponCode);
			}
		}
	}

	private Promotion getPromotion(int promotionId) {
		Promotion promotion = promotionCache.get(promotionId);
		if (promotion == null) {
			//its not cached, load it
			promotion = promotionDao.load(promotionId);

			if (promotion != null) {
				cachePromotion(promotionId, promotion);
			}
		}
		return promotion;
	}

	private void cachePromotion(Integer promotionId, Promotion promotion) {
		//TODO need to figure out which promotions to cache
		try {
			promotionCache.lock(promotionId);
			if (promotionCache.get(promotionId) == null) {
				promotionCache.put(promotionId, promotion);
			}
		} finally {
			promotionCache.unlock(promotionId);
		}
	}
}
