package com.fb.platform.test;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.fb.platform.promotion._1_0.CouponRequest;
import com.fb.platform.promotion._1_0.CouponResponse;
import com.fb.platform.promotion._1_0.OrderItem;
import com.fb.platform.promotion._1_0.OrderRequest;
import com.fb.platform.promotion._1_0.Product;

public class PromotionRestClient {

	
	public static void main(String[] args) throws Exception {
		//test login
		String sessionToken = login();
		
		BigDecimal discValue = applyCoupon(sessionToken);
		System.out.println(discValue);
		//test logout
//		logout(sessionToken);
	}

	private static String login() throws Exception {
		HttpClient httpClient = new HttpClient();
		PostMethod loginMethod = new PostMethod("http://localhost:8080/userWS/auth/login");
		//StringRequestEntity requestEntity = new StringRequestEntity("<loginRequest><username>vinayak</username><password>password</password></loginRequest>", "application/xml", null);
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
		System.out.println("Got the login Response : \n" + loginResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		LoginResponse loginResponse = (LoginResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(loginResponseStr)));

		return loginResponse.getSessionToken();
	}

	public static BigDecimal applyCoupon(String sessionToken) throws Exception{
		HttpClient httpClient = new HttpClient();
		PostMethod couponMethod = new PostMethod("http://localhost:8080/promotionWS/coupon/apply");
		
		CouponRequest couponRequest = new CouponRequest();
		couponRequest.setCouponCode("global_coupon_1");
		couponRequest.setSessionToken(sessionToken);
//		//Create Products
		Product p1 = new Product();
		p1.setPrice(new BigDecimal(10));
		p1.getCategory().addAll(Arrays.asList(1,2,3,4,5));
		p1.getBrand().addAll(Arrays.asList(1,2,3));
		Product p2 = new Product();
		p2.setPrice(new BigDecimal(50));
		p2.getCategory().addAll(Arrays.asList(2,3,4,5,6,7,8,9,10));
		p2.getBrand().addAll(Arrays.asList(2,3,4,5));
		Product p3 = new Product();
		p3.setPrice(new BigDecimal(100));
		p3.getCategory().addAll(Arrays.asList(6,7,8,9,10));
		p3.getBrand().addAll(Arrays.asList(4,5,6));
		//Create OrderItems
		OrderItem oItem1 = new OrderItem(); //30 Rs
		oItem1.setQuantity(3);
		oItem1.setProduct(p1);
		OrderItem oItem2 = new OrderItem(); //250 Rs
		oItem2.setQuantity(5);
		oItem2.setProduct(p2);
		OrderItem oItem3 = new OrderItem(); //500 Rs
		oItem3.setQuantity(5);
		oItem3.setProduct(p3);
		//Create OrderReq
		OrderRequest orderReq1 = new OrderRequest(); //780 Rs
		orderReq1.setOrderId(1);
		List<OrderItem> oList1 = new ArrayList<OrderItem>();
		oList1.add(oItem1);
		oList1.add(oItem2);
		oList1.add(oItem3);
		orderReq1.getOrderItem().addAll(oList1);
		couponRequest.setOrderRequest(orderReq1);

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.promotion._1_0");
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(couponRequest, sw);

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		couponMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(couponMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the applyCoupon method : " + statusCode);
			return null;
		}
		String couponResponseStr = couponMethod.getResponseBodyAsString();
		System.out.println("Got the login Response : \n" + couponResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		CouponResponse couponResponse = (CouponResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(couponResponseStr)));
		System.out.println(couponResponse);
		return couponResponse.getDiscountValue();
	}
	
}
