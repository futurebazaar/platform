/**
 * 
 */
package com.fb.platform.sap;

import com.fb.commons.PlatformException;

/**
 * The base class for the all the SAP communication related exceptions.
 * @author vinayak
 *
 */
public class PlatformSapException extends PlatformException {

	/**
	 * 
	 */
	public PlatformSapException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public PlatformSapException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public PlatformSapException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public PlatformSapException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
