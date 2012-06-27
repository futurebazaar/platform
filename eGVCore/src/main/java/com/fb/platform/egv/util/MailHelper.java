/**
 * 
 */
package com.fb.platform.egv.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.springframework.mail.MailException;

import com.fb.commons.mail.exception.MailerException;
import com.fb.commons.mail.to.MailTO;


/**
 * @author keith
 *
 */
public class MailHelper {
	
	public static final String MESSAGE_TEMPLATE_URL = "egv_mailer_template.html";
	
	//Used to load the template in this
	public static String MESSAGE_TEMPLATE_STR = "";
	
	public static String EGV_NUMBER_MESSAGE_TEMPLATE_STR = "egv.number";
	
	public static String EGV_PIN_MESSAGE_TEMPLATE_STR = "egv.pin";
	
	public static String EGV_VALIDITY_MESSAGE_TEMPLATE_STR = "egv.validity";
	
	public static String EGV_AMOUNT_MESSAGE_TEMPLATE_STR = "egv.amount";
		
	public static final String FROM = "orders@futuregroup.com";
	
	public static final String SUBJECT = "Congratulations you have received a Gift Voucher worth Rs. egv.amount";
	
	public static final String CC = "";
	
	public static final String BCC = "pm@futuregroup.com";

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
	/**
	 * @param to
	 * @param amount
	 * @param eGVNumber
	 * @param eGVPin
	 * @return
	 * @throws MailException
	 */
	public static MailTO createMailTO(String to, BigDecimal amount, String eGVNumber, String eGVPin, DateTime date) throws MailException{
		MailTO mail = new MailTO();
		mail.setFrom(FROM);
		//Set message using template		
		String message = MESSAGE_TEMPLATE_STR.replaceAll(EGV_NUMBER_MESSAGE_TEMPLATE_STR,eGVNumber).replaceAll(EGV_PIN_MESSAGE_TEMPLATE_STR, eGVPin).replaceAll(EGV_AMOUNT_MESSAGE_TEMPLATE_STR, amount.toString()).replaceAll(EGV_VALIDITY_MESSAGE_TEMPLATE_STR, date.getDayOfMonth() + "-" + date.monthOfYear().getAsShortText() + "-" + date.getYear());
		mail.setMessage(message);
		mail.setSubject(SUBJECT.replaceAll(EGV_AMOUNT_MESSAGE_TEMPLATE_STR, amount.toString()));
		mail.setTo(new String[] { to });
//		mail.setCc(new String[] { CC });
		mail.setBcc(new String[] {BCC });
		mail.setHtmlText(true);
		return mail;
	}
	
	// Static block to load the message template from html
	static {
		try {
			  InputStream messageTemplateStream = MailHelper.class.getClassLoader().getResourceAsStream(MESSAGE_TEMPLATE_URL);
			  BufferedReader br = new BufferedReader(new InputStreamReader(messageTemplateStream));
			  StringBuilder messageTemplateString = new StringBuilder("");
			  String strLine;
			  while ((strLine = br.readLine()) != null)   {
				  messageTemplateString.append(strLine);
			  }
			  MESSAGE_TEMPLATE_STR = messageTemplateString.toString();
		} catch (IOException e) {
			throw new MailerException("Error While Reading Message Template from file " + MESSAGE_TEMPLATE_URL, e);
		}
		
	}
	
}
