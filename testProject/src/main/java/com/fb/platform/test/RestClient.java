/**
 * 
 */
package com.fb.platform.test;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
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
import com.fb.platform.promotion.admin._1_0.AlphaNumericType;
import com.fb.platform.promotion.admin._1_0.AlphabetCase;
import com.fb.platform.promotion.admin._1_0.AssignCouponToUserRequest;
import com.fb.platform.promotion.admin._1_0.AssignCouponToUserResponse;
import com.fb.platform.promotion.admin._1_0.CodeDetails;
import com.fb.platform.promotion.admin._1_0.CouponTO;
import com.fb.platform.promotion.admin._1_0.CouponType;
import com.fb.platform.promotion.admin._1_0.CreateCouponRequest;
import com.fb.platform.promotion.admin._1_0.CreateCouponResponse;
import com.fb.platform.promotion.admin._1_0.CreatePromotionRequest;
import com.fb.platform.promotion.admin._1_0.CreatePromotionResponse;
import com.fb.platform.promotion.admin._1_0.CreatePromotionTO;
import com.fb.platform.promotion.admin._1_0.FetchRuleRequest;
import com.fb.platform.promotion.admin._1_0.FetchRuleResponse;
import com.fb.platform.promotion.admin._1_0.PromotionTO;
import com.fb.platform.promotion.admin._1_0.RuleConfigItemTO;
import com.fb.platform.promotion.admin._1_0.SearchCouponOrderBy;
import com.fb.platform.promotion.admin._1_0.SearchCouponRequest;
import com.fb.platform.promotion.admin._1_0.SearchCouponResponse;
import com.fb.platform.promotion.admin._1_0.SearchPromotionOrderBy;
import com.fb.platform.promotion.admin._1_0.SearchPromotionRequest;
import com.fb.platform.promotion.admin._1_0.SearchPromotionResponse;
import com.fb.platform.promotion.admin._1_0.SortOrder;
import com.fb.platform.promotion.admin._1_0.UpdatePromotionRequest;
import com.fb.platform.promotion.admin._1_0.UpdatePromotionResponse;
import com.fb.platform.promotion.admin._1_0.ViewCouponRequest;
import com.fb.platform.promotion.admin._1_0.ViewCouponResponse;
import com.fb.platform.promotion.admin._1_0.ViewPromotionRequest;
import com.fb.platform.promotion.admin._1_0.ViewPromotionResponse;
/**
 * @author vinayak
 *
 */
public class RestClient {
	
	private static String QAURL = "http://10.0.102.21:8082/";
	
	private static String localhost = "http://localhost:8080/";
	
	private static String url = localhost;

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		//test login
		String sessionToken = login();
		//String username = getUser(sessionToken);
		int orderId = RandomUtils.nextInt();
		BigDecimal discountValue = applyCoupon(sessionToken, orderId, "GlobalCoupon1000Off");
		commitCouupon(sessionToken, orderId, discountValue);
		releaseCoupon(sessionToken, orderId);
		clearCoupon(sessionToken);
		clearPromotion(sessionToken);
		getAllPromotionRuleList(sessionToken);
		createPromotion(sessionToken);
		searchPromotion(sessionToken);
		viewPromotion(sessionToken);
		updatePromotion(sessionToken);
		assignCouponToUser(sessionToken);
		//applyCoupon(sessionToken, orderId, "preIssuedNoCouponUserEntry");
		searchCoupon(sessionToken);
		viewCoupon(sessionToken);
		createCoupon(sessionToken);
		logout(sessionToken);
	}

	public static String login() throws Exception {
		HttpClient httpClient = new HttpClient();
		//PostMethod loginMethod = new PostMethod("http://10.0.102.12:8082/userWS/auth/login");
		PostMethod loginMethod = new PostMethod(url + "userWS/auth/login");
		//StringRequestEntity requestEntity = new StringRequestEntity("<loginRequest><username>vinayak</username><password>password</password></loginRequest>", "application/xml", null);
		LoginRequest loginRequest = new LoginRequest();
		//loginRequest.setUsername("9920694762");
		//loginRequest.setPassword("test");
		loginRequest.setUsername("neha.garani@gmail.com");
		loginRequest.setPassword("testpass");
//		loginRequest.setUsername("1010101010");
//		loginRequest.setPassword("shagunankush");

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.auth._1_0");
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(loginRequest, sw);

		System.out.println("\n http://localhost:8080/userWS/auth/login");
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

	private static BigDecimal applyCoupon(String sessionToken, int orderId, String couponCode) throws Exception {
		HttpClient httpClient = new HttpClient();

		PostMethod applyPromotionMethod = new PostMethod(url + "promotionWS/coupon/apply");

		ApplyCouponRequest couponRequest = new ApplyCouponRequest();
		couponRequest.setCouponCode(couponCode);
		couponRequest.setSessionToken(sessionToken);

		OrderRequest orderRequest = createSampleOrderRequest(orderId);
		couponRequest.setOrderRequest(orderRequest);

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.promotion._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(couponRequest, sw);

		System.out.println("\n http://localhost:8080/promotionWS/coupon/apply");
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

		PostMethod commitCouponMethod = new PostMethod(url + "promotionWS/coupon/commit");

		CommitCouponRequest commitCouponRequest = new CommitCouponRequest();
		commitCouponRequest.setSessionToken(sessionToken);
		commitCouponRequest.setCouponCode("GlobalCoupon1000Off");
		commitCouponRequest.setOrderId(orderId);
		commitCouponRequest.setDiscountValue(discountValue);

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.promotion._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(commitCouponRequest, sw);

		System.out.println("\n http://localhost:8080/promotionWS/coupon/commit");
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

		PostMethod releaseCouponMethod = new PostMethod(url + "promotionWS/coupon/release");

		ReleaseCouponRequest releaseCouponRequest = new ReleaseCouponRequest();
		releaseCouponRequest.setSessionToken(sessionToken);
		releaseCouponRequest.setCouponCode("GlobalCoupon1000Off");
		releaseCouponRequest.setOrderId(orderId);

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.promotion._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(releaseCouponRequest, sw);

		System.out.println("\n http://localhost:8080/promotionWS/coupon/release");
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
		PostMethod clearCoupon = new PostMethod(url + "promotionWS/coupon/clear/coupon");
		ClearCouponCacheRequest clearCouponCacheRequest = new ClearCouponCacheRequest();
		clearCouponCacheRequest.setCouponCode("GlobalCoupon1000Off");
		clearCouponCacheRequest.setSessionToken(sessionToken);
		
		JAXBContext context = JAXBContext.newInstance("com.fb.platform.promotion._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(clearCouponCacheRequest, sw);
		
		System.out.println("\n http://localhost:8080/promotionWS/coupon/clear/coupon");
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
		PostMethod clearPromotion = new PostMethod(url + "promotionWS/coupon/clear/promotion");
		ClearPromotionCacheRequest clearPromotionCacheRequest = new ClearPromotionCacheRequest();
		clearPromotionCacheRequest.setPromotionId(-2000);
		clearPromotionCacheRequest.setSessionToken(sessionToken);
		
		JAXBContext context = JAXBContext.newInstance("com.fb.platform.promotion._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(clearPromotionCacheRequest, sw);

		System.out.println("\n http://localhost:8080/promotionWS/coupon/clear/promotion");
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
	
	private static void getAllPromotionRuleList(String sessionToken) throws Exception {
		HttpClient httpClient = new HttpClient();
		PostMethod getAllPromotionRuleList = new PostMethod(url + "promotionAdminWS/promotionAdmin/rules");
		FetchRuleRequest fetchRuleRequest = new FetchRuleRequest();
		fetchRuleRequest.setSessionToken(sessionToken);
		
		JAXBContext context = JAXBContext.newInstance("com.fb.platform.promotion.admin._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(fetchRuleRequest, sw);

		System.out.println("\n http://localhost:8080/promotionAdminWS/promotionAdmin/rules");
		System.out.println("\n\ngetAllPromotionRuleList : \n" + sw.toString());

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		getAllPromotionRuleList.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(getAllPromotionRuleList);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the getAllPromotionRuleList method : " + statusCode);
			System.exit(1);
		}
		String getAllPromotionRuleListResponseStr = getAllPromotionRuleList.getResponseBodyAsString();
		System.out.println("Got the getAllPromotionRuleList Response : \n\n" + getAllPromotionRuleListResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		FetchRuleResponse fetchRuleResponse = (FetchRuleResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(getAllPromotionRuleListResponseStr)));
		System.out.println(fetchRuleResponse.getFetchRulesEnum().toString());
	}
	
	private static void createPromotion(String sessionToken) throws Exception {
		HttpClient httpClient = new HttpClient();

		//PostMethod logoutMethod = new PostMethod("http://10.0.102.12:8082/userWS/auth/logout");
		PostMethod createPromotion = new PostMethod(url + "promotionAdminWS/promotionAdmin/promotion/create");
		CreatePromotionRequest createPromotionRequest = new CreatePromotionRequest();
		CreatePromotionTO promotionTO = new CreatePromotionTO();
		
		promotionTO.setPromotionName("New Promotion");
		
		GregorianCalendar gregCal = new GregorianCalendar();
		gregCal.set(2012, 01, 29, 00, 00, 00);
		promotionTO.setValidFrom(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal));
		gregCal.set(2013, 01, 29, 00, 00, 00);
		promotionTO.setValidTill(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal));
		promotionTO.setDescription("Test new promotion 2");
		promotionTO.setIsActive(true);
		promotionTO.setMaxUses(20);
		promotionTO.setMaxUsesPerUser(1);
		promotionTO.setMaxAmount(new BigDecimal(20000.00));
		promotionTO.setMaxAmountPerUser(new BigDecimal(1000.00));
		promotionTO.setRuleName("BUY_WORTH_X_GET_Y_RS_OFF");
		
		RuleConfigItemTO configItem = new RuleConfigItemTO();
		configItem.setRuleConfigName("FIXED_DISCOUNT_RS_OFF");
		configItem.setRuleConfigValue("100");
		promotionTO.getRuleConfigItemTO().add(configItem);
		
		configItem = new RuleConfigItemTO();
		configItem.setRuleConfigName("CATEGORY_INCLUDE_LIST");
		configItem.setRuleConfigValue("1,2");
		promotionTO.getRuleConfigItemTO().add(configItem);
		
		createPromotionRequest.setCreatePromotionTO(promotionTO);
		createPromotionRequest.setSessionToken(sessionToken);
		
		JAXBContext context = JAXBContext.newInstance("com.fb.platform.promotion.admin._1_0");
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(createPromotionRequest, sw);

		System.out.println("\n http://localhost:8080/promotionAdminWS/promotionAdmin/promotion/create");
		System.out.println("\n\ncreatePromotion : \n" + sw.toString());

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		createPromotion.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(createPromotion);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the createPromotion method : " + statusCode);
			System.exit(1);
		}
		String createPromotionResponseStr = createPromotion.getResponseBodyAsString();
		System.out.println("Got the createPromotion Response : \n\n" + createPromotionResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		CreatePromotionResponse createPromotionResponse = (CreatePromotionResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(createPromotionResponseStr)));
		System.out.println(createPromotionResponse.getCreatePromotionEnum().toString());
		if(!StringUtils.isEmpty(createPromotionResponse.getErrorCause())) {
			System.out.println(createPromotionResponse.getErrorCause());
		}
	}
	
	private static void searchPromotion(String sessionToken) throws Exception {
		HttpClient httpClient = new HttpClient();

		//PostMethod logoutMethod = new PostMethod("http://10.0.102.12:8082/userWS/auth/logout");
		PostMethod searchPromotionMethod = new PostMethod(url + "promotionAdminWS/promotionAdmin/promotion/search");
		
		SearchPromotionRequest nameSearchPromotionRequest = new SearchPromotionRequest();
		nameSearchPromotionRequest.setSessionToken(sessionToken);
		nameSearchPromotionRequest.setBatchSize(3);
		nameSearchPromotionRequest.setStartRecord(0);
		nameSearchPromotionRequest.setPromotionName("promotion");
		nameSearchPromotionRequest.setSearchPromotionOrderBy(SearchPromotionOrderBy.VALID_FROM);
		nameSearchPromotionRequest.setIsActive(true);
		nameSearchPromotionRequest.setSortOrder(SortOrder.ASCENDING);
		
		GregorianCalendar gregCal = new GregorianCalendar();
		gregCal.clear();
		gregCal.set(2012, 00, 02);
		nameSearchPromotionRequest.setValidFrom(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal));
		
		gregCal.clear();
		gregCal.set(2012, 05, 30);
		nameSearchPromotionRequest.setValidTill(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal));
		
		JAXBContext context = JAXBContext.newInstance("com.fb.platform.promotion.admin._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(nameSearchPromotionRequest, sw);

		System.out.println("\n http://localhost:8080/promotionAdminWS/promotionAdmin/promotion/search");
		System.out.println("\n\nsearchPromotionRequest : \n" + sw.toString());

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		searchPromotionMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(searchPromotionMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the search promotion : " + statusCode);
			return;
		}
		String searchPromotionResponseStr = searchPromotionMethod.getResponseBodyAsString();
		System.out.println("Got the search promotion Response : \n\n" + searchPromotionResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		SearchPromotionResponse searchPromotionResponse = (SearchPromotionResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(searchPromotionResponseStr)));
		System.out.println(searchPromotionResponse.getSearchPromotionEnum().toString());
		if(!StringUtils.isEmpty(searchPromotionResponse.getErrorCause())) {
			System.out.println(searchPromotionResponse.getErrorCause());
		}
		
	}
	
	private static void viewPromotion(String sessionToken) throws Exception {
		HttpClient httpClient = new HttpClient();

		//PostMethod logoutMethod = new PostMethod("http://10.0.102.12:8082/userWS/auth/logout");
		PostMethod viewPromotionMethod = new PostMethod(url + "promotionAdminWS/promotionAdmin/promotion/view");
		ViewPromotionRequest viewPromotionRequest = new ViewPromotionRequest();
		
		viewPromotionRequest.setSessionToken(sessionToken);
		viewPromotionRequest.setPromotionId(-4100);
		
		JAXBContext context = JAXBContext.newInstance("com.fb.platform.promotion.admin._1_0");
		
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(viewPromotionRequest, sw);
		
		System.out.println("\n http://localhost:8080/promotionAdminWS/promotionAdmin/promotion/view");
		System.out.println("\n\nviewPromotionRequest : \n" + sw.toString());

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		viewPromotionMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(viewPromotionMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the view promotion : " + statusCode);
			return;
		}
		String viewPromotionResponseStr = viewPromotionMethod.getResponseBodyAsString();
		System.out.println("Got the view promotion Response : \n\n" + viewPromotionResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		ViewPromotionResponse viewPromotionResponse = (ViewPromotionResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(viewPromotionResponseStr)));
		System.out.println(viewPromotionResponse.getViewPromotionEnum().toString());
		if(!StringUtils.isEmpty(viewPromotionResponse.getErrorCause())) {
			System.out.println(viewPromotionResponse.getErrorCause());
		}
	}
	
	private static void updatePromotion(String sessionToken) throws Exception {
		HttpClient httpClient = new HttpClient();

		//PostMethod logoutMethod = new PostMethod("http://10.0.102.12:8082/userWS/auth/logout");
		PostMethod updatePromotionMethod = new PostMethod(url + "promotionAdminWS/promotionAdmin/promotion/update");
		UpdatePromotionRequest updatePromotionRequest = new UpdatePromotionRequest();
		PromotionTO updatePromotion = new PromotionTO();
		
		updatePromotion.setPromotionName("End to End Test Promoti");
		
		GregorianCalendar gregCal = new GregorianCalendar();
		gregCal.set(2012, 01, 22);
		updatePromotion.setValidFrom(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal));
		gregCal.set(2013, 01, 22);
		updatePromotion.setValidTill(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal));
		updatePromotion.setDescription("Test new promotion NEHA");
		updatePromotion.setIsActive(false);
		updatePromotion.setMaxUses(22);
		updatePromotion.setMaxUsesPerUser(2);
		updatePromotion.setMaxAmount(new BigDecimal(2222.00));
		updatePromotion.setMaxAmountPerUser(new BigDecimal(2222.00));
		updatePromotion.setRuleName("FIRST_PURCHASE_BUY_WORTH_X_GET_Y_RS_OFF");
		updatePromotion.setPromotionId(-4100);
		
		RuleConfigItemTO configItem = new RuleConfigItemTO();
		configItem.setRuleConfigName("FIXED_DISCOUNT_RS_OFF");
		configItem.setRuleConfigValue("222");
		updatePromotion.getRuleConfigItemTO().add(configItem);
		
		configItem = new RuleConfigItemTO();
		configItem.setRuleConfigName("CATEGORY_INCLUDE_LIST");
		configItem.setRuleConfigValue("1,	3");
		updatePromotion.getRuleConfigItemTO().add(configItem);
		
		configItem = new RuleConfigItemTO();
		configItem.setRuleConfigName("CATEGORY_EXCLUDE_LIST");
		configItem.setRuleConfigValue(null);
		updatePromotion.getRuleConfigItemTO().add(configItem);
		
		updatePromotionRequest.setPromotionTO(updatePromotion);
		updatePromotionRequest.setSessionToken(sessionToken);
		
		JAXBContext context = JAXBContext.newInstance("com.fb.platform.promotion.admin._1_0");
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(updatePromotionRequest, sw);

		System.out.println("\n http://localhost:8080/promotionAdminWS/promotionAdmin/promotion/update");
		System.out.println("\n\n updatePromotion : \n" + sw.toString());

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		updatePromotionMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(updatePromotionMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the updatePromotion method : " + statusCode);
			System.exit(1);
		}
		String updatePromotionResponseStr = updatePromotionMethod.getResponseBodyAsString();
		System.out.println("Got the updatePromotion Response : \n\n" + updatePromotionResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		UpdatePromotionResponse updatePromotionResponse = (UpdatePromotionResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(updatePromotionResponseStr)));
		System.out.println(updatePromotionResponse.getUpdatePromotionEnum().toString());
		if(!StringUtils.isEmpty(updatePromotionResponse.getErrorCause())) {
			System.out.println(updatePromotionResponse.getErrorCause());
		}
	}

	private static void logout(String sessionToken) throws Exception {
		HttpClient httpClient = new HttpClient();

		//PostMethod logoutMethod = new PostMethod("http://10.0.102.12:8082/userWS/auth/logout");
		PostMethod logoutMethod = new PostMethod(url + "userWS/auth/logout");
		LogoutRequest logoutReq = new LogoutRequest();
		logoutReq.setSessionToken(sessionToken);
		
		JAXBContext context = JAXBContext.newInstance("com.fb.platform.auth._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(logoutReq, sw);

		System.out.println("\n http://localhost:8080/userWS/auth/logout");
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
		PostMethod getUserMethod = new PostMethod(url + "userWS/user/get");
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

	private static void assignCouponToUser(String sessionToken) throws Exception {
		HttpClient httpClient = new HttpClient();

		PostMethod postMethod = new PostMethod(url + "promotionAdminWS/promotionAdmin/user/assign");

		AssignCouponToUserRequest request = new AssignCouponToUserRequest();
		request.setSessionToken(sessionToken);
		
		request.setCouponCode("preIssuedNoCouponUserEntry");
		request.setUserName("neha.garani@gmail.com");

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.promotion.admin._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(request, sw);

		System.out.println("\n http://localhost:8080/promotionAdminWS/promotionAdmin/user/assign");
		System.out.println("\n\n assignCouponToUserRequest : \n" + sw.toString());

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		postMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(postMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the assignCouponToUser method : " + statusCode);
			System.exit(1);
		}
		String responseStr = postMethod.getResponseBodyAsString();
		System.out.println("Got the assignCouponToUser Response : \n\n" + responseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		AssignCouponToUserResponse response = (AssignCouponToUserResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(responseStr)));
		System.out.println(response.getAssignCouponToUserStatusEnum());

	}
	
	private static void searchCoupon(String sessionToken) throws Exception {
		HttpClient httpClient = new HttpClient();

		PostMethod postMethod = new PostMethod(url + "promotionAdminWS/promotionAdmin/coupon/search");

		SearchCouponRequest request = new SearchCouponRequest();
		request.setCouponCode("pre_issued_1");
		request.setSessionToken(sessionToken);
		request.setSearchCouponOrderBy(SearchCouponOrderBy.COUPON_CODE);
		request.setSortOrder(SortOrder.ASCENDING);

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.promotion.admin._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(request, sw);

		System.out.println("\n http://localhost:8080/promotionAdminWS/promotionAdmin/coupon/search");
		System.out.println("\n\n searchCouponRequest : \n" + sw.toString());

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		postMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(postMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the searchCoupon method : " + statusCode);
			System.exit(1);
		}
		String responseStr = postMethod.getResponseBodyAsString();
		System.out.println("Got the searchCoupon Response : \n\n" + responseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		SearchCouponResponse response = (SearchCouponResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(responseStr)));
		System.out.println(response.getSearchCouponStatus());

	}
	
	private static void viewCoupon(String sessionToken) throws Exception {
		HttpClient httpClient = new HttpClient();

		PostMethod postMethod = new PostMethod(url + "promotionAdminWS/promotionAdmin/coupon/view");

		ViewCouponRequest request = new ViewCouponRequest();
		request.setSessionToken(sessionToken);
		request.setCouponCode("DCBF78KT54");
		

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.promotion.admin._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(request, sw);

		System.out.println("\n http://localhost:8080/promotionAdminWS/promotionAdmin/coupon/view");
		System.out.println("\n\n viewCouponRequest : \n" + sw.toString());

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		postMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(postMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the viewCoupon method : " + statusCode);
			System.exit(1);
		}
		String responseStr = postMethod.getResponseBodyAsString();
		System.out.println("Got the viewCoupon Response : \n\n" + responseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		ViewCouponResponse response = (ViewCouponResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(responseStr)));
		System.out.println(response.getViewCouponStatus());
	}
	
	private static void createCoupon(String sessionToken) throws Exception {
		HttpClient httpClient = new HttpClient();

		PostMethod postMethod = new PostMethod(url + "promotionAdminWS/promotionAdmin/coupon/create");

		CreateCouponRequest request = new CreateCouponRequest();
		request.setSessionToken(sessionToken);
		request.setNumberOfCoupon(1100);
		
		CouponTO couponTO = new CouponTO();
		couponTO.setPromotionId(-3005);
		couponTO.setCouponType(CouponType.PRE_ISSUE);
		couponTO.setMaxAmount(new BigDecimal(10000.00));
		couponTO.setMaxAmountPerUser(new BigDecimal(1000.00));
		couponTO.setMaxUses(20);
		couponTO.setMaxUsesPerUser(4);
		
		CodeDetails codeDetails = new CodeDetails();
		codeDetails.setAlphabetCase(AlphabetCase.LOWER);
		codeDetails.setAlphaNumericType(AlphaNumericType.ALPHA_NUMERIC);
		codeDetails.setCodeLength(10);
		codeDetails.setEndsWith("1");
		codeDetails.setStartsWith("1");

		request.setCouponTO(couponTO);
		
		request.setCodeDetails(codeDetails);
		
		JAXBContext context = JAXBContext.newInstance("com.fb.platform.promotion.admin._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(request, sw);

		System.out.println("\n http://localhost:8080/promotionAdminWS/promotionAdmin/coupon/create");
		System.out.println("\n\n createCouponRequest : \n" + sw.toString());

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		postMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(postMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the createCoupon method : " + statusCode);
			System.exit(1);
		}
		String responseStr = postMethod.getResponseBodyAsString();
		System.out.println("Got the createCoupon Response : \n\n" + responseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		CreateCouponResponse response = (CreateCouponResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(responseStr)));
		System.out.println(response.getCreateCouponStatus());
	}
}
