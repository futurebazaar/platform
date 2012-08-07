/**
 * 
 */
package com.fb.platform.promotion.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.platform.auth.AuthenticationService;
import com.fb.platform.promotion.product.to.ApplyAutoPromotionRequest;
import com.fb.platform.promotion.product.to.ApplyAutoPromotionResponse;
import com.fb.platform.promotion.product.to.GetApplicablePromotionsRequest;
import com.fb.platform.promotion.product.to.GetApplicablePromotionsResponse;
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
		try {
			List<Integer> activeAutoPromotions = promotionService.getActiveAutoPromotions();
		} catch (Exception e) {
		}
		return null;
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
}
