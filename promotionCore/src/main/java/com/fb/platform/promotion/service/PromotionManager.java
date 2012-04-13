/**
 * 
 */
package com.fb.platform.promotion.service;

import com.fb.platform.promotion.to.ApplyCouponRequest;
import com.fb.platform.promotion.to.ApplyCouponResponse;
import com.fb.platform.promotion.to.CommitCouponRequest;
import com.fb.platform.promotion.to.CommitCouponResponse;
import com.fb.platform.promotion.to.ReleaseCouponRequest;
import com.fb.platform.promotion.to.ReleaseCouponResponse;

/**
 * @author vinayak
 *
 */
public interface PromotionManager {

	public ApplyCouponResponse applyCoupon(ApplyCouponRequest request);

	public CommitCouponResponse commitCouponUse(CommitCouponRequest request);
	
	public ReleaseCouponResponse releaseCoupon(ReleaseCouponRequest request);

	public void clearCache(int promotionId);

	public void clearCache(String couponCode);
}
