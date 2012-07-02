/**
 * 
 */
package com.fb.platform.promotion.exception;

import com.fb.commons.PlatformException;

/**
 * @author vinayak
 *
 */
public class MandatoryDataMissingException extends PlatformException {

	/**
	 * 
	 */
	public MandatoryDataMissingException() {
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MandatoryDataMissingException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public MandatoryDataMissingException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public MandatoryDataMissingException(Throwable cause) {
		super(cause);
	}
}
