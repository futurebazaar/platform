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
import com.fb.platform.mom.util.LoggerConstants;

/**
 * @author nehaga
 *
 */
public class ItemAckMessageReceiver implements PlatformMessageReceiver {

	private static Log infoLog = LogFactory.getLog(ItemAckMessageReceiver.class);
	
	private static Log auditLog = LogFactory.getLog(LoggerConstants.ITEM_ACK_RECEIVER_AUDIT_LOG);
	
	private static Properties prop = initProperties();

	private static Properties initProperties() {
		Properties properties = new Properties();
		InputStream propertiesStream = ItemAckMessageReceiver.class.getClassLoader().getResourceAsStream("receivers.properties");
		try {
			properties.load(propertiesStream);
		} catch (IOException e) {
			infoLog.error("Error loading properties file.", e);
			throw new PlatformException("Error loading properties file.", e);
		}
		return properties;
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.mom.manager.PlatformMessageReceiver#handleMessage(java.lang.Object)
	 */
	@Override
	public void handleMessage(Object message) {
		ItemTO itemTO = (ItemTO) message;
		long uid = itemTO.getSapIdoc().getAckUID();
		String idocNumber = itemTO.getSapIdoc().getIdocNumber();
		String timestamp = itemTO.getSapIdoc().getTimestamp().toString();

		auditLog.info(uid + "," + idocNumber + "," + timestamp + ",false");
		infoLog.info("Received the message : " + message);

		sendAck(itemTO);
	}
	
	private boolean isOrderStateValid(ItemTO itemAck) {
		boolean isOrderStateValid = true;
		OrderStateEnum orderState = OrderStateEnum.getInstance(itemAck.getOrderState());
		if(orderState == OrderStateEnum.R) {
			isOrderStateValid = false;
		} else if (orderState == OrderStateEnum.C) {
			isOrderStateValid = false;
		} else if (StringUtils.isBlank(itemAck.getPlantId())) {
			isOrderStateValid = false;
		} else if (itemAck.getOrderId().equalsIgnoreCase(itemAck.getDeliveryNumber())) {
			isOrderStateValid = false;
		}
		return isOrderStateValid;
	}

	private void sendAck(ItemTO itemAck) {

		String orderURL = prop.getProperty("receiver.itemAck.url");
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(orderURL);

		List<NameValuePair> parameters = new ItemAckParameterFactory().getParameters(itemAck);
		
		for (NameValuePair param : parameters) {
			infoLog.info(param.getName() + "********" + param.getValue());
		}
		
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
					infoLog.error("Item ack not delivered : " + itemAck.toString());
					throw new PlatformException("Item ack not delivered to tinla on URL : " + orderURL);
				}
				auditLog.info(itemAck.getSapIdoc().getAckUID() + "," + itemAck.getSapIdoc().getIdocNumber() + "," + itemAck.getSapIdoc().getTimestamp() + ",true");
				infoLog.info("Item ack delivered to tinla. Status code : " + statusCode);
			} else {
				auditLog.info(itemAck.getSapIdoc().getAckUID() + "," + itemAck.getSapIdoc().getIdocNumber() + "," + itemAck.getSapIdoc().getTimestamp() + ",true");
				infoLog.info("Request not sent to tinla because order state is not processed by tinla : " + itemAck.toString());
			}
		} catch (UnsupportedEncodingException e) {
			infoLog.error("Error communicating with tinla on url : " + orderURL, e);
			infoLog.error("Item ack not delivered : " + itemAck.toString());
			throw new PlatformException("Error communicating with tinla on url : " + orderURL, e);
		} catch (ClientProtocolException e) {
			infoLog.error("Error communicating with tinla on url : " + orderURL, e);
			infoLog.error("Item ack not delivered : " + itemAck.toString());
			throw new PlatformException("Error communicating with tinla on url : " + orderURL, e);
		} catch (IOException e) {
			infoLog.error("Error communicating with tinla on url : " + orderURL, e);
			infoLog.error("Item ack not delivered : " + itemAck.toString());
			throw new PlatformException("Error communicating with tinla on url : " + orderURL, e);
		}

	}
}
