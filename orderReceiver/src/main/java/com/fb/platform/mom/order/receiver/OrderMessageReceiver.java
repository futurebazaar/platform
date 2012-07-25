/**
 * 
 */
package com.fb.platform.mom.order.receiver;

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
import com.fb.commons.mom.to.OrderTO;
import com.fb.platform.mom.manager.PlatformMessageReceiver;

/**
 * @author nehaga
 *
 */
public class OrderMessageReceiver implements PlatformMessageReceiver {

	/* (non-Javadoc)
	 * @see com.fb.platform.mom.manager.PlatformMessageReceiver#handleMessage(java.lang.Object)
	 */
	
	private static Log log = LogFactory.getLog(OrderMessageReceiver.class);
	
	private static Properties prop = initProperties();

	private static Properties initProperties() {
		Properties properties = new Properties();
		InputStream propertiesStream = OrderMessageReceiver.class.getClassLoader().getResourceAsStream("receivers.properties");
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

		OrderTO orderTo = (OrderTO) message;
		sendAck(orderTo);
	}

	private void sendAck(OrderTO orderTO) {

		String orderURL = prop.getProperty("receiver.order.url");
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(orderURL);

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();

		if(orderTO.getSapIdoc() != null) {
			parameters.add(new BasicNameValuePair("idocnumber", orderTO.getSapIdoc().getIdocNumber()));
		}
		parameters.add(new BasicNameValuePair("sender", "MOM"));

		UrlEncodedFormEntity entity;
		try {
			entity = new UrlEncodedFormEntity(parameters, "UTF-8");
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				log.error("Order ack not delivered : " + orderTO.toString());
				throw new PlatformException("Order ack not delivered to tinla on URL : " + orderURL);
			}
			log.info("Order ack delivered to tinla. Status code : " + statusCode);
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
