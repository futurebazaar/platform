/**
 * 
 */
package com.fb.platform.egv.mock;

import com.fb.commons.communication.to.SmsTO;
import com.fb.commons.mail.exception.SmsException;

/**
 * This is mock implementation of Sending SMS for testing purpose only
 * 
 * @author keith
 * 
 */
public class SmsSender {

	public String send(final SmsTO smsTO) throws SmsException {
		// Dummy return
		return "<SUCCESS></SUCCESS>";
	}
}
