/**
 * 
 */
package com.fb.platform.promotion.service.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.fb.commons.PlatformException;
import com.fb.commons.to.Money;
import com.fb.platform.promotion.cache.CouponCacheAccess;
import com.fb.platform.promotion.cache.PromotionCacheAccess;
import com.fb.platform.promotion.dao.CouponDao;
import com.fb.platform.promotion.dao.PromotionDao;
import com.fb.platform.promotion.dao.ScratchCardDao;
import com.fb.platform.promotion.model.GlobalPromotionUses;
import com.fb.platform.promotion.model.Promotion;
import com.fb.platform.promotion.model.UserPromotionUses;
import com.fb.platform.promotion.model.UserPromotionUsesEntry;
import com.fb.platform.promotion.model.coupon.Coupon;
import com.fb.platform.promotion.model.coupon.CouponType;
import com.fb.platform.promotion.model.coupon.GlobalCouponUses;
import com.fb.platform.promotion.model.coupon.UserCouponUses;
import com.fb.platform.promotion.model.coupon.UserCouponUsesEntry;
import com.fb.platform.promotion.model.scratchCard.ScratchCard;
import com.fb.platform.promotion.service.CouponNotFoundException;
import com.fb.platform.promotion.service.PromotionNotFoundException;
import com.fb.platform.promotion.service.PromotionService;
import com.fb.platform.promotion.to.ClearCacheEnum;
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
public class PromotionServiceImpl implements PromotionService {

	private static Log logger = LogFactory.getLog(PromotionServiceImpl.class);
	
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
	public PromotionStatusEnum isApplicable(int userId, OrderRequest orderRequest, Money discountAmount, Coupon coupon, Promotion promotion, boolean isCouponCommitted){
		
		PromotionStatusEnum checkBasicConstraints = isApplicable(userId, orderRequest.getOrderId(), orderRequest, discountAmount, coupon, promotion, isCouponCommitted);
		if(PromotionStatusEnum.SUCCESS.compareTo(checkBasicConstraints) !=0){
			logger.warn("Basic checks failed for couponCode - " + coupon.getCode()+" userId= "+userId+" orderId= "+orderRequest.getOrderId());
			return checkBasicConstraints;
		}

		return PromotionStatusEnum.SUCCESS;
	}
	
	@Override
	public PromotionStatusEnum isApplicable(int userId, int orderId, Money discountAmount, Coupon coupon, Promotion promotion, boolean isCouponCommitted){
		
		PromotionStatusEnum checkBasicConstraints = isApplicable(userId, orderId, null, discountAmount, coupon, promotion, isCouponCommitted);
		if(PromotionStatusEnum.SUCCESS.compareTo(checkBasicConstraints) !=0){
			logger.warn("Basic checks failed for couponCode - " + coupon.getCode()+" userId= "+userId+" orderId= "+orderId);
			return checkBasicConstraints;
		}

		
		return PromotionStatusEnum.SUCCESS;
	}
	
	private PromotionStatusEnum isApplicable(int userId, int orderId, OrderRequest orderRequest, Money discountAmount, Coupon coupon, Promotion promotion, boolean isCouponCommitted){

		GlobalCouponUses globalCouponUses = couponDao.loadGlobalUses(coupon.getId());
		UserCouponUses userCouponUses = couponDao.loadUserUses(coupon.getId(), userId);
		
		GlobalPromotionUses globalPromotionUses = promotionDao.loadGlobalUses(promotion.getId());
		UserPromotionUses userPromotionUses = promotionDao.loadUserUses(promotion.getId(), userId);
		
		if(isCouponCommitted){
			// load the coupon and promotion uses as it is a already committed coupon
			UserCouponUsesEntry userCouponUsesEntry = couponDao.load(coupon.getId(), userId, orderId);
			UserPromotionUsesEntry userPromotionUsesEntry = promotionDao.load(promotion.getId(), userId, orderId);
			
			if(userCouponUsesEntry==null || userPromotionUsesEntry==null){
				logger.error("No entry present for thecommitted coupon for user ="+ userId + " having couponId =" + coupon.getId() + " on the orderId = "+ orderId);
				return PromotionStatusEnum.NO_APPLIED_COUPON_ON_ORDER;
			}
			
			//decrement the uses from both coupon and promotion
			globalCouponUses.decrement(userCouponUsesEntry.getDiscountAmount());
			userCouponUses.decrement(userCouponUsesEntry.getDiscountAmount());
			
			globalPromotionUses.decrement(userPromotionUsesEntry.getDiscountAmount());
			userPromotionUses.decrement(userPromotionUsesEntry.getDiscountAmount());
		}
		
		//check if the couponId is already applied on the same orderId for the same user
		//If yes, then return error
		if(!isCouponCommitted){
			boolean isCouponApplicable = couponDao.isCouponApplicable(coupon.getId(), userId, orderId);
			if(!isCouponApplicable){
				logger.error("Already an entry present for the user ="+ userId + " having couponId =" + coupon.getId() + " on the orderId = "+ orderId);
				return PromotionStatusEnum.ALREADY_APPLIED_COUPON_ON_ORDER;
			}
			
			//check if the promotionId is already applied on the same orderId for the same user
			//If yes, then return error
			boolean isPromotionApplicable = promotionDao.isPromotionApplicable(promotion.getId(), userId, orderId);
			if(!isPromotionApplicable){
				logger.error("Already an entry present for the user ="+ userId + " having promotionId =" + promotion.getId() + " on the orderId = "+ orderId);
				return PromotionStatusEnum.ALREADY_APPLIED_PROMOTION_ON_ORDER;
			}
		}
		
		PromotionStatusEnum withinCouponUsesLimitsStatus = validateCouponUses(coupon, globalCouponUses, userCouponUses);
		if (withinCouponUsesLimitsStatus.compareTo(PromotionStatusEnum.LIMIT_SUCCESS)!=0) {
			logger.warn("Coupon exceeded limit. Coupon code : " + coupon.getCode());
			return withinCouponUsesLimitsStatus;
		}
		
		PromotionStatusEnum withinPromotionUsesLimitsStatus = validatePromotionUses(promotion, globalPromotionUses, userPromotionUses);
		if (withinPromotionUsesLimitsStatus.compareTo(PromotionStatusEnum.LIMIT_SUCCESS) != 0) {
			logger.warn("Coupon exceeded Promotions limit. Coupon code : " + coupon.getCode());
			return withinPromotionUsesLimitsStatus;
		}
		
		//check if the promotion is applicable on this request.
		PromotionStatusEnum promotionStatusEnum = promotion.isApplicable(orderRequest);
		if (PromotionStatusEnum.SUCCESS.compareTo(promotionStatusEnum) !=0) {
			logger.warn("Coupon code used when not applicable. Coupon code : " + coupon.getCode());
			return promotionStatusEnum;
		}
		
		//if the discount amount is not null, then validate the limits taking
		// into account the discount amount
		if(null!=discountAmount){
			globalCouponUses.increment(discountAmount);
			userCouponUses.increment(discountAmount);
			PromotionStatusEnum withinCouponUsesLimitsStatusAfterApplying = validateCouponUses(coupon, globalCouponUses, userCouponUses);
			if (withinCouponUsesLimitsStatusAfterApplying.compareTo(PromotionStatusEnum.LIMIT_SUCCESS)!=0) {
				logger.warn("Coupon exceeded limit. Coupon code : " + coupon.getCode());
				return withinCouponUsesLimitsStatusAfterApplying;
			}
			
			globalPromotionUses.increment(discountAmount);
			userPromotionUses.increment(discountAmount);
			PromotionStatusEnum withinPromotionUsesLimitsStatusAfterApplying = validatePromotionUses(promotion, globalPromotionUses, userPromotionUses);
			if (withinPromotionUsesLimitsStatusAfterApplying.compareTo(PromotionStatusEnum.LIMIT_SUCCESS) != 0) {
				logger.warn("Coupon exceeded Promotions limit. Coupon code : " + coupon.getCode());
				return withinPromotionUsesLimitsStatusAfterApplying;
			}
		}
		
		return PromotionStatusEnum.SUCCESS;
	}

	
	private PromotionStatusEnum validatePromotionUses(Promotion promotion, GlobalPromotionUses globalPromotionUses, UserPromotionUses userPromotionUses) {
		return promotion.isWithinLimits(globalPromotionUses, userPromotionUses);
	}

	private PromotionStatusEnum validateCouponUses(Coupon coupon, GlobalCouponUses globalUses, UserCouponUses userUses) {
		return coupon.isWithinLimits(globalUses, userUses);
	}
	
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
	public ClearPromotionCacheResponse clearCache(ClearPromotionCacheRequest clearPromotionCacheRequest) {
		Promotion promotion = promotionCacheAccess.get(clearPromotionCacheRequest.getPromotionId());
		ClearPromotionCacheResponse clearPromotionCacheResponse = new ClearPromotionCacheResponse();
		if (promotion != null) {
			clearPromotionCacheResponse.setPromotionId(clearPromotionCacheRequest.getPromotionId());
			boolean isClear = promotionCacheAccess.clear(clearPromotionCacheRequest.getPromotionId());
			if(isClear == true) {
				clearPromotionCacheResponse.setClearCacheEnum(ClearCacheEnum.SUCCESS);
			} else {
				clearPromotionCacheResponse.setClearCacheEnum(ClearCacheEnum.INTERNAL_ERROR);
			}
		} else {
			clearPromotionCacheResponse.setClearCacheEnum(ClearCacheEnum.CACHE_NOT_FOUND);
		}
		//get all the coupons cached corresponding to this promotion and then clear them from cache
		Collection<Coupon> couponsList = couponDao.getCouponsForPromotion(clearPromotionCacheRequest.getPromotionId());
		Iterator<Coupon> iterator = couponsList.iterator();
		while(iterator.hasNext()) {
			Coupon coupon = iterator.next();
			ClearCouponCacheRequest clearCouponCacheRequest = new ClearCouponCacheRequest();
			clearCouponCacheRequest.setCouponCode(coupon.getCode());
			clearCouponCacheRequest.setSessionToken(clearPromotionCacheRequest.getSessionToken());
			clearPromotionCacheResponse.getClearCouponCacheResponse().add(clearCache(clearCouponCacheRequest));
		}
		return clearPromotionCacheResponse;
	}

	@Override
	public ClearCouponCacheResponse clearCache(ClearCouponCacheRequest clearCouponCacheRequest) {
		Coupon coupon = couponCacheAccess.get(clearCouponCacheRequest.getCouponCode());
		ClearCouponCacheResponse clearCouponCacheResponse = new ClearCouponCacheResponse();
		clearCouponCacheResponse.setCouponCode(clearCouponCacheRequest.getCouponCode());
		clearCouponCacheResponse.setSessionToken(clearCouponCacheRequest.getSessionToken());
		if (coupon != null) {
			boolean isClear = couponCacheAccess.clear(clearCouponCacheRequest.getCouponCode());
			if(isClear == true) {
				clearCouponCacheResponse.setClearCacheEnum(ClearCacheEnum.SUCCESS);
			} else {
				clearCouponCacheResponse.setClearCacheEnum(ClearCacheEnum.INTERNAL_ERROR);
			}
		} else {
			clearCouponCacheResponse.setClearCacheEnum(ClearCacheEnum.CACHE_NOT_FOUND);
		}
		return clearCouponCacheResponse;
		
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
