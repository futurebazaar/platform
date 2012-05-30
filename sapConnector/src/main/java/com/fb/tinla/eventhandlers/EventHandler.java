package com.fb.tinla.eventhandlers;

import java.util.*;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

public abstract class EventHandler {

	static Logger logger = Logger.getLogger("auris");
	
	
	protected String path;

	protected EventHandler(String path) {
		this.path = path;
	}

	public void sendEventToTinla(Map<String, String> params) {
		try {
			// Create a post request
			HttpPost httpPost = new HttpPost(String.format(
					"http://admin.futurebazaar.com/support%s", path));

			// Set the parameters
			StringBuilder logStr = new StringBuilder();
			
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			Iterator<String> keyIterator = params.keySet().iterator();
			while (keyIterator.hasNext()) {
				String key = keyIterator.next();
				parameters.add(new BasicNameValuePair(key, params.get(key)));
				logStr.append(key + ":" + params.get(key) + " ");
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "UTF-8"); 
			httpPost.setEntity(entity);

			
			logger.debug("Sending to tinla " + httpPost.getURI().toString());
			logger.debug("Sending to tinla " + logStr.toString());
			// Send the request
			HttpClient client = new DefaultHttpClient();
			client.execute(httpPost);
			logger.debug("Sent to tinla");
		} catch (Exception e) {
			logger.error("Error sending to tina",e);
		}
	}
}
