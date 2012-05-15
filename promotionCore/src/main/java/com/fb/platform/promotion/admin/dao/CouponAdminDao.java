/**
 * 
 */
package com.fb.platform.promotion.admin.dao;

import java.util.List;
import java.util.Set;

import com.fb.platform.promotion.admin.to.CouponBasicDetails;
import com.fb.platform.promotion.admin.to.CouponTO;
import com.fb.platform.promotion.admin.to.SearchCouponOrderBy;
import com.fb.platform.promotion.admin.to.SortOrder;
import com.fb.platform.promotion.model.coupon.Coupon;
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

	public Coupon loadCouponWithoutConfig(String couponCode);
	
	public Set<Integer> loadAllCouponForUser(int userId);
	
	public List<CouponBasicDetails> searchCoupons(String couponCode, Set<Integer> allCouponIdsForUser, SearchCouponOrderBy orderBy,
			SortOrder sortOrder, int startRecord, int batchSize);
	
	public CouponTO load(int couponId);
	
	public CouponTO load(String couponCode);
}
