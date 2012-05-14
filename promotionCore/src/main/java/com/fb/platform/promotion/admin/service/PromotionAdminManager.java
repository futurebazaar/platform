package com.fb.platform.promotion.admin.service;


import com.fb.platform.promotion.admin.to.AssignCouponToUserRequest;
import com.fb.platform.promotion.admin.to.AssignCouponToUserResponse;
import com.fb.platform.promotion.admin.to.CreateCouponRequest;
import com.fb.platform.promotion.admin.to.CreateCouponResponse;
import com.fb.platform.promotion.admin.to.CreatePromotionRequest;
import com.fb.platform.promotion.admin.to.CreatePromotionResponse;
import com.fb.platform.promotion.admin.to.FetchRuleRequest;
import com.fb.platform.promotion.admin.to.FetchRuleResponse;
import com.fb.platform.promotion.admin.to.SearchPromotionRequest;
import com.fb.platform.promotion.admin.to.SearchPromotionResponse;

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
	 * This function fetches a list of promotions that meet the specified search criteria.
	 * @param searchPromotionRequest
	 * @return
	 */
	public SearchPromotionResponse searchPromotion(SearchPromotionRequest searchPromotionRequest);

	/**
	 * Creates the coupons as per the requirements specified in the request.
	 * @param createCouponRequest
	 * @return 
	 */
	public CreateCouponResponse createCoupons(CreateCouponRequest createCouponRequest);

	/**
	 * 
	 * @param request
	 * @return
	 */
	public AssignCouponToUserResponse assignCouponToUser(AssignCouponToUserRequest request);
}
