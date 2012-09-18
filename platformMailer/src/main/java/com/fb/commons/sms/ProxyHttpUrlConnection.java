/**
 * 
 */
package com.fb.commons.sms;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import com.fb.commons.sms.util.SMSPropertiesUtil;
import com.sun.jersey.client.urlconnection.HttpURLConnectionFactory;

/**
 * @author keith
 * 
 */
public class ProxyHttpUrlConnection implements HttpURLConnectionFactory {

	public static final String PROXY_HOST_PROPERTY_KEY = "HTTP_PROXY_HOST";

	public static final String PROXY_PORT_PROPERTY_KEY = "HTTP_PROXY_PORT";

	@Override
	public HttpURLConnection getHttpURLConnection(URL url) throws IOException {

		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
				SMSPropertiesUtil.getProperty(PROXY_HOST_PROPERTY_KEY), Integer.parseInt(SMSPropertiesUtil
						.getProperty(PROXY_PORT_PROPERTY_KEY))));

		return new sun.net.www.protocol.http.HttpURLConnection(url, proxy);

	}
}
