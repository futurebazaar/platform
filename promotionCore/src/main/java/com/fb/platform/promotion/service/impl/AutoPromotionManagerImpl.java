/**
 * 
 */
package com.fb.platform.promotion.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.PlatformException;
import com.fb.platform.auth.AuthenticationService;
import com.fb.platform.auth.AuthenticationTO;
import com.fb.platform.promotion.exception.NoActiveAutoPromotionFoundException;
import com.fb.platform.promotion.exception.PromotionNotFoundException;
import com.fb.platform.promotion.model.OrderDiscount;
import com.fb.platform.promotion.model.Promotion;
import com.fb.platform.promotion.product.model.promotion.AutoPromotion;
import com.fb.platform.promotion.product.to.ApplyAutoPromotionRequest;
import com.fb.platform.promotion.product.to.ApplyAutoPromotionResponse;
import com.fb.platform.promotion.product.to.ApplyAutoPromotionResponseStatusEnum;
import com.fb.platform.promotion.product.to.CommitAutoPromotionRequest;
import com.fb.platform.promotion.product.to.CommitAutoPromotionResponse;
import com.fb.platform.promotion.product.to.CommitAutoPromotionResponseStatusEnum;
import com.fb.platform.promotion.product.to.GetApplicablePromotionsRequest;
import com.fb.platform.promotion.product.to.GetApplicablePromotionsResponse;
import com.fb.platform.promotion.product.to.GetAppliedAutoPromotionRequest;
import com.fb.platform.promotion.product.to.GetAppliedAutoPromotionResponse;
import com.fb.platform.promotion.product.to.GetAppliedAutoPromotionResponseStatusEnum;
import com.fb.platform.promotion.product.to.RefreshAutoPromotionRequest;
import com.fb.platform.promotion.product.to.RefreshAutoPromotionResponse;
import com.fb.platform.promotion.product.to.RefreshAutoPromotionResponseStatusEnum;
import com.fb.platform.promotion.service.AutoPromotionManager;
import com.fb.platform.promotion.service.PromotionService;

/**
 * @author vinayak
 *
 */
public class AutoPromotionManagerImpl implements AutoPromotionManager {

	private static Log logger = LogFactory.getLog(AutoPromotionManagerImpl.class);

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private PromotionService promotionService = null;


	@Override
	public GetApplicablePromotionsResponse getApplicablePromotions(GetApplicablePromotionsRequest request) {
		return null;
	}

	@Override
	public ApplyAutoPromotionResponse apply(ApplyAutoPromotionRequest request) {

		if (logger.isDebugEnabled()) {
			if (request.getOrderReq() != null) {
				logger.debug("Applying auto promotion on order id : " + request.getOrderReq().getOrderId());
			} else {
				logger.debug("Null order request received for auto promotion apply.");
			}
		}
		ApplyAutoPromotionResponse response = new ApplyAutoPromotionResponse();

		try {
			List<Integer> autoPromotionsToApply = null;
			boolean modification = false;
			//List<Integer> alreadyAppliedPromotions = request.getAppliedPromotions();
			if (request.getAppliedPromotions() != null && request.getAppliedPromotions().size() > 0) {
				//this is a modification order
				modification = true;
				autoPromotionsToApply = request.getAppliedPromotions();
			} else {
				//get the list of all active auto promotion ids
				autoPromotionsToApply = promotionService.getActiveAutoPromotions();
			}

			//reset any discounted price coming in
			request.resetDiscountedPrice();

			OrderDiscount orderResponse = new OrderDiscount();
			orderResponse.setOrderRequest(request.getOrderReq());

			//loop through all the auto promotions and see any of them is applicable on the order
			for (Integer promotionId : autoPromotionsToApply) {
				Promotion promotion = promotionService.getPromotion(promotionId);

				if (!modification) {
					if (!promotion.isActive() || !promotion.isWithinDates()) {
						continue;
					}
				}
				if (!(promotion instanceof AutoPromotion)) {
					//wrong promotion type, should not happen but check just in case.
					continue;
				}
				AutoPromotion autoPromotion = (AutoPromotion)promotion;
				if (!autoPromotion.isApplicableOn(request.getOrderReq(), 0)) {
					continue;
				}
				boolean applied = autoPromotion.apply(request.getOrderReq(), orderResponse);
				if (applied) {
					response.getAppliedPromotions().add(promotion);
				}
			}
			response.setOrderDiscount(orderResponse);
			response.setApplyAutoPromotionStatus(ApplyAutoPromotionResponseStatusEnum.SUCCESS);
		} catch (NoActiveAutoPromotionFoundException e) {
			//this is ok.
			response.setApplyAutoPromotionStatus(ApplyAutoPromotionResponseStatusEnum.SUCCESS);
		} catch (PlatformException e) {
			logger.error(e);
			response.setApplyAutoPromotionStatus(ApplyAutoPromotionResponseStatusEnum.INTERNAL_ERROR);
		}
		return response;
	}

	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	public void setPromotionService(PromotionService promotionService) {
		this.promotionService = promotionService;
	}

	@Override
	public RefreshAutoPromotionResponse refresh(RefreshAutoPromotionRequest request) {
		RefreshAutoPromotionResponse response = new RefreshAutoPromotionResponse();
		
		if (request == null || StringUtils.isBlank(request.getSessionToken())) {
			response.setRefreshAutoPromotionStatus(RefreshAutoPromotionResponseStatusEnum.NO_SESSION);
			return response;
		}

		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			//invalid session token
			response.setRefreshAutoPromotionStatus(RefreshAutoPromotionResponseStatusEnum.NO_SESSION);
			return response;
		}
		
		response.setSessionToken(request.getSessionToken());
		try {
			promotionService.refresh();
			response.setRefreshAutoPromotionStatus(RefreshAutoPromotionResponseStatusEnum.SUCCESS);
		} catch (Exception e) {
			response.setRefreshAutoPromotionStatus(RefreshAutoPromotionResponseStatusEnum.INTERNAL_ERROR);
			logger.error("Error in auto promotion cache refresh.", e);
		}
		return response;
	}

	@Override
	public CommitAutoPromotionResponse commit(CommitAutoPromotionRequest request) {
		logger.info("Commiting auto promotion usage.");
		CommitAutoPromotionResponse response = new CommitAutoPromotionResponse();
		
		response.setSessionToken(request.getSessionToken());
		if (request == null || StringUtils.isBlank(request.getSessionToken())) {
			response.setCommitAutoPromotionStatus(CommitAutoPromotionResponseStatusEnum.NO_SESSION);
			return response;
		}
		
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			//invalid session token
			response.setCommitAutoPromotionStatus(CommitAutoPromotionResponseStatusEnum.NO_SESSION);
			return response;
		}
		
		int userId = authentication.getUserID();
		
		try {
			for(Integer promotionId : request.getAppliedPromotionsList()) {
				promotionService.deleteUserAutoPromotionUses(userId, request.getOrderId());
				promotionService.updateUserPromotionUses(promotionId, userId, request.getOrderId());
			}
			response.setCommitAutoPromotionStatus(CommitAutoPromotionResponseStatusEnum.SUCCESS);
		} catch (PromotionNotFoundException e) {
			logger.error("No Promotion Found for promotion codes for order : " + request.getOrderId());
			response.setCommitAutoPromotionStatus(CommitAutoPromotionResponseStatusEnum.INTERNAL_ERROR);
		} catch (PlatformException e) {
			logger.error("Error while committing the promotion usage for order: " + request.getOrderId(), e);
			response.setCommitAutoPromotionStatus(CommitAutoPromotionResponseStatusEnum.INTERNAL_ERROR);
		}
		
		return response;
	}

	@Override
	public GetAppliedAutoPromotionResponse getAppliedPromotions(GetAppliedAutoPromotionRequest request) {
		logger.info("Fetching auto promotion usage for order : " + request.getOrderId());
		GetAppliedAutoPromotionResponse response = new GetAppliedAutoPromotionResponse();
		
		response.setSessionToken(request.getSessionToken());
		if (request == null || StringUtils.isBlank(request.getSessionToken())) {
			response.setGetAppliedAutoPromotionStatus(GetAppliedAutoPromotionResponseStatusEnum.NO_SESSION);
			return response;
		}
		
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			//invalid session token
			response.setGetAppliedAutoPromotionStatus(GetAppliedAutoPromotionResponseStatusEnum.NO_SESSION);
			return response;
		}
		
		int userId = authentication.getUserID();
		
		try {
			List<Integer> promoList = promotionService.getUserAutoPromotionUses(userId, request.getOrderId());
			List<Promotion> promotionsList  = new ArrayList<Promotion>();
			for(int promoId : promoList) {
				promotionsList.add(promotionService.getPromotion(promoId));
			}
			response.setPromotionList(promotionsList);
			response.setGetAppliedAutoPromotionStatus(GetAppliedAutoPromotionResponseStatusEnum.SUCCESS);
		} catch (PlatformException e) {
			logger.error("Error while fetching the auto promotion usage for user : " + userId + ", order: " + request.getOrderId(), e);
			response.setGetAppliedAutoPromotionStatus(GetAppliedAutoPromotionResponseStatusEnum.INTERNAL_ERROR);
		}
		
		return response;
	}
}
