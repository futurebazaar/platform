/**
 * 
 */
package com.fb.platform.promotion.exception;

import com.fb.commons.PlatformException;

/**
 * @author vinayak
 *
 */
public class UserNotElligibleException extends PlatformException {

	/**
	 * 
	 */
	public UserNotElligibleException() {
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UserNotElligibleException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public UserNotElligibleException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public UserNotElligibleException(Throwable cause) {
		super(cause);
	}
}