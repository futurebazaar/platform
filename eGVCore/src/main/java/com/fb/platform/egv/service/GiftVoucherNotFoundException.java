/**
 * 
 */
package com.fb.platform.egv.service;

import com.fb.commons.PlatformException;

/**
 * @author keith
 *
 */
public class GiftVoucherNotFoundException extends PlatformException {

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
