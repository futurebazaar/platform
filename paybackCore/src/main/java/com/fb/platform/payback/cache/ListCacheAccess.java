package com.fb.platform.payback.cache;

import com.fb.platform.caching.AbstractCacheAccess;
import com.fb.platform.caching.NamedCachesEnum;

public class ListCacheAccess extends AbstractCacheAccess{
	
	public void put(String key, String value) {
		platformCachingManager.put(NamedCachesEnum.POINTS_CACHE, key, value);
	}

	public  String  get(String key) {
		return (String) platformCachingManager.get(NamedCachesEnum.POINTS_CACHE, key);
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
