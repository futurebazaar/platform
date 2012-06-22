/**
 * 
 */
package com.fb.commons.mail.exception;

import com.fb.commons.PlatformException;

/**
 * @author nehaga
 *
 */
public class MailNoReceiverException extends PlatformException {

	/**
	 * 
	 */
	public MailNoReceiverException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MailNoReceiverException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public MailNoReceiverException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public MailNoReceiverException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
