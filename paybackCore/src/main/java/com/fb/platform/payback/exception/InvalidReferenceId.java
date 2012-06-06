package com.fb.platform.payback.exception;

import com.fb.commons.PlatformException;

public class InvalidReferenceId extends PlatformException {
	private static final long serialVersionUID = 1L;

	public InvalidReferenceId(String message) {
		super(message);
	}
}
