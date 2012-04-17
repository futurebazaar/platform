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
<<<<<<< Updated upstream
import com.fb.platform.promotion.admin.service.PromotionAdminService;
import com.fb.platform.promotion.dao.CouponDao;
import com.fb.platform.promotion.dao.PromotionDao;
import com.fb.platform.promotion.model.GlobalPromotionUses;
=======
>>>>>>> Stashed changes
import com.fb.platform.promotion.model.Promotion;
import com.fb.platform.promotion.model.coupon.Coupon;
<<<<<<< Updated upstream
import com.fb.platform.promotion.model.coupon.GlobalCouponUses;
import com.fb.platform.promotion.model.coupon.UserCouponUses;
import com.fb.platform.promotion.model.scratchCard.ScratchCard;
import com.fb.platform.promotion.rule.impl.ApplicableResponse;
import com.fb.platform.promotion.service.CouponAlreadyAssignedToUserException;
=======
>>>>>>> Stashed changes
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
import com.fb.platform.promotion.to.ClearCacheEnum;
import com.fb.platform.promotion.to.ClearCouponCacheResponse;
import com.fb.platform.promotion.to.ClearPromotionCacheRequest;
import com.fb.platform.promotion.to.ClearPromotionCacheResponse;
import com.fb.platform.promotion.to.CommitCouponRequest;
import com.fb.platform.promotion.to.CommitCouponResponse;
import com.fb.platform.promotion.to.CommitCouponStatusEnum;
import com.fb.platform.promotion.to.PromotionStatusEnum;
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
		
		if(request.getIsOrderCommitted()==null){
			request.setIsOrderCommitted(false);
		}
		
		response.setSessionToken(request.getSessionToken());
		response.setCouponCode(request.getCouponCode());
		
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


			PromotionStatusEnum isApplicableStatus = promotionService.isApplicable(userId, request.getOrderReq(), null, coupon, promotion, request.getIsOrderCommitted());
			if(PromotionStatusEnum.SUCCESS.compareTo(isApplicableStatus)!=0){
				response.setCouponStatus(isApplicableStatus);
				return response;
			}
			
			Money discount = promotion.apply(request.getOrderReq());
			
			if(discount!=null){
				response.setDiscountValue(discount.getAmount());
				PromotionStatusEnum postDiscountCheckStatus = promotionService.isApplicable(userId, request.getOrderReq(), discount, coupon, promotion, request.getIsOrderCommitted());
				if(PromotionStatusEnum.SUCCESS.compareTo(postDiscountCheckStatus)!=0){
					response.setCouponStatus(postDiscountCheckStatus);
					return response;
				}
			}
			
			response.setCouponStatus(ApplyCouponResponseStatusEnum.SUCCESS);

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
		response.setSessionToken(request.getSessionToken());
		
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

			PromotionStatusEnum isApplicableStatus = promotionService.isApplicable(userId, request.getOrderId(), new Money(request.getDiscountValue()), coupon, promotion, false);
			if(PromotionStatusEnum.SUCCESS.compareTo(isApplicableStatus)!=0){
				//response.setCommitCouponStatus(isApplicableStatus);
				return response;
			}
			
			//update the user uses for coupon and promotion
			promotionService.updateUserUses(coupon.getId(), promotion.getId(), userId, request.getDiscountValue(), request.getOrderId());

			response.setCommitCouponStatus(CommitCouponStatusEnum.SUCCESS);

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

<<<<<<< Updated upstream
	private ApplyCouponResponseStatusEnum validatePromotionUses(Promotion promotion, GlobalPromotionUses globalPromotionUses, UserPromotionUses userPromotionUses) {
		return promotion.isWithinLimits(globalPromotionUses, userPromotionUses);
	}

	private ApplyCouponResponseStatusEnum validateCouponUses(Coupon coupon, GlobalCouponUses globalUses, UserCouponUses userUses) {
		return coupon.isWithinLimits(globalUses, userUses);
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

	@Override
	public ClearPromotionCacheResponse clearCache(ClearPromotionCacheRequest clearPromotionCacheRequest) {
		
		ClearPromotionCacheResponse clearPromotionCacheResponse = new ClearPromotionCacheResponse();
		
		if (clearPromotionCacheRequest == null || StringUtils.isBlank(clearPromotionCacheRequest.getSessionToken())) {
			clearPromotionCacheResponse.setClearCacheEnum(ClearCacheEnum.NO_SESSION);
			return clearPromotionCacheResponse;
		}

		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(clearPromotionCacheRequest.getSessionToken());
		if (authentication == null) {
			//invalid session token
			clearPromotionCacheResponse.setClearCacheEnum(ClearCacheEnum.NO_SESSION);
			return clearPromotionCacheResponse;
		}
		
		clearPromotionCacheResponse = promotionService.clearCache(clearPromotionCacheRequest);
		return clearPromotionCacheResponse;
	}

	@Override
	public ClearCouponCacheResponse clearCache(com.fb.platform.promotion.to.ClearCouponCacheRequest clearCouponCacheRequest) {
		ClearCouponCacheResponse clearCouponCacheResponse = new ClearCouponCacheResponse();
		if (clearCouponCacheRequest == null || StringUtils.isBlank(clearCouponCacheRequest.getSessionToken())) {
			clearCouponCacheResponse.setClearCacheEnum(ClearCacheEnum.NO_SESSION);
			return clearCouponCacheResponse;
		}

		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(clearCouponCacheRequest.getSessionToken());
		if (authentication == null) {
			//invalid session token
			clearCouponCacheResponse.setClearCacheEnum(ClearCacheEnum.NO_SESSION);
			return clearCouponCacheResponse;
		}
		
		clearCouponCacheResponse = promotionService.clearCache(clearCouponCacheRequest);
		return clearCouponCacheResponse;
	}

=======
>>>>>>> Stashed changes
	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	public void setPromotionService(PromotionService promotionService) {
		this.promotionService = promotionService;
	}

	public void setPromotionAdminService(PromotionAdminService promotionAdminService) {
		this.promotionAdminService = promotionAdminService;
	}
}
