package com.fb.commons.ftp.exception;

import com.fb.commons.PlatformException;

/**
 * @author nehaga
 *
 */
public class FTPLogoutException extends PlatformException{

	/**
	 * 
	 */
	public FTPLogoutException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public FTPLogoutException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public FTPLogoutException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public FTPLogoutException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
