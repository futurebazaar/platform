/**
 * 
 */
package com.fb.platform.promotion.exception;

import com.fb.commons.PlatformException;

/**
 * @author vinayak
 *
 */
public class CouponNotFoundException extends PlatformException {

	public CouponNotFoundException() {
		super();
	}

	public CouponNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public CouponNotFoundException(String message) {
		super(message);
	}

	public CouponNotFoundException(Throwable cause) {
		super(cause);
	}
}
