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

import com.fb.platform.promotion._1_0.ApplyCouponRequest;
import com.fb.platform.promotion._1_0.ApplyCouponResponse;
import com.fb.platform.promotion._1_0.OrderItem;
import com.fb.platform.promotion._1_0.OrderRequest;
import com.fb.platform.promotion._1_0.Product;
import com.fb.platform.promotion.admin._1_0.CreatePromotionRequest;
import com.fb.platform.promotion.admin._1_0.CreatePromotionResponse;
import com.fb.platform.promotion.admin._1_0.CreatePromotionTO;
import com.fb.platform.promotion.admin._1_0.FetchRuleRequest;
import com.fb.platform.promotion.admin._1_0.FetchRuleResponse;
import com.fb.platform.promotion.admin._1_0.RuleConfigItemTO;

public class PromotionRestClient {

	
	public static void main(String[] args) throws Exception {
		//login
		String sessionToken = RestClient.login();

		createClearanceDiscountPromotion(sessionToken);
		getAllPromotionRuleList(sessionToken);
		//BigDecimal discValue = applyCoupon(sessionToken);
		//System.out.println(discValue);
	}

	private static void createClearanceDiscountPromotion(String sessionToken) throws Exception {
		HttpClient httpClient = new HttpClient();

		PostMethod postMethod = new PostMethod(RestClient.url + "promotionAdminWS/promotionAdmin/promotion/create");

		CreatePromotionRequest xmlRequest = new CreatePromotionRequest();
		xmlRequest.setSessionToken(sessionToken);

		CreatePromotionTO promotion = new CreatePromotionTO();
		promotion.setRuleName("DISCOUNT_ON_CLEARANCE_PRODUCT");
		promotion.setIsActive(true);
		promotion.setMaxAmount(new BigDecimal("-1"));
		promotion.setMaxAmountPerUser(new BigDecimal("-1"));
		promotion.setMaxUses(-1);
		promotion.setMaxUsesPerUser(-1);
		promotion.setPromotionName("clerance" + System.currentTimeMillis());

		RuleConfigItemTO fixedDiscountOffConfig = new RuleConfigItemTO();
		fixedDiscountOffConfig.setRuleConfigName("FIXED_DISCOUNT_RS_OFF");
		fixedDiscountOffConfig.setRuleConfigValue("500");
		promotion.getRuleConfigItemTO().add(fixedDiscountOffConfig);

		//MIN_ORDER_VALUE
		RuleConfigItemTO minOrderValueConfig = new RuleConfigItemTO();
		minOrderValueConfig.setRuleConfigName("MIN_ORDER_VALUE");
		minOrderValueConfig.setRuleConfigValue("2000");
		promotion.getRuleConfigItemTO().add(minOrderValueConfig);

		xmlRequest.setCreatePromotionTO(promotion);

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.promotion.admin._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(xmlRequest, sw);
		
		System.out.println("\n" + RestClient.url + "promotionAdminWS/promotionAdmin/promotion/create");
		System.out.println("\n createClearanceDiscountPromotion : \n" + sw.toString());

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		postMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(postMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the createClearanceDiscountPromotion method : " + statusCode);
			System.exit(1);
		}
		String xmlStr = postMethod.getResponseBodyAsString();
		System.out.println("Got the createClearanceDiscountPromotion Response : \n\n" + xmlStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		CreatePromotionResponse xmlResponse = (CreatePromotionResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(xmlStr)));
		System.out.println(xmlResponse.getCreatePromotionEnum().toString());
	}

	private static void getAllPromotionRuleList(String sessionToken) throws Exception {
		HttpClient httpClient = new HttpClient();
		PostMethod getAllPromotionRuleList = new PostMethod(RestClient.url + "promotionAdminWS/promotionAdmin/rules");
		FetchRuleRequest fetchRuleRequest = new FetchRuleRequest();
		fetchRuleRequest.setSessionToken(sessionToken);
		
		JAXBContext context = JAXBContext.newInstance("com.fb.platform.promotion.admin._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(fetchRuleRequest, sw);

		System.out.println("\n" + RestClient.url + "promotionAdminWS/promotionAdmin/rules");
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

	public static BigDecimal applyCoupon(String sessionToken) throws Exception{
		HttpClient httpClient = new HttpClient();
		PostMethod couponMethod = new PostMethod("http://localhost:8080/promotionWS/coupon/apply");
		
		ApplyCouponRequest couponRequest = new ApplyCouponRequest();
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
		ApplyCouponResponse couponResponse = (ApplyCouponResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(couponResponseStr)));
		System.out.println(couponResponse);
		return couponResponse.getOrderDiscount().getDiscountValue();
	}
	
}
