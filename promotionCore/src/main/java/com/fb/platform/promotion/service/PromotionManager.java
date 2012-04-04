/**
 * 
 */
package com.fb.platform.promotion.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
public interface PromotionManager {

	@Transactional(propagation=Propagation.REQUIRED)
	public CouponResponse applyCoupon(CouponRequest request);

	@Transactional(propagation=Propagation.REQUIRED)
	public CommitCouponResponse commitCouponUse(CommitCouponRequest request);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public ReleaseCouponResponse releaseCoupon(ReleaseCouponRequest request);
}
