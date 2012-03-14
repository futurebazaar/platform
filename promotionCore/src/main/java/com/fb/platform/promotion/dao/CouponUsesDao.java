package com.fb.platform.promotion.dao;


public interface CouponUsesDao {

	
	/**
	 * @param couponCode Coupon Code which is used
	 * @param userId user who used the coupon code
	 * @return the number of uses by the user object
	 */
	public int getUses(String  couponCode,Integer userId);

	/**
	 * @param couponCode Coupon Code which is used
	 * @param userId user who used the coupon code
	 */
	public void removeUse(String couponCode,Integer userId);

	/**
	 * @param couponCode Coupon Code which is used
	 * @param userId user who used the coupon code
	 */
	public void addUse(String couponCode,Integer userId);

		
}
