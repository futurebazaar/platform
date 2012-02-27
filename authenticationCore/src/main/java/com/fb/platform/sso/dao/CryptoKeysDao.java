/**
 * 
 */
package com.fb.platform.sso.dao;

import com.fb.platform.sso.CryptoKeysTO;

/**
 * @author vinayak
 *
 */
public interface CryptoKeysDao {

	/**
	 * Load the current and old crypto keys
	 * @return
	 */
	public abstract CryptoKeysTO loadCryptoKeys();
}
