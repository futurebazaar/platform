package com.fb.platform.payback.rule;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.payback.cache.ListCacheAccess;
import com.fb.platform.payback.dao.ListDao;
import com.fb.platform.payback.dao.PointsRuleDao;
import com.fb.platform.payback.rule.impl.BuyHeroDealEarnYPoints;
import com.fb.platform.payback.to.OrderItemRequest;
import com.fb.platform.payback.to.OrderRequest;
import com.fb.platform.payback.to.PaymentRequest;
import com.fb.platform.payback.util.PointsUtil;

public class RulesTest extends BaseTestCase{

		@Autowired
		private ListDao listDao;
		
		@Autowired
		private PointsRuleDao pointsRuleDao;
		
		@Autowired
		private PointsUtil pointsUtil;
		
		@Autowired
		private ListCacheAccess listCacheAccess;
		
		private OrderRequest orderRequest = new OrderRequest();
		
		
		@Before
		public void setOrderRequest(){
			
			orderRequest.setLoyaltyCard("1234567890123456");
			orderRequest.setAmount(new BigDecimal(2000));
			orderRequest.setOrderTotal(new BigDecimal(2000));
			orderRequest.setOrderId(1);
			orderRequest.setTxnTimestamp(new DateTime(2012, 05, 24, 10, 0, 0));
			orderRequest.setReferenceId("5051234567");
			
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
			
			orderRequest.setOrderItemRequest(orderItemRequest);

			List<PaymentRequest> paymentRequest = new ArrayList<PaymentRequest>();
			PaymentRequest payment = new PaymentRequest();
			payment.setAmount(new BigDecimal(2000));
			payment.setPaymentMode("payback");
			paymentRequest.add(payment);
			
			orderRequest.setPaymentRequest(paymentRequest);
		}
		
	@Test
	public void buyHeroDeailEarnYPointsTest(){
		
		PointsRule rule = pointsRuleDao.loadEarnRule(EarnPointsRuleEnum.BUY_DOD_EARN_Y_POINTS);
		((BuyHeroDealEarnYPoints)rule).setPointsUtil(pointsUtil);
		((BuyHeroDealEarnYPoints)rule).setListDao(listDao);
		((BuyHeroDealEarnYPoints)rule).setListCacheAccess(listCacheAccess);
		
		BigDecimal txnPoints = BigDecimal.ZERO;
		for (OrderItemRequest itemRequest : orderRequest.getOrderItemRequest()){
			if (rule.isApplicable(orderRequest, itemRequest)){
				txnPoints = txnPoints.add(rule.execute(orderRequest, itemRequest));
			}
		}
		
		assertEquals(new BigDecimal(120).intValue(), txnPoints.intValue());
		
	}
	
	@Test
	public void buyProductXEarnYPoints(){
		PointsRule rule = pointsRuleDao.loadEarnRule(EarnPointsRuleEnum.BUY_PRODUCT_X_EARN_Y_POINTS);
		BigDecimal txnPoints = BigDecimal.ZERO;
		for (OrderItemRequest itemRequest : orderRequest.getOrderItemRequest()){
			if (rule.isApplicable(orderRequest, itemRequest)){
				txnPoints = txnPoints.add(rule.execute(orderRequest, itemRequest));
			}
		}
		
		assertEquals(new BigDecimal(120).intValue(), txnPoints.intValue());
		
	}
	
	@Test
	public void buyWorthXEarnYBonusPointsTest(){
		PointsRule rule = pointsRuleDao.loadEarnRule(EarnPointsRuleEnum.BUY_WORTH_X_EARN_Y_BONUS_POINTS);
		BigDecimal txnPoints = BigDecimal.ZERO;
		for (OrderItemRequest itemRequest : orderRequest.getOrderItemRequest()){
			if (rule.isApplicable(orderRequest, itemRequest)){
				txnPoints = txnPoints.add(rule.execute(orderRequest, itemRequest));
			}
		}
		
		assertEquals(new BigDecimal(0).intValue(), txnPoints.intValue());
		assertEquals(200, orderRequest.getBonusPoints().intValue());
	}
	
	@Test
	public void earnXPointsOnYDayTest(){
		PointsRule rule = pointsRuleDao.loadEarnRule(EarnPointsRuleEnum.EARN_X_POINTS_ON_Y_DAY);
		BigDecimal txnPoints = BigDecimal.ZERO;
		orderRequest.setTxnTimestamp(new DateTime(2012, 05, 25, 0, 0, 0));
		for (OrderItemRequest itemRequest : orderRequest.getOrderItemRequest()){
			if (rule.isApplicable(orderRequest, itemRequest)){
				txnPoints = txnPoints.add(rule.execute(orderRequest, itemRequest));
			}
		}
		
		assertEquals(new BigDecimal(240).intValue(), txnPoints.intValue());
	}
	
	@Test
	public void enterLoyaltyCardEarnXPoints(){
		PointsRule rule = pointsRuleDao.loadEarnRule(EarnPointsRuleEnum.ENTER_LOYALTY_CARD_EARN_X_POINTS);
		BigDecimal txnPoints = BigDecimal.ZERO;
		orderRequest.setTxnTimestamp(new DateTime(2012, 05, 25, 0, 0, 0));
		for (OrderItemRequest itemRequest : orderRequest.getOrderItemRequest()){
			if (rule.isApplicable(orderRequest, itemRequest)){
				txnPoints = txnPoints.add(rule.execute(orderRequest, itemRequest));
			}
		}
		
		assertEquals(new BigDecimal(120).intValue(), txnPoints.intValue());
	}
	
	@Test
	public void purchaseOrderBurnXPoints(){
		PointsRule rule = pointsRuleDao.loadBurnRule(BurnPointsRuleEnum.PURCHASE_ORDER_BURN_X_POINTS);
		BigDecimal txnPoints = BigDecimal.ZERO;
		orderRequest.setTxnTimestamp(new DateTime(2012, 05, 25, 0, 0, 0));
		for (OrderItemRequest itemRequest : orderRequest.getOrderItemRequest()){
			if (rule.isApplicable(orderRequest, itemRequest)){
				txnPoints = rule.execute(orderRequest, itemRequest);
			}
		}
		
		assertEquals(new BigDecimal(8000).intValue(), txnPoints.intValue());
	}
	
	@Test
	public void earnXPointsOnYPaymentMode(){
		PointsRule rule = pointsRuleDao.loadEarnRule(EarnPointsRuleEnum.EARN_X_POINTS_ON_Y_PAYMENT_MODE);
		BigDecimal txnPoints = BigDecimal.ZERO;
		orderRequest.setTxnTimestamp(new DateTime(2012, 05, 25, 0, 0, 0));
		for (OrderItemRequest itemRequest : orderRequest.getOrderItemRequest()){
			if (rule.isApplicable(orderRequest, itemRequest)){
				txnPoints = txnPoints.add(rule.execute(orderRequest, itemRequest));
			}
		}
		assertEquals(new BigDecimal(600).intValue(), txnPoints.intValue());
	}
	
	@Test
	public void earnXPointsOnYCategoryForZPaymentMode(){
		PointsRule rule = pointsRuleDao.loadEarnRule(EarnPointsRuleEnum.EARN_X_POINTS_ON_Y_CATEGORY_FOR_Z_PAYMENT_MODE);
		BigDecimal txnPoints = BigDecimal.ZERO;
		orderRequest.setTxnTimestamp(new DateTime(2012, 05, 25, 0, 0, 0));
		for (OrderItemRequest itemRequest : orderRequest.getOrderItemRequest()){
			if (rule.isApplicable(orderRequest, itemRequest)){
				txnPoints = txnPoints.add(rule.execute(orderRequest, itemRequest));
			}
		}
		assertEquals(new BigDecimal(480).intValue(), txnPoints.intValue());
	}
}
