/**
 * 
 */
package com.fb.platform.promotion.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.promotion.to.CouponRequest;
import com.fb.platform.promotion.to.CouponResponse;

/**
 * @author vinayak
 *
 */
@Transactional
public interface PromotionManager {

	@Transactional(propagation=Propagation.REQUIRED)
	public CouponResponse applyCoupon(CouponRequest request);

	@Transactional(propagation=Propagation.REQUIRED)
	public Object commitCouponUse(CouponRequest request);
}
