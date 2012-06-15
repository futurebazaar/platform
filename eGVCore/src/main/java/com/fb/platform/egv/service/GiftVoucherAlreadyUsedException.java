/**
 * 
 */
package com.fb.platform.egv.service;

import com.fb.commons.PlatformException;

/**
 * @author keith
 *
 */
public class GiftVoucherAlreadyUsedException extends PlatformException {

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
