package com.fb.platform.payback.exception;

import com.fb.commons.PlatformException;

public class RuleNotFoundException extends PlatformException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RuleNotFoundException(String message){
		super(message);
	}
}
