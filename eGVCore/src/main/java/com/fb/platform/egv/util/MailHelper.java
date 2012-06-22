/**
 * 
 */
package com.fb.platform.egv.util;

import java.math.BigDecimal;

import org.springframework.mail.MailException;

import com.fb.commons.mail.to.MailTO;


/**
 * @author keith
 *
 */
public class MailHelper {
		
	public static final String MESSAGE_TEMPLATE = "Congratulations you have received a Gift Voucher worth ";
	
	public static final String FROM = "no-reply@futuregroup.com";
	
	public static final String SUBJECT = "Congratulations you have received a Gift Voucher worth ";
	
	public static final String CC = "keith.fernandez@futuregroup.in";
	
	public static final String BCC = "kishan.gajjar@futuregroup.in";

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
	public static MailTO createMailTO(String to, BigDecimal amount, String eGV, String pin) throws MailException{
		MailTO mail = new MailTO();
		mail.setFrom(FROM);
		//Set message using template
		String message = MESSAGE_TEMPLATE + amount + " GV number : " + eGV + " and Password is " + pin;
		mail.setMessage(message);
		mail.setSubject(SUBJECT+amount);
		mail.setTo(new String[] { to });
		mail.setCc(new String[] { CC });
		mail.setBcc(new String[] {BCC });
		return mail;
	}
	
}
