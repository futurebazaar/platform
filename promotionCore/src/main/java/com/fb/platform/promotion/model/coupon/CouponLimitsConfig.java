/**
 * 
 */
package com.fb.platform.promotion.model.coupon;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.to.ApplyCouponResponseStatusEnum;

/**
 * @author vinayak
 *
 */
public class CouponLimitsConfig implements Serializable {

	private int maxUses;
	private Money maxAmount;
	private int maxUsesPerUser;
	private Money maxAmountPerUser;

	public ApplyCouponResponseStatusEnum isWithinLimits(GlobalCouponUses globalUses, UserCouponUses userUses) {
		Money zeroMoney = new Money(BigDecimal.ZERO);
		
		if (maxUses > 0 && maxUses < globalUses.getCurrentCount()) {
			return ApplyCouponResponseStatusEnum.TOTAL_MAX_USES_EXCEEDED;
		}
		if (maxAmount.gt(zeroMoney) && maxAmount.lt(globalUses.getCurrentAmount())) {
			return ApplyCouponResponseStatusEnum.TOTAL_MAX_AMOUNT_EXCEEDED;
		}
		if (maxUsesPerUser > 0 && maxUsesPerUser < userUses.getCurrentCount()) {
			return ApplyCouponResponseStatusEnum.TOTAL_MAX_USES_PER_USER_EXCEEDED;
		}
		if (maxAmountPerUser.gt(zeroMoney) && maxAmountPerUser.lt(userUses.getCurrentAmount())) {
			return ApplyCouponResponseStatusEnum.TOTAL_MAX_AMOUNT_PER_USER_EXCEEDED;
		}
		return ApplyCouponResponseStatusEnum.LIMIT_SUCCESS;
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
