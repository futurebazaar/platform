/**
 * 
 */
package com.fb.commons.sms;

import javax.ws.rs.core.MultivaluedMap;

import com.fb.commons.communication.to.SmsTO;
import com.fb.commons.mail.exception.SmsException;
import com.fb.commons.sms.util.SMSPropertiesUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * @author keith
 * 
 */
public class SmsSender {

	public static String IS_PROXY = "IS_PROXY";
	public static String SMS_FEED_ID_STR = "feedid";
	public static String SMS_PASSWORD_STR = "password";
	public static String SMS_USERNAME_STR = "username";
	public static String SMS_TEXT_STR = "text";
	public static String SMS_TO_STR = "to";
	public static String SMS_BCC_STR = "bcc";
	public static String SMS_SENDER_ID_STR = "senderid";

	public static String SMS_API_URL = "SMS_API_URL";

	/**
	 * @param smsTO
	 *            : Contains details of the SMS Message to be sent
	 * @throws SmsException
	 */
	public String send(final SmsTO smsTO) throws SmsException {
		String output = null;
		try {
			boolean isProxy = false;
			isProxy = Boolean.parseBoolean(SMSPropertiesUtil.getProperty(IS_PROXY));
			Client client = null;
			if (isProxy) {
				// Get Proxy Connection
				URLConnectionClientHandler urlConnectionClientHandler = new URLConnectionClientHandler(
						new ProxyHttpUrlConnection());

				client = new Client(urlConnectionClientHandler);
			} else {
				client = Client.create();
			}

			WebResource webResource = client.resource(SMSPropertiesUtil.getProperty(SMS_API_URL));

			MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
			queryParams.add(SMS_FEED_ID_STR, SMSPropertiesUtil.getProperty(SMS_FEED_ID_STR));
			queryParams.add(SMS_PASSWORD_STR, SMSPropertiesUtil.getProperty(SMS_PASSWORD_STR));
			queryParams.add(SMS_USERNAME_STR, SMSPropertiesUtil.getProperty(SMS_USERNAME_STR));
			queryParams.add(SMS_TEXT_STR, smsTO.getMessage());
			queryParams.add(SMS_TO_STR, smsTO.toListAsString());
			queryParams.add(SMS_BCC_STR, smsTO.getBcc());
			queryParams.add(SMS_SENDER_ID_STR, SMSPropertiesUtil.getProperty(SMS_SENDER_ID_STR));

			ClientResponse response = webResource.queryParams(queryParams).accept("application/xml")
					.get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new SmsException("Error While Sending SMS Failed : HTTP error code : " + response.getStatus());
			}

			output = response.getEntity(String.class);

			System.out.println(output);
			if (output.indexOf("<ERROR>") != -1) {
				throw new SmsException("Error While Sending SMS bulkpush");
			}
			return output;

		} catch (SmsException e) {
			throw new SmsException("Error While Sending SMS " + output, e);
		} catch (Exception e) {
			throw new SmsException("Unknown Error while sending sms", e);
		}
	}

}
