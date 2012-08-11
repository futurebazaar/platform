/**
 * 
 */
package com.fb.platform.egv.exception;


/**
 * @author keith
 *
 */
public class GiftVoucherNotFoundException extends GiftVoucherException {

	/**
	 * 
	 */
	public GiftVoucherNotFoundException() {
	}

	/**
	 * @param message
	 * @param cause
	 */
	public GiftVoucherNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public GiftVoucherNotFoundException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public GiftVoucherNotFoundException(Throwable cause) {
		super(cause);
	}

}
