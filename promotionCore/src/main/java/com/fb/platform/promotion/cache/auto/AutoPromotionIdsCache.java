/**
 * 
 */
package com.fb.platform.promotion.cache.auto;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fb.platform.caching.AbstractCacheAccess;
import com.fb.platform.caching.NamedCachesEnum;

/**
 * @author vinayak
 *
 */
@Component
public class AutoPromotionIdsCache extends AbstractCacheAccess {

	private static final String CACHE_KEY = "ActiveAutoPromotionIds";

	public void put(List<Integer> ids) {
		platformCachingManager.put(NamedCachesEnum.AUTO_PROMOTION_IDS_CACHE, CACHE_KEY, ids);
	}

	public List<Integer> get() {
		return (List<Integer>) platformCachingManager.get(NamedCachesEnum.AUTO_PROMOTION_IDS_CACHE, CACHE_KEY);
	}

	public void clear() {
		platformCachingManager.remove(NamedCachesEnum.AUTO_PROMOTION_IDS_CACHE, CACHE_KEY);
	}

	public void lock() {
		platformCachingManager.lock(NamedCachesEnum.AUTO_PROMOTION_IDS_CACHE, CACHE_KEY);
	}

	public void unlock() {
		platformCachingManager.unlock(NamedCachesEnum.AUTO_PROMOTION_IDS_CACHE, CACHE_KEY);
	}

}
