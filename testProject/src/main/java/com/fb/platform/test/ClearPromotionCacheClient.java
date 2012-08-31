/**
 * 
 */
package com.fb.platform.test;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import com.fb.platform.auth._1_0.LoginRequest;
import com.fb.platform.auth._1_0.LoginResponse;
import com.fb.platform.promotion._1_0.ClearPromotionCacheRequest;
import com.fb.platform.promotion._1_0.ClearPromotionCacheResponse;
import com.fb.platform.promotion._1_0.RefreshAutoPromotionRequest;
import com.fb.platform.promotion._1_0.RefreshAutoPromotionResponse;

/**
 * @author vinayak
 *
 */
public class ClearPromotionCacheClient {

	private static final String QAURL = "http://10.0.102.21:8082/";

	private static final String PROD1 = "http://10.0.101.28:8080/";

	private static final String PROD2 = "http://10.0.101.35:8080/";
	
	private static final String localhost = "http://localhost:8080/";
	
	private static final String url = localhost;

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		//login and get session token
		String sessionToken = login();
		refreshPromotionCache(sessionToken, 539);
	}

	public static String login() throws Exception {
		HttpClient httpClient = new HttpClient();
		PostMethod loginMethod = new PostMethod(url + "userWS/auth/login");
		LoginRequest loginRequest = new LoginRequest();

		if (url.equals(localhost)) {
			loginRequest.setUsername("neha.garani@gmail.com");
			loginRequest.setPassword("testpass");
		} else if (url.equals(PROD1) || url.equals(PROD2)) {
			//prod
			loginRequest.setUsername("vinayak.karnataki@futuregroup.in");
			loginRequest.setPassword("faphde123");
		}
		
		JAXBContext context = JAXBContext.newInstance("com.fb.platform.auth._1_0");
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(loginRequest, sw);

		System.out.println("\n" + url + "userWS/auth/login");
		System.out.println("\n\nLoginReq : \n" + sw.toString());
		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		loginMethod.setRequestEntity(requestEntity);

		int statusCode = 2;
		
		try{
			statusCode = httpClient.executeMethod(loginMethod);
		}catch (Exception e) {
			System.out.println("login failing\n"+e);
		}
		
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("\n\n\nunable to execute the login method : \n\n" + statusCode);
			System.exit(1);
		}
		String loginResponseStr = loginMethod.getResponseBodyAsString();
		System.out.println("Got the login Response : \n\n" + loginResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		LoginResponse loginResponse = (LoginResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(loginResponseStr)));

		return loginResponse.getSessionToken();
	}

	private static void refreshAutoPromotionsCache(String sessionToken) throws Exception {
		HttpClient httpClient = new HttpClient();

		PostMethod postMethod = new PostMethod(url + "promotionWS/autoPromotion/refresh");
		RefreshAutoPromotionRequest xmlRequest = new RefreshAutoPromotionRequest();
		xmlRequest.setSessionToken(sessionToken);

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.promotion._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(xmlRequest, sw);
		
		System.out.println("\n" + url + "promotionWS/autoPromotion/refresh");
		System.out.println("\n apply refreshAutoPromotionsCache : \n" + sw.toString());

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		postMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(postMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the refreshAutoPromotionsCache method : " + statusCode);
			System.exit(1);
		}
		String xmlStr = postMethod.getResponseBodyAsString();
		System.out.println("Got the refreshAutoPromotionsCache Response : \n\n" + xmlStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		RefreshAutoPromotionResponse xmlResponse = (RefreshAutoPromotionResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(xmlStr)));
		System.out.println(xmlResponse.getRefreshAutoPromotionStatus().toString());
	}

	private static void refreshPromotionCache(String sessionToken, int promotionId) throws Exception {
		HttpClient httpClient = new HttpClient();

		PostMethod postMethod = new PostMethod(url + "promotionWS/coupon/clear/promotion");
		ClearPromotionCacheRequest xmlRequest = new ClearPromotionCacheRequest();
		xmlRequest.setSessionToken(sessionToken);
		xmlRequest.setPromotionId(promotionId);

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.promotion._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(xmlRequest, sw);
		
		System.out.println("\n" + url + "promotionWS/autoPromotion/refresh");
		System.out.println("\n apply refreshAutoPromotionsCache : \n" + sw.toString());

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		postMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(postMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the refreshAutoPromotionsCache method : " + statusCode);
			System.exit(1);
		}
		String xmlStr = postMethod.getResponseBodyAsString();
		System.out.println("Got the refreshAutoPromotionsCache Response : \n\n" + xmlStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		ClearPromotionCacheResponse xmlResponse = (ClearPromotionCacheResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(xmlStr)));
		System.out.println(xmlResponse.getClearCacheEnum().toString());
	}
}
