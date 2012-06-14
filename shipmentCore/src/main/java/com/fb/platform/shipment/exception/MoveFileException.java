package com.fb.platform.shipment.exception;

import com.fb.commons.PlatformException;

/**
 * @author nehaga
 *
 */
public class MoveFileException extends PlatformException{

	/**
	 * 
	 */
	public MoveFileException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MoveFileException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public MoveFileException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public MoveFileException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}

