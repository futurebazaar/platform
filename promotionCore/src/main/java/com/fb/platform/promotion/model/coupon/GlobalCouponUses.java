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
public class GlobalCouponUses implements Serializable {

	private int couponId = 0;
	private int currentCount = 0;
	private Money currentAmount = null;

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
