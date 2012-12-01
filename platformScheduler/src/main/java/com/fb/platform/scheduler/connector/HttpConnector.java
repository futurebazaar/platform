/**
 * 
 */
package com.fb.platform.scheduler.connector;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.scheduler.to.HttpResponseTO;

/**
 * @author anubhav
 *
 */
public class HttpConnector {

	private static Log logger = LogFactory.getLog(HttpConnector.class);
	private static HttpClient httpClient = new HttpClient();
	
	public static HttpResponseTO sendXml(String url, String xml) throws HttpException, IOException {
		PostMethod postMethod = new PostMethod(url);
		StringRequestEntity requestEntity = new StringRequestEntity(xml);
		postMethod.setRequestEntity(requestEntity);
		return execute(postMethod);
	}
	
	public static HttpResponseTO sendParameter(String url, Map<String, String> keyValuePair) throws HttpException, IOException {
		PostMethod postMethod = new PostMethod(url);
		for (String key : keyValuePair.keySet()) {
			postMethod.addParameter(key, keyValuePair.get(key));
			logger.info("name Value pair :" + postMethod.getParameter(key));
		}
		return execute(postMethod);
	}
	
	private static HttpResponseTO execute(PostMethod postMethod) throws HttpException, IOException {
		int statusCode = httpClient.executeMethod(postMethod);
		String responseMessage = postMethod.getResponseBodyAsString();
		HttpResponseTO httpResponseTO = new HttpResponseTO();
		httpResponseTO.setMessage(responseMessage);
		httpResponseTO.setStatusCode(statusCode);
		return httpResponseTO;
		
	}
}
