/**
 * 
 */
package com.fb.platform.mom.itemAck.receiver;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
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

import com.fb.commons.PlatformException;
import com.fb.commons.mom.to.ItemTO;
import com.fb.commons.mom.to.OrderStateEnum;
import com.fb.platform.mom.itemAck.sender.ItemAckParameterFactory;
import com.fb.platform.mom.manager.PlatformMessageReceiver;

/**
 * @author nehaga
 *
 */
public class ItemAckMessageReceiver implements PlatformMessageReceiver {

	/* (non-Javadoc)
	 * @see com.fb.platform.mom.manager.PlatformMessageReceiver#handleMessage(java.lang.Object)
	 */
	
	private static Log log = LogFactory.getLog(ItemAckMessageReceiver.class);
	
	private static Properties prop = initProperties();

	private static Properties initProperties() {
		Properties properties = new Properties();
		InputStream propertiesStream = ItemAckMessageReceiver.class.getClassLoader().getResourceAsStream("receivers.properties");
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

		ItemTO itemAck = (ItemTO) message;
		sendAck(itemAck);
	}
	
	private boolean isOrderStateValid(ItemTO itemAck) {
		boolean isOrderStateValid = true;
		OrderStateEnum orderState = OrderStateEnum.valueOf(itemAck.getOrderState());
		if(orderState == OrderStateEnum.R) {
			isOrderStateValid = false;
		} else if (orderState == OrderStateEnum.C && itemAck.getSapDocumentId() <= 0) {
			isOrderStateValid = false;
		} else if (StringUtils.isBlank(itemAck.getPlantId())) {
			isOrderStateValid = false;
		}
		return isOrderStateValid;
	}

	private void sendAck(ItemTO itemAck) {

		String orderURL = prop.getProperty("receiver.itemAck.url");
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(orderURL);

		List<NameValuePair> parameters = new ItemAckParameterFactory().getParameters(itemAck);
		
		parameters.add(new BasicNameValuePair("sender", "MOM"));

		UrlEncodedFormEntity entity;
		try {
			//TINLA is not processing these states as the plant id is null for these states
			//plant id are null because sap is sending the site as null.
			if(isOrderStateValid(itemAck)) {
				entity = new UrlEncodedFormEntity(parameters, "UTF-8");
				httpPost.setEntity(entity);
				HttpResponse response = httpClient.execute(httpPost);
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					log.error("Item ack not delivered : " + itemAck.toString());
					throw new PlatformException("Item ack not delivered to tinla on URL : " + orderURL);
				}
				log.info("Item ack delivered to tinla. Status code : " + statusCode);
			} else {
				log.info("Request not sent to tinla because order state is C / R : " + itemAck.toString());
			}
		} catch (UnsupportedEncodingException e) {
			log.error("Error communicating with tinla on url : " + orderURL, e);
			throw new PlatformException("Error communicating with tinla on url : " + orderURL, e);
		} catch (ClientProtocolException e) {
			log.error("Error communicating with tinla on url : " + orderURL, e);
			throw new PlatformException("Error communicating with tinla on url : " + orderURL, e);
		} catch (IOException e) {
			log.error("Error communicating with tinla on url : " + orderURL, e);
			throw new PlatformException("Error communicating with tinla on url : " + orderURL, e);
		}

	}
}
