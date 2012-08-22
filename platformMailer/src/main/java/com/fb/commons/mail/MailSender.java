/**
 * 
 */
package com.fb.commons.mail;

import com.fb.commons.mom.to.MailTO;

/**
 * @author nehaga
 *
 */
public interface MailSender {

	public void send(final MailTO mailTO);
}
