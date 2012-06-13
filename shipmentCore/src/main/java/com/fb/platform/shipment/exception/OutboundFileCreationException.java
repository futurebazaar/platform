package com.fb.platform.shipment.exception;

import com.fb.commons.PlatformException;

/**
 * @author nehaga
 *
 */
public class OutboundFileCreationException extends PlatformException{

	/**
	 * 
	 */
	public OutboundFileCreationException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public OutboundFileCreationException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public OutboundFileCreationException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public OutboundFileCreationException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}

