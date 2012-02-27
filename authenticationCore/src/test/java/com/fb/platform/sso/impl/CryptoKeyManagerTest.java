/**
 * 
 */
package com.fb.platform.sso.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.platform.dao.BaseTestCase;
import com.fb.platform.sso.CryptoKeyManager;
import com.fb.platform.sso.CryptoKeysTO;

/**
 * @author vinayak
 *
 */
public class CryptoKeyManagerTest extends BaseTestCase {

	@Autowired
	private CryptoKeyManager cryptoKeyManager = null;

	@Test
	public void testLoadKeys() {
		//this call will read it from DB
		CryptoKeysTO keys = cryptoKeyManager.loadKeys();

		//read it again and should come from cache
		CryptoKeysTO cachedKeys = cryptoKeyManager.loadKeys();

		assertNotNull(keys);
		assertNotNull(cachedKeys);
	}
}
