package com.fb.platform.payback.cache;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.payback.dao.ListDao;
import com.fb.platform.payback.dao.PointsRuleDao;
import com.fb.platform.payback.rule.EarnPointsRuleEnum;
import com.fb.platform.payback.rule.PointsRule;
import com.fb.platform.payback.rule.impl.BuyHeroDealEarnYPoints;
import com.fb.platform.payback.service.PointsService;
import com.fb.platform.payback.to.OrderItemRequest;
import com.fb.platform.payback.to.OrderRequest;
import com.fb.platform.payback.to.PaymentRequest;
import com.fb.platform.payback.util.PointsUtil;

public class CacheTest extends BaseTestCase{
	
	@Autowired
	private PointsRuleDao pointsRuleDao;
	
	@Autowired
	private ListDao listDao;
	
	@Autowired
	private RuleCacheAccess ruleCacheAccess;
	
	@Autowired
	private ListCacheAccess listCacheAccess;
	
	@Autowired
	private PointsUtil pointsUtil;
	
	@Test
	public void loyaltyCardRuleCacheAccessTest(){
		PointsRule rule = pointsRuleDao.loadEarnRule(EarnPointsRuleEnum.ENTER_LOYALTY_CARD_EARN_X_POINTS);
		ruleCacheAccess.put(EarnPointsRuleEnum.ENTER_LOYALTY_CARD_EARN_X_POINTS.name(), rule);
		PointsRule cacheRule =ruleCacheAccess.get(EarnPointsRuleEnum.ENTER_LOYALTY_CARD_EARN_X_POINTS.name());
		 
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setTxnTimestamp(DateTime.now());
		OrderItemRequest itemRequest = new OrderItemRequest();
		itemRequest.setAmount(new BigDecimal(1000));
		List<OrderItemRequest> orderItem = new ArrayList<OrderItemRequest>();
		orderItem.add(itemRequest);
		orderRequest.setOrderItemRequest(orderItem);
		
		assertEquals(30, cacheRule.execute(orderRequest, itemRequest).intValue());
		
	}
	
	@Test
	public void bonusPointsRuleCacheTest(){
		PointsRule rule = pointsRuleDao.loadEarnRule(EarnPointsRuleEnum.BUY_WORTH_X_EARN_Y_BONUS_POINTS);
		ruleCacheAccess.put(EarnPointsRuleEnum.BUY_WORTH_X_EARN_Y_BONUS_POINTS.name(), rule);
		PointsRule cacheRule =ruleCacheAccess.get(EarnPointsRuleEnum.BUY_WORTH_X_EARN_Y_BONUS_POINTS.name());
		assertTrue(cacheRule.allowNext(null, null));
		 
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setTxnTimestamp(DateTime.now());
		orderRequest.setAmount(new BigDecimal(3000));
		OrderItemRequest itemRequest = new OrderItemRequest();
		itemRequest.setAmount(new BigDecimal(1000));
		List<OrderItemRequest> orderItem = new ArrayList<OrderItemRequest>();
		orderItem.add(itemRequest);
		orderRequest.setOrderItemRequest(orderItem);
		cacheRule.execute(orderRequest, itemRequest);
		assertEquals(200, orderRequest.getBonusPoints().intValue());
		
	}
	
	@Test
	public void offerDayTest(){
		PointsRule rule = pointsRuleDao.loadEarnRule(EarnPointsRuleEnum.EARN_X_POINTS_ON_Y_DAY);
		ruleCacheAccess.put(EarnPointsRuleEnum.EARN_X_POINTS_ON_Y_DAY.name(), rule);
		PointsRule cacheRule =ruleCacheAccess.get(EarnPointsRuleEnum.EARN_X_POINTS_ON_Y_DAY.name());
		assertFalse(cacheRule.allowNext(null, null));
		 
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setTxnTimestamp(new DateTime(2012, 05, 25, 0, 0, 0));
		orderRequest.setAmount(new BigDecimal(3000));
		OrderItemRequest itemRequest = new OrderItemRequest();
		itemRequest.setAmount(new BigDecimal(1000));
		List<OrderItemRequest> orderItem = new ArrayList<OrderItemRequest>();
		orderItem.add(itemRequest);
		orderRequest.setOrderItemRequest(orderItem);
		cacheRule.execute(orderRequest, itemRequest);
		assertEquals(60, cacheRule.execute(orderRequest, itemRequest).intValue());
	}
	
	@Test
	public void listCacheAccessTest(){
		
		DateTime dealTime = new DateTime(2012, 05, 24, 0, 0, 1);
		List<Long> dealId = listDao.getHeroDealSellerRateChart(dealTime);
		assertEquals(new Long(1),  dealId.get(0));
		
		String bookingDate = pointsUtil.convertDateToFormat(dealTime, "yyyy-MM-dd");
		String key = PointsCacheConstants.HERO_DEAL +  "#" + bookingDate;
		listCacheAccess.lock(key);
		listCacheAccess.put(key, String.valueOf(dealId.get(0)));
		listCacheAccess.unlock(key);
		assertEquals("1",  listCacheAccess.get(key));
		
	}
	
	@Test
	public void earnCategoryPaymentModeRuleCacheTest(){
		PointsRule rule = pointsRuleDao.loadEarnRule(EarnPointsRuleEnum.EARN_X_POINTS_ON_Y_CATEGORY_FOR_Z_PAYMENT_MODE);
		ruleCacheAccess.put(EarnPointsRuleEnum.EARN_X_POINTS_ON_Y_CATEGORY_FOR_Z_PAYMENT_MODE.name(), rule);
		PointsRule cacheRule = ruleCacheAccess.get(EarnPointsRuleEnum.EARN_X_POINTS_ON_Y_CATEGORY_FOR_Z_PAYMENT_MODE.name());
		assertTrue(cacheRule.allowNext(null, null));
		 
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setTxnTimestamp(DateTime.now());
		orderRequest.setAmount(new BigDecimal(3000));

		List<PaymentRequest> paymentRequest = new ArrayList<PaymentRequest>();
		PaymentRequest payment = new PaymentRequest();
		payment.setAmount(new BigDecimal(2000));
		payment.setPaymentMode("payback");
		paymentRequest.add(payment);
		orderRequest.setPaymentRequest(paymentRequest);

		List<OrderItemRequest> orderItem = new ArrayList<OrderItemRequest>();
		OrderItemRequest itemRequest1 = new OrderItemRequest();
		itemRequest1.setAmount(new BigDecimal(1000));
		itemRequest1.setCategoryId(1234);
		orderItem.add(itemRequest1);
		
		OrderItemRequest itemRequest2 = new OrderItemRequest();
		itemRequest2.setAmount(new BigDecimal(1000));
		itemRequest2.setCategoryId(1);
		orderItem.add(itemRequest2);		
		orderRequest.setOrderItemRequest(orderItem);

		assertTrue(cacheRule.isApplicable(orderRequest, itemRequest1));
		assertEquals(90, cacheRule.execute(orderRequest, itemRequest1).intValue());
		
		assertTrue(cacheRule.isApplicable(orderRequest, itemRequest2));
		assertEquals(150, cacheRule.execute(orderRequest, itemRequest2).intValue());
	}
	
	@Test
	public void clearRuleCacheTest(){
		PointsRule rule = new BuyHeroDealEarnYPoints();
		ruleCacheAccess.lock(EarnPointsRuleEnum.EARN_X_POINTS_ON_Y_DAY.name());
		ruleCacheAccess.put(EarnPointsRuleEnum.EARN_X_POINTS_ON_Y_DAY.name(), rule);
		ruleCacheAccess.unlock(EarnPointsRuleEnum.EARN_X_POINTS_ON_Y_DAY.name());
		assertTrue(ruleCacheAccess.clear(EarnPointsRuleEnum.EARN_X_POINTS_ON_Y_DAY.name()));
		assertFalse(ruleCacheAccess.clear(EarnPointsRuleEnum.EARN_X_POINTS_ON_Y_DAY.name()));
		assertNull(ruleCacheAccess.get(EarnPointsRuleEnum.EARN_X_POINTS_ON_Y_DAY.name()));
		
	}
	
}
