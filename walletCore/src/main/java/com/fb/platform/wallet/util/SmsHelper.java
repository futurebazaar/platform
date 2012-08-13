package com.fb.platform.wallet.util;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.springframework.mail.MailException;

import com.fb.commons.communication.to.SmsTO;


/**
 * @author kumar
 *
 */
public class SmsHelper {
	
	//Used to load the template in this
	public static String SMS_TEMPLATE_STR = "Hi! ewallet.reciever,your electronic wallet has been created at Futurebazaar.com." +
"Use ewallet.password as password to pay using wallet. For help," +
"call 09222221947";
	
	public static String EWALLET_PASSWORD_SMS_TEMPLATE_STR = "ewallet.password";
	
	public static String EWALLET_SENDER_SMS_TEMPLATE_STR = "ewallet.sender";
	
	public static String EWALLET_RECEIVER_SMS_TEMPLATE_STR = "ewallet.receiver";
	
	
	/**
	 * @param mobileNum
	 * @param ewalletPassword
	 * @return
	 * @throws MailException
	 */
	public static SmsTO createSmsTO(String mobileNum, String eWalletPassword,String receiverName) throws MailException{
		SmsTO smsTo = new SmsTO();
		smsTo.addTo(mobileNum);
		
		//Set message using template		
		String message = SMS_TEMPLATE_STR.replaceAll(EWALLET_PASSWORD_SMS_TEMPLATE_STR, eWalletPassword)
				.replaceAll(EWALLET_RECEIVER_SMS_TEMPLATE_STR, receiverName);
		smsTo.setMessage(message);
		System.out.println(message);
		return smsTo;
	}
	
}
