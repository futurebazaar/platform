/**
 * 
 */
package com.fb.platform.promotion.exception;

import com.fb.commons.PlatformException;

/**
 * @author vinayak
 *
 */
public class CouponAlreadyAssignedToUserException extends PlatformException {

	/**
	 * 
	 */
	public CouponAlreadyAssignedToUserException() {
	}

	/**
	 * @param message
	 * @param cause
	 */
	public CouponAlreadyAssignedToUserException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public CouponAlreadyAssignedToUserException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public CouponAlreadyAssignedToUserException(Throwable cause) {
		super(cause);
	}
}
