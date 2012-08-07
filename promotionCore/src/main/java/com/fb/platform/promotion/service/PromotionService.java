/**
 * 
 */
package com.fb.platform.promotion.service;

import java.math.BigDecimal;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.commons.PlatformException;
import com.fb.commons.to.Money;
import com.fb.platform.promotion.exception.CouponNotCommitedException;
import com.fb.platform.promotion.exception.CouponNotFoundException;
import com.fb.platform.promotion.exception.PromotionNotFoundException;
import com.fb.platform.promotion.model.Promotion;
import com.fb.platform.promotion.model.coupon.Coupon;
import com.fb.platform.promotion.model.scratchCard.ScratchCard;
import com.fb.platform.promotion.product.to.RefreshAutoPromotionRequest;
import com.fb.platform.promotion.product.to.RefreshAutoPromotionResponse;
import com.fb.platform.promotion.product.to.RefreshAutoPromotionResponseStatusEnum;
import com.fb.platform.promotion.to.ClearCouponCacheRequest;
import com.fb.platform.promotion.to.ClearCouponCacheResponse;
import com.fb.platform.promotion.to.ClearPromotionCacheRequest;
import com.fb.platform.promotion.to.ClearPromotionCacheResponse;
import com.fb.platform.promotion.to.OrderRequest;
import com.fb.platform.promotion.to.PromotionStatusEnum;

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

	/**
	 * Clears the cached promotion, if it is cached. Also clears any cached coupon belonging to this promotion.
	 * @param clearPromotionCacheRequest
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public ClearPromotionCacheResponse clearCache(ClearPromotionCacheRequest clearPromotionCacheRequest);

	/**
	 * Clears the cached coupon associated with this coupon code, if it is cached.
	 * @param clearCouponCacheRequest
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public ClearCouponCacheResponse clearCache(ClearCouponCacheRequest clearCouponCacheRequest);

	@Transactional(propagation=Propagation.REQUIRED)
	public ScratchCard loadScratchCard(String cardNumber);

	@Transactional(propagation=Propagation.REQUIRED)
	public String getCouponCode(String store, int userId);

	@Transactional(propagation=Propagation.REQUIRED)
	public void commitScratchCard(int scratchCardId, int userId, String couponCode);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean isUserFirstOrder(int userId);

	/**
	 * 
	 */
	public PromotionStatusEnum isApplicable(int userId, OrderRequest orderRequest, Money discountAmount, Coupon coupon, Promotion promotion, boolean isOrderCommitted);
	
	/**
	 * 
	 * @param userId
	 * @param orderId
	 * @param discountAmount
	 * @param coupon
	 * @param promotion
	 * @param isOrderCommitted
	 * @return
	 */
	public PromotionStatusEnum isApplicable(int userId, int orderId, Money discountAmount, Coupon coupon, Promotion promotion, boolean isOrderCommitted);
	
	/**
	 * 
	 * @return
	 */
	public RefreshAutoPromotionResponseStatusEnum refresh();
	
	
}
