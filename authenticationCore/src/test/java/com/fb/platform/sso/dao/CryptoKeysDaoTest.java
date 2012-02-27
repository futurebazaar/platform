/**
 * 
 */
package com.fb.platform.sso.dao;

import static org.junit.Assert.*;

import java.security.Key;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.platform.dao.BaseTestCase;
import com.fb.platform.sso.CryptoKeysTO;

/**
 * @author vinayak
 *
 */
public class CryptoKeysDaoTest extends BaseTestCase {

	@Autowired
	private CryptoKeysDao cryptoKeysDao = null;

	@Test
	public void testLoadKeys() {
		CryptoKeysTO cryptoKeys = cryptoKeysDao.loadCryptoKeys();

		assertNotNull(cryptoKeys);
		Key currentKey = cryptoKeys.getCurrentKey();
		Key oldKey = cryptoKeys.getOldKey();
		Key nextKey = cryptoKeys.getNextKey();

		assertNotNull(currentKey);
		assertNotNull(oldKey);
		assertNotNull(nextKey);

		assertEquals("DESede", currentKey.getAlgorithm());
		assertEquals("DESede", oldKey.getAlgorithm());
		assertEquals("DESede", nextKey.getAlgorithm());

	}
}
