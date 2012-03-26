/**
 * 
 */
package com.fb.platform.promotion.cache;

import org.springframework.stereotype.Component;

import com.fb.platform.caching.AbstractCacheAccess;
import com.fb.platform.caching.NamedCachesEnum;
import com.fb.platform.promotion.model.coupon.Coupon;

/**
 * @author vinayak
 *
 */
@Component
public class CouponCacheAccess extends AbstractCacheAccess {

	public void put(String couponCode, Coupon coupon) {
		platformCachingManager.put(NamedCachesEnum.COUPON_CACHE, couponCode, coupon);
	}

	public Coupon get(String couponCode) {
		return (Coupon) platformCachingManager.get(NamedCachesEnum.COUPON_CACHE, couponCode);
	}

	public void clear(String couponCode) {
		platformCachingManager.remove(NamedCachesEnum.COUPON_CACHE, couponCode);
	}

	public void lock(String couponCode) {
        platformCachingManager.lock(NamedCachesEnum.COUPON_CACHE, couponCode);
    }
    
    public void unlock(String couponCode) {
        platformCachingManager.unlock(NamedCachesEnum.COUPON_CACHE, couponCode);
    }
}
