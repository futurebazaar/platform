/**
 * 
 */
package com.fb.platform.promotion.admin.service;

import com.fb.commons.PlatformException;

/**
 * @author nehaga
 *
 */
public class PromotionNameDuplicateException extends PlatformException {
	/**
	 * 
	 */
	public PromotionNameDuplicateException() {
	}

	/**
	 * @param message
	 * @param cause
	 */
	public PromotionNameDuplicateException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public PromotionNameDuplicateException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public PromotionNameDuplicateException(Throwable cause) {
		super(cause);
	}
}
