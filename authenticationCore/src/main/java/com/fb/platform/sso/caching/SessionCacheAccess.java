/**
 * 
 */
package com.fb.platform.sso.caching;

import org.springframework.stereotype.Component;

import com.fb.platform.caching.AbstractCacheAccess;
import com.fb.platform.caching.NamedCachesEnum;
import com.fb.platform.sso.SSOSessionId;
import com.fb.platform.sso.SSOSessionTO;

/**
 * @author vinayak
 *
 */
@Component
public class SessionCacheAccess extends AbstractCacheAccess {

    public void remove(SSOSessionId key) {
        platformCachingManager.remove(NamedCachesEnum.SESSION_CACHE, key);
    }
    
	public void put(SSOSessionId key, SSOSessionTO session) {
		platformCachingManager.put(NamedCachesEnum.SESSION_CACHE, key, session);
	}
	
	public SSOSessionTO get(SSOSessionId key) {
		return (SSOSessionTO) platformCachingManager.get(NamedCachesEnum.SESSION_CACHE, key);
	}
	
	public void lock(SSOSessionId key) {
        platformCachingManager.lock(NamedCachesEnum.SESSION_CACHE, key);
    }
    
    public void unlock(SSOSessionId key) {
        platformCachingManager.unlock(NamedCachesEnum.SESSION_CACHE, key);
    }
}
