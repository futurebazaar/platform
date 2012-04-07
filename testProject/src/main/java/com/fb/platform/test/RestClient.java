/**
 * 
 */
package com.fb.platform.test;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import com.fb.platform.auth._1_0.AddUserRequest;
import com.fb.platform.auth._1_0.AddUserResponse;
import com.fb.platform.auth._1_0.GetUserRequest;
import com.fb.platform.auth._1_0.GetUserResponse;
import com.fb.platform.auth._1_0.LoginRequest;
import com.fb.platform.auth._1_0.LoginResponse;
import com.fb.platform.auth._1_0.LogoutRequest;
import com.fb.platform.auth._1_0.LogoutResponse;
import com.fb.platform.promotion._1_0.CommitCouponRequest;
import com.fb.platform.promotion._1_0.CommitCouponResponse;
import com.fb.platform.promotion._1_0.CouponRequest;
import com.fb.platform.promotion._1_0.CouponResponse;
import com.fb.platform.promotion._1_0.OrderItem;
import com.fb.platform.promotion._1_0.OrderRequest;
import com.fb.platform.promotion._1_0.Product;
import com.fb.platform.promotion._1_0.ReleaseCouponRequest;
import com.fb.platform.promotion._1_0.ReleaseCouponResponse;
/**
 * @author vinayak
 *
 */
public class RestClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		//test login
		String sessionToken = login();
		//String username = getUser(sessionToken);
		applyPromotion(sessionToken);
		commitCouupon(sessionToken);
		releaseCoupon(sessionToken);
		logout(sessionToken);
	}

	private static String login() throws Exception {
		HttpClient httpClient = new HttpClient();
		//PostMethod loginMethod = new PostMethod("http://10.0.102.12:8082/userWS/auth/login");
		PostMethod loginMethod = new PostMethod("http://localhost:8080/userWS/auth/login");
		//StringRequestEntity requestEntity = new StringRequestEntity("<loginRequest><username>vinayak</username><password>password</password></loginRequest>", "application/xml", null);
		LoginRequest loginRequest = new LoginRequest();
		//loginRequest.setUsername("9920694762");
		//loginRequest.setPassword("test");
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

	private static void applyPromotion(String sessionToken) throws Exception {
		HttpClient httpClient = new HttpClient();

		PostMethod applyPromotionMethod = new PostMethod("http://localhost:8080/promotionWeb/coupon/apply");

		CouponRequest couponRequest = new CouponRequest();
		couponRequest.setClientId(10);
		couponRequest.setCouponCode("GlobalCoupon1000Off");
		couponRequest.setSessionToken(sessionToken);

		OrderRequest orderRequest = createSampleOrderRequest();
		couponRequest.setOrderRequest(orderRequest);

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.promotion._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(couponRequest, sw);

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		applyPromotionMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(applyPromotionMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the applyPromotion method : " + statusCode);
			return;
		}
		String applyPromotionResponseStr = applyPromotionMethod.getResponseBodyAsString();
		System.out.println("Got the applyPromotion Response : \n" + applyPromotionResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		CouponResponse couponResponse = (CouponResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(applyPromotionResponseStr)));
		System.out.println(couponResponse.getCouponStatus());
	}

	private static OrderRequest createSampleOrderRequest() {
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setOrderId(2000);
		OrderItem orderItem = new OrderItem();
		orderItem.setQuantity(2);

		Product product = new Product();
		product.setPrice(new BigDecimal("2000"));
		product.setProductId(20000);
		product.getCategory().add(2000);
		product.getBrand().add(2000);

		orderItem.setProduct(product);

		orderRequest.getOrderItem().add(orderItem);
		return orderRequest;
	}

	private static void commitCouupon(String sessionToken) throws Exception {
		HttpClient httpClient = new HttpClient();

		PostMethod commitCouponMethod = new PostMethod("http://localhost:8080/promotionWeb/coupon/commit");

		CommitCouponRequest commitCouponRequest = new CommitCouponRequest();
		commitCouponRequest.setSessionToken(sessionToken);
		commitCouponRequest.setCouponCode("GlobalCoupon1000Off");
		commitCouponRequest.setOrderId(2000);
		commitCouponRequest.setDiscountValue(new BigDecimal("1000.00"));

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.promotion._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(commitCouponRequest, sw);

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		commitCouponMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(commitCouponMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the commitCouupon method : " + statusCode);
			return;
		}
		String commitCouponResponseStr = commitCouponMethod.getResponseBodyAsString();
		System.out.println("Got the commitCoupon Response : \n" + commitCouponResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		CommitCouponResponse commitCouponResponse = (CommitCouponResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(commitCouponResponseStr)));
		System.out.println(commitCouponResponse.getCommitCouponStatus());
	}

	private static void releaseCoupon(String sessionToken) throws Exception {
		HttpClient httpClient = new HttpClient();

		PostMethod releaseCouponMethod = new PostMethod("http://localhost:8080/promotionWeb/coupon/release");

		ReleaseCouponRequest releaseCouponRequest = new ReleaseCouponRequest();
		releaseCouponRequest.setSessionToken(sessionToken);
		releaseCouponRequest.setCouponCode("GlobalCoupon1000Off");
		releaseCouponRequest.setOrderId(2000);

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.promotion._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(releaseCouponRequest, sw);

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		releaseCouponMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(releaseCouponMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the releaseCouupon method : " + statusCode);
			return;
		}
		String releaseCouponResponseStr = releaseCouponMethod.getResponseBodyAsString();
		System.out.println("Got the releaseCoupon Response : \n" + releaseCouponResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		ReleaseCouponResponse releaseCouponResponse = (ReleaseCouponResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(releaseCouponResponseStr)));
		System.out.println(releaseCouponResponse.getReleaseCouponStatus());
	}

	private static void logout(String sessionToken) throws Exception {
		HttpClient httpClient = new HttpClient();

		//PostMethod logoutMethod = new PostMethod("http://10.0.102.12:8082/userWS/auth/logout");
		PostMethod logoutMethod = new PostMethod("http://localhost:8080/userWS/auth/logout");
		LogoutRequest logoutReq = new LogoutRequest();
		logoutReq.setSessionToken(sessionToken);
		
		JAXBContext context = JAXBContext.newInstance("com.fb.platform.auth._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(logoutReq, sw);

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		logoutMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(logoutMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the logout method : " + statusCode);
			return;
		}
		String logoutResponseStr = logoutMethod.getResponseBodyAsString();
		System.out.println("Got the logout Response : \n" + logoutResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		LogoutResponse logoutResponse = (LogoutResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(logoutResponseStr)));
		System.out.println(logoutResponse.getLogoutStatus());
	}
	
	private static String getUser(String sessionToken) throws Exception{
		
		HttpClient httpClient = new HttpClient();

		//PostMethod getUserMethod = new PostMethod("http://10.0.102.12:8082/userWS/user/get");
		PostMethod getUserMethod = new PostMethod("http://localhost:8080/userWS/user/get");
		GetUserRequest getUserRequest = new GetUserRequest();
		//getUserRequest.setKey("9920694762");
		getUserRequest.setKey("jasvipul@gmail.com");
		getUserRequest.setSessionToken(sessionToken);

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.auth._1_0");
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(getUserRequest, sw);

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		getUserMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(getUserMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the get user method : " + statusCode);
			return null;
		}
		String getUserResponseStr = getUserMethod.getResponseBodyAsString();
		System.out.println("Got the get user Response : \n" + getUserResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		GetUserResponse getUserResponse = (GetUserResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(getUserResponseStr)));
		return getUserResponse.getUserName();		
	}
	
	private static String addUser() throws Exception{
		HttpClient httpClient = new HttpClient();

		PostMethod addUserMethod = new PostMethod("http://10.0.102.12:8082/userWS/user/add");
		AddUserRequest addUserRequest = new AddUserRequest();
		addUserRequest.setUserName("newtestuserviaclient@test.com");
		addUserRequest.setPassword("testpass");

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.auth._1_0");
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(addUserRequest, sw);

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		addUserMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(addUserMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the add user method : " + statusCode);
			return null;
		}
		String addUserResponseStr = addUserMethod.getResponseBodyAsString();
		System.out.println("Got the add user Response : \n" + addUserResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		AddUserResponse addUserResponse = (AddUserResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(addUserResponseStr)));
		return addUserResponse.getSessionToken();	
	}
}
