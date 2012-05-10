package com.fb.platform.promotion.admin.service;


import com.fb.platform.promotion.admin.to.CreateCouponRequest;
import com.fb.platform.promotion.admin.to.CreateCouponResponse;
import com.fb.platform.promotion.to.CreatePromotionRequest;
import com.fb.platform.promotion.to.CreatePromotionResponse;
import com.fb.platform.promotion.to.FetchRuleRequest;
import com.fb.platform.promotion.to.FetchRuleResponse;

/**
 * @author nehaga
 *
 */
public interface PromotionAdminManager {
	
	/**
	 * This function returns a list of rules and their corresponding rule config items.
	 * @param fetchRuleRequest
	 * @return
	 */
	public FetchRuleResponse fetchRules(FetchRuleRequest fetchRuleRequest); 
	
	/**
	 * This function creates a new promotion and returns the promotion Id.
	 * @param createPromotionRequest
	 * @return
	 */
	public CreatePromotionResponse createPromotion(CreatePromotionRequest createPromotionRequest);

	/**
	 * Creates the coupons as per the requirements specified in the request.
	 * @param createCouponRequest
	 * @return 
	 */
	public CreateCouponResponse createCoupons(CreateCouponRequest createCouponRequest);
}
