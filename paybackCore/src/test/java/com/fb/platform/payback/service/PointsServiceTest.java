package com.fb.platform.payback.service;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.PlatformException;
import com.fb.commons.test.BaseTestCase;
import com.fb.platform.payback.dao.PointsDao;
import com.fb.platform.payback.model.PointsHeader;
import com.fb.platform.payback.to.BurnActionCodesEnum;
import com.fb.platform.payback.to.EarnActionCodesEnum;
import com.fb.platform.payback.to.OrderItemRequest;
import com.fb.platform.payback.to.OrderRequest;
import com.fb.platform.payback.to.PointsRequest;
import com.fb.platform.payback.to.PointsResponseCodeEnum;
import com.sun.jersey.api.MessageException;

public class PointsServiceTest extends BaseTestCase{
	
	@Autowired
	private PointsService pointsService;
	
	@Autowired
	private PointsDao pointsDao;
	
	private PointsHeader pointsHeader = new PointsHeader();
	
	@Before
	public void insertTestData(){
		PointsRequest pr = new PointsRequest();
		pr.setTxnActionCode("PREALLOC_EARN");
		pr.setClientName("Future Bazaar");
		
		OrderRequest request = setOrderRequest(new Long(1), "1234");
		request.setLoyaltyCard("1234123412341234");
		request.getOrderItemRequest().add(setItemRequest(1, 2000));
		pr.setOrderRequest(request);
		assertEquals(PointsResponseCodeEnum.SUCCESS, pointsService.storePoints(pr));
	}
	
//	@Test
//	public void mailBurnDataTest(){
//		try{
//			assertFalse(pointsService.mailBurnData(BurnActionCodesEnum.BURN_REVERSAL, "90012970").isEmpty());
//		} catch(MessageException me){
//			me.printStackTrace();
//		}
//	}

//	@Test
//	public void getEarnDataTest(){
//		BigDecimal txnPoints = new BigDecimal(30);
//		int ROUND = BigDecimal.ROUND_HALF_DOWN;
//		BigDecimal amount = new BigDecimal(1000);
//		
//		pointsHeader.setPartnerMerchantId("90012970");
//		pointsHeader.setPartnerTerminalId("6241718");
//		pointsHeader.setTxnClassificationCode("BONUS_POINTS");
//		pointsHeader.setTxnPaymentType("NO_PAYMENT");
//		pointsHeader.setTxnActionCode("EARN_REVERSAL");
//		pointsHeader.setTxnPoints(txnPoints.setScale(0, ROUND).intValue());
//		pointsHeader.setTxnValue(amount.setScale(0, ROUND).intValue());
//		pointsHeader.setReferenceId("1234");
//		pointsHeader.setLoyaltyCard("1234567812345678");
//		pointsHeader.setReason("TEST ORDER");
//		pointsHeader.setSettlementDate(DateTime.now().minusDays(1));
//		pointsHeader.setTxnDate(DateTime.now().minusDays(1));
//		pointsHeader.setTxnTimestamp(DateTime.now().minusDays(1));
//		pointsHeader.setOrderId(2);
//		
//		pointsDao.insertPointsHeaderData(pointsHeader);
//		
//		try{
//			String data = pointsService.postEarnData(EarnActionCodesEnum.EARN_REVERSAL, "90012970", "Future Bazaar");
//			assertFalse(data.isEmpty());
//		} catch(PlatformException e){
//			e.printStackTrace();
//		}
//	}
	
	@Test
	public void storeEarnPoints(){
		
		PointsRequest pr = new PointsRequest();
		pr.setTxnActionCode("PREALLOC_EARN");
		pr.setClientName("Future Bazaar");
		
		OrderRequest request = setOrderRequest(new Long(2), "1234");
		request.setLoyaltyCard("1234123412341234");
		pr.setOrderRequest(request);
		assertEquals(PointsResponseCodeEnum.INVALID_POINTS, pointsService.storePoints(pr));
		
		request.getOrderItemRequest().add(setItemRequest(1, 2000));
		pr.setOrderRequest(request);
		assertEquals(PointsResponseCodeEnum.SUCCESS, pointsService.storePoints(pr));
	}
	
	@Test
	public void storeEarnReversalPoints(){
		PointsRequest pr = new PointsRequest();
		pr.setTxnActionCode("EARN_REVERSAL");
		pr.setClientName("Future Bazaar");
		
		OrderRequest request = setOrderRequest(new Long(1), "1234");
		request.setLoyaltyCard("1234567812345678");
		
		request.getOrderItemRequest().add(setItemRequest(1, 100));
		pr.setOrderRequest(request);
		assertEquals(PointsResponseCodeEnum.SUCCESS, pointsService.storePoints(pr));
		
	}
	
	@Test
	public void storeBurnReversalPoints(){
		
		PointsRequest pr = new PointsRequest();
		pr.setTxnActionCode("BURN_REVERSAL");
		pr.setClientName("Future Bazaar");
		
		OrderRequest request  = setOrderRequest(new Long(2), "1234");
		
		pr.setOrderRequest(request);
		assertEquals(PointsResponseCodeEnum.SUCCESS, pointsService.storePoints(pr));
		
	}
	
	@Test
	public void getPointsToBeDisplayed(){
		PointsRequest pr = new PointsRequest();
		pr.setTxnActionCode("PREALLOC_EARN");
		OrderRequest request = setOrderRequest(new Long(1), "1234");
		request.getOrderItemRequest().add(setItemRequest(1, 500));
		request.getOrderItemRequest().add(setItemRequest(2, 639));
		pr.setOrderRequest(request);
		PointsRequest newRequest = pointsService.getPointsToBeDisplayed(pr);
		assertEquals(68, newRequest.getOrderRequest().getTxnPoints().intValue());
		
		pr.setTxnActionCode("BURN_REVERSAL");
		newRequest = pointsService.getPointsToBeDisplayed(pr);
		assertEquals(8000, newRequest.getOrderRequest().getTxnPoints().intValue());
	}
	
	private OrderRequest setOrderRequest(Long orderId, String referenceId) {
		OrderRequest request = new OrderRequest();
		request.setOrderId(orderId);
		request.setAmount(new BigDecimal(2000));
		request.setTxnTimestamp(new DateTime(2012, 05, 24, 10, 0, 0));
		request.setReferenceId(referenceId);
		return request;
		
	}
	
	private OrderItemRequest setItemRequest(int itemId, int amount){
		OrderItemRequest orderItem1 = new OrderItemRequest();
		orderItem1.setAmount(new BigDecimal(amount));
		orderItem1.setArticleId("1234");
		orderItem1.setCategoryId(1234);
		orderItem1.setDepartmentCode(1234);
		orderItem1.setDepartmentName("Pooh");
		orderItem1.setId(new Long(itemId));
		orderItem1.setQuantity(1);
		orderItem1.setSellerRateChartId(1);
		return orderItem1;
	}
	
}
