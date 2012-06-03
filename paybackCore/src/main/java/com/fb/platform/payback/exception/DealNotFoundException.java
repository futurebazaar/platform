package com.fb.platform.payback.exception;

import com.fb.commons.PlatformException;

public class DealNotFoundException extends PlatformException {

	private static final long serialVersionUID = 1L;

	public DealNotFoundException(String message) {
		super(message);
	}
}
