/**
 * 
 */
package com.fb.platform.promotion.service.impl;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.PlatformException;
import com.fb.commons.to.Money;
import com.fb.platform.auth.AuthenticationService;
import com.fb.platform.auth.AuthenticationTO;
import com.fb.platform.promotion.dao.CouponDao;
import com.fb.platform.promotion.dao.PromotionDao;
import com.fb.platform.promotion.model.GlobalPromotionUses;
import com.fb.platform.promotion.model.Promotion;
import com.fb.platform.promotion.model.UserPromotionUses;
import com.fb.platform.promotion.model.coupon.Coupon;
import com.fb.platform.promotion.model.coupon.GlobalCouponUses;
import com.fb.platform.promotion.model.coupon.UserCouponUses;
import com.fb.platform.promotion.rule.impl.ApplicableResponse;
import com.fb.platform.promotion.service.CouponNotCommitedException;
import com.fb.platform.promotion.service.CouponNotFoundException;
import com.fb.platform.promotion.service.PromotionManager;
import com.fb.platform.promotion.service.PromotionNotFoundException;
import com.fb.platform.promotion.service.PromotionService;
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
	private CouponDao couponDao = null;

	@Autowired
	private PromotionDao promotionDao = null;

	@Autowired
	private PromotionService promotionService = null;

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

		Coupon coupon = null;
		Promotion promotion = null;

		try {
			//find the coupon.
			coupon = promotionService.getCoupon(request.getCouponCode(), userId);
			//find the associated promotion
			promotion = promotionService.getPromotion(coupon.getPromotionId());

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

		} catch (CouponNotFoundException e) {
			//we dont recognise this coupon code, bye bye
			response.setCouponStatus(CouponResponseStatusEnum.INVALID_COUPON_CODE);
		} catch (PromotionNotFoundException e) {
			logger.error("No Promotion Found for Coupon code : " + request.getCouponCode());
			response.setCouponStatus(CouponResponseStatusEnum.INTERNAL_ERROR);
		} catch (PlatformException e) {
			logger.error("Error while applying the Coupon code : " + request.getCouponCode(), e);
			response.setCouponStatus(CouponResponseStatusEnum.INTERNAL_ERROR);
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

		if(request.getDiscountValue().compareTo(BigDecimal.ZERO) <= 0){
			//discount cannot be negative or zero
			logger.error("Discount amount for commit is invalid : " + request.getDiscountValue());
			response.setCommitCouponStatus(CommitCouponStatusEnum.INVALID_DISCOUNT_AMOUNT);
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

		Coupon coupon = null;
		Promotion promotion = null;

		try {
			coupon = promotionService.getCoupon(request.getCouponCode(), userId);
			promotion = promotionService.getPromotion(coupon.getPromotionId());

			//update the user uses for coupon and promotion
			promotionService.updateUserUses(coupon.getId(), promotion.getId(), userId, request.getDiscountValue(), request.getOrderId());

			response.setCommitCouponStatus(CommitCouponStatusEnum.SUCCESS);
			response.setSessionToken(request.getSessionToken());

		} catch (CouponNotFoundException e) {
			//we dont recognise this coupon code, bye bye
			response.setCommitCouponStatus(CommitCouponStatusEnum.INVALID_COUPON_CODE);
		} catch (PromotionNotFoundException e) {
			logger.error("No Promotion Found for Coupon code : " + request.getCouponCode());
			response.setCommitCouponStatus(CommitCouponStatusEnum.INTERNAL_ERROR);
		} catch (PlatformException e) {
			logger.error("Error while committing the Coupon code : " + request.getCouponCode(), e);
			response.setCommitCouponStatus(CommitCouponStatusEnum.INTERNAL_ERROR);
		}

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

		Coupon coupon = null;
		Promotion promotion = null;

		try {
			coupon = promotionService.getCoupon(request.getCouponCode(), userId);
			promotion = promotionService.getPromotion(coupon.getPromotionId());

			//release the coupon and promotion
			promotionService.release(coupon.getId(), promotion.getId(), userId, request.getOrderId());

			response.setReleaseCouponStatus(ReleaseCouponStatusEnum.SUCCESS);
			response.setSessionToken(request.getSessionToken());

		} catch (CouponNotFoundException e) {
			//we dont recognise this coupon code, bye bye
			response.setReleaseCouponStatus(ReleaseCouponStatusEnum.INVALID_COUPON_CODE);
		} catch (PromotionNotFoundException e) {
			logger.error("No Promotion Found for Coupon code : " + request.getCouponCode());
			response.setReleaseCouponStatus(ReleaseCouponStatusEnum.INTERNAL_ERROR);
		} catch (CouponNotCommitedException e) {
			logger.error("Coupon is not committed, can not release : " + request.getCouponCode());
			response.setReleaseCouponStatus(ReleaseCouponStatusEnum.COUPON_NOT_COMMITTED);
		} catch (PlatformException e) {
			logger.error("Error while release the Coupon code : " + request.getCouponCode(), e);
			response.setReleaseCouponStatus(ReleaseCouponStatusEnum.INTERNAL_ERROR);
		}
		return response;
	}

	private boolean validatePromotionUses(Promotion promotion, GlobalPromotionUses globalPromotionUses, UserPromotionUses userPromotionUses) {
		return promotion.isWithinLimits(globalPromotionUses, userPromotionUses);
	}

	private boolean validateCouponUses(Coupon coupon, GlobalCouponUses globalUses, UserCouponUses userUses) {
		return coupon.isWithinLimits(globalUses, userUses);
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

	public void setPromotionService(PromotionService promotionService) {
		this.promotionService = promotionService;
	}
}
