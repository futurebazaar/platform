package com.fb.platform.payback.rule;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.payback.service.PointsService;
import com.fb.platform.payback.to.EarnActionCodesEnum;
import com.fb.platform.payback.to.OrderItemRequest;
import com.fb.platform.payback.to.OrderRequest;
import com.fb.platform.payback.to.PointsRequest;
import com.fb.platform.payback.to.PointsResponseCodeEnum;

public class RulesTest extends BaseTestCase{

		@Autowired
		private PointsService pointsService;
		
	@Test
	public void testRule(){
	
		
		PointsRequest pr = new PointsRequest();
		pr.setTxnActionCode("PREALLOC_EARN");
		pr.setClientName("Future Bazaar");
		
		OrderRequest request  = new OrderRequest();
		request.setLoyaltyCard("1234567890123456");
		request.setAmount(new BigDecimal(2001));
		request.setOrderId(1);
		request.setTxnTimestamp(DateTime.now().plusDays(1));
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
		//assertEquals(PointsResponseCodeEnum.SUCCESS, pointsService.storePoints(pr));
		
		//System.out.println(pointsService.postEarnData(EarnActionCodesEnum.PREALLOC_EARN, "90012970"));

	}
	
}
