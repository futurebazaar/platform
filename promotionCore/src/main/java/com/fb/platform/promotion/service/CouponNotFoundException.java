/**
 * 
 */
package com.fb.platform.promotion.service;

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
