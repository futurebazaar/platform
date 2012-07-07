package com.fb.platform.fulfilment.service;

import com.fb.commons.PlatformException;

/**
 * @author suhas
 *
 */
public class NonServicablePincodeException extends PlatformException{
	
	public NonServicablePincodeException() {
		super();
	}

	public NonServicablePincodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public NonServicablePincodeException(String message) {
		super(message);
	}

	public NonServicablePincodeException(Throwable cause) {
		super(cause);
	}
}
