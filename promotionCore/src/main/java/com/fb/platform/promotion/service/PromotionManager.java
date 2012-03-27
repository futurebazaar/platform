/**
 * 
 */
package com.fb.platform.promotion.service;

import com.fb.platform.promotion.to.OrderCouponRequest;

/**
 * @author vinayak
 *
 */
public interface PromotionManager {

	public Object applyCoupon(OrderCouponRequest request);
}
