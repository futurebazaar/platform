package com.fb.commons.ftp.exception;

import com.fb.commons.PlatformException;

/**
 * @author nehaga
 *
 */
public class FTPUploadException extends PlatformException{

	/**
	 * 
	 */
	public FTPUploadException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public FTPUploadException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public FTPUploadException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public FTPUploadException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
