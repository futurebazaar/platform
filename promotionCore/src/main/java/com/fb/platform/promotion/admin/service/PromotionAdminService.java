package com.fb.platform.promotion.admin.service;

import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.promotion.admin.to.CouponBasicDetails;
import com.fb.platform.promotion.admin.to.CouponTO;
import com.fb.platform.promotion.admin.to.PromotionTO;
import com.fb.platform.promotion.admin.to.SearchCouponOrderBy;
import com.fb.platform.promotion.admin.to.SearchPromotionOrderBy;
import com.fb.platform.promotion.admin.to.SortOrder;
import com.fb.platform.promotion.model.coupon.CouponLimitsConfig;
import com.fb.platform.promotion.model.coupon.CouponType;
import com.fb.platform.promotion.rule.RulesEnum;

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
	 * This function fetches a list of promotions that meet the specified search criteria.
	 * @param promotionName
	 * @param validFrom
	 * @param validTill
	 * @param startRecord
	 * @param batchSize
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<PromotionTO> searchPromotion(String promotionName, DateTime validFrom, DateTime validTill, int isActive, SearchPromotionOrderBy orderBy,
			SortOrder order, int startRecord, int batchSize);
	
	/**
	 * This function fetches a count of all promotions that meet the specified search criteria.
	 * @param promotionName
	 * @param validFrom
	 * @param validTill
	 * @param isActive
	 * @return
	 */
	public int getPromotionCount(String promotionName, DateTime validFrom, DateTime validTill, int isActive);
	
	/**
	 * Fetching a complete view of promotion. Including limits and rule config.
	 * @param promotionId
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public PromotionTO viewPromotion(int promotionId);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int promotionCouponCount(int promotionId); 
	
	/**
	 * This function updates an existing promotion.
	 * @param promotion
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean updatePromotion(PromotionTO promotionTO);

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

	/**
	 * Assigns the PRE_ISSUE couponCode to user identified by the userId
	 * @param couponCode
	 * @param userId
	 * @param overriddenUserLimit If greater than zero, the coupon uses limit will be overriden by this value for this user.
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void assignCouponToUser(String couponCode, int userId, int overriddenUserLimit);
	
	public Set<CouponBasicDetails> searchCoupons(String couponCode, String userName, SearchCouponOrderBy orderBy, SortOrder sortOrder, int startRecord, int batchSize);
	
	public CouponTO viewCoupons(String couponCode);
	
	public CouponTO viewCoupons(int couponId);
}
