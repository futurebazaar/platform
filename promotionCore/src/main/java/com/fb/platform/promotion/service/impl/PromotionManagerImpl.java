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
import com.fb.platform.promotion.admin.service.PromotionAdminService;
import com.fb.platform.promotion.dao.CouponDao;
import com.fb.platform.promotion.dao.PromotionDao;
import com.fb.platform.promotion.model.GlobalPromotionUses;
import com.fb.platform.promotion.model.Promotion;
import com.fb.platform.promotion.model.UserPromotionUses;
import com.fb.platform.promotion.model.coupon.Coupon;
import com.fb.platform.promotion.model.coupon.GlobalCouponUses;
import com.fb.platform.promotion.model.coupon.UserCouponUses;
import com.fb.platform.promotion.model.scratchCard.ScratchCard;
import com.fb.platform.promotion.rule.impl.ApplicableResponse;
import com.fb.platform.promotion.service.CouponAlreadyAssignedToUserException;
import com.fb.platform.promotion.service.CouponNotCommitedException;
import com.fb.platform.promotion.service.CouponNotFoundException;
import com.fb.platform.promotion.service.PromotionManager;
import com.fb.platform.promotion.service.PromotionNotFoundException;
import com.fb.platform.promotion.service.PromotionService;
import com.fb.platform.promotion.service.ScratchCardNotFoundException;
import com.fb.platform.promotion.to.ApplyCouponRequest;
import com.fb.platform.promotion.to.ApplyCouponResponse;
import com.fb.platform.promotion.to.ApplyCouponResponseStatusEnum;
import com.fb.platform.promotion.to.ApplyScratchCardRequest;
import com.fb.platform.promotion.to.ApplyScratchCardResponse;
import com.fb.platform.promotion.to.ApplyScratchCardStatus;
import com.fb.platform.promotion.to.CommitCouponRequest;
import com.fb.platform.promotion.to.CommitCouponResponse;
import com.fb.platform.promotion.to.CommitCouponStatusEnum;
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

	@Autowired
	private PromotionAdminService promotionAdminService = null;

	@Override
	public ApplyCouponResponse applyCoupon(ApplyCouponRequest request) {
		if(logger.isDebugEnabled()) {
			logger.debug("Applying coupon : " + request.getCouponCode());
		}
		ApplyCouponResponse response = new ApplyCouponResponse();

		if (request == null || StringUtils.isBlank(request.getSessionToken())) {
			response.setCouponStatus(ApplyCouponResponseStatusEnum.NO_SESSION);
			return response;
		}

		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			//invalid session token
			response.setCouponStatus(ApplyCouponResponseStatusEnum.NO_SESSION);
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
			
			response.setPromoName(promotion.getName());
			response.setPromoDescription(promotion.getDescription());

			//check if the couponId is already applied on the same orderId for the same user
			//If yes, then return error
			boolean isCouponApplicable = couponDao.isCouponApplicable(coupon.getId(), userId, request.getOrderReq().getOrderId());
			if(!isCouponApplicable){
				logger.error("Already an entry present for the user ="+ userId + " having couponId =" + coupon.getId() + " on the orderId = "+ request.getOrderReq().getOrderId());
				response.setCouponStatus(ApplyCouponResponseStatusEnum.ALREADY_APPLIED_COUPON_ON_ORDER);
				return response;
			}
			
			//check if the promotionId is already applied on the same orderId for the same user
			//If yes, then return error
			boolean isPromotionApplicable = promotionDao.isPromotionApplicable(promotion.getId(), userId, request.getOrderReq().getOrderId());
			if(!isPromotionApplicable){
				logger.error("Already an entry present for the user ="+ userId + " having promotionId =" + promotion.getId() + " on the orderId = "+ request.getOrderReq().getOrderId());
				response.setCouponStatus(ApplyCouponResponseStatusEnum.ALREADY_APPLIED_PROMOTION_ON_ORDER);
				return response;
			}
			
			GlobalCouponUses globalCouponUses = couponDao.loadGlobalUses(coupon.getId());
			UserCouponUses userCouponUses = couponDao.loadUserUses(coupon.getId(), userId);
			ApplyCouponResponseStatusEnum withinCouponUsesLimitsStatus = validateCouponUses(coupon, globalCouponUses, userCouponUses);
			if (withinCouponUsesLimitsStatus.compareTo(ApplyCouponResponseStatusEnum.LIMIT_SUCCESS)!=0) {
				logger.warn("Coupon exceeded limit. Coupon code : " + coupon.getCode());
				response.setCouponStatus(withinCouponUsesLimitsStatus);
				return response;
			}

			GlobalPromotionUses globalPromotionUses = promotionDao.loadGlobalUses(promotion.getId());
			UserPromotionUses userPromotionUses = promotionDao.loadUserUses(promotion.getId(), userId);
			ApplyCouponResponseStatusEnum withinPromotionUsesLimitsStatus = validatePromotionUses(promotion, globalPromotionUses, userPromotionUses);
			if (withinPromotionUsesLimitsStatus.compareTo(ApplyCouponResponseStatusEnum.LIMIT_SUCCESS)!=0) {
				logger.warn("Coupon exceeded Promotions limit. Coupon code : " + coupon.getCode());
				response.setCouponStatus(withinPromotionUsesLimitsStatus);
				return response;
			}

			//check if the promotion is applicable on this request.
			ApplicableResponse applicable = promotion.isApplicable(request.getOrderReq());
			if (applicable.getStatusCode().compareTo(ApplyCouponResponseStatusEnum.SUCCESS) !=0) {
				logger.warn("Coupon code used when not applicable. Coupon code : " + coupon.getCode());
				response.setCouponStatus(applicable.getStatusCode());
				return response;
			}

			Money discount = promotion.apply(request.getOrderReq());
			if (discount != null) {
				globalCouponUses.increment(discount);
				userCouponUses.increment(discount);
				ApplyCouponResponseStatusEnum withinCouponUsesLimitsStatusAfterApplying = validateCouponUses(coupon, globalCouponUses, userCouponUses);
				if (withinCouponUsesLimitsStatusAfterApplying.compareTo(ApplyCouponResponseStatusEnum.LIMIT_SUCCESS)!=0) {
					logger.warn("Coupon exceeded limit. Coupon code : " + coupon.getCode());
					response.setCouponStatus(withinCouponUsesLimitsStatusAfterApplying);
					return response;
				}
				
				globalPromotionUses.increment(discount);
				userPromotionUses.increment(discount);
				ApplyCouponResponseStatusEnum withinPromotionUsesLimitsStatusAfterApplying = validatePromotionUses(promotion, globalPromotionUses, userPromotionUses);
				if (withinPromotionUsesLimitsStatusAfterApplying.compareTo(ApplyCouponResponseStatusEnum.LIMIT_SUCCESS) != 0) {
					logger.warn("Coupon exceeded Promotions limit. Coupon code : " + coupon.getCode());
					response.setCouponStatus(withinPromotionUsesLimitsStatusAfterApplying);
					return response;
				}
				
				response.setCouponStatus(ApplyCouponResponseStatusEnum.SUCCESS);
				response.setDiscountValue(discount.getAmount());
				response.setCouponCode(request.getCouponCode());
				response.setSessionToken(request.getSessionToken());
			}

		} catch (CouponNotFoundException e) {
			//we dont recognise this coupon code, bye bye
			response.setCouponStatus(ApplyCouponResponseStatusEnum.INVALID_COUPON_CODE);
		} catch (PromotionNotFoundException e) {
			logger.error("No Promotion Found for Coupon code : " + request.getCouponCode());
			response.setCouponStatus(ApplyCouponResponseStatusEnum.INTERNAL_ERROR);
		} catch (PlatformException e) {
			logger.error("Error while applying the Coupon code : " + request.getCouponCode(), e);
			response.setCouponStatus(ApplyCouponResponseStatusEnum.INTERNAL_ERROR);
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

	private ApplyCouponResponseStatusEnum validatePromotionUses(Promotion promotion, GlobalPromotionUses globalPromotionUses, UserPromotionUses userPromotionUses) {
		return promotion.isWithinLimits(globalPromotionUses, userPromotionUses);
	}

	private ApplyCouponResponseStatusEnum validateCouponUses(Coupon coupon, GlobalCouponUses globalUses, UserCouponUses userUses) {
		return coupon.isWithinLimits(globalUses, userUses);
	}

	@Override
	public void clearCache(int promotionId) {
	}

	@Override
	public void clearCache(String couponCode) {
	}

	@Override
	public ApplyScratchCardResponse applyScratchCard(ApplyScratchCardRequest request) {
		if(logger.isDebugEnabled()) {
			logger.debug("applyScratchCard : " + request.getCardNumber());
		}
		ApplyScratchCardResponse response = new ApplyScratchCardResponse();
		response.setSessionToken(request.getSessionToken());

		if (request == null || StringUtils.isBlank(request.getSessionToken())) {
			response.setApplyScratchCardStatus(ApplyScratchCardStatus.NO_SESSION);
			return response;
		}

		if (StringUtils.isBlank(request.getCardNumber())) {
			response.setApplyScratchCardStatus(ApplyScratchCardStatus.INVALID_SCRATCH_CARD);
			return response;
		}

		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			//invalid session token
			response.setApplyScratchCardStatus(ApplyScratchCardStatus.NO_SESSION);
			return response;
		}

		int userId = authentication.getUserID();

		try {
			//load the scratch card
			ScratchCard scratchCard = promotionService.loadScratchCard(request.getCardNumber());
			if (!scratchCard.isActive()) {
				logger.warn("Inactive scratch card used : " + request.getCardNumber());
				response.setApplyScratchCardStatus(ApplyScratchCardStatus.INVALID_SCRATCH_CARD);
				return response;
			}

			//get a coupon for the store associated with the scratch card.
			String couponCode = promotionService.getCouponCode(scratchCard.getStore(), userId);
			//assign coupon to this user
			promotionAdminService.assignCouponToUser(userId, couponCode, 0);

			//commit the scratch card use by this user
			promotionService.commitScratchCard(scratchCard.getId(), userId, couponCode);

			response.setCouponCode(couponCode);
			response.setApplyScratchCardStatus(ApplyScratchCardStatus.SUCCESS);

		} catch (ScratchCardNotFoundException e) {
			logger.error("scratchCard not found : " + request.getCardNumber(), e);
			response.setApplyScratchCardStatus(ApplyScratchCardStatus.INVALID_SCRATCH_CARD);
		} catch (CouponAlreadyAssignedToUserException e) {
			logger.error("Coupon alrady given to scratch card : " + request.getCardNumber(), e);
			response.setApplyScratchCardStatus(ApplyScratchCardStatus.COUPON_ALREADY_ASSIGNED_TO_USER);
		} catch (PlatformException e) {
			logger.error("Error while applying the scratchCard : " + request.getCardNumber(), e);
			response.setApplyScratchCardStatus(ApplyScratchCardStatus.INTERNAL_ERROR);
		}

		return response;
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

	public void setPromotionAdminService(PromotionAdminService promotionAdminService) {
		this.promotionAdminService = promotionAdminService;
	}
}
