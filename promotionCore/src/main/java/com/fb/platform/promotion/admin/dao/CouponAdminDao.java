/**
 * 
 */
package com.fb.platform.promotion.admin.dao;

import java.util.List;

import com.fb.platform.promotion.model.coupon.CouponLimitsConfig;
import com.fb.platform.promotion.model.coupon.CouponType;

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

	public List<String> findExistingCodes(List<String> newCodes);

	public void createCouponsInBatch(List<String> couponCodes, int promotionId, CouponType couponType, CouponLimitsConfig limitsConfig);
}
