/**
 * 
 */
package com.fb.platform.egv.exception;

import com.fb.commons.PlatformException;

/**
 * @author keith
 *
 */
public class GiftVoucherException extends PlatformException {

	/**
	 * 
	 */
	public GiftVoucherException() {
	}

	/**
	 * @param message
	 * @param cause
	 */
	public GiftVoucherException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public GiftVoucherException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public GiftVoucherException(Throwable cause) {
		super(cause);
	}

}
