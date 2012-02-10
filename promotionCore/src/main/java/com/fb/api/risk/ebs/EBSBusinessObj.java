package com.fb.api.risk.ebs;

import java.util.*;
import java.net.*;
import java.io.*;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class EBSBusinessObj {

	EBSRMSConfig ebsConfig= new EBSRMSConfig();
	int numservers = 1;
	
	String[] server = { ebsConfig.getEbsServerOne()};

	String url;

	public HashMap queries;

	public HashMap allowed_fields;

	public HashMap outputstr;

	public boolean isSecure = true;

	public float timeout = 10; // default timeout is 10 seconds

	public boolean debug = false;

	public String check_field = "score";

	EBSBusinessObj() 
	{
		queries = new HashMap();
		allowed_fields = new HashMap();
		outputstr = new HashMap();
		isSecure = true;
	}

	EBSBusinessObj(boolean s) 
	{
		queries = new HashMap();
		allowed_fields = new HashMap();
		outputstr = new HashMap();
		isSecure = s;
	}

	public boolean getIsSecure() 
	{
		return isSecure;
	}

	public void setIsSecure(boolean isSecure) 
	{
		this.isSecure = isSecure;
	}

	public float getTimeout() 
	{
		return timeout;
	}

	public void setTimeout(float t) 
	{
		timeout = t;
	}

	// queries the servers
	public boolean query() 
	{
		if (debug) 
		{
			//System.out.println("number of servers = " + numservers);
		}

		// query every server using its domain name
		for (int i = 0; i < numservers; i++) 
		{
			boolean result = this.querySingleServer(server[i]);
			if (debug) 
			{
				//System.out.println("queried server = " + server[i]+ ", result = " + result);
			}
			if (result == true) 
			{
				return result;
			}
		}
		return false;
	}

	// takes a input hash and stores it in the hash named queries
	public void input(HashMap h) 
	{
		queries = new HashMap();
		
		for (Iterator i = h.keySet().iterator(); i.hasNext();) 
		{
			String key = (String) i.next();
			// check if key is a allowed field
			if (allowed_fields.containsKey(key)) 
			{
				String value = (String) h.get(key);
				queries.put(key, filter_field(key, value));
			} 
			else 
			{
				//System.out.println("key " + key + " is not a allowed field ");
			}
		}
	}

	// sub-class should override this if it needs to filter inputs
	protected String filter_field(String key, String value) 
	{
		return value;
	}

	// returns the output from the server
	public HashMap output() 
	{
		return outputstr;
	}

	// queries a single server
	boolean querySingleServer(String server) 
	{
		String scheme, url2;

		NameValuePair[] query_data = new NameValuePair[queries.size() + 1];
		// check if we using the Secure HTTPS proctol
		if (isSecure == true) 
		{
			
			scheme = "https://"; // Secure HTTPS proctol
			
		} 
		else 
		{
			
			scheme = "http://"; // Regular HTTP proctol
		}

		// build a query string from the hash called queries
		int n = 0;
		for (Iterator i = queries.keySet().iterator(); i.hasNext();) 
		{
			// for each element in the hash called queries
			// append the key and the value of the element to the query string
			
			String key = (String) i.next();
			String value = (String) queries.get(key);
			
			if (debug) 
			{
				
				//System.out.println("query key " + key + " = " + value);
			}
			query_data[n] = new NameValuePair(key, value);
			n++;
		}
		query_data[n] = new NameValuePair("clientAPI", "Java/0.0.2");
		
		// scheme already has the name of the proctol
		// append the domain name of the server, url of the web service
		// and the query string to the string named url2
		
		url2 = scheme + server + "/" + url;
		
		//System.out.println("SERVER URL:::::::"+url2);
		
		if (debug) 
		{
			
			System.out.println("url2 = " + url2);
		}
		try 
		{
			
			String proxyIP= ebsConfig.getProxyIP();
			int proxyPort=  Integer.parseInt(ebsConfig.getProxyPort());
			HostConfiguration hconfig = new HostConfiguration();
			hconfig.setProxy(proxyIP, proxyPort);

			org.apache.commons.httpclient.HttpClient client = new HttpClient();
			client.setHostConfiguration(hconfig);

			client.setConnectionTimeout((int) timeout * 1000);
			client.setTimeout((int) timeout * 1000);

			// connect the server
			org.apache.commons.httpclient.methods.PostMethod method = new PostMethod(
					url2);
			method.setRequestBody(query_data);
			int r = client.executeMethod(method);
			BufferedInputStream in = new BufferedInputStream(method
					.getResponseBodyAsStream());
			StringBuffer temp = new StringBuffer();
			int i = in.read();
			while (i > -1) {
				temp.append((char) i);
				i = in.read();
			}
			String content = temp.toString();
			outputstr.put("HTTP_RESPONSE", method.getStatusCode()+"");
			
			if (method.getStatusCode() == 200) 
			{
				
				if (debug) 
				{
					
					//System.out.println("content = " + content);
				}

				// get the keys and values from
				// the string content and store them
				// the hash named outputstr

				// split content into pairs containing both
				// the key and the value
				
				StringTokenizer st = new StringTokenizer(content, ";");

				// for each pair store key and value into the
				// hash named outputstr
				
				while (st.hasMoreTokens()) {
					String keyvaluepair = st.nextToken();

					// split the pair into a key and a value
					
					StringTokenizer st2 = new StringTokenizer(keyvaluepair, "=");
					String key;
					String value;
					key = st2.nextToken();
					if (st2.hasMoreTokens()) {
						value = st2.nextToken();
					} 
					else 
					{
						value = "";
					}
					// store the key and the value into the
					// hash named outputstr
					outputstr.put(key, value);
					if (debug) 
					{
						//System.out.println("key = " + key + ", value = "+ value);
					}
				}
				if (outputstr.containsKey(check_field) == false) 
				{
					// if the output does not have the field it is checking for
					// then return false
					return false;
				}
				method.releaseConnection();
				return true;
			} 
			else 
			{
				method.releaseConnection();
				return false;
			}
		} 
		catch (Exception e) 
		{
			 outputstr.put("ERROR", e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

}
