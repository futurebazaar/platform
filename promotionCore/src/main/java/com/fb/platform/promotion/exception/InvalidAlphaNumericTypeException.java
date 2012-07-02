/**
 * 
 */
package com.fb.platform.promotion.exception;

import com.fb.commons.PlatformException;

/**
 * @author ashish
 *
 */
public class InvalidAlphaNumericTypeException extends PlatformException {

	public InvalidAlphaNumericTypeException() {
		super();
	}

	public InvalidAlphaNumericTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidAlphaNumericTypeException(String message) {
		super(message);
	}

	public InvalidAlphaNumericTypeException(Throwable cause) {
		super(cause);
	}
}
