package com.fb.platform.payback.exception;

import com.fb.commons.PlatformException;

public class InvalidSession extends PlatformException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidSession(String message){
		super(message);
	}
}