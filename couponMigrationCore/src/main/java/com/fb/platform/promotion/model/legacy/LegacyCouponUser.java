/**
 * 
 */
package com.fb.platform.promotion.model.legacy;

/**
 * @author vinayak
 *
 */
public class LegacyCouponUser {

	private String couponCode;
	private int userId;

	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
