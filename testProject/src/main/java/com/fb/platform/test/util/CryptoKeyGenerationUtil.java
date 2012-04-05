/**
 * 
 */
package com.fb.platform.test.util;

import org.apache.commons.lang.RandomStringUtils;

/**
 * @author kumar
 *
 */
public class CryptoKeyGenerationUtil {

	public static void main(String[] args) {
		System.out.println("Random Crypto Key Next:: " + RandomStringUtils.random(48, true, true).toUpperCase());
	}
}
