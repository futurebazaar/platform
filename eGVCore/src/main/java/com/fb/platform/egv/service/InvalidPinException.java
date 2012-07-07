/**
 * 
 */
package com.fb.platform.egv.service;

import com.fb.commons.PlatformException;

/**
 * @author keith
 *
 */
public class InvalidPinException extends PlatformException {

	/**
	 * 
	 */
	public InvalidPinException() {
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InvalidPinException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public InvalidPinException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public InvalidPinException(Throwable cause) {
		super(cause);
	}

}
