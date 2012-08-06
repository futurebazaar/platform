/**
 * 
 */
package com.fb.commons.sms;

import java.util.Properties;

import javax.ws.rs.core.MultivaluedMap;

import com.fb.commons.communication.to.SmsTO;
import com.fb.commons.mail.exception.SmsException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * @author keith
 * 
 */
public class SmsSender {

	public static String PROXY_HOST_STR = "http.proxyHost";
	public static String PROXY_PORT_STR = "http.proxyPort";
	public static String SMS_FEED_ID_STR = "feedid";
	public static String SMS_PASSWORD_STR = "password";
	public static String SMS_USERNAME_STR = "username";
	public static String SMS_TEXT_STR = "text";
	public static String SMS_TO_STR = "to";
	public static String SMS_SENDER_ID_STR = "senderid";

	public static String SMS_API_URL = "http://bulkpush.mytoday.com/BulkSms/SingleMsgApi";

	/**
	 * @param smsTO
	 *            : Contains to whom and the SMS Message to be sent
	 * @throws SmsException
	 */
	public String send(final SmsTO smsTO) throws SmsException {
		try {

			Properties prop = System.getProperties();
			System.setProperty(PROXY_HOST_STR, "10.202.18.154");
			System.setProperty(PROXY_PORT_STR, "3128");

			Client client = Client.create();

			WebResource webResource = client.resource(SMS_API_URL);

			MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
			queryParams.add(SMS_FEED_ID_STR, "315128");
			queryParams.add(SMS_PASSWORD_STR, "dwdjd");
			queryParams.add(SMS_USERNAME_STR, "9819766601");
			queryParams.add(SMS_TEXT_STR, smsTO.getMessage());
			queryParams.add(SMS_TO_STR, smsTO.toListAsString());
			queryParams.add(SMS_SENDER_ID_STR, "FutrBazr");

			ClientResponse response = webResource.queryParams(queryParams).accept("application/xml")
					.get(ClientResponse.class);

			System.out.println(webResource.getURI().toURL().toString());
			System.setProperties(prop);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			String output = response.getEntity(String.class);

			return output;
			//
			// if(output.indexOf("<ERROR") != -1) {
			// throw new SmsException("Error While Sending SMS" +
			// output.toString());
			// }
		} catch (SmsException e) {
			throw new SmsException("Error While Sending SMS", e);
		} catch (Exception e) {
			throw new SmsException("Error sending sms", e);
		}
	}

}
