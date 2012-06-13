package com.fb.commons.ftp.exception;

import com.fb.commons.PlatformException;

/**
 * @author nehaga
 *
 */
public class FTPDisconnectException extends PlatformException{

	/**
	 * 
	 */
	public FTPDisconnectException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public FTPDisconnectException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public FTPDisconnectException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public FTPDisconnectException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
