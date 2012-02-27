/**
 * 
 */
package com.fb.platform.sso.caching;

import org.springframework.stereotype.Component;

import com.fb.platform.caching.AbstractCacheAccess;
import com.fb.platform.caching.NamedCachesEnum;
import com.fb.platform.sso.SSOSessionId;
import com.fb.platform.sso.SSOToken;

/**
 * @author vinayak
 *
 */
@Component
public class SessionTokenCacheAccess extends AbstractCacheAccess {

	public void put(SSOToken token, SSOSessionId sessionId) {
		platformCachingManager.put(NamedCachesEnum.SSO_SESSION_CACHE, token, sessionId);
	}
    
    public SSOSessionId get(SSOToken token) {
        return (SSOSessionId) platformCachingManager.get(NamedCachesEnum.SSO_SESSION_CACHE, token);
    }
    
    public void clear(SSOToken token) {
        platformCachingManager.remove(NamedCachesEnum.SSO_SESSION_CACHE, token);
    }
	
	public void lock(SSOToken token) {
        platformCachingManager.lock(NamedCachesEnum.SSO_SESSION_CACHE, token);
    }
    
    public void unlock(SSOToken token) {
        platformCachingManager.unlock(NamedCachesEnum.SSO_SESSION_CACHE, token);
    }
}
