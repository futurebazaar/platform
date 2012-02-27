/**
 * 
 */
package com.fb.platform.sso.util;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.fb.commons.PlatformException;
import com.fb.platform.sso.CryptoKeysTO;

/**
 * @author vinayak
 *
 */
public class CryptoKeyUtils {

	private static Log logger = LogFactory.getLog(CryptoKeyUtils.class);

	private static final String OLD_CIPHER_TRANSFORM_TYPE = "DESede";

	private static final String NEW_CIPHER_TRANSFORM_TYPE = "DESede/CBC/PKCS5Padding";
	
	private static byte[] iv = { 0x0a, 0x01, 0x02, 0x03, 0x04, 0x0b, 0x0c, 0x0d };

	/**
	 * Padding character used by Base64 Encoding Algorithm
	 */
	private static final String BASE64_PADDING_CHAR = "=";

	/**
	 * Number of Base64 alphabets in one encoded group
	 */
	private static final int BASE64_GROUP_SIZE = 4;
  
	private CryptoKeyUtils(){
	}

	/**
	 * Turns the key data into a key object.  The encodedKey is a 48 character hexadecimal number represting 24 bytes
	 * of dey data
	 * e.g. 5A5FB40D45E4968BF883163919B0DC9CD751DAB709EC43B1
	 * @param encodedKey
	 * @return the key if decoding is successful
	 */
	public static Key fromExternalForm(String encodedKey) {

		try {
			Key key = null;
			if (encodedKey != null) {
				byte[] bytes = Hex.decodeHex(encodedKey.toCharArray());
				boolean isCBCEncryptionModeEnabled = isCBCEncryptionModeEnabled();
				if (isCBCEncryptionModeEnabled) {
					key = SecretKeyFactory.getInstance(NEW_CIPHER_TRANSFORM_TYPE).generateSecret(new DESedeKeySpec(bytes));
				} else {
					key = SecretKeyFactory.getInstance(OLD_CIPHER_TRANSFORM_TYPE).generateSecret(new DESedeKeySpec(bytes));					
				}
			} else {
				logger.error("Encryption key data cannont be null");
				throw new IllegalArgumentException("encryption key data cannot be null");
			}
			return key;
		}
		catch (DecoderException e) {
			logger.error("Failed to decode key data " + encodedKey);
			throw new PlatformException("Failed to decode key data" + encodedKey, e);
		}
		catch (InvalidKeyException e) {
			logger.error("Unable to decode encryption key bytes",e);
			throw new PlatformException("Failed to decode key data" + encodedKey, e);
		}
		catch (InvalidKeySpecException e) {
			logger.error("Unable to decode encryption key bytes",e);
			throw new PlatformException("Failed to decode key data" + encodedKey, e);
		}
		catch (NoSuchAlgorithmException e) {
			logger.error("Unable to decode encryption key bytes",e);
			throw new PlatformException("Failed to decode key data" + encodedKey, e);
		}
	}

	/**
	 * Get an instance of a cipher
	 */
	public static Cipher getCipher(boolean isCBCEncryptionModeEnabled) {
	    try {
			if (isCBCEncryptionModeEnabled) {
		    	return Cipher.getInstance(NEW_CIPHER_TRANSFORM_TYPE);
			} else {
		    	return Cipher.getInstance(OLD_CIPHER_TRANSFORM_TYPE);
			}
		} catch (Exception e) {
			logger.fatal("cannot initialise api decryption cipher.");
			throw new PlatformException(e);
		}
	}

	/**
	 * Encrypt the plainText using the current crypto key
	 * @param plainText
	 * @param keys
	 * @return String - the encrypted text
	 */
	public static String encrypt(String plainText, CryptoKeysTO keys) {

		if (keys == null || keys.getCurrentKey() == null) {
			logger.error("Cannont encrypt text, current encryption key not available");
			throw new PlatformException("Failed to encrypt text, no current key");
		}

		boolean isCBCEncryptionModeEnabled = isCBCEncryptionModeEnabled();
		
		try {
			byte[] plainTextBytes = plainText.getBytes("UTF-8");
			IvParameterSpec ips = new IvParameterSpec(iv);
			
			Cipher cipher = getCipher(isCBCEncryptionModeEnabled);
			if (isCBCEncryptionModeEnabled) {
				cipher.init(Cipher.ENCRYPT_MODE,keys.getCurrentKey(), ips);			
			} else {
				cipher.init(Cipher.ENCRYPT_MODE,keys.getCurrentKey());
			}
			byte[] encryptedBytes = cipher.doFinal(plainTextBytes);

			return new BASE64Encoder().encode(encryptedBytes);
		}
		catch (Exception e) {
			throw new PlatformException(e);
		}
	}

	/**
	 * Decrypt the cipher text using current key, if that is unsuccessfull use old key,
     * if that is unsuccessfull use old key
	 * @param cipherText
	 * @param keys
	 * @return String decrypted text if possible, otherwise null
	 */
	public static String decrypt(String cipherText, CryptoKeysTO keys) {

		if (cipherText == null) {
			return null;
		}

		if (keys == null || keys.getCurrentKey() == null) {
			logger.error("Cannont decrypt text, current encryption key not available");
			throw new PlatformException("Failed to decrypt text, no current key");
		}
		boolean isCBCEncryptionModeEnabled = isCBCEncryptionModeEnabled();
		
		Cipher cipher = getCipher(isCBCEncryptionModeEnabled);
		try {
            // try to decrypt with the current key
            try {
                return decrypt(cipher, cipherText, keys.getCurrentKey(), isCBCEncryptionModeEnabled);
            }
            catch (BadPaddingException e) {
                //This occurs if the text was encrypted with a key we no longer have
            }

            // the current key is invalid try with the old key
            try {
                if (keys.getOldKey() != null) {
                    return decrypt(cipher, cipherText, keys.getOldKey(), isCBCEncryptionModeEnabled);
                }
            }
            catch (BadPaddingException e) {
                //very old key... expired token
            }

            // test the edge case where the token has been encrypted with the next key
            try {
                return decrypt(cipher, cipherText, keys.getNextKey(), isCBCEncryptionModeEnabled);
            }
            catch (BadPaddingException e) {
                //very old key... expired token
            }
        } catch (IllegalBlockSizeException e) {
        	logger.error("Cannont decrypt text.", e);
        	//this might happen when an invalid session token is passed.
        	return null;
        } catch (Exception e) {
        	throw new PlatformException("Unable to decrypt session token", e);
        }
        return null;

	}

    private static String decrypt(Cipher cipher, String cipherText, Key key, boolean isCBCEncryptionModeEnabled) 
    		throws IOException, InvalidKeyException, IllegalStateException, 
    		IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {

    	byte[] encryptedTextBytes = new BASE64Decoder().decodeBuffer(addStrippedEqualSignsHack(cipherText));
		IvParameterSpec ips = new IvParameterSpec(iv);
		if (isCBCEncryptionModeEnabled) {
			cipher.init(Cipher.DECRYPT_MODE, key, ips);
		} else {
			cipher.init(Cipher.DECRYPT_MODE, key);
		}
		byte[] decryptedBytes = cipher.doFinal(encryptedTextBytes);
		return new String(decryptedBytes, "UTF-8");
	}

    public static boolean isCBCEncryptionModeEnabled() {
        //TODO read this from config file? ConfigEntryEnum.IS_CBC_ENCRYPTION_MODE_ENABLED
    	return false;
    }


    /**
     * Append equal sign('=') to the <code>cipherText</code> if required. This required because a new version of
     * Tomcat(5.5.28 onwards) doesn't allow '=' to be presented in a cookie value and strip it out.
     *
     * @param cipherText a cipher text
     * @return fixed cipher text (with equals signs ('=') appended is required)
     */
    private static String addStrippedEqualSignsHack(final String cipherText)
    {
        String result = cipherText;
        if(result != null && !result.contains(BASE64_PADDING_CHAR))
        {
            int mod = result.length() % BASE64_GROUP_SIZE;
            switch (mod) {
                case 2:
                    result += BASE64_PADDING_CHAR + BASE64_PADDING_CHAR;
                    break;
                case 3:
                    result += BASE64_PADDING_CHAR;
                    break;
                default:
                    // nothing to do
                    break;
            }
        }

        return result;
    }

}
