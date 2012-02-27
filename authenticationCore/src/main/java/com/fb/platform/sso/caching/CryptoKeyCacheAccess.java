/**
 * 
 */
package com.fb.platform.sso.caching;

import org.springframework.stereotype.Component;

import com.fb.platform.caching.AbstractCacheAccess;
import com.fb.platform.caching.NamedCachesEnum;
import com.fb.platform.sso.CryptoKeysTO;

/**
 * @author vinayak
 *
 */
@Component
public class CryptoKeyCacheAccess extends AbstractCacheAccess {

	private static final String CACHE_KEY = "Key";

	public void put(CryptoKeysTO cryptoKey) {
		platformCachingManager.put(NamedCachesEnum.CRYPTO_KEY_CACHE, CACHE_KEY, cryptoKey);
	}

	public CryptoKeysTO get() {
		return (CryptoKeysTO) platformCachingManager.get(NamedCachesEnum.CRYPTO_KEY_CACHE, CACHE_KEY);
	}

	public void clear() {
		platformCachingManager.remove(NamedCachesEnum.CRYPTO_KEY_CACHE, CACHE_KEY);
	}

	public void lock() {
		platformCachingManager.lock(NamedCachesEnum.CRYPTO_KEY_CACHE, CACHE_KEY);
	}

	public void unlock() {
		platformCachingManager.unlock(NamedCachesEnum.CRYPTO_KEY_CACHE, CACHE_KEY);
	}
}
