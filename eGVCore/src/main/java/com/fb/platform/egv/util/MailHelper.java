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
		
	public static final String MESSAGE_TEMPLATE = "Congratulation you have received a Gift Voucher worth ";
	
	public static final String FROM = "keith.fernandez@futuregroup.in";
	
	public static final String SUBJECT = "Congratulation you have received a Gift Voucher worth ";
	
	public static final String CC = "keith.fernandez@futuregroup.in";
	
	public static final String BCC = "keith.fernandez@futuregroup.in";

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
		String message = MESSAGE_TEMPLATE + " GV number : " + eGV + " and Password is " + pin;
		mail.setMessage(message);
		mail.setSubject(SUBJECT+amount);
		mail.setTo(new String[] { "keithfernandez25@gmail.com" }); //to });
		mail.setCc(new String[] { CC });
		mail.setBcc(new String[] {BCC });
		return mail;
	}
	
}
