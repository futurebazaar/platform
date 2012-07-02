/**
 * 
 */
package com.fb.platform.promotion.exception;

import com.fb.commons.PlatformException;

/**
 * @author vinayak
 *
 */
public class CouponNotCommitedException extends PlatformException {

	public CouponNotCommitedException() {
		super();
	}

	public CouponNotCommitedException(String message, Throwable cause) {
		super(message, cause);
	}

	public CouponNotCommitedException(String message) {
		super(message);
	}

	public CouponNotCommitedException(Throwable cause) {
		super(cause);
	}
}
