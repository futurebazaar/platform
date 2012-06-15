/**
 * 
 */
package com.fb.platform.egv.service;

import com.fb.commons.PlatformException;

/**
 * @author keith
 *
 */
public class GiftVoucherExpiredException extends PlatformException {

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
