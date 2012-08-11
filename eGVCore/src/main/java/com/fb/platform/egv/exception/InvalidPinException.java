/**
 * 
 */
package com.fb.platform.egv.exception;


/**
 * @author keith
 *
 */
public class InvalidPinException extends GiftVoucherException {

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
