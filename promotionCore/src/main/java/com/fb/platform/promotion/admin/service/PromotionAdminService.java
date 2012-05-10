package com.fb.platform.promotion.admin.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.promotion.model.coupon.CouponLimitsConfig;
import com.fb.platform.promotion.model.coupon.CouponType;
import com.fb.platform.promotion.rule.RulesEnum;
import com.fb.platform.promotion.to.PromotionTO;

/**
 * @author nehaga
 *
 */

@Transactional
public interface PromotionAdminService {

	/**
	 * This function returns a list of Promotion rules.
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<RulesEnum> getAllPromotionRules();
	
	/**
	 * This function creates a new promotion.
	 * @param promotion
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public int createPromotion(PromotionTO promotionTO);

	/**
	 * Create new coupons and stores them in the database. 
	 * @param count Number of coupon codes to create
	 * @param length Length of coupon codes
	 * @param startsWith Optional string which will be prepended to all the coupon codes
	 * @param endsWith Optional string which will be appended to all the coupon codes
	 * @param promotionId The promotion which these coupons will belong to
	 * @param type Type of the coupons to create.
	 * @param limits The coupon limits config specifying max uses and max amount per coupon
	 * @return the newly generated coupon codes
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<String> createCoupons(int count, int length, String startsWith, String endsWith, int promotionId, CouponType type, CouponLimitsConfig limits);
}
