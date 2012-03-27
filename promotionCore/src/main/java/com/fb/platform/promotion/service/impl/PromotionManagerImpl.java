/**
 * 
 */
package com.fb.platform.promotion.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.platform.auth.AuthenticationService;
import com.fb.platform.auth.AuthenticationTO;
import com.fb.platform.promotion.cache.CouponCacheAccess;
import com.fb.platform.promotion.cache.PromotionCacheAccess;
import com.fb.platform.promotion.dao.CouponDao;
import com.fb.platform.promotion.dao.PromotionDao;
import com.fb.platform.promotion.model.GlobalPromotionUses;
import com.fb.platform.promotion.model.Promotion;
import com.fb.platform.promotion.model.UserPromotionUses;
import com.fb.platform.promotion.model.coupon.Coupon;
import com.fb.platform.promotion.model.coupon.GlobalCouponUses;
import com.fb.platform.promotion.model.coupon.UserCouponUses;
import com.fb.platform.promotion.service.PromotionManager;
import com.fb.platform.promotion.to.CouponRequest;

/**
 * @author vinayak
 *
 */
public class PromotionManagerImpl implements PromotionManager {

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private CouponCacheAccess couponCache = null;

	@Autowired
	private PromotionCacheAccess promotionCache = null;

	@Autowired
	private CouponDao couponDao = null;

	@Autowired
	private PromotionDao promotionDao = null;

	@Override
	public Object applyCoupon(CouponRequest request) {
		if (request == null) {
			return null; //TODO
		}
		if (StringUtils.isBlank(request.getSessionToken())) {
			//TODO return no session error 
		}
		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			//invalid session token
			//TODO return no session error
		}
		int userId = authentication.getUserID();

		//check if we have coupon cached.
		Coupon coupon = getCoupon(request.getCouponCode());
		if (coupon == null) {
			//we dont recognise this coupon code, bye bye
			//TODO return error
			return null;
		}

		//find the associated promotion
		Promotion promotion = getPromotion(coupon.getPromotionId());
		if (promotion == null) {
			//problem.
			//TODO return error
			return null;
		}

		boolean withinCouponUsesLimits = validateCouponUses(coupon, userId);
		if (!withinCouponUsesLimits) {
			//TODO return error
			return null;
		}

		boolean withinPromotionUsesLimits = validatePromotionUses(promotion, userId);
		if (!withinPromotionUsesLimits) {
			//TODO return error
			return null;
		}

		return promotion.apply(request);
	}

	private boolean validatePromotionUses(Promotion promotion, int userId) {
		GlobalPromotionUses globalUses = promotionDao.loadGlobalUses(promotion.getId());
		UserPromotionUses userUses = promotionDao.loadUserUses(promotion.getId(), userId);
		return promotion.isWithinLimits(globalUses, userUses);
	}

	private boolean validateCouponUses(Coupon coupon, int userId) {
		GlobalCouponUses globalUses = couponDao.loadGlobalUses(coupon.getId());
		UserCouponUses userUses = couponDao.loadUserUses(coupon.getId(), userId);

		return coupon.isWithinLimits(globalUses, userUses);
	}

	private Coupon getCoupon(String couponCode) {
		//check if we have coupon cached.
		Coupon coupon = couponCache.get(couponCode);
		if (coupon == null) {
			//load it using dao
			coupon = couponDao.load(couponCode);
		}
		return coupon;
	}

	private Promotion getPromotion(int promotionId) {
		Promotion promotion = promotionCache.get(promotionId);
		if (promotion == null) {
			//its not cached, load it
			promotion = promotionDao.load(promotionId);
		}
		return promotion;
	}
}
