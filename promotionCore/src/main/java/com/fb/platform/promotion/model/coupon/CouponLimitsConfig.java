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
public class CouponLimitsConfig implements Serializable {

	private int maxUses;
	private Money maxAmount;
	private int maxUsesPerUser;
	private Money maxAmountPerUser;

	public boolean isWithinLimits(GlobalCouponUses globalUses, UserCouponUses userUses) {
		if (maxUses <= globalUses.getCurrentCount()) {
			return false;
		}
		if (maxAmount.lteq(globalUses.getCurrentAmount())) {
			return false;
		}
		if (maxUsesPerUser <= userUses.getCurrentCount()) {
			return false;
		}
		if (maxAmountPerUser.lteq(userUses.getCurrentAmount())) {
			return false;
		}
		return true;
	}

	public void setMaxUses(int maxUses) {
		this.maxUses = maxUses;
	}
	public void setMaxAmount(Money maxAmount) {
		this.maxAmount = maxAmount;
	}
	public void setMaxUsesPerUser(int maxUsesPerUser) {
		this.maxUsesPerUser = maxUsesPerUser;
	}
	public void setMaxAmountPerUser(Money maxAmountPerUser) {
		this.maxAmountPerUser = maxAmountPerUser;
	}

	public int getMaxUses() {
		return maxUses;
	}

	public Money getMaxAmount() {
		return maxAmount;
	}

	public int getMaxUsesPerUser() {
		return maxUsesPerUser;
	}

	public Money getMaxAmountPerUser() {
		return maxAmountPerUser;
	}
}
