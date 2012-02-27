/**
 * 
 */
package com.fb.platform.caching;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author vinayak
 * 
 *  Superclass for cache access singletons.  Allows cache access singletons to 
 *  be pre-initialised with a mock caching manager during unit testing, 
 *  thereby "turning off" the caching layer.
 */
public abstract class AbstractCacheAccess {

	@Autowired
	protected PlatformCachingManager platformCachingManager;

	protected AbstractCacheAccess() {
		//default constructor, used by spring.
	}

	/**
	 * Used by unit testing
	 * @param cachingManager
	 */
	protected AbstractCacheAccess(PlatformCachingManager cachingManager) {
		this.platformCachingManager = cachingManager;
	}
}
