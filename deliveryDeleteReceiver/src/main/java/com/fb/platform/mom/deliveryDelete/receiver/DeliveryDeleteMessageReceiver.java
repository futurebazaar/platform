/**
 * 
 */
package com.fb.platform.mom.deliveryDelete.receiver;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.DateTime;

import com.fb.commons.PlatformException;
import com.fb.commons.mom.to.DeliveryDeleteTO;
import com.fb.commons.mom.to.TinlaAckType;
import com.fb.platform.mom.manager.PlatformMessageReceiver;

/**
 * @author nehaga
 *
 */
public class DeliveryDeleteMessageReceiver implements PlatformMessageReceiver {

	/* (non-Javadoc)
	 * @see com.fb.platform.mom.manager.PlatformMessageReceiver#handleMessage(java.lang.Object)
	 */
	
	private static Log log = LogFactory.getLog(DeliveryDeleteMessageReceiver.class);
	
	private static Properties prop = initProperties();

	private static Properties initProperties() {
		Properties properties = new Properties();
		InputStream propertiesStream = DeliveryDeleteMessageReceiver.class.getClassLoader().getResourceAsStream("receivers.properties");
		try {
			properties.load(propertiesStream);
		} catch (IOException e) {
			log.error("Error loading properties file.", e);
			throw new PlatformException("Error loading properties file.", e);
		}
		return properties;
	}

	@Override
	public void handleMessage(Object message) {
		log.info("Received the message : " + message);

		DeliveryDeleteTO deliveryDeleteTO = (DeliveryDeleteTO) message;
		sendAck(deliveryDeleteTO);
	}

	private void sendAck(DeliveryDeleteTO deliveryDeleteTO) {

		String deliveryDeleteURL = prop.getProperty("receiver.deliveryDelete.url");
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(deliveryDeleteURL);

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();

		parameters.add(new BasicNameValuePair("orderId", deliveryDeleteTO.getOrderNo()));
		parameters.add(new BasicNameValuePair("sapDocumentId", deliveryDeleteTO.getItemNo() + ""));
		parameters.add(new BasicNameValuePair("deletedUser", deliveryDeleteTO.getUser()));
		parameters.add(new BasicNameValuePair("deletedTCode", deliveryDeleteTO.getTransactionCode()));
		parameters.add(new BasicNameValuePair("deliveryNumber", deliveryDeleteTO.getDeliveryNo()));
		parameters.add(new BasicNameValuePair("atgDocumentId", ""));
		parameters.add(new BasicNameValuePair("header", TinlaAckType.DEL_ACK.toString()));
		parameters.add(new BasicNameValuePair("lspUpdDescr", ""));
		parameters.add(new BasicNameValuePair("lspName", ""));
		parameters.add(new BasicNameValuePair("lspUpdDescr", ""));
		parameters.add(new BasicNameValuePair("awbNumber", ""));	
		
		Integer year = Integer.parseInt(deliveryDeleteTO.getDate().substring(0, 4));
		Integer month = Integer.parseInt(deliveryDeleteTO.getDate().substring(4, 6));
		Integer date = Integer.parseInt(deliveryDeleteTO.getDate().substring(6));
		Integer hour = Integer.parseInt(deliveryDeleteTO.getTime().substring(0, 2));
		Integer minute = Integer.parseInt(deliveryDeleteTO.getTime().substring(2, 4));
		Integer second = Integer.parseInt(deliveryDeleteTO.getTime().substring(4));
		DateTime dateTime = new DateTime(year, month, date, hour, minute, second);
		parameters.add(new BasicNameValuePair("deletionDateTime", dateTime.toString()));
		if(deliveryDeleteTO.getSapIdoc() != null) {
			parameters.add(new BasicNameValuePair("idocnumber", deliveryDeleteTO.getSapIdoc().getIdocNumber()));
		}
		parameters.add(new BasicNameValuePair("sender", "MOM"));

		UrlEncodedFormEntity entity;
		try {
			entity = new UrlEncodedFormEntity(parameters, "UTF-8");
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				log.error("Delivery Delete ack not delivered : " + deliveryDeleteTO.toString());
				throw new PlatformException("Delivery Delete ack not delivered to tinla on URL : " + deliveryDeleteURL);
			}
			log.info("Delivery Delete ack delivered to tinla. Status code : " + statusCode);
		} catch (UnsupportedEncodingException e) {
			log.error("Error communicating with tinla on url : " + deliveryDeleteURL, e);
			throw new PlatformException("Error communicating with tinla on url : " + deliveryDeleteURL, e);
		} catch (ClientProtocolException e) {
			log.error("Error communicating with tinla on url : " + deliveryDeleteURL, e);
			throw new PlatformException("Error communicating with tinla on url : " + deliveryDeleteURL, e);
		} catch (IOException e) {
			log.error("Error communicating with tinla on url : " + deliveryDeleteURL, e);
			throw new PlatformException("Error communicating with tinla on url : " + deliveryDeleteURL, e);
		}

	}
}