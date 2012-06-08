package com.fb.platform.payback.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NoPermissionException;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.payback.to.ClearCacheRequest;
import com.fb.platform.payback.to.OrderItemRequest;
import com.fb.platform.payback.to.OrderRequest;
import com.fb.platform.payback.to.PointsRequest;
import com.fb.platform.payback.to.PointsResponse;
import com.fb.platform.payback.to.PointsResponseCodeEnum;
import com.fb.platform.payback.to.PointsTxnClassificationCodeEnum;
import com.fb.platform.payback.to.RollbackRequest;
import com.fb.platform.payback.to.RollbackResponse;
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
	
	@Test(expected=IllegalArgumentException.class)
	public void storeEarnPointsInvalidActionCodeTest() {
		PointsRequest pr = new PointsRequest();
		pr.setTxnActionCode("TEST");
		pr.setClientName("Future Bazaar");
		pr.setSessionToken(responseUser1.getSessionToken());
		
		OrderRequest request  = setOrderRequest(new Long(2), null);
		pr.setOrderRequest(request);
		
		pointsManager.getPointsReponse(pr);
	}
	
	@Test
	public void storeEarnPointsTest(){
		PointsRequest pr = new PointsRequest();
		pr.setTxnActionCode("PREALLOC_EARN");
		pr.setClientName("Future Bazaar");
		
		OrderRequest request  = setOrderRequest(new Long(2), null);
		pr.setOrderRequest(request);
		
		pr.setOrderRequest(request);
		PointsResponse pointsResponse = pointsManager.getPointsReponse(pr);
		assertEquals(PointsTxnClassificationCodeEnum.PREALLOC_EARN, pointsResponse.getActionCode());
		assertEquals(PointsResponseCodeEnum.NO_SESSION, pointsResponse.getPointsResponseCodeEnum());
		
		pr.setSessionToken(responseUser1.getSessionToken());
		PointsResponse pointsResponse1 = pointsManager.getPointsReponse(pr);
		assertEquals(PointsTxnClassificationCodeEnum.PREALLOC_EARN, pointsResponse1.getActionCode());
		assertEquals(PointsResponseCodeEnum.INVALID_REFERENCE_ID, pointsResponse1.getPointsResponseCodeEnum());
		
		request.setReferenceId("1234");
		pr.setOrderRequest(request);
		PointsResponse pointsResponse2 = pointsManager.getPointsReponse(pr);
		assertEquals(PointsTxnClassificationCodeEnum.PREALLOC_EARN, pointsResponse2.getActionCode());
		assertEquals(PointsResponseCodeEnum.INVALID_CARD_NO, pointsResponse2.getPointsResponseCodeEnum());
		
		request.setLoyaltyCard("1234567812345678");
		pr.setOrderRequest(request);
		PointsResponse pointsResponse3 = pointsManager.getPointsReponse(pr);
		assertEquals(PointsTxnClassificationCodeEnum.PREALLOC_EARN, pointsResponse3.getActionCode());
		assertEquals(PointsResponseCodeEnum.SUCCESS, pointsResponse3.getPointsResponseCodeEnum());
		assertNotSame(0, pointsResponse3.getPointsHeaderId());
		
		request.setOrderId(new Long(20));
		pr.setOrderRequest(request);
		pointsResponse3 = pointsManager.getPointsReponse(pr);
		assertEquals(PointsTxnClassificationCodeEnum.PREALLOC_EARN, pointsResponse3.getActionCode());
		assertEquals(PointsResponseCodeEnum.INTERNAL_ERROR, pointsResponse3.getPointsResponseCodeEnum());
		
	}
	
	@Test
	public void storeEarnReversalPointsTest(){
		PointsRequest pr = new PointsRequest();
		pr.setClientName("Future Bazaar");
		pr.setTxnActionCode("EARN_REVERSAL");
		PointsResponse pointsResponse = pointsManager.getPointsReponse(pr);
		assertEquals(PointsTxnClassificationCodeEnum.EARN_REVERSAL, pointsResponse.getActionCode());
		assertEquals(PointsResponseCodeEnum.NO_SESSION, pointsResponse.getPointsResponseCodeEnum());
		
		OrderRequest request  = setOrderRequest(new Long(2), "1234");
		pr.setOrderRequest(request);
		
		pointsResponse = pointsManager.getPointsReponse(pr);
		assertEquals(PointsTxnClassificationCodeEnum.EARN_REVERSAL, pointsResponse.getActionCode());
		assertEquals(PointsResponseCodeEnum.NO_SESSION, pointsResponse.getPointsResponseCodeEnum());
		
		pr.setSessionToken(responseUser1.getSessionToken());
		pointsResponse = pointsManager.getPointsReponse(pr);
		assertEquals(PointsTxnClassificationCodeEnum.EARN_REVERSAL, pointsResponse.getActionCode());
		assertEquals(PointsResponseCodeEnum.INVALID_CARD_NO, pointsResponse.getPointsResponseCodeEnum());
		
		request.setLoyaltyCard("1234567812345678");
		pr.setOrderRequest(request);
		PointsResponse pointsResponse1 = pointsManager.getPointsReponse(pr);
		assertEquals(PointsResponseCodeEnum.HEADER_DOES_NOT_EXIST, pointsResponse1.getPointsResponseCodeEnum());
		assertNotNull(pointsResponse1.getStatusMessage());
		
		request.setOrderId(new Long(1));
		pr.setOrderRequest(request);
		
		pointsResponse = pointsManager.getPointsReponse(pr);
		assertEquals(PointsTxnClassificationCodeEnum.EARN_REVERSAL, pointsResponse.getActionCode());
		assertEquals(PointsResponseCodeEnum.SUCCESS, pointsResponse.getPointsResponseCodeEnum());
		
	}
	
	@Test
	public void storeBurnReversalPointsTest(){
		PointsRequest pr = new PointsRequest();
		pr.setTxnActionCode("BURN_REVERSAL");
		pr.setClientName("Future Bazaar");
		pr.setSessionToken(responseUser1.getSessionToken());
		
		OrderRequest request  = new OrderRequest();
		request.setLoyaltyCard("");
		request.setAmount(new BigDecimal(2001));
		request.setOrderId(1);
		request.setTxnTimestamp(new DateTime(2012, 05, 24, 10, 0, 0));
		request.setReferenceId("123456");
		
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
		OrderRequest request = setOrderRequest(new Long(1), "1234");
		pr.setOrderRequest(request);
		PointsRequest newRequest = pointsManager.getPointsToBeDisplayed(pr);
		assertEquals(68, newRequest.getOrderRequest().getTxnPoints().intValue());
		assertEquals(17, newRequest.getOrderRequest().getPointsValue().intValue());
		
		request.setAmount(new BigDecimal(4000));
		newRequest = pointsManager.getPointsToBeDisplayed(pr);
		assertEquals(68, newRequest.getOrderRequest().getTxnPoints().intValue());
		assertEquals(17, newRequest.getOrderRequest().getPointsValue().intValue());
		assertEquals(268, newRequest.getOrderRequest().getTotalTxnPoints().intValue());
	}
	
	private OrderRequest setOrderRequest(Long orderId, String referenceId) {
		OrderRequest request = new OrderRequest();
		request.setOrderId(orderId);
		request.setAmount(new BigDecimal(500));
		request.setTxnTimestamp(new DateTime(2012, 05, 24, 10, 0, 0));
		request.setReferenceId(referenceId);
		
		List<OrderItemRequest> orderItemRequest = new ArrayList<OrderItemRequest>();
		
		OrderItemRequest orderItem1 = new OrderItemRequest();
		orderItem1.setAmount(new BigDecimal(500));
		orderItem1.setArticleId("1234");
		orderItem1.setCategoryId(1234);
		orderItem1.setDepartmentCode(1234);
		orderItem1.setDepartmentName("Pooh");
		orderItem1.setId(1);
		orderItem1.setQuantity(1);
		orderItem1.setSellerRateChartId(1);
		orderItemRequest.add(orderItem1);
		
		OrderItemRequest orderItem2 = new OrderItemRequest();
		orderItem2.setAmount(new BigDecimal(639));
		orderItem2.setArticleId("1234");
		orderItem2.setCategoryId(1234);
		orderItem2.setDepartmentCode(1234);
		orderItem2.setDepartmentName("Pooh");
		orderItem2.setId(1);
		orderItem2.setQuantity(1);
		orderItem2.setSellerRateChartId(2);
		orderItemRequest.add(orderItem2);
		
		request.setOrderItemRequest(orderItemRequest);
		return request;
		
	}
	
	@Test
	public void clearCacheTest(){
		ClearCacheRequest request =null;
		PointsResponseCodeEnum cacheResponse = pointsManager.clearPointsCache(request);
		assertEquals(PointsResponseCodeEnum.NO_SESSION, cacheResponse);
		
		request = new ClearCacheRequest();
		cacheResponse = pointsManager.clearPointsCache(request);
		assertEquals(PointsResponseCodeEnum.NO_SESSION, cacheResponse);
		
		request.setSessionToken("1234");
		cacheResponse = pointsManager.clearPointsCache(request);
		assertEquals(PointsResponseCodeEnum.NO_SESSION, cacheResponse);
		
		request.setSessionToken(responseUser1.getSessionToken());
		cacheResponse = pointsManager.clearPointsCache(request);
		assertEquals(PointsResponseCodeEnum.FAILURE,  cacheResponse);
	}
	
	@Test
	public void mailBurnDataTest(){
		assertEquals("", pointsManager.mailBurnData());
	}
	
	@Test
	public void uploadEarnDataTest(){
		pointsManager.uploadEarnFilesOnSFTP();
	}
	
	@Test
	public void rollbackTransactionTest() {
		PointsRequest pr = new PointsRequest();
		pr.setTxnActionCode("BURN_REVERSAL");
		pr.setClientName("Future Bazaar");
		pr.setSessionToken(responseUser1.getSessionToken());
		
		OrderRequest request  = new OrderRequest();
		request.setLoyaltyCard("");
		request.setAmount(new BigDecimal(2001));
		request.setOrderId(1);
		request.setTxnTimestamp(new DateTime(2012, 05, 24, 10, 0, 0));
		request.setReferenceId("123456");
		
		pr.setOrderRequest(request);
		
		PointsResponse pointsResponse = pointsManager.getPointsReponse(pr);
		assertNotSame(0, pointsResponse.getPointsHeaderId());
		
		RollbackRequest rollbackRequest = new RollbackRequest();
		rollbackRequest.setHeaderId(pr.getOrderRequest().getPointsHeaderId());
		RollbackResponse response = pointsManager.rollbackTransaction(rollbackRequest);
		
		assertEquals(0, response.getDeletedHeaderRows());
		assertEquals(PointsResponseCodeEnum.NO_SESSION, response.getResponseEnum());
		
		rollbackRequest.setSessionToken(responseUser1.getSessionToken());
		response = pointsManager.rollbackTransaction(rollbackRequest);
		assertEquals(1, response.getDeletedHeaderRows());
		assertEquals(PointsResponseCodeEnum.SUCCESS, response.getResponseEnum());
		
		response = pointsManager.rollbackTransaction(rollbackRequest);
		assertEquals(0, response.getDeletedHeaderRows());
	}
}

