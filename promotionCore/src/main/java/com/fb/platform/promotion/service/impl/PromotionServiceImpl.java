/**
 * 
 */
package com.fb.platform.promotion.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.fb.commons.PlatformException;
import com.fb.platform.promotion.cache.CouponCacheAccess;
import com.fb.platform.promotion.cache.PromotionCacheAccess;
import com.fb.platform.promotion.dao.CouponDao;
import com.fb.platform.promotion.dao.PromotionDao;
import com.fb.platform.promotion.dao.ScratchCardDao;
import com.fb.platform.promotion.model.Promotion;
import com.fb.platform.promotion.model.coupon.Coupon;
import com.fb.platform.promotion.model.coupon.CouponType;
import com.fb.platform.promotion.model.scratchCard.ScratchCard;
import com.fb.platform.promotion.service.CouponNotFoundException;
import com.fb.platform.promotion.service.PromotionNotFoundException;
import com.fb.platform.promotion.service.PromotionService;

/**
 * @author vinayak
 *
 */
public class PromotionServiceImpl implements PromotionService {

	@Autowired
	private CouponCacheAccess couponCacheAccess = null;

	@Autowired
	private PromotionCacheAccess promotionCacheAccess = null;

	@Autowired
	private CouponDao couponDao = null;

	@Autowired
	private PromotionDao promotionDao = null;

	@Autowired
	private ScratchCardDao scratchCardDao = null;

	@Override
	public Coupon getCoupon(String couponCode, int userId) throws CouponNotFoundException, PlatformException {
		//check if we have coupon cached.
		Coupon coupon = couponCacheAccess.get(couponCode);
		if (coupon == null) {
			//load it using dao
			try {
				coupon = couponDao.load(couponCode, userId);
			} catch (DataAccessException e) {
				throw new PlatformException("Error while loading the coupon. Coupon code : " + couponCode + ". User Id : " + userId, e);
			}

			if (coupon != null) {
				cacheCoupon(couponCode, coupon);
			} else {
				throw new CouponNotFoundException("Coupon not found. Coupon code : " + couponCode + ". User Id : " + userId);
			}
		}
		return coupon;
	}

	private void cacheCoupon(String couponCode, Coupon coupon) {
		//cache the global coupon
		if (coupon.getType() == CouponType.GLOBAL) {
			try {
				couponCacheAccess.lock(couponCode);
				if (couponCacheAccess.get(couponCode) == null) {
					couponCacheAccess.put(couponCode, coupon);
				}
			} finally {
				couponCacheAccess.unlock(couponCode);
			}
		}
	}

	@Override
	public Promotion getPromotion(int promotionId) throws PromotionNotFoundException, PlatformException {
		Promotion promotion = promotionCacheAccess.get(promotionId);
		if (promotion == null) {
			//its not cached, load it
			try {
				promotion = promotionDao.load(promotionId);
			} catch (DataAccessException e) {
				throw new PlatformException("Error while loading the promotion. Promotion Id  : " + promotionId, e);
			}

			if (promotion != null) {
				cachePromotion(promotionId, promotion);
			} else {
				throw new PromotionNotFoundException("Promotion not found. Promotion Id : " + promotionId);
			}
		}
		return promotion;
	}

	private void cachePromotion(Integer promotionId, Promotion promotion) {
		//TODO need to figure out which promotions to cache
		try {
			promotionCacheAccess.lock(promotionId);
			if (promotionCacheAccess.get(promotionId) == null) {
				promotionCacheAccess.put(promotionId, promotion);
			}
		} finally {
			promotionCacheAccess.unlock(promotionId);
		}
	}

	@Override
	public void release(int couponId, int promotionId, int userId, int orderId) {
		try {
			couponDao.releaseCoupon(couponId, userId, orderId);
			promotionDao.releasePromotion(promotionId, userId, orderId);
		} catch (DataAccessException e) {
			throw new PlatformException("Error while releasing the coupon and promotion. couponId : " + couponId + ", promotionId : " + promotionId, e);
		}
	}

	@Override
	public void updateUserUses(int couponId, int promotionId, int userId, BigDecimal valueApplied, int orderId) {
		try {
			couponDao.updateUserUses(couponId, userId, valueApplied, orderId);
			promotionDao.updateUserUses(promotionId, userId, valueApplied, orderId);
		} catch (DataAccessException e) {
			throw new PlatformException("Error while updating the uses for coupon and promotion. " +
							"couponId : " + couponId + ", " +
							"promotionId : " + promotionId + ", " +
							"userId : " + userId + ", " +
							"orderId : " + orderId, e);
		}
	}

	public void setCouponDao(CouponDao couponDao) {
		this.couponDao = couponDao;
	}

	public void setPromotionDao(PromotionDao promotionDao) {
		this.promotionDao = promotionDao;
	}

	@Override
	public void clearCache(int promotionId) {
		Promotion promotion = promotionCacheAccess.get(promotionId);
		if (promotion != null) {
			promotionCacheAccess.clear(promotionId);
		}
	}

	@Override
	public void clearCache(String couponCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ScratchCard loadScratchCard(String cardNumber) {
		ScratchCard scratchCard = null;
		try {
			scratchCard = scratchCardDao.load(cardNumber);
		} catch (DataAccessException e) {
			throw new PlatformException("Error while loading the scratch card from the DB", e);
		}
		return scratchCard;
	}

	@Override
	public String getCouponCode(String store, int userId) {
		List<String> couponCodesForStore = scratchCardDao.getCouponCodesForStore(store);
		//TODO in future, if there are multiple couponCodes per store, assign the one already not assigned to user.
		return couponCodesForStore.get(0);
	}

	@Override
	public void commitScratchCard(int scratchCardId, int userId, String couponCode) {
		try {
			scratchCardDao.commitUse(scratchCardId, userId, couponCode);
		} catch (DataAccessException e) {
			throw new PlatformException("Error while committing the scratchCard. scratchCardId : " + scratchCardId + ", userId : " + userId + ", couponCode : " + couponCode, e);
		}
	}
}
