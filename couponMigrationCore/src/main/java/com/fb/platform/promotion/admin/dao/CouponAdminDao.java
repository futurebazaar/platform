/**
 * 
 */
package com.fb.platform.promotion.admin.dao;

/**
 * @author vinayak
 *
 */
public interface CouponAdminDao {

	/**
	 * Assigns the PRE_ISSUE coupon identified by the couponCode to the userId.
	 * @param userId
	 * @param couponId
	 * @param overriddenUserLimit
	 */
	public void assignToUser(int userId, String couponCode, int overriddenUserLimit);
}
