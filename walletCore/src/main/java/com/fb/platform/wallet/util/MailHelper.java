package com.fb.platform.wallet.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.springframework.mail.MailException;

import com.fb.commons.mail.exception.MailerException;
import com.fb.commons.mom.to.MailTO;


/**
 * @author keith
 *
 */
public class MailHelper {
	
	public static final String MESSAGE_TEMPLATE_URL = "ewallet_mailer_template.html";
	
	public static final String MESSAGE_RESET_TEMPLATE_URL = "ewallet_reset_mailer_template.html";
	
	//Used to load the template in this
	public static String MESSAGE_TEMPLATE_STR = "";
	public static String MESSAGE_RESET_TEMPLATE_STR = "";
	
	public static String EWALET_PASSWORD_MESSAGE_TEMPLATE_STR = "ewallet.password";
	
	public static String EWALET_RECEIVER_MESSAGE_TEMPLATE_STR = "ewallet.receiver";
	
	public static final String FROM = "order@futurebazaar.com";
	
	public static final String SUBJECT = "Your Future Bazaar Wallet Password";
	
	public static final String SUBJECT_RESET = "Your Future Bazaar Wallet Pin has been reset";
	
	public static final String CC = "";
	
	public static final String BCC = "pm@futurebazaar.com";
	
	public static final String FROM_NAME = "Future Bazaar";

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
	 * @param eWalletPassword
	 * @return
	 * @throws MailException
	 */
	public static MailTO createMailTO(String to, String eWalletPassword, String receiverName,boolean isReset) throws MailException{
		MailTO mail = new MailTO();
		mail.setFrom(FROM);
		mail.setFromPersonal(FROM_NAME);
		//Set message using template		
		String message = MESSAGE_TEMPLATE_STR.replaceAll(EWALET_PASSWORD_MESSAGE_TEMPLATE_STR, eWalletPassword)
				.replaceAll(EWALET_RECEIVER_MESSAGE_TEMPLATE_STR, receiverName);
		String messageReset = MESSAGE_RESET_TEMPLATE_STR.replaceAll(EWALET_PASSWORD_MESSAGE_TEMPLATE_STR, eWalletPassword)
				.replaceAll(EWALET_RECEIVER_MESSAGE_TEMPLATE_STR, receiverName);
		
		mail.setMessage(message);
		mail.setSubject(SUBJECT);
		if(isReset){
			mail.setMessage(messageReset);
			mail.setSubject(SUBJECT_RESET);
		}
		
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
	// Static block to load the reset message template from html
	static {
		try {
			  InputStream messageTemplateStream = MailHelper.class.getClassLoader().getResourceAsStream(MESSAGE_RESET_TEMPLATE_URL);
			  BufferedReader br = new BufferedReader(new InputStreamReader(messageTemplateStream));
			  StringBuilder messageTemplateString = new StringBuilder("");
			  String strLine;
			  while ((strLine = br.readLine()) != null)   {
				  messageTemplateString.append(strLine);
			  }
			  MESSAGE_RESET_TEMPLATE_STR = messageTemplateString.toString();
		} catch (IOException e) {
			throw new MailerException("Error While Reading Message Template from file " + MESSAGE_RESET_TEMPLATE_URL, e);
		}
		
	}
	
}