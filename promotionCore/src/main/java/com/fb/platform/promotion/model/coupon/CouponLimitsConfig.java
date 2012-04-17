/**
 * 
 */
package com.fb.platform.promotion.model.coupon;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.model.PromotionLimitsConfig;
import com.fb.platform.promotion.to.PromotionStatusEnum;

/**
 * @author vinayak
 *
 */
public class CouponLimitsConfig implements Serializable {
	
	private static Log logger = LogFactory.getLog(CouponLimitsConfig.class);

	private int maxUses;
	private Money maxAmount;
	private int maxUsesPerUser;
	private Money maxAmountPerUser;

	public PromotionStatusEnum isWithinLimits(GlobalCouponUses globalUses, UserCouponUses userUses) {
		Money zeroMoney = new Money(BigDecimal.ZERO);
		
		logger.info("Limits for Coupon : " 
				+ "  globalUsesCount = "+globalUses.getCurrentCount() + ", maxUses = " + maxUses 
				+ ", globalUsesAmount = " + globalUses.getCurrentAmount() + ", maxAmount = " + maxAmount 
				+ ", UserUsesCount = "+userUses.getCurrentCount() + ", maxUsesPerUser = " + maxUsesPerUser
				+ ", UserUsesAmount = "+userUses.getCurrentAmount() + ", maxAmountPerUser = "+maxAmountPerUser);
		
		if (maxUses > 0 && maxUses < globalUses.getCurrentCount()) {
			return PromotionStatusEnum.TOTAL_MAX_USES_EXCEEDED;
		}
		if (maxAmount.gt(zeroMoney) && maxAmount.lteq(globalUses.getCurrentAmount())) {
			return PromotionStatusEnum.TOTAL_MAX_AMOUNT_EXCEEDED;
		}
		if (maxUsesPerUser > 0 && maxUsesPerUser < userUses.getCurrentCount()) {
			return PromotionStatusEnum.TOTAL_MAX_USES_PER_USER_EXCEEDED;
		}
		if (maxAmountPerUser.gt(zeroMoney) && maxAmountPerUser.lteq(userUses.getCurrentAmount())) {
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
