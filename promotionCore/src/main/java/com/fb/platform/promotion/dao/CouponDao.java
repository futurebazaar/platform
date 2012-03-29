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

	public Coupon load(String couponCode);

	public GlobalCouponUses loadGlobalUses(int couponId);

	public UserCouponUses loadUserUses(int couponId, int userId);

	public boolean updateGlobalUses(int couponId, BigDecimal valueApplied);

	public boolean updateUserUses(int couponId, int userId, BigDecimal valueApplied);
}
