/**
 * 
 */
package com.fb.platform.user.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import com.fb.commons.PlatformException;

import sun.misc.BASE64Encoder;

/**
 * @author kumar
 *
 */
public class PasswordUtil {

	public static String getEncryptedPassword(String plainPassword) {

	    MessageDigest md = null;
	    String randomStr =  UUID.randomUUID().toString().substring(0,5);

	    try {
	    	md = MessageDigest.getInstance("SHA-1"); //step 2
	    } catch(NoSuchAlgorithmException e) {
	    	throw new PlatformException(e);
	    }

	    try {
	    	md.update((randomStr + plainPassword).getBytes("UTF-8")); //step 3
	    } catch(UnsupportedEncodingException e) {
	    	throw new PlatformException(e);
	    }

	    byte raw[] = md.digest(); //step 4
	    String hash = (new BASE64Encoder()).encode(raw); //step 5
	    String password = "sha1$" + randomStr + "$" + hash;
	    return password; //step 6
	}

	public static boolean checkPassword(String plainPassword, String hashedData) {
		//String algo = hashedPassword.split("$")[0];
		String randomStr = hashedData.split("$")[1] ;
		String storedPasswordHash = hashedData.split("$")[2];

		MessageDigest md = null;

	    try {
	    	md = MessageDigest.getInstance("SHA-1"); //step 2
	    } catch(NoSuchAlgorithmException e) {
	    	throw new PlatformException(e);
	    }

	    try {
	    	md.update((randomStr + plainPassword).getBytes("UTF-8")); //step 3
	    } catch(UnsupportedEncodingException e) {
	    	throw new PlatformException(e);
	    }

	    byte raw[] = md.digest(); //step 4
	    String hash = (new BASE64Encoder()).encode(raw); //step 5
	    return (storedPasswordHash.equals(hash)); //step 6
	}
}
