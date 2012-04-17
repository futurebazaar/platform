/**
 * 
 */
package com.fb.platform.promotion.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.to.ApplyCouponResponseStatusEnum;
import com.fb.platform.promotion.to.PromotionStatusEnum;

/**
 * @author vinayak
 *
 */
public class PromotionLimitsConfig implements Serializable {

	private int maxUses = 0;
	private Money maxAmount = null;
	private int maxUsesPerUser = 0;
	private Money maxAmountPerUser = null;

	public PromotionStatusEnum isWithinLimit(GlobalPromotionUses globalUses, UserPromotionUses userUses) {
		Money zeroMoney = new Money(BigDecimal.ZERO);
		
		if (maxUses > 0 && maxUses < globalUses.getCurrentCount()) {
			return PromotionStatusEnum.TOTAL_MAX_USES_EXCEEDED;
		}
		if (maxAmount.gt(zeroMoney) && maxAmount.lt(globalUses.getCurrentAmount())) {
			return PromotionStatusEnum.TOTAL_MAX_AMOUNT_EXCEEDED;
		}
		if (maxUsesPerUser > 0 && maxUsesPerUser < userUses.getCurrentCount()) {
			return PromotionStatusEnum.TOTAL_MAX_USES_PER_USER_EXCEEDED;
		}
		if (maxAmountPerUser.gt(zeroMoney) && maxAmountPerUser.lt(userUses.getCurrentAmount())) {
			return PromotionStatusEnum.TOTAL_MAX_AMOUNT_PER_USER_EXCEEDED;
		}
		return PromotionStatusEnum.LIMIT_SUCCESS;
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
