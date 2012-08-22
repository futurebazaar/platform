/**
 * 
 */
package com.fb.platform.mom.inventory.receiver;

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

import com.fb.commons.PlatformException;
import com.fb.commons.mom.to.InventoryTO;
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
		InputStream propertiesStream = InventoryMessageReceiver.class.getClassLoader().getResourceAsStream("receivers.properties");
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
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(inventoryURL);

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();

		parameters.add(new BasicNameValuePair("transactioncode", inventoryTO.getTransactionCode()));
		parameters.add(new BasicNameValuePair("articleid", inventoryTO.getArticleId()));
		parameters.add(new BasicNameValuePair("issuingsite", inventoryTO.getIssuingSite()));
		parameters.add(new BasicNameValuePair("receivingsite", inventoryTO.getReceivingSite()));
		parameters.add(new BasicNameValuePair("issuingstorageloc", inventoryTO.getIssuingStorageLoc()));
		parameters.add(new BasicNameValuePair("receivingstorageloc", inventoryTO.getReceivingStorageLoc()));
		parameters.add(new BasicNameValuePair("movementtype", inventoryTO.getMovementType()));
		parameters.add(new BasicNameValuePair("sellingunit", inventoryTO.getSellingUnit()));
		parameters.add(new BasicNameValuePair("quantity", inventoryTO.getQuantity()));
		if(inventoryTO.getSapIdoc() != null) {
			parameters.add(new BasicNameValuePair("idocnumber", inventoryTO.getSapIdoc().getIdocNumber()));
			parameters.add(new BasicNameValuePair("idocrefuid", inventoryTO.getSapIdoc().getRefUID()));
			parameters.add(new BasicNameValuePair("segment", String.valueOf(inventoryTO.getSapIdoc().getSegmentNumber())));
			parameters.add(new BasicNameValuePair("ponumber", inventoryTO.getSapIdoc().getPoNumber()));
			parameters.add(new BasicNameValuePair("cangr", inventoryTO.getSapIdoc().getCanGr()));
		}
		parameters.add(new BasicNameValuePair("sender", "MOM"));

		UrlEncodedFormEntity entity;
		try {
			entity = new UrlEncodedFormEntity(parameters, "UTF-8");
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				log.error("Inventory ack not delivered : " + inventoryTO.toString());
				throw new PlatformException("Inventory ack not delivered to tinla on URL : " + inventoryURL);
			}
			log.info("Inventory ack delivered to tinla. Status code : " + statusCode);
		} catch (UnsupportedEncodingException e) {
			log.error("Error communicating with tinla on url : " + inventoryURL, e);
			throw new PlatformException("Error communicating with tinla on url : " + inventoryURL, e);
		} catch (ClientProtocolException e) {
			log.error("Error communicating with tinla on url : " + inventoryURL, e);
			throw new PlatformException("Error communicating with tinla on url : " + inventoryURL, e);
		} catch (IOException e) {
			log.error("Error communicating with tinla on url : " + inventoryURL, e);
			throw new PlatformException("Error communicating with tinla on url : " + inventoryURL, e);
		}

	}
}
