/**
 * 
 */
package com.fb.platform.promotion.service;

import java.math.BigDecimal;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.commons.PlatformException;
import com.fb.platform.promotion.model.Promotion;
import com.fb.platform.promotion.model.coupon.Coupon;

/**
 * @author vinayak
 *
 */
@Transactional
public interface PromotionService {

	/**
	 * Returns the Coupon associated with the coupon code. Tries to read it from cache.
	 * If not found in cache then loads it using DAO. Caches the global coupons.
	 * @param couponCode
	 * @param userId
	 * @throws CouponNotFoundException When no coupon is found matching the couponCode.
	 * @throws PlatformException When an unrecovarable error happens.
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Coupon getCoupon(String couponCode, int userId) throws CouponNotFoundException, PlatformException;

	/**
	 * Returns the Promotion identified by the promotionId. 
	 * @param promotionId
	 * @return
	 * @throws PromotionNotFoundException When no promotion is found matching the promotionId.
	 * @throws PlatformException When an unrecovarable error happens.
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Promotion getPromotion(int promotionId) throws PromotionNotFoundException, PlatformException;

	/**
	 * Releases the coupon and promotion. This resets the previous commit of the coupon and promotion.
	 * @param couponId
	 * @param promotionId
	 * @param userId
	 * @param orderId
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void release(int couponId, int promotionId, int userId, int orderId) throws CouponNotCommitedException;

	/**
	 * Called when committing the coupon. Records the users user of the promotion and coupon.
	 * @param couponId
	 * @param promotionId
	 * @param userId
	 * @param valueApplied
	 * @param orderId
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void updateUserUses(int couponId, int promotionId, int userId, BigDecimal valueApplied, int orderId);
}
