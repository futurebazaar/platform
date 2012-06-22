/**
 * 
 */
package com.fb.platform.egv.service;

import com.fb.commons.PlatformException;

/**
 * @author ashish
 *
 */
public class InvalidNumericTypeException extends PlatformException {

	public InvalidNumericTypeException() {
		super();
	}

	public InvalidNumericTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidNumericTypeException(String message) {
		super(message);
	}

	public InvalidNumericTypeException(Throwable cause) {
		super(cause);
	}
}
