/**
 * 
 */
package com.fb.platform.promotion.model.coupon;

import java.io.Serializable;

import com.fb.commons.to.Money;

/**
 * @author vinayak
 *
 */
public class UserCouponUses implements Serializable {

	private int userId;
	private int currentCount;
	private Money currentAmount;
	private int couponId;

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getCurrentCount() {
		return currentCount;
	}
	public void setCurrentCount(int currentCount) {
		this.currentCount = currentCount;
	}
	public Money getCurrentAmount() {
		return currentAmount;
	}
	public void setCurrentAmount(Money currentAmount) {
		this.currentAmount = currentAmount;
	}
	public int getCouponId() {
		return couponId;
	}
	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}
}
