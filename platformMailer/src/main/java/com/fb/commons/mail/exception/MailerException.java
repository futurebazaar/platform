/**
 * 
 */
package com.fb.commons.mail.exception;

import com.fb.commons.PlatformException;

/**
 * @author nehaga
 *
 */
public class MailerException extends PlatformException {

	/**
	 * 
	 */
	public MailerException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MailerException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public MailerException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public MailerException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
