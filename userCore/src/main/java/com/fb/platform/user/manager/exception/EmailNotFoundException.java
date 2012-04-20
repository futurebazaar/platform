/**
 * 
 */
package com.fb.platform.user.manager.exception;

import com.fb.commons.PlatformException;

/**
 * @author kumar
 *
 */
public class EmailNotFoundException extends PlatformException {

	/**
	 * 
	 */
	public EmailNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public EmailNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public EmailNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public EmailNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
