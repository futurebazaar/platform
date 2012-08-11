/**
 * 
 */
package com.fb.platform.egv.exception;


/**
 * @author keith
 *
 */
public class NoOrderItemExistsException extends GiftVoucherException {

	/**
	 * 
	 */
	public NoOrderItemExistsException() {
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NoOrderItemExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public NoOrderItemExistsException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public NoOrderItemExistsException(Throwable cause) {
		super(cause);
	}

}
