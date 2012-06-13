package com.fb.commons.ftp.exception;

import com.fb.commons.PlatformException;

/**
 * @author nehaga
 *
 */
public class FTPDownloadException extends PlatformException{

	/**
	 * 
	 */
	public FTPDownloadException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public FTPDownloadException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public FTPDownloadException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public FTPDownloadException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
