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
		BigDecimal txnPoints = new BigDecimal(30);
		int ROUND = BigDecimal.ROUND_HALF_DOWN;
		BigDecimal amount = new BigDecimal(1000);
		
		pointsHeader.setPartnerMerchantId("90012970");
		pointsHeader.setPartnerTerminalId("6241718");
		pointsHeader.setTxnClassificationCode("CASH_CASH");
		pointsHeader.setTxnPaymentType("OTHERS");
		pointsHeader.setTxnActionCode("BURN_REVERSAL");
		pointsHeader.setTxnPoints(txnPoints.setScale(0, ROUND).intValue());
		pointsHeader.setTxnValue(amount.setScale(0, ROUND).intValue());
		pointsHeader.setReferenceId("1234");
		pointsHeader.setLoyaltyCard("1234567812345678");
		pointsHeader.setReason("TEST ORDER");
		pointsHeader.setSettlementDate(DateTime.now().minusDays(1));
		pointsHeader.setTxnDate(DateTime.now().minusDays(1));
		pointsHeader.setTxnTimestamp(DateTime.now().minusDays(1));
		pointsHeader.setOrderId(1);
		
		pointsDao.insertPointsHeaderData(pointsHeader);
		
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
		orderItem1.setId(2);
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
		assertEquals(PointsResponseCodeEnum.SUCCESS, pointsService.storePoints(pr));
		
	}
	
	@Test
	public void storeBurnReversalPoints(){
		
		PointsRequest pr = new PointsRequest();
		pr.setTxnActionCode("BURN_REVERSAL");
		pr.setClientName("Future Bazaar");
		
		OrderRequest request  = new OrderRequest();
		request.setLoyaltyCard("1234567890123456");
		request.setAmount(new BigDecimal(2001));
		request.setOrderId(2);
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
		assertEquals(PointsResponseCodeEnum.SUCCESS, pointsService.storePoints(pr));
		
	}
	
	@Test
	public void getPointsToBeDisplayed(){
		PointsRequest pr = new PointsRequest();
		pr.setTxnActionCode("PREALLOC_EARN");
		pr.setClientName("Future Bazaar");
		
		OrderRequest request  = new OrderRequest();
		request.setLoyaltyCard("1234567890123456");
		request.setAmount(new BigDecimal(2001));
		request.setOrderId(2);
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
		PointsRequest newRequest = pointsService.getPointsToBeDisplayed(pr);
		assertEquals(180, newRequest.getOrderRequest().getTxnPoints().intValue());
	}
	
	
	
}
