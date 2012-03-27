/**
 * 
 */
package com.fb.platform.promotion.service;

import com.fb.platform.promotion.to.CouponRequest;

/**
 * @author vinayak
 *
 */
public interface PromotionManager {

	public Object applyCoupon(CouponRequest request);
}
