/**
 * 
 */
package com.fb.platform.egv.exception;


/**
 * @author keith
 *
 */
public class GiftVoucherAlreadyUsedException extends GiftVoucherException {

	/**
	 * 
	 */
	public GiftVoucherAlreadyUsedException() {
	}

	/**
	 * @param message
	 * @param cause
	 */
	public GiftVoucherAlreadyUsedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public GiftVoucherAlreadyUsedException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public GiftVoucherAlreadyUsedException(Throwable cause) {
		super(cause);
	}

}
