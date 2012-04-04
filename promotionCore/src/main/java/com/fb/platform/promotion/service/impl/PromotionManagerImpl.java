/**
 * 
 */
package com.fb.platform.promotion.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.to.Money;
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
import com.fb.platform.promotion.to.ReleaseCouponRequest;
import com.fb.platform.promotion.to.ReleaseCouponResponse;
import com.fb.platform.promotion.to.ReleaseCouponStatusEnum;

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
		Coupon coupon = getCoupon(request.getCouponCode(), userId);
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

		GlobalCouponUses globalCouponUses = couponDao.loadGlobalUses(coupon.getId());
		UserCouponUses userCouponUses = couponDao.loadUserUses(coupon.getId(), userId);
		boolean withinCouponUsesLimits = validateCouponUses(coupon, globalCouponUses, userCouponUses);
		if (!withinCouponUsesLimits) {
			logger.warn("Coupon exceeded limit. Coupon code : " + coupon.getCode());
			response.setCouponStatus(CouponResponseStatusEnum.NUMBER_OF_USES_EXCEEDED);
			return response;
		}

		GlobalPromotionUses globalPromotionUses = promotionDao.loadGlobalUses(promotion.getId());
		UserPromotionUses userPromotionUses = promotionDao.loadUserUses(promotion.getId(), userId);
		boolean withinPromotionUsesLimits = validatePromotionUses(promotion, globalPromotionUses, userPromotionUses);
		if (!withinPromotionUsesLimits) {
			logger.warn("Coupon exceeded Promotions limit. Coupon code : " + coupon.getCode());
			response.setCouponStatus(CouponResponseStatusEnum.NUMBER_OF_USES_EXCEEDED);
			return response;
		}

		//check if the promotion is applicable on this request.
		boolean applicable = promotion.isApplicable(request.getOrderReq());
		if (!applicable) {
			logger.warn("Coupon code used when not applicable. Coupon code : " + coupon.getCode());
			response.setCouponStatus(CouponResponseStatusEnum.NOT_APPLICABLE);
			return response;
		}
		Money discount = promotion.apply(request.getOrderReq());
		if (discount != null) {
			globalCouponUses.increment(discount);
			userCouponUses.increment(discount);
			boolean withinCouponUsesLimitsAfterApplying = validateCouponUses(coupon, globalCouponUses, userCouponUses);
			if (!withinCouponUsesLimitsAfterApplying) {
				logger.warn("Coupon exceeded limit. Coupon code : " + coupon.getCode());
				response.setCouponStatus(CouponResponseStatusEnum.NUMBER_OF_USES_EXCEEDED);
				return response;
			}
			
			globalPromotionUses.increment(discount);
			userPromotionUses.increment(discount);
			boolean withinPromotionUsesLimitsAfterApplying = validatePromotionUses(promotion, globalPromotionUses, userPromotionUses);
			if (!withinPromotionUsesLimitsAfterApplying) {
				logger.warn("Coupon exceeded Promotions limit. Coupon code : " + coupon.getCode());
				response.setCouponStatus(CouponResponseStatusEnum.NUMBER_OF_USES_EXCEEDED);
				return response;
			}
			
			response.setCouponStatus(CouponResponseStatusEnum.SUCCESS);
			response.setDiscountValue(discount.getAmount());
			response.setCouponCode(request.getCouponCode());
			response.setSessionToken(request.getSessionToken());
		}
		return response;
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
		Coupon coupon = getCoupon(request.getCouponCode(), userId);
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

		boolean userCuponUpdateStatus = couponDao.updateUserUses(coupon.getId(), userId, request.getDiscountValue(), request.getOrderId());
		if (!userCuponUpdateStatus) {
			logger.error("Unable to update the user uses for coupon code : " + coupon.getCode());
			response.setCommitCouponStatus(CommitCouponStatusEnum.INTERNAL_ERROR);
			return response;
		}

		boolean userPromotionUpdateStatus = promotionDao.updateUserUses(promotion.getId(), userId, request.getDiscountValue(), request.getOrderId());
		if (!userPromotionUpdateStatus) {
			logger.error("Unable to update user promotion uses for Coupon code : " + coupon.getCode());
			response.setCommitCouponStatus(CommitCouponStatusEnum.INTERNAL_ERROR);
			return response;
		}

		response.setCommitCouponStatus(CommitCouponStatusEnum.SUCCESS);
		return response;
	}
	
	@Override
	public ReleaseCouponResponse releaseCoupon(ReleaseCouponRequest request){
		ReleaseCouponResponse response = new ReleaseCouponResponse();

		if (request == null || StringUtils.isBlank(request.getSessionToken())) {
			response.setReleaseCouponStatus(ReleaseCouponStatusEnum.NO_SESSION);
			return response;
		}

		if (StringUtils.isBlank(request.getCouponCode())) {
			response.setReleaseCouponStatus(ReleaseCouponStatusEnum.INVALID_COUPON_CODE);
			return response;
		}

		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			//invalid session token
			response.setReleaseCouponStatus(ReleaseCouponStatusEnum.NO_SESSION);
			return response;
		}

		int userId = authentication.getUserID();

		//find the coupon.
		Coupon coupon = getCoupon(request.getCouponCode(), userId);
		if (coupon == null) {
			//we dont recognise this coupon code, bye bye
			response.setReleaseCouponStatus(ReleaseCouponStatusEnum.INVALID_COUPON_CODE);
			return response;
		}

		//find the associated promotion
		Promotion promotion = getPromotion(coupon.getPromotionId());
		if (promotion == null) {
			//problem.
			logger.error("No Promotion Found for Coupon code : " + coupon.getCode());
			response.setReleaseCouponStatus(ReleaseCouponStatusEnum.INTERNAL_ERROR);
			return response;
		}

		boolean userCuponUpdateStatus = couponDao.cancelUserUses(coupon.getId(), userId, request.getOrderId());
		if (!userCuponUpdateStatus) {
			logger.error("Unable to update the user uses for coupon code : " + coupon.getCode());
			response.setReleaseCouponStatus(ReleaseCouponStatusEnum.INTERNAL_ERROR);
			return response;
		}

		boolean userPromotionUpdateStatus = couponDao.cancelUserUses(coupon.getId(), userId, request.getOrderId());
		if (!userPromotionUpdateStatus) {
			logger.error("Unable to update user promotion uses for Coupon code : " + coupon.getCode());
			response.setReleaseCouponStatus(ReleaseCouponStatusEnum.INTERNAL_ERROR);
			return response;
		}

		response.setReleaseCouponStatus(ReleaseCouponStatusEnum.SUCCESS);
		return response;
	}

	private boolean validatePromotionUses(Promotion promotion, GlobalPromotionUses globalPromotionUses, UserPromotionUses userPromotionUses) {
		return promotion.isWithinLimits(globalPromotionUses, userPromotionUses);
	}

	private boolean validateCouponUses(Coupon coupon, GlobalCouponUses globalUses, UserCouponUses userUses) {
		return coupon.isWithinLimits(globalUses, userUses);
	}

	private Coupon getCoupon(String couponCode, int userId) {
		//check if we have coupon cached.
		Coupon coupon = couponCache.get(couponCode);
		if (coupon == null) {
			//load it using dao
			coupon = couponDao.load(couponCode, userId);

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
