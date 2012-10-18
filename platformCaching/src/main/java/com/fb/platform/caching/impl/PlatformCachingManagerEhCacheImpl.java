/**
 * 
 */
package com.fb.platform.caching.impl;

import java.io.InputStream;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.caching.NamedCachesEnum;
import com.fb.platform.caching.PlatformCachingManager;

/**
 * @author vinayak
 *
 */
public class PlatformCachingManagerEhCacheImpl implements PlatformCachingManager {

	private CacheManager cacheManager = null;
	
	private Log logger = LogFactory.getLog(PlatformCachingManagerEhCacheImpl.class);

	public PlatformCachingManagerEhCacheImpl() {
		//this will attempt to read the ehcache.xml from the classpath
		//and will initialize all the caches.
		logger.info("Trying to read ehcache.xml from the classpath and initializing all the caches");
		InputStream ehcacheStream = this.getClass().getClassLoader().getResourceAsStream("ehcache.xml");
		cacheManager = CacheManager.create(ehcacheStream);
	}

	@Override
	public void put(NamedCachesEnum cacheName, Object key, Object value) {
		if(logger.isDebugEnabled()) {
			logger.debug("Put into the cache object key : " + key + " , value : " + value);
		}
		Element element = new Element(key, value);
		cacheManager.getCache(cacheName.getName()).put(element);
	}

	@Override
	public Object get(NamedCachesEnum cacheName, Object key) {
		if(logger.isDebugEnabled()) {
			logger.debug("Get from cache object with key : " + key );
		}
		String name = cacheName.getName();
		Ehcache cache = cacheManager.getCache(name);
		Element element = cache.get(key);
		if (element == null) {
			return null;
		}
		return element.getValue();
	}

	@Override
	public boolean remove(NamedCachesEnum cacheName, Object key) {
		if(logger.isDebugEnabled()) {
			logger.debug("Remove from the cache object with key : " + key );
		}
		return cacheManager.getCache(cacheName.getName()).remove(key);
	}

	@Override
	public void lock(NamedCachesEnum cacheName, Object key) {
		if(logger.isDebugEnabled()) {
			logger.debug("Acquire write lock on the cache object with key : " + key );
		}
		cacheManager.getCache(cacheName.getName()).acquireWriteLockOnKey(key);
	}

	@Override
	public void unlock(NamedCachesEnum cacheName, Object key) {
		if(logger.isDebugEnabled()) {
			logger.debug("Release write lock on the cache object with key : " + key );
		}
		cacheManager.getCache(cacheName.getName()).releaseWriteLockOnKey(key);
	}
}
