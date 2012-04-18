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
import org.apache.commons.lang.math.RandomUtils;

import com.fb.platform.auth._1_0.AddUserRequest;
import com.fb.platform.auth._1_0.AddUserResponse;
import com.fb.platform.auth._1_0.GetUserRequest;
import com.fb.platform.auth._1_0.GetUserResponse;
import com.fb.platform.auth._1_0.LoginRequest;
import com.fb.platform.auth._1_0.LoginResponse;
import com.fb.platform.auth._1_0.LogoutRequest;
import com.fb.platform.auth._1_0.LogoutResponse;
import com.fb.platform.promotion._1_0.ApplyCouponRequest;
import com.fb.platform.promotion._1_0.ApplyCouponResponse;
import com.fb.platform.promotion._1_0.ClearCouponCacheRequest;
import com.fb.platform.promotion._1_0.ClearCouponCacheResponse;
import com.fb.platform.promotion._1_0.ClearPromotionCacheRequest;
import com.fb.platform.promotion._1_0.ClearPromotionCacheResponse;
import com.fb.platform.promotion._1_0.CommitCouponRequest;
import com.fb.platform.promotion._1_0.CommitCouponResponse;
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
		int orderId = RandomUtils.nextInt();
		BigDecimal discountValue = applyPromotion(sessionToken, orderId);
		commitCouupon(sessionToken, orderId, discountValue);
		releaseCoupon(sessionToken, orderId);
		clearCoupon(sessionToken);
		clearPromotion(sessionToken);
		logout(sessionToken);
	}

	public static String login() throws Exception {
		HttpClient httpClient = new HttpClient();
		//PostMethod loginMethod = new PostMethod("http://10.0.102.12:8082/userWS/auth/login");
		PostMethod loginMethod = new PostMethod("http://localhost:8080/userWS/auth/login");
		//StringRequestEntity requestEntity = new StringRequestEntity("<loginRequest><username>vinayak</username><password>password</password></loginRequest>", "application/xml", null);
		LoginRequest loginRequest = new LoginRequest();
		//loginRequest.setUsername("9920694762");
		//loginRequest.setPassword("test");
		loginRequest.setUsername("neha.garani@gmail.com");
		loginRequest.setPassword("testpass");

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.auth._1_0");
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(loginRequest, sw);

		System.out.println("\n\nLoginReq : \n" + sw.toString());
		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		loginMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(loginMethod);
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

	private static BigDecimal applyPromotion(String sessionToken, int orderId) throws Exception {
		HttpClient httpClient = new HttpClient();

		PostMethod applyPromotionMethod = new PostMethod("http://localhost:8080/promotionWS/coupon/apply");

		ApplyCouponRequest couponRequest = new ApplyCouponRequest();
		couponRequest.setCouponCode("GlobalCoupon1000Off");
		couponRequest.setSessionToken(sessionToken);

		OrderRequest orderRequest = createSampleOrderRequest(orderId);
		couponRequest.setOrderRequest(orderRequest);

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.promotion._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(couponRequest, sw);

		System.out.println("\n\napplyPromotionReq : \n" + sw.toString());

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		applyPromotionMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(applyPromotionMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("\n\nunable to execute the applyPromotion method : " + statusCode);
			System.exit(1);
		}
		String applyPromotionResponseStr = applyPromotionMethod.getResponseBodyAsString();
		System.out.println("Got the applyPromotion Response : \n\n" + applyPromotionResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		ApplyCouponResponse couponResponse = (ApplyCouponResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(applyPromotionResponseStr)));
		System.out.println(couponResponse.getCouponStatus());
		return couponResponse.getDiscountValue();
	}

	private static OrderRequest createSampleOrderRequest(int orderId) {
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setOrderId(orderId);
		orderRequest.setClientId(5);
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

	private static void commitCouupon(String sessionToken, int orderId, BigDecimal discountValue) throws Exception {
		HttpClient httpClient = new HttpClient();

		PostMethod commitCouponMethod = new PostMethod("http://localhost:8080/promotionWS/coupon/commit");

		CommitCouponRequest commitCouponRequest = new CommitCouponRequest();
		commitCouponRequest.setSessionToken(sessionToken);
		commitCouponRequest.setCouponCode("GlobalCoupon1000Off");
		commitCouponRequest.setOrderId(orderId);
		commitCouponRequest.setDiscountValue(discountValue);

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.promotion._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(commitCouponRequest, sw);

		System.out.println("\n\ncommitCouponReq : \n" + sw.toString());

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		commitCouponMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(commitCouponMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the commitCouupon method : " + statusCode);
			System.exit(1);
		}
		String commitCouponResponseStr = commitCouponMethod.getResponseBodyAsString();
		System.out.println("Got the commitCoupon Response : \n\n" + commitCouponResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		CommitCouponResponse commitCouponResponse = (CommitCouponResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(commitCouponResponseStr)));
		System.out.println(commitCouponResponse.getCommitCouponStatus());
	}

	private static void releaseCoupon(String sessionToken, int orderId) throws Exception {
		HttpClient httpClient = new HttpClient();

		PostMethod releaseCouponMethod = new PostMethod("http://localhost:8080/promotionWS/coupon/release");

		ReleaseCouponRequest releaseCouponRequest = new ReleaseCouponRequest();
		releaseCouponRequest.setSessionToken(sessionToken);
		releaseCouponRequest.setCouponCode("GlobalCoupon1000Off");
		releaseCouponRequest.setOrderId(orderId);

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.promotion._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(releaseCouponRequest, sw);

		System.out.println("\n\nreleaseCouponReq : \n" + sw.toString());

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		releaseCouponMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(releaseCouponMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the releaseCouupon method : " + statusCode);
			System.exit(1);
		}
		String releaseCouponResponseStr = releaseCouponMethod.getResponseBodyAsString();
		System.out.println("Got the releaseCoupon Response : \n" + releaseCouponResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		ReleaseCouponResponse releaseCouponResponse = (ReleaseCouponResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(releaseCouponResponseStr)));
		System.out.println(releaseCouponResponse.getReleaseCouponStatus());
	}
	
	private static void clearCoupon(String sessionToken) throws Exception {
		HttpClient httpClient = new HttpClient();
		//PostMethod logoutMethod = new PostMethod("http://10.0.102.12:8082/promotionWS/coupon/clear/coupon");
		PostMethod clearCoupon = new PostMethod("http://localhost:8080/promotionWS/coupon/clear/coupon");
		ClearCouponCacheRequest clearCouponCacheRequest = new ClearCouponCacheRequest();
		clearCouponCacheRequest.setCouponCode("GlobalCoupon1000Off");
		clearCouponCacheRequest.setSessionToken(sessionToken);
		
		JAXBContext context = JAXBContext.newInstance("com.fb.platform.promotion._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(clearCouponCacheRequest, sw);

		System.out.println("\n\nclearCoupon : \n" + sw.toString());

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		clearCoupon.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(clearCoupon);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the clearCoupon method : " + statusCode);
			System.exit(1);
		}
		String clearCouponResponseStr = clearCoupon.getResponseBodyAsString();
		System.out.println("Got the clearCouponCache Response : \n\n" + clearCouponResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		ClearCouponCacheResponse clearCouponCacheResponse = (ClearCouponCacheResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(clearCouponResponseStr)));
		System.out.println(clearCouponCacheResponse.getClearCacheEnum().toString());
		
	}
	
	private static void clearPromotion(String sessionToken) throws Exception {
		HttpClient httpClient = new HttpClient();
		//PostMethod logoutMethod = new PostMethod("http://10.0.102.12:8082/promotionWS/coupon/clear/promotion");
		PostMethod clearPromotion = new PostMethod("http://localhost:8080/promotionWS/coupon/clear/promotion");
		ClearPromotionCacheRequest clearPromotionCacheRequest = new ClearPromotionCacheRequest();
		clearPromotionCacheRequest.setPromotionId(-2000);
		clearPromotionCacheRequest.setSessionToken(sessionToken);
		
		JAXBContext context = JAXBContext.newInstance("com.fb.platform.promotion._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(clearPromotionCacheRequest, sw);

		System.out.println("\n\nclearPromotion : \n" + sw.toString());

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		clearPromotion.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(clearPromotion);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the clearPromotion method : " + statusCode);
			System.exit(1);
		}
		String clearPromotionResponseStr = clearPromotion.getResponseBodyAsString();
		System.out.println("Got the clearPromotionCache Response : \n\n" + clearPromotionResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		ClearPromotionCacheResponse clearPromotionCacheResponse = (ClearPromotionCacheResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(clearPromotionResponseStr)));
		System.out.println(clearPromotionCacheResponse.getClearCacheEnum().toString());
		
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

		System.out.println("\n\nLogoutReq : \n" + sw.toString());

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		logoutMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(logoutMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the logout method : " + statusCode);
			return;
		}
		String logoutResponseStr = logoutMethod.getResponseBodyAsString();
		System.out.println("Got the logout Response : \n\n" + logoutResponseStr);
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
