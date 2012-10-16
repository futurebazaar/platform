/**
 * 
 */
package com.fb.platform.egv.exception;


/**
 * @author keith
 *
 */
public class GiftVoucherExpiredException extends GiftVoucherException {

	/**
	 * 
	 */
	public GiftVoucherExpiredException() {
	}

	/**
	 * @param message
	 * @param cause
	 */
	public GiftVoucherExpiredException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public GiftVoucherExpiredException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public GiftVoucherExpiredException(Throwable cause) {
		super(cause);
	}

}
