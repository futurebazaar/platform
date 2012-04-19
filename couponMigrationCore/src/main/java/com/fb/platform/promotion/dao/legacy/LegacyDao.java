/**
 * 
 */
package com.fb.platform.promotion.dao.legacy;

import java.util.List;

import com.fb.platform.promotion.model.legacy.LegacyCouponUser;
import com.fb.platform.promotion.model.legacy.LegacyPromotion;
import com.fb.platform.promotion.model.legacy.LegacyPromotionCoupon;

/**
 * @author vinayak
 *
 */
public interface LegacyDao {

	public LegacyPromotion loadPromotion(int promotionId);

	public List<Integer> loadIdsToMigrate(int startRecord, int batchSize);
}
