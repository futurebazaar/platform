/**
 * 
 */
package com.fb.platform.promotion.service;

import com.fb.platform.promotion.to.CommitCouponRequest;
import com.fb.platform.promotion.to.CommitCouponResponse;
import com.fb.platform.promotion.to.CouponRequest;
import com.fb.platform.promotion.to.CouponResponse;
import com.fb.platform.promotion.to.ReleaseCouponRequest;
import com.fb.platform.promotion.to.ReleaseCouponResponse;

/**
 * @author vinayak
 *
 */
public interface PromotionManager {

	public CouponResponse applyCoupon(CouponRequest request);

	public CommitCouponResponse commitCouponUse(CommitCouponRequest request);
	
	public ReleaseCouponResponse releaseCoupon(ReleaseCouponRequest request);
}
