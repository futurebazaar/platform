package com.fb.platform.payback.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NoPermissionException;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.payback.to.OrderItemRequest;
import com.fb.platform.payback.to.OrderRequest;
import com.fb.platform.payback.to.PointsRequest;
import com.fb.platform.payback.to.PointsResponse;
import com.fb.platform.payback.to.PointsResponseCodeEnum;
import com.fb.platform.payback.to.PointsTxnClassificationCodeEnum;
import com.fb.platform.user.manager.interfaces.UserManager;
import com.fb.platform.user.manager.model.auth.LoginRequest;
import com.fb.platform.user.manager.model.auth.LoginResponse;

public class PointsManagerTest  extends BaseTestCase{
	
	@Autowired
	private PointsManager pointsManager;
	
	@Autowired
	private UserManager userManager;
	
	LoginResponse responseUser1 = null;
	
	@Before
	public void insertEarnPointsTest(){
		
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("jasvipul@gmail.com");
		loginRequest.setPassword("testpass");

		responseUser1 = userManager.login(loginRequest);

		
		PointsRequest pr = new PointsRequest();
		pr.setTxnActionCode("PREALLOC_EARN");
		pr.setClientName("Future Bazaar");
		pr.setSessionToken(responseUser1.getSessionToken());
		
		OrderRequest request  = new OrderRequest();
		request.setLoyaltyCard("1234567890123457");
		request.setAmount(new BigDecimal(2001));
		request.setOrderId(1);
		request.setTxnTimestamp(new DateTime(2012, 05, 24, 10, 0, 0));
		request.setReferenceId("5051234568");
		
		List<OrderItemRequest> orderItemRequest = new ArrayList<OrderItemRequest>();
		OrderItemRequest orderItem1 = new OrderItemRequest();
		orderItem1.setAmount(new BigDecimal(2000));
		orderItem1.setArticleId("1234");
		orderItem1.setCategoryId(1234);
		orderItem1.setDepartmentCode(1234);
		orderItem1.setDepartmentName("Pooh");
		orderItem1.setId(1);
		orderItem1.setQuantity(1);
		orderItem1.setSellerRateChartId(1);
		orderItemRequest.add(orderItem1);
		
		OrderItemRequest orderItem2 = new OrderItemRequest();
		orderItem2.setAmount(new BigDecimal(2000));
		orderItem2.setArticleId("1234");
		orderItem2.setCategoryId(1);
		orderItem2.setDepartmentCode(1234);
		orderItem2.setDepartmentName("Pooh");
		orderItem2.setId(1);
		orderItem2.setQuantity(1);
		orderItem2.setSellerRateChartId(2);
		orderItemRequest.add(orderItem2);
		
		request.setOrderItemRequest(orderItemRequest);
		
		pr.setOrderRequest(request);
		
		pointsManager.getPointsReponse(pr);
	}
	
	@Test
	public void storeEarnPointsTest(){
		PointsRequest pr = new PointsRequest();
		pr.setTxnActionCode("PREALLOC_EARN");
		pr.setClientName("Future Bazaar");
		pr.setSessionToken(responseUser1.getSessionToken());
		
		OrderRequest request  = new OrderRequest();
		request.setLoyaltyCard("1234567890123456");
		request.setAmount(new BigDecimal(639));
		request.setOrderId(2);
		request.setTxnTimestamp(new DateTime(2012, 05, 27, 10, 0, 0));
		request.setReferenceId("5052");
		
		List<OrderItemRequest> orderItemRequest = new ArrayList<OrderItemRequest>();
		OrderItemRequest orderItem1 = new OrderItemRequest();
		orderItem1.setAmount(new BigDecimal(639));
		orderItem1.setArticleId("1234");
		orderItem1.setCategoryId(1193);
		orderItem1.setDepartmentCode(1234);
		orderItem1.setDepartmentName("Pooh");
		orderItem1.setId(3);
		orderItem1.setQuantity(1);
		orderItem1.setSellerRateChartId(49447);
		orderItemRequest.add(orderItem1);
		
		/*OrderItemRequest orderItem2 = new OrderItemRequest();
		orderItem2.setAmount(new BigDecimal(2000));
		orderItem2.setArticleId("1234");
		orderItem2.setCategoryId(1);
		orderItem2.setDepartmentCode(1234);	
		orderItem2.setDepartmentName("Pooh");
		orderItem2.setId(1);
		orderItem2.setQuantity(1);
		orderItem2.setSellerRateChartId(2);
		orderItemRequest.add(orderItem2);*/
		
		request.setOrderItemRequest(orderItemRequest);
		
		pr.setOrderRequest(request);
		
		PointsResponse pointsResponse = pointsManager.getPointsReponse(pr);
		assertEquals(PointsResponseCodeEnum.SUCCESS, pointsResponse.getPointsResponseCodeEnum());
		assertEquals(PointsTxnClassificationCodeEnum.PREALLOC_EARN, pointsResponse.getActionCode());
		assertNotNull(pointsResponse.getStatusMessage());
		
	}
	
	@Test
	public void storeEarnReversalPointsTest(){
		PointsRequest pr = new PointsRequest();
		pr.setTxnActionCode("EARN_REVERSAL");
		pr.setClientName("Future Bazaar");
		pr.setSessionToken(responseUser1.getSessionToken());
		
		OrderRequest request  = new OrderRequest();
		request.setLoyaltyCard("1234567890123456");
		request.setAmount(new BigDecimal(4000));
		request.setOrderId(1);
		request.setTxnTimestamp(new DateTime(2012, 05, 24, 10, 0, 0));
		request.setReferenceId("5051234568");
		
		List<OrderItemRequest> orderItemRequest = new ArrayList<OrderItemRequest>();
		OrderItemRequest orderItem1 = new OrderItemRequest();
		orderItem1.setAmount(new BigDecimal(2000));
		orderItem1.setArticleId("1234");
		orderItem1.setCategoryId(1234);
		orderItem1.setDepartmentCode(1234);
		orderItem1.setDepartmentName("Pooh");
		orderItem1.setId(1);
		orderItem1.setQuantity(1);
		orderItem1.setSellerRateChartId(1);
		orderItemRequest.add(orderItem1);
		
		OrderItemRequest orderItem2 = new OrderItemRequest();
		orderItem2.setAmount(new BigDecimal(2000));
		orderItem2.setArticleId("1234");
		orderItem2.setCategoryId(1);
		orderItem2.setDepartmentCode(1234);
		orderItem2.setDepartmentName("Pooh");
		orderItem2.setId(1);
		orderItem2.setQuantity(1);
		orderItem2.setSellerRateChartId(2);
		orderItemRequest.add(orderItem2);
		
		request.setOrderItemRequest(orderItemRequest);
		
		pr.setOrderRequest(request);
		
		PointsResponse pointsResponse = pointsManager.getPointsReponse(pr);
		assertEquals(PointsResponseCodeEnum.SUCCESS, pointsResponse.getPointsResponseCodeEnum());
		assertEquals(PointsTxnClassificationCodeEnum.EARN_REVERSAL, pointsResponse.getActionCode());
		assertNotNull(pointsResponse.getStatusMessage());
		
	}
	
	@Test
	public void storeBurnReversalPointsTest(){
		PointsRequest pr = new PointsRequest();
		pr.setTxnActionCode("BURN_REVERSAL");
		pr.setClientName("Future Bazaar");
		pr.setSessionToken(responseUser1.getSessionToken());
		
		OrderRequest request  = new OrderRequest();
		request.setLoyaltyCard("1234567890123456");
		request.setAmount(new BigDecimal(2001));
		request.setOrderId(1);
		request.setTxnTimestamp(new DateTime(2012, 05, 24, 10, 0, 0));
		request.setReferenceId("5051234567");
		
		List<OrderItemRequest> orderItemRequest = new ArrayList<OrderItemRequest>();
		OrderItemRequest orderItem1 = new OrderItemRequest();
		orderItem1.setAmount(new BigDecimal(2000));
		orderItem1.setArticleId("1234");
		orderItem1.setCategoryId(1234);
		orderItem1.setDepartmentCode(1234);
		orderItem1.setDepartmentName("Pooh");
		orderItem1.setId(1);
		orderItem1.setQuantity(1);
		orderItem1.setSellerRateChartId(1);
		orderItemRequest.add(orderItem1);
		
		OrderItemRequest orderItem2 = new OrderItemRequest();
		orderItem2.setAmount(new BigDecimal(2000));
		orderItem2.setArticleId("1234");
		orderItem2.setCategoryId(1);
		orderItem2.setDepartmentCode(1234);
		orderItem2.setDepartmentName("Pooh");
		orderItem2.setId(1);
		orderItem2.setQuantity(1);
		orderItem2.setSellerRateChartId(2);
		orderItemRequest.add(orderItem2);
		
		request.setOrderItemRequest(orderItemRequest);
		
		pr.setOrderRequest(request);
		
		PointsResponse pointsResponse = pointsManager.getPointsReponse(pr);
		assertEquals(PointsResponseCodeEnum.SUCCESS, pointsResponse.getPointsResponseCodeEnum());
		assertEquals(PointsTxnClassificationCodeEnum.BURN_REVERSAL, pointsResponse.getActionCode());
		assertNotNull(pointsResponse.getStatusMessage());
		
	}
	
	@Test
	public void getPointsToBeDisplayedTest() throws NoPermissionException{
		PointsRequest pr = new PointsRequest();
		pr.setTxnActionCode("PREALLOC_EARN");
		
		OrderRequest request  = new OrderRequest();
		request.setTxnTimestamp(new DateTime(2012, 05, 24, 10, 0, 0));
		
		List<OrderItemRequest> orderItemRequest = new ArrayList<OrderItemRequest>();
		OrderItemRequest orderItem1 = new OrderItemRequest();
		orderItem1.setAmount(new BigDecimal(2000));
		orderItem1.setArticleId("1234");
		orderItem1.setCategoryId(1234);
		orderItem1.setDepartmentCode(1234);
		orderItem1.setDepartmentName("Pooh");
		orderItem1.setId(1);
		orderItem1.setQuantity(1);
		orderItem1.setSellerRateChartId(1);
		orderItemRequest.add(orderItem1);
		
		OrderItemRequest orderItem2 = new OrderItemRequest();
		orderItem2.setAmount(new BigDecimal(2000));
		orderItem2.setArticleId("1234");
		orderItem2.setCategoryId(1);
		orderItem2.setDepartmentCode(1234);
		orderItem2.setDepartmentName("Pooh");
		orderItem2.setId(1);
		orderItem2.setQuantity(1);
		orderItem2.setSellerRateChartId(2);
		orderItemRequest.add(orderItem2);
		
		request.setOrderItemRequest(orderItemRequest);
		
		pr.setOrderRequest(request);
		PointsRequest newRequest = pointsManager.getPointsToBeDisplayed(pr);
		assertEquals(180, newRequest.getOrderRequest().getTxnPoints().intValue());
	}
	
}
