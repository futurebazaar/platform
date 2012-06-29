/**
 * 
 */
package com.fb.commons.mail.exception;

import com.fb.commons.PlatformException;

/**
 * @author nehaga
 *
 */
public class MailNoSenderException extends PlatformException {

	/**
	 * 
	 */
	public MailNoSenderException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MailNoSenderException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public MailNoSenderException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public MailNoSenderException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
