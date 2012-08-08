/**
 * 
 */
package com.fb.commons.sms;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import com.sun.jersey.client.urlconnection.HttpURLConnectionFactory;

/**
 * @author keith
 * 
 */
public class ProxyHttpUrlConnection implements HttpURLConnectionFactory {

	public static final String host = "10.202.18.154";

	public static final int port = 3128;

	@Override
	public HttpURLConnection getHttpURLConnection(URL url) throws IOException {
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));

		return new sun.net.www.protocol.http.HttpURLConnection(url, proxy);

	}
}
