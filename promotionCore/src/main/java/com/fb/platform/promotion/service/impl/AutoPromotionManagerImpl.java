/**
 * 
 */
package com.fb.platform.promotion.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.platform.auth.AuthenticationService;
import com.fb.platform.auth.AuthenticationTO;
import com.fb.platform.promotion.exception.NoActiveAutoPromotionFoundException;
import com.fb.platform.promotion.model.Promotion;
import com.fb.platform.promotion.product.model.promotion.AutoPromotion;
import com.fb.platform.promotion.product.to.ApplyAutoPromotionRequest;
import com.fb.platform.promotion.product.to.ApplyAutoPromotionResponse;
import com.fb.platform.promotion.product.to.ApplyAutoPromotionResponseStatusEnum;
import com.fb.platform.promotion.product.to.CommitAutoPromotionRequest;
import com.fb.platform.promotion.product.to.CommitAutoPromotionResponse;
import com.fb.platform.promotion.product.to.GetApplicablePromotionsRequest;
import com.fb.platform.promotion.product.to.GetApplicablePromotionsResponse;
import com.fb.platform.promotion.product.to.GetAppliedAutoPromotionRequest;
import com.fb.platform.promotion.product.to.GetAppliedAutoPromotionResponse;
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

		if (request == null || StringUtils.isBlank(request.getSessionToken())) {
			response.setApplyAutoPromotionStatus(ApplyAutoPromotionResponseStatusEnum.NO_SESSION);
			return response;
		}

		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			//invalid session token
			response.setApplyAutoPromotionStatus(ApplyAutoPromotionResponseStatusEnum.NO_SESSION);
			return response;
		}

		response.setSessionToken(request.getSessionToken());

		int userId = authentication.getUserID();

		try {
			List<Integer> activeAutoPromotions = promotionService.getActiveAutoPromotions();

			//loop through all the active auto promotions and see any of them is applicable on the order
			for (Integer promotionId : activeAutoPromotions) {
				Promotion promotion = promotionService.getPromotion(promotionId);
				if (!promotion.isActive() || !promotion.isWithinDates()) {
					continue;
				}
				if (!(promotion instanceof AutoPromotion)) {
					//wrong promotion type, should not happen but check just in case.
					continue;
				}
				AutoPromotion autoPromotion = (AutoPromotion)promotion;
			}
		} catch (NoActiveAutoPromotionFoundException e) {
			//this is ok.
			response.setApplyAutoPromotionStatus(ApplyAutoPromotionResponseStatusEnum.SUCCESS);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetAppliedAutoPromotionResponse getAppliedPromotions(
			GetAppliedAutoPromotionRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
}
