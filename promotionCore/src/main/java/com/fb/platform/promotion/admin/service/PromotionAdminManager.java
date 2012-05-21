package com.fb.platform.promotion.admin.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.promotion.admin.to.AssignCouponToUserRequest;
import com.fb.platform.promotion.admin.to.AssignCouponToUserResponse;
import com.fb.platform.promotion.admin.to.CreateCouponRequest;
import com.fb.platform.promotion.admin.to.CreateCouponResponse;
import com.fb.platform.promotion.admin.to.CreatePromotionRequest;
import com.fb.platform.promotion.admin.to.CreatePromotionResponse;
import com.fb.platform.promotion.admin.to.FetchRuleRequest;
import com.fb.platform.promotion.admin.to.FetchRuleResponse;
import com.fb.platform.promotion.admin.to.SearchCouponRequest;
import com.fb.platform.promotion.admin.to.SearchCouponResponse;
import com.fb.platform.promotion.admin.to.SearchPromotionRequest;
import com.fb.platform.promotion.admin.to.SearchPromotionResponse;
import com.fb.platform.promotion.admin.to.UpdatePromotionRequest;
import com.fb.platform.promotion.admin.to.UpdatePromotionResponse;
import com.fb.platform.promotion.admin.to.ViewCouponRequest;
import com.fb.platform.promotion.admin.to.ViewCouponResponse;
import com.fb.platform.promotion.admin.to.ViewPromotionRequest;
import com.fb.platform.promotion.admin.to.ViewPromotionResponse;

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
	 * Fetching a complete view of promotion. Including limits and rule config.
	 * @param viewPromotionRequest
	 * @return
	 */
	public ViewPromotionResponse viewPromotion(ViewPromotionRequest viewPromotionRequest);
	
	/**
	 * This function updates an existing promotion.
	 * @param promotion
	 * @return
	 */
	public UpdatePromotionResponse updatePromotion(UpdatePromotionRequest updatePromotionRequest);

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
	/**
	 * Searches coupons on the basis of the input given in the request.
	 * @param searchCouponRequest
	 * @return
	 */
	public SearchCouponResponse searchCoupons(SearchCouponRequest searchCouponRequest);
	/**
	 * The API returns the coupon detail for the given request.
	 * @param viewCouponRequest
	 * @return
	 */
	public ViewCouponResponse viewCoupon(ViewCouponRequest viewCouponRequest);
	
}
