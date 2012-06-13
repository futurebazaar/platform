package com.fb.platform.test;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import com.fb.platform.auth._1_0.LoginRequest;
import com.fb.platform.auth._1_0.LoginResponse;
import com.fb.platform.payback._1_0.ActionCode;
import com.fb.platform.payback._1_0.ClearCacheRequest;
import com.fb.platform.payback._1_0.ClearCacheResponse;
import com.fb.platform.payback._1_0.DisplayPointsRequest;
import com.fb.platform.payback._1_0.DisplayPointsResponse;
import com.fb.platform.payback._1_0.OrderItemRequest;
import com.fb.platform.payback._1_0.OrderRequest;
import com.fb.platform.payback._1_0.PointsRequest;
import com.fb.platform.payback._1_0.PointsResponse;
import com.fb.platform.payback.rule.EarnPointsRuleEnum;


public class PointsRestClient {

	private static String API_URL = "http://localhost:8080/";
	
	private static String login() throws Exception {
		HttpClient httpClient = new HttpClient();
		PostMethod loginMethod = new PostMethod("http://localhost:8080/userWS/auth/login");
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("jasvipul@gmail.com");
		loginRequest.setPassword("testpass");

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.auth._1_0");
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(loginRequest, sw);

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		loginMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(loginMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the login method : " + statusCode);
			return null;
		}
		String loginResponseStr = loginMethod.getResponseBodyAsString();
		System.out.println("\n Got the login Response : \n" + loginResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		LoginResponse loginResponse = (LoginResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(loginResponseStr)));

		return loginResponse.getSessionToken();
	}
	
	public static void main(String[] args) throws Exception {
			clearCache();
			storeEarnPoints();
			displayPoints();
	}

	private static void displayPoints() throws Exception {
		HttpClient httpClient = new HttpClient();

		PostMethod pointsMethod = new PostMethod(API_URL + "paybackWS/points/display");

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.payback._1_0");
		
		DisplayPointsRequest request = setDisplayPointsRequest();
		
		
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(request, sw);

		System.out.println("\n Display Points Req : \n" + sw.toString());

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		pointsMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(pointsMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the points method : " + statusCode);
			System.exit(1);
		}
		String responseStr = pointsMethod.getResponseBodyAsString();
		System.out.println("Got the Display Points Response : \n" + responseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		DisplayPointsResponse response = (DisplayPointsResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(responseStr)));
		System.out.println(response.getTotalPoints());
		
	}

	private static DisplayPointsRequest setDisplayPointsRequest() throws Exception {
		DisplayPointsRequest request = new DisplayPointsRequest();
		request.setActionCode(ActionCode.BURN_REVERSAL);
		request.setTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(2012, 05, 27, 0, 0, 0, 0, 0));
		request.setOrderAmount(new BigDecimal(500));
		OrderItemRequest itemRequest = new OrderItemRequest();
		itemRequest.setAmount(new BigDecimal(639));
		itemRequest.setArticleId("1234");
		itemRequest.setCategoryId(1193);
		itemRequest.setDepartmentCode(1234);
		itemRequest.setDepartmentName("Pooh");
		itemRequest.setItemId(3);
		itemRequest.setQuantity(1);
		itemRequest.setSellerRateChartId(49447);
		
		request.getOrderItemRequest().add(itemRequest);
		return request;
	}

	private static void clearCache() throws Exception {
		HttpClient httpClient = new HttpClient();

		PostMethod pointsMethod = new PostMethod(API_URL + "paybackWS/points/clear/rule");

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.payback._1_0");
		
		ClearCacheRequest request = setClearCacheRequest();
		
		
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(request, sw);

		System.out.println("\n Clear Cache Req : \n" + sw.toString());

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		pointsMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(pointsMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the points method : " + statusCode);
			System.exit(1);
		}
		String responseStr = pointsMethod.getResponseBodyAsString();
		System.out.println("Got the Clear Cache Response : \n" + responseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		ClearCacheResponse response = (ClearCacheResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(responseStr)));
		System.out.println(response.getStatusCode());
	}

	private static ClearCacheRequest setClearCacheRequest() throws Exception {
		ClearCacheRequest cacheRequest = new ClearCacheRequest();
		cacheRequest.setRuleName(EarnPointsRuleEnum.BUY_WORTH_X_EARN_Y_BONUS_POINTS.name());
		cacheRequest.setSessionToken(login());
		return cacheRequest;
	}

	private static PointsRequest setPointsRequest() throws Exception {
		PointsRequest request = new PointsRequest();
		request.setActionCode(ActionCode.EARN_REVERSAL);
		request.setClientName("Future Bazaar");
		request.setSessionToken(login());
		
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setAmount(new BigDecimal(500));
		orderRequest.setLoyaltyCard("1234567812345678");
		orderRequest.setOrderId(2);
		orderRequest.setReason("REST CLIENT");
		orderRequest.setReferenceId("5052");
		orderRequest.setTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(2012, 05, 27, 0, 0, 0, 0, 0));
		
		OrderItemRequest itemRequest = new OrderItemRequest();
		itemRequest.setAmount(new BigDecimal(639));
		itemRequest.setArticleId("1234");
		itemRequest.setCategoryId(1193);
		itemRequest.setDepartmentCode(1234);
		itemRequest.setDepartmentName("Pooh");
		itemRequest.setItemId(3);
		itemRequest.setQuantity(1);
		itemRequest.setSellerRateChartId(49447);
		
		orderRequest.getOrderItemRequest().add(itemRequest);
		request.setOrderRequest(orderRequest);
		return request;
	}
	
	public static void storeEarnPoints() throws Exception{
		HttpClient httpClient = new HttpClient();

		PostMethod pointsMethod = new PostMethod(API_URL + "paybackWS/points/store");

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.payback._1_0");
		
		PointsRequest request = setPointsRequest();
		
		
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(request, sw);

		System.out.println("\n storePointsReq : \n" + sw.toString());

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		pointsMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(pointsMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the points method : " + statusCode);
			System.exit(1);
		}
		String responseStr = pointsMethod.getResponseBodyAsString();
		System.out.println("Got the storePoints Response : \n" + responseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		PointsResponse response = (PointsResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(responseStr)));
		System.out.println(response.getStatusCode());

	}
	
}