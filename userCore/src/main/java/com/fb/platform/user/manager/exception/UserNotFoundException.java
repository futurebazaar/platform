package com.fb.platform.user.manager.exception;

import com.fb.commons.PlatformException;

public class UserNotFoundException extends PlatformException {

	public UserNotFoundException(){		
	}
	
	/**
	 * @param message
	 * @param cause
	 */
	public UserNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public UserNotFoundException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public UserNotFoundException(Throwable cause) {
		super(cause);
	}
}
