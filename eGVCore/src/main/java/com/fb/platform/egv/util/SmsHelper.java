/**
 * 
 */
package com.fb.platform.egv.util;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.springframework.mail.MailException;

import com.fb.commons.communication.to.SmsTO;


/**
 * @author keith
 *
 */
public class SmsHelper {
	
	//Used to load the template in this
	public static String SMS_TEMPLATE_STR = "Hooray! egv.sender sent you a Futurebazaar.com eGift Voucher worth Rs." +
"egv.amount eGV number egv.number. Use pin egv.pin to redeem. For help," +
"call 09222221947";
	
	public static String EGV_NUMBER_SMS_TEMPLATE_STR = "egv.number";
	
	public static String EGV_PIN_SMS_TEMPLATE_STR = "egv.pin";
	
	public static String EGV_SENDER_SMS_TEMPLATE_STR = "egv.sender";
	
	public static String EGV_RECEIVER_SMS_TEMPLATE_STR = "egv.receiver";
	
	public static String EGV_VALIDITY_SMS_TEMPLATE_STR = "egv.validity";
	
	public static String EGV_AMOUNT_SMS_TEMPLATE_STR = "egv.amount";
	
	public static String EGV_GIFT_SMS_MESSAGE_TEMPLATE_STR = "egv.message";
	
	/**
	 * @param from The mail sender email address
	 * @param subject Subject of the email
	 * @param to The mail recipients TO list
	 * @param message The Text message to be sent 
	 * @throws MailException
	 * @see MailException
	 */
	/**
	 * @param mobileNum
	 * @param amount
	 * @param eGVNumber
	 * @param eGVPin
	 * @return
	 * @throws MailException
	 */
	public static SmsTO createSmsTO(String mobileNum, BigDecimal amount, String eGVNumber, String eGVPin, DateTime date, String senderName, String receiverName, String giftMessage) throws MailException{
		SmsTO smsTo = new SmsTO();
		smsTo.addTo(mobileNum);
		
		//Set message using template		
		String message = SMS_TEMPLATE_STR.replaceAll(EGV_NUMBER_SMS_TEMPLATE_STR,eGVNumber)
				.replaceAll(EGV_PIN_SMS_TEMPLATE_STR, eGVPin)
				.replaceAll(EGV_AMOUNT_SMS_TEMPLATE_STR, String.valueOf(amount.intValue()))
				.replaceAll(EGV_VALIDITY_SMS_TEMPLATE_STR, date.getDayOfMonth() + " " + date.monthOfYear().getAsShortText() + ", " + date.getYear())
				.replaceAll(EGV_SENDER_SMS_TEMPLATE_STR, senderName)
				.replaceAll(EGV_RECEIVER_SMS_TEMPLATE_STR, receiverName)
				.replaceAll(EGV_GIFT_SMS_MESSAGE_TEMPLATE_STR, giftMessage);
		smsTo.setMessage(message);
		System.out.println(message);
		return smsTo;
	}
	
}
