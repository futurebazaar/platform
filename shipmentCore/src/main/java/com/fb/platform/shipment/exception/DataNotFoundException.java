package com.fb.platform.shipment.exception;

import com.fb.commons.PlatformException;

/**
 * @author nehaga
 *
 */
public class DataNotFoundException extends PlatformException{

	/**
	 * 
	 */
	public DataNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DataNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public DataNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public DataNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}

