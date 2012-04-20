package com.fb.platform.user.manager.exception;

import com.fb.commons.PlatformException;

public class UserAlreadyExistsException extends PlatformException{
	
	public UserAlreadyExistsException(){		
	}
	
	/**
	 * @param message
	 * @param cause
	 */
	public UserAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public UserAlreadyExistsException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public UserAlreadyExistsException(Throwable cause) {
		super(cause);
	}
}
