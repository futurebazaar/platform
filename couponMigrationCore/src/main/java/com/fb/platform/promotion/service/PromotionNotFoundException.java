/**
 * 
 */
package com.fb.platform.promotion.service;

import com.fb.commons.PlatformException;

/**
 * @author vinayak
 *
 */
public class PromotionNotFoundException extends PlatformException {

	/**
	 * 
	 */
	public PromotionNotFoundException() {
	}

	/**
	 * @param message
	 * @param cause
	 */
	public PromotionNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public PromotionNotFoundException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public PromotionNotFoundException(Throwable cause) {
		super(cause);
	}

}
