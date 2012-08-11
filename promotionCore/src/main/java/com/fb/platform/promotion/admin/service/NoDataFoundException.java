/**
 * 
 */
package com.fb.platform.promotion.admin.service;

import com.fb.commons.PlatformException;

/**
 * @author nehaga
 *
 */
public class NoDataFoundException extends PlatformException {
	/**
	 * 
	 */
	public NoDataFoundException() {
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NoDataFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public NoDataFoundException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public NoDataFoundException(Throwable cause) {
		super(cause);
	}
}
