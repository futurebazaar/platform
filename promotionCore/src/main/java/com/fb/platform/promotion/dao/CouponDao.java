/**
 * 
 */
package com.fb.platform.promotion.dao;

import java.math.BigDecimal;

import com.fb.platform.promotion.model.coupon.Coupon;
import com.fb.platform.promotion.model.coupon.GlobalCouponUses;
import com.fb.platform.promotion.model.coupon.UserCouponUses;

/**
 * @author vinayak
 *
 */
public interface CouponDao {

	public Coupon load(String couponCode, int userId);

	public GlobalCouponUses loadGlobalUses(int couponId);

	public UserCouponUses loadUserUses(int couponId, int userId);

	public void updateUserUses(int couponId, int userId, BigDecimal valueApplied, int orderId);
	
	public void releaseCoupon(int couponId, int userId, int orderId);
	
	public boolean isCouponApplicable(int couponId, int userId, int orderId);

}
