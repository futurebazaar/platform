package com.fb.platform.payback.cache;

import java.io.Serializable;

import com.fb.platform.caching.AbstractCacheAccess;
import com.fb.platform.caching.NamedCachesEnum;

public class ListCacheAccess extends AbstractCacheAccess implements
		Serializable {
	private static final long serialVersionUID = 1L;

	public void put(String key, Long value) {
		platformCachingManager.put(NamedCachesEnum.POINTS_CACHE, key, value);
	}

	public Long get(String key) {
		Object value = platformCachingManager.get(NamedCachesEnum.POINTS_CACHE,
				key);
		if (value != null) {
			return (Long) value;
		}
		return null;
	}

	public boolean clear(String key) {
		return platformCachingManager.remove(NamedCachesEnum.POINTS_CACHE, key);
	}

	public void lock(String key) {
		platformCachingManager.lock(NamedCachesEnum.POINTS_CACHE, key);
	}

	public void unlock(String key) {
		platformCachingManager.unlock(NamedCachesEnum.POINTS_CACHE, key);
	}

}
