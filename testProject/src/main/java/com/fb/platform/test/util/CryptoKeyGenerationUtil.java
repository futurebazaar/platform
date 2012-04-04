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
		for (int i=0 ;i <3 ; i++ ){
			if (i==0){
					System.out.println("Random Crypto Key Present:: " + RandomStringUtils.random(48, true, true).toUpperCase());
			}
			if(i==1){
					System.out.println("Random Crypto Key Previous:: " + RandomStringUtils.random(48, true, true).toUpperCase());
			}
			if(i==2){
					System.out.println("Random Crypto Key Next:: " + RandomStringUtils.random(48, true, true).toUpperCase());
			}			
			
		}
	}
}
