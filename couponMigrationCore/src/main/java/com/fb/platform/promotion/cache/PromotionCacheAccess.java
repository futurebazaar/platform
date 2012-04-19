/**
 * 
 */
package com.fb.platform.promotion.cache;

import org.springframework.stereotype.Component;

import com.fb.platform.caching.AbstractCacheAccess;
import com.fb.platform.caching.NamedCachesEnum;
import com.fb.platform.promotion.model.Promotion;

/**
 * @author vinayak
 *
*/
@Component
public class PromotionCacheAccess extends AbstractCacheAccess {

	public void put(Integer promotionId, Promotion promotion) {
		platformCachingManager.put(NamedCachesEnum.PROMOTION_CACHE, promotionId, promotion);
	}

	public Promotion get(Integer promotionId) {
		return (Promotion) platformCachingManager.get(NamedCachesEnum.PROMOTION_CACHE, promotionId);
	}

	public boolean clear(Integer promotionId) {
		return platformCachingManager.remove(NamedCachesEnum.PROMOTION_CACHE, promotionId);
	}

	public void lock(Integer promotionId) {
        platformCachingManager.lock(NamedCachesEnum.PROMOTION_CACHE, promotionId);
    }
    
    public void unlock(Integer promotionId) {
        platformCachingManager.unlock(NamedCachesEnum.PROMOTION_CACHE, promotionId);
    }
}
