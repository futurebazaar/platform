/**
 * 
 */
package com.fb.platform.sso.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.fb.platform.sso.CryptoKeyManager;
import com.fb.platform.sso.CryptoKeysTO;
import com.fb.platform.sso.caching.CryptoKeyCacheAccess;
import com.fb.platform.sso.dao.CryptoKeysDao;

/**
 * @author vinayak
 *
 */
public class CryptoKeyManagerImpl implements CryptoKeyManager {

	@Autowired
	private CryptoKeyCacheAccess cryptoKeyCacheAccess = null;

	private CryptoKeysDao cryptoKeysDao = null;

	@Override
	public CryptoKeysTO loadKeys() {
		CryptoKeysTO keys = cryptoKeyCacheAccess.get();

		if (keys == null) {
			try {
				cryptoKeyCacheAccess.lock();
				if (cryptoKeyCacheAccess.get() == null) {
					keys = cryptoKeysDao.loadCryptoKeys();
					cryptoKeyCacheAccess.put(keys);
				}
			} finally {
				cryptoKeyCacheAccess.unlock();
			}
		}
		return keys;
	}

	public CryptoKeysDao getCryptoKeysDao() {
		return cryptoKeysDao;
	}

	public void setCryptoKeysDao(CryptoKeysDao cryptoKeysDao) {
		this.cryptoKeysDao = cryptoKeysDao;
	}
}
