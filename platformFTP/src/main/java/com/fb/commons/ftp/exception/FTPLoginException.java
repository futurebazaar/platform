package com.fb.commons.ftp.exception;

import com.fb.commons.PlatformException;

/**
 * @author nehaga
 *
 */
public class FTPLoginException extends PlatformException{

	/**
	 * 
	 */
	public FTPLoginException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public FTPLoginException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public FTPLoginException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public FTPLoginException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
