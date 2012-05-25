/**
 * 
 */
package com.fb.platform.payback.cache;

import org.springframework.stereotype.Component;

import com.fb.platform.caching.AbstractCacheAccess;
import com.fb.platform.caching.NamedCachesEnum;
import com.fb.platform.payback.rule.PointsRule;

/**
 * @author vinayak
 *
 */
@Component
public class RuleCacheAccess extends AbstractCacheAccess {

	public void put(String ruleName, PointsRule rule) {
		platformCachingManager.put(NamedCachesEnum.POINTS_CACHE, ruleName, rule);
	}

	public  PointsRule get(String ruleName) {
		return (PointsRule) platformCachingManager.get(NamedCachesEnum.POINTS_CACHE, ruleName);
	}

	public boolean clear(String ruleName) {
		return platformCachingManager.remove(NamedCachesEnum.POINTS_CACHE, ruleName);
	}

	public void lock(String ruleName) {
        platformCachingManager.lock(NamedCachesEnum.POINTS_CACHE, ruleName);
    }
    
    public void unlock(String ruleName) {
        platformCachingManager.unlock(NamedCachesEnum.POINTS_CACHE, ruleName);
    }

}
