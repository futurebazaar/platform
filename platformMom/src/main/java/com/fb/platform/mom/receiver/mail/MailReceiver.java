/**
 * 
 */
package com.fb.platform.mom.receiver.mail;

import javax.jms.TextMessage;

/**
 * @author vinayak
 *
 */
public interface MailReceiver {

	public TextMessage receiveMail();
}
