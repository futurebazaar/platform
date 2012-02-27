/**
 * 
 */
package com.fb.platform.caching.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import com.fb.platform.caching.NamedCachesEnum;
import com.fb.platform.caching.PlatformCachingManager;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 * @author vinayak
 *
 */
public class PlatformCachingManagerEhCacheImpl implements PlatformCachingManager {

	private CacheManager cacheManager = null;

	public PlatformCachingManagerEhCacheImpl() {
		//this will attempt to read the ehcache.xml from the classpath
		//and will initialize all the caches.
		InputStream ehcacheStream = this.getClass().getClassLoader().getResourceAsStream("ehcache.xml");
		cacheManager = CacheManager.create(ehcacheStream);
	}

	@Override
	public void put(NamedCachesEnum cacheName, Object key, Object value) {
		Element element = new Element(key, value);
		cacheManager.getCache(cacheName.getName()).put(element);
	}

	@Override
	public Object get(NamedCachesEnum cacheName, Object key) {
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
		return cacheManager.getCache(cacheName.getName()).remove(key);
	}

	@Override
	public void lock(NamedCachesEnum cacheName, Object key) {
		cacheManager.getCache(cacheName.getName()).acquireWriteLockOnKey(key);
	}

	@Override
	public void unlock(NamedCachesEnum cacheName, Object key) {
		cacheManager.getCache(cacheName.getName()).releaseWriteLockOnKey(key);
	}
}
