/**
 * 
 */
package com.fb.platform.payback.cache;

import org.springframework.stereotype.Component;

import com.fb.platform.caching.AbstractCacheAccess;
import com.fb.platform.caching.NamedCachesEnum;

/**
 * @author vinayak
 *
 */
@Component
public class RuleCacheAccess extends AbstractCacheAccess {

	public void put(Integer promotionId) {
		platformCachingManager.put(NamedCachesEnum.PROMOTION_CACHE, promotionId, null);
	}

	/*public  get(Integer promotionId) {
		return (Promotion) platformCachingManager.get(NamedCachesEnum.PROMOTION_CACHE, promotionId);
	}*/

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
