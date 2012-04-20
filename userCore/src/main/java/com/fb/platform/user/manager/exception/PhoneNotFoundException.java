/**
 * 
 */
package com.fb.platform.user.manager.exception;

import com.fb.commons.PlatformException;

/**
 * @author kumar
 *
 */
public class PhoneNotFoundException extends PlatformException {

	/**
	 * 
	 */
	public PhoneNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public PhoneNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public PhoneNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public PhoneNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
