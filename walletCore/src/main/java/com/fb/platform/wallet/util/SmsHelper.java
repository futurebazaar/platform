package com.fb.platform.wallet.util;

import org.springframework.mail.MailException;

import com.fb.commons.communication.to.SmsTO;


/**
 * @author kumar
 *
 */
public class SmsHelper {
	
	//Used to load the template in this
	public static String SMS_TEMPLATE_STR = "Your Future Bazaar wallet is now active. " +
"Use pin: ewallet.password to shop using wallet on futurebazaar.com. Please do not share your pin. For queries, please call 0922-222-1947";
	
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
		if(mobileNum.length() == 11 && mobileNum.substring(0,1)== "0"){
			mobileNum = mobileNum.substring(0);
		}
		if(mobileNum.length() == 10){
			smsTo.addTo("91" + mobileNum);
		}else{
			smsTo.addTo(mobileNum);
		}
		
		
		//Set message using template
		
		String message = SMS_TEMPLATE_STR.replaceAll(EWALLET_PASSWORD_SMS_TEMPLATE_STR, eWalletPassword);
		smsTo.setMessage(message);
		return smsTo;
	}
	
}
