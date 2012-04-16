/**
 * 
 */
package com.fb.platform.promotion.admin.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.promotion.service.CouponAlreadyAssignedToUserException;

/**
 * @author vinayak
 *
 */
@Transactional
public interface PromotionAdminService {

	/**
	 * Assigns a PRE_ISSUE coupon identified by couponCode to userId.
	 * @param userId
	 * @param couponCode
	 * @param overrideUserLimit
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void assignCouponToUser(int userId, String couponCode, int overrideUserLimit) throws CouponAlreadyAssignedToUserException;

}
