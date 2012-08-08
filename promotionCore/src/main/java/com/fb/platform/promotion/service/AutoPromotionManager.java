/**
 * 
 */
package com.fb.platform.promotion.service;

import com.fb.platform.promotion.product.to.ApplyAutoPromotionRequest;
import com.fb.platform.promotion.product.to.ApplyAutoPromotionResponse;
import com.fb.platform.promotion.product.to.CommitAutoPromotionRequest;
import com.fb.platform.promotion.product.to.CommitAutoPromotionResponse;
import com.fb.platform.promotion.product.to.GetApplicablePromotionsRequest;
import com.fb.platform.promotion.product.to.GetApplicablePromotionsResponse;
import com.fb.platform.promotion.product.to.GetAppliedAutoPromotionRequest;
import com.fb.platform.promotion.product.to.GetAppliedAutoPromotionResponse;
import com.fb.platform.promotion.product.to.RefreshAutoPromotionRequest;
import com.fb.platform.promotion.product.to.RefreshAutoPromotionResponse;

/**
 * @author vinayak
 *
 */
public interface AutoPromotionManager {

	public GetApplicablePromotionsResponse getApplicablePromotions(GetApplicablePromotionsRequest request);

	public ApplyAutoPromotionResponse apply(ApplyAutoPromotionRequest request);
	
	public RefreshAutoPromotionResponse refresh(RefreshAutoPromotionRequest request);
	
	public CommitAutoPromotionResponse commit(CommitAutoPromotionRequest request);
	
	public GetAppliedAutoPromotionResponse getAppliedPromotions(GetAppliedAutoPromotionRequest request);
	
}
