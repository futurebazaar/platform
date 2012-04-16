/**
 * 
 */
package com.fb.platform.promotion.migration.dao;

import java.sql.Timestamp;

import com.fb.commons.to.Money;

/**
 * @author vinayak
 *
 */
public interface MigrationDao {

	int createPromotion(String name, String description, Timestamp validFrom,
			Timestamp validTill, int maxUses, Money maxAmount,
			int maxUsesPerUser, Money maxAmountPerUser,int global, int active);
	
	boolean createPromotionRuleConfig(String appliedOn,String discountType,Money minOrderValue,Money discountValue,String promotionType,int clientId);
	
	boolean createCoupon(String couponCode,int promotionId, int maxUsesPerCoupon,String appliedOn,String DiscountType);
	
	boolean createCouponUser(String couponCode, int userId);
	
	/**
	 * To be done from orders table
	 * @return
	 */
	boolean createCouponUses();
	
	/**
	 * To be done from orders table
	 * @return
	 */
	boolean createPromotionUses();

}
