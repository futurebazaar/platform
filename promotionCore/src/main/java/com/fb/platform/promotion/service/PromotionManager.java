/**
 * 
 */
package com.fb.platform.promotion.service;

import com.fb.platform.promotion.to.ApplyCouponRequest;
import com.fb.platform.promotion.to.ApplyCouponResponse;
import com.fb.platform.promotion.to.ClearCouponCacheRequest;
import com.fb.platform.promotion.to.ClearCouponCacheResponse;
import com.fb.platform.promotion.to.ClearPromotionCacheRequest;
import com.fb.platform.promotion.to.ClearPromotionCacheResponse;
import com.fb.platform.promotion.to.CommitCouponRequest;
import com.fb.platform.promotion.to.CommitCouponResponse;
import com.fb.platform.promotion.to.ReleaseCouponRequest;
import com.fb.platform.promotion.to.ReleaseCouponResponse;

/**
 * @author vinayak
 *
 */
public interface PromotionManager {

	public ApplyCouponResponse applyCoupon(ApplyCouponRequest request);

	public CommitCouponResponse commitCouponUse(CommitCouponRequest request);
	
	public ReleaseCouponResponse releaseCoupon(ReleaseCouponRequest request);

	public ClearPromotionCacheResponse clearCache(ClearPromotionCacheRequest clearPromotionCacheRequest);

	public ClearCouponCacheResponse clearCache(ClearCouponCacheRequest clearCouponCacheRequest);
}
