/**
 * 
 */
package com.fb.platform.egv.exception;


/**
 * @author vinayak
 *
 */
public class UserNotElligibleException extends GiftVoucherException {

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
