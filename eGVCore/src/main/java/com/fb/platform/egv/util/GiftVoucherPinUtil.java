package com.fb.platform.egv.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import com.fb.commons.PlatformException;

/**
 * @author keith
 *
 */
public class GiftVoucherPinUtil {

	public static String getEncryptedPassword(String plainPassword) {

		MessageDigest md = null;
		String randomStr =  UUID.randomUUID().toString().substring(0,5);

		try {
			md = MessageDigest.getInstance("sha1"); //step 2
		} catch (NoSuchAlgorithmException e) {
			throw new PlatformException(e);
		}

		try {
			md.update((randomStr + plainPassword).getBytes("UTF-8")); //step 3
		} catch (UnsupportedEncodingException e) {
			throw new PlatformException(e);
		}

		byte[] raw = md.digest(); //step 4
		String hash = "" ;
		for (int i = 0; i < raw.length; i++)
		 {
			String hex = Integer.toHexString(raw[i]);
			 if (hex.length() == 1) {
				 	hex = "0" + hex;
			 }
			 hex = hex.substring(hex.length() - 2);
			 hash += hex;
	     }
		String password = "sha1$" + randomStr + "$" + hash;
		return password; //step 6
	}

	public static boolean checkPassword(String plainPassword, String hashedData) {
		//String algo = hashedPassword.split("$")[0];
		try {
			String randomStr = hashedData.split("\\$")[1];
			String storedPasswordHash = hashedData.split("\\$")[2];

			MessageDigest md = null;

			try {
				md = MessageDigest.getInstance("sha1"); //step 2
			} catch (NoSuchAlgorithmException e) {
				throw new PlatformException(e);
			}

			try {
				md.update((randomStr + plainPassword).getBytes("UTF-8")); //step 3				
			} catch (UnsupportedEncodingException e) {
				throw new PlatformException(e);
			}

			byte[] raw = md.digest(); //step 4
			String hash = "" ;
			for (int i = 0; i < raw.length; i++)
			 {
				String hex = Integer.toHexString(raw[i]);
				 if (hex.length() == 1) {
					 	hex = "0" + hex;
				 }
				 hex = hex.substring(hex.length() - 2);
				 hash += hex;
		     }
			
			return (storedPasswordHash.equals(hash)); //step 6
		} catch (Exception e) {
			return false;
		}
	}

	//Code to Test it individually
	public static void main(String[] args) {
		System.out.println(getEncryptedPassword("12345"));
	}
}
