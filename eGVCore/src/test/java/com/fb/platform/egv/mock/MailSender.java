/**
 * 
 */
package com.fb.platform.egv.mock;

import org.springframework.mail.MailException;

import com.fb.commons.mom.to.MailTO;

/**
 * This is mock implementation of Sending mail for testing purpose only
 * 
 * @author keith
 * 
 */
public class MailSender {

	public void send(final MailTO mailTO) throws MailException {
		// Do nothing ( Dummy Implementation )
	}
}
