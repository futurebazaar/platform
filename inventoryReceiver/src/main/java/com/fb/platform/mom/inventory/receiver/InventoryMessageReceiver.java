/**
 * 
 */
package com.fb.platform.mom.inventory.receiver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.PlatformException;
import com.fb.platform.mom.inventory.to.InventoryTO;
import com.fb.platform.mom.manager.PlatformMessageReceiver;

/**
 * @author nehaga
 *
 */
public class InventoryMessageReceiver implements PlatformMessageReceiver {

	/* (non-Javadoc)
	 * @see com.fb.platform.mom.manager.PlatformMessageReceiver#handleMessage(java.lang.Object)
	 */
	
	private static Log log = LogFactory.getLog(InventoryMessageReceiver.class);
	
	private static Properties prop = initProperties();

	private static Properties initProperties() {
		Properties properties = new Properties();
		InputStream propertiesStream = InventoryMessageReceiver.class.getClassLoader().getResourceAsStream("tinlaReceivers.properties");
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

		InventoryTO inventoryTO = (InventoryTO) message;
		sendAck(inventoryTO);
	}

	private void sendAck(InventoryTO inventoryTO) {

		String inventoryURL = prop.getProperty("receiver.inventory.url");
		
		HttpClient httpClient = new HttpClient();
		PostMethod inventoryAckMethod = new PostMethod(inventoryURL);
		StringRequestEntity requestEntity = new StringRequestEntity(inventoryTO.toString());
		inventoryAckMethod.setRequestEntity(requestEntity);
		int statusCode;
		try {
			statusCode = httpClient.executeMethod(inventoryAckMethod);
			if (statusCode != HttpStatus.SC_OK) {
				log.error("Inventory ack not delivered : " + inventoryTO.toString());
				throw new PlatformException("Inventory ack not delivered to tinla on URL : " + inventoryURL);
			}
			log.info("Inventory ack delivered to tinla. Status code : " + statusCode);
		} catch (HttpException e) {
			throw new PlatformException("Error communicating with tinla on url : " + inventoryURL, e);
		} catch (IOException e) {
			throw new PlatformException("Error communicating with tinla on url : " + inventoryURL, e);
		}
	}
}
