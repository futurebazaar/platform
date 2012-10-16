/**
 * 
 */
package com.fb.platform.egv.exception;


/**
 * @author vinayak
 *
 */
public class InvalidCouponTypeException extends GiftVoucherException {

	/**
	 * 
	 */
	public InvalidCouponTypeException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InvalidCouponTypeException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public InvalidCouponTypeException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public InvalidCouponTypeException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
