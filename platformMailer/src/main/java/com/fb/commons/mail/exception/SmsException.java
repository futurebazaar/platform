/**
 * 
 */
package com.fb.commons.mail.exception;

import com.fb.commons.PlatformException;

/**
 * @author keith
 *
 */
public class SmsException extends PlatformException {

	/**
	 * 
	 */
	public SmsException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SmsException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public SmsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public SmsException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
