/**
 * 
 */
package com.fb.platform.egv.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;

import com.fb.commons.mail.MailSender;
import com.fb.commons.mail.exception.MailerException;
import com.fb.commons.mail.to.MailTO;


/**
 * @author keith
 *
 */
public class SendEmailUtil {
	
	@Autowired
	public static MailSender mailSender = null;
	
	public static final String MESSAGE_TEMPLATE = "";
	
	public static final String FROM = "";
	
	public static final String SUBJECT = "";
	
	public static final String CC = "";
	
	public static final String BCC = "";

	/**
	 * @param from The mail sender email address
	 * @param subject Subject of the email
	 * @param to The mail recipients TO list
	 * @param cc The mail recipients CC list
	 * @param bcc The mail recipients BCC list
	 * @param message The Text message to be sent 
	 * @throws MailException
	 * @see MailException
	 */
	public static void sendMail(String to,String eGV, String pin) throws MailException{
		MailTO mail = new MailTO();
		mail.setFrom(FROM);
		//Set message using template
		String message = MESSAGE_TEMPLATE + "";
		mail.setMessage(message);
		mail.setSubject(SUBJECT);
		mail.setTo(new String[] { to });
		mail.setCc(new String[] { CC });
		mail.setBcc(new String[] {BCC });
		try {
			mailSender.send(mail);
		} catch (MailException e) {
			throw new MailerException("Error sending mail", e);
		}
	}
	
	public void setMailSender(MailSender m) {
		this.mailSender = m;
	}

}
