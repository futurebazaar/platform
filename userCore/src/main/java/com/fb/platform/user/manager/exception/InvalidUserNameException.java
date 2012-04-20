package com.fb.platform.user.manager.exception;

import com.fb.commons.PlatformException;

public class InvalidUserNameException extends PlatformException {
	public InvalidUserNameException(){
		super();
	}
	
	/**
	 * @param message
	 * @param cause
	 */
	public InvalidUserNameException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public InvalidUserNameException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public InvalidUserNameException(Throwable cause) {
		super(cause);
	}
}
