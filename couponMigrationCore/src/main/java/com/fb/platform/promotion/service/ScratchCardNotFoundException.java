/**
 * 
 */
package com.fb.platform.promotion.service;

import com.fb.commons.PlatformException;

/**
 * @author vinayak
 *
 */
public class ScratchCardNotFoundException extends PlatformException {

	public ScratchCardNotFoundException() {
		super();
	}

	public ScratchCardNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ScratchCardNotFoundException(String message) {
		super(message);
	}

	public ScratchCardNotFoundException(Throwable cause) {
		super(cause);
	}
}
