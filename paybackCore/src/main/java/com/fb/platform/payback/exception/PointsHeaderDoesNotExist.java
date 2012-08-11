package com.fb.platform.payback.exception;

import com.fb.commons.PlatformException;

public class PointsHeaderDoesNotExist extends PlatformException {

	private static final long serialVersionUID = 1L;

	public PointsHeaderDoesNotExist(String message) {
		super(message);
	}
}
