/**
 * 
 */
package com.fb.platform.mom.receiver.mail;

import static org.junit.Assert.assertEquals;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;

/**
 * @author vinayak
 *
 */
public class MailReceiverTest extends BaseTestCase {

	@Autowired
	private MailReceiver mailReceiver;

	@Autowired
	private MailSender mailSender;

	@Test
	public void receiveMail() throws JMSException {
		mailSender.sendMail();
		TextMessage receiveMail = mailReceiver.receiveMail();
		assertEquals("Hello world!", receiveMail.getText());
	}
}
