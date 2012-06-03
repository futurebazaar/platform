package com.fb.platform.payback.exception;

import com.fb.commons.PlatformException;

public class InvalidActionCode extends PlatformException {

	private static final long serialVersionUID = 1L;

	public InvalidActionCode(String message) {
		super(message);
	}
}