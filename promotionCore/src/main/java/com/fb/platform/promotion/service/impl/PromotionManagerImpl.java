/**
 * 
 */
package com.fb.platform.promotion.service.impl;

import java.math.BigDecimal;

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
import com.fb.platform.promotion.rule.impl.ApplicableResponse;
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
	private CouponCacheAccess couponCacheAccess = null;

	@Autowired
	private PromotionCacheAccess promotionCacheAccess = null;

	@Autowired
	private CouponDao couponDao = null;

	@Autowired
	private PromotionDao promotionDao = null;

	@Override
	public CouponResponse applyCoupon(CouponRequest request) {
		if(logger.isDebugEnabled()) {
			logger.debug("Applying coupon : " + request.getCouponCode());
		}
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

		//check if the couponId is already applied on the same orderId for the same user
		//If yes, then return error
		boolean isCouponApplicable = couponDao.isCouponApplicable(coupon.getId(), userId, request.getOrderReq().getOrderId());
		if(!isCouponApplicable){
			logger.error("Already an entry present for the user ="+ userId + " having couponId =" + coupon.getId() + " on the orderId = "+ request.getOrderReq().getOrderId());
			response.setCouponStatus(CouponResponseStatusEnum.ALREADY_APPLIED_COUPON_ON_ORDER);
			return response;
		}
		
		//check if the promotionId is already applied on the same orderId for the same user
		//If yes, then return error
		boolean isPromotionApplicable = promotionDao.isPromotionApplicable(promotion.getId(), userId, request.getOrderReq().getOrderId());
		if(!isPromotionApplicable){
			logger.error("Already an entry present for the user ="+ userId + " having promotionId =" + promotion.getId() + " on the orderId = "+ request.getOrderReq().getOrderId());
			response.setCouponStatus(CouponResponseStatusEnum.ALREADY_APPLIED_PROMOTION_ON_ORDER);
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
		ApplicableResponse applicable = promotion.isApplicable(request.getOrderReq());
		if (applicable.getStatusCode().compareTo(CouponResponseStatusEnum.SUCCESS) !=0) {
			logger.warn("Coupon code used when not applicable. Coupon code : " + coupon.getCode());
			response.setCouponStatus(applicable.getStatusCode());
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
		if(logger.isDebugEnabled()) {
			logger.debug("Commiting coupon usage : " + request.getCouponCode());
		}
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

		if(request.getDiscountValue().compareTo(BigDecimal.ZERO) <= 0){
			//discount cannot be negative or zero
			logger.error("Discount amount for commit is invalid : " + request.getDiscountValue());
			response.setCommitCouponStatus(CommitCouponStatusEnum.INVALID_DISCOUNT_AMOUNT);
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
		response.setSessionToken(request.getSessionToken());
		return response;
	}
	
	@Override
	public ReleaseCouponResponse releaseCoupon(ReleaseCouponRequest request){
		if(logger.isDebugEnabled()) {
			logger.debug("Release coupon : " + request.getCouponCode());
		}
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

		/*//check if the couponId is already applied on the same orderId for the same user
		//If no entry, then return error
		UserCouponUsesEntry releaseCoupon = couponDao.loadUserOrderCoupon(coupon.getId(), userId, request.getOrderId());
		if(null==releaseCoupon){
			logger.error("No entry present for the user ="+ userId + " having couponId =" + coupon.getId() + " on the orderId = "+ request.getOrderId());
			response.setReleaseCouponStatus(ReleaseCouponStatusEnum.INVALID_RELEASE_COUPON);
			return response;
		}
		
		//check if the promotionId is already applied on the same orderId for the same user
		//If no entry, then return error
		UserPromotionUsesEntry releasePromotion = promotionDao.loadUserOrderPromotion(promotion.getId(), userId, request.getOrderId());
		if(null==releasePromotion){
			logger.error("Already an entry present for the user ="+ userId + " having promotionId =" + promotion.getId() + " on the orderId = "+ request.getOrderId());
			response.setReleaseCouponStatus(ReleaseCouponStatusEnum.INVALID_RELEASE_COUPON);
			return response;
		}*/
		
		boolean releasedCouponEntry = couponDao.releaseCoupon(coupon.getId(), userId, request.getOrderId());
		if(!releasedCouponEntry){
			logger.error("Releasing coupon entry failed for the user ="+ userId + " having couponId =" + coupon.getId() + " on the orderId = "+ request.getOrderId());
			response.setReleaseCouponStatus(ReleaseCouponStatusEnum.INTERNAL_ERROR);
			return response;
		}
		
		boolean releasedPromotionEntry = promotionDao.releasePromotion(promotion.getId(), userId, request.getOrderId());
		if(!releasedPromotionEntry){
			logger.error("Releasing promotion entry failed for the user ="+ userId + " having promotionId =" + promotion.getId() + " on the orderId = "+ request.getOrderId());
			response.setReleaseCouponStatus(ReleaseCouponStatusEnum.INTERNAL_ERROR);
			return response;
		}
		
		/*boolean userCouponCancelStatus = couponDao.cancelUserUses(coupon.getId(), userId, request.getOrderId());
		if (!userCouponCancelStatus) {
			logger.error("Unable to update the user uses for coupon code : " + coupon.getCode());
			response.setReleaseCouponStatus(ReleaseCouponStatusEnum.INTERNAL_ERROR);
			return response;
		}

		boolean userPromotionCancelStatus = promotionDao.cancelUserUses(coupon.getId(), userId, request.getOrderId());
		if (!userPromotionCancelStatus) {
			logger.error("Unable to update user promotion uses for Coupon code : " + coupon.getCode());
			response.setReleaseCouponStatus(ReleaseCouponStatusEnum.INTERNAL_ERROR);
			return response;
		}*/

		response.setReleaseCouponStatus(ReleaseCouponStatusEnum.SUCCESS);
		response.setSessionToken(request.getSessionToken());
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
		Coupon coupon = couponCacheAccess.get(couponCode);
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
				couponCacheAccess.lock(couponCode);
				if (couponCacheAccess.get(couponCode) == null) {
					couponCacheAccess.put(couponCode, coupon);
				}
			} finally {
				couponCacheAccess.unlock(couponCode);
			}
		}
	}

	private Promotion getPromotion(int promotionId) {
		Promotion promotion = promotionCacheAccess.get(promotionId);
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
			promotionCacheAccess.lock(promotionId);
			if (promotionCacheAccess.get(promotionId) == null) {
				promotionCacheAccess.put(promotionId, promotion);
			}
		} finally {
			promotionCacheAccess.unlock(promotionId);
		}
	}

	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	public void setCouponDao(CouponDao couponDao) {
		this.couponDao = couponDao;
	}

	public void setPromotionDao(PromotionDao promotionDao) {
		this.promotionDao = promotionDao;
	}
}
