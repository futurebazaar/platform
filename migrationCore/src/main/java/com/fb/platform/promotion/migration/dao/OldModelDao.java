/**
 * 
 */
package com.fb.platform.promotion.migration.dao;

import java.util.List;

import com.fb.platform.promotion.migration.oldModel.OldCouponUser;
import com.fb.platform.promotion.migration.oldModel.OldPromoCoupon;
import com.fb.platform.promotion.migration.oldModel.OldPromotion;


/**
 * @author keith
 *
 */
public interface OldModelDao {
	
	public OldPromotion loadOldPromotion(int promotionId);
	public List<OldPromoCoupon> loadOldCoupons(int promotionId);
	public List<OldCouponUser> loadCouponUsers(String couponCode);

}
