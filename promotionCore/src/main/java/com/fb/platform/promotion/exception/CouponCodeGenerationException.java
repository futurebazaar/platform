/**
 * 
 */
package com.fb.platform.promotion.exception;

import com.fb.commons.PlatformException;

/**
 * @author ashish
 *
 */
public class CouponCodeGenerationException extends PlatformException {

	/**
	 * 
	 */
	public CouponCodeGenerationException() {
	}

	/**
	 * @param message
	 * @param cause
	 */
	public CouponCodeGenerationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public CouponCodeGenerationException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public CouponCodeGenerationException(Throwable cause) {
		super(cause);
	}

}
