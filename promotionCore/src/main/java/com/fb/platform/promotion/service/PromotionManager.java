/**
 * 
 */
package com.fb.platform.promotion.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.promotion.to.CommitCouponRequest;
import com.fb.platform.promotion.to.CommitCouponResponse;
import com.fb.platform.promotion.to.ApplyCouponRequest;
import com.fb.platform.promotion.to.ApplyCouponResponse;
import com.fb.platform.promotion.to.ReleaseCouponRequest;
import com.fb.platform.promotion.to.ReleaseCouponResponse;

/**
 * @author vinayak
 *
 */
public interface PromotionManager {

	@Transactional(propagation=Propagation.REQUIRED)
	public ApplyCouponResponse applyCoupon(ApplyCouponRequest request);

	public CommitCouponResponse commitCouponUse(CommitCouponRequest request);
	
	public ReleaseCouponResponse releaseCoupon(ReleaseCouponRequest request);
}
