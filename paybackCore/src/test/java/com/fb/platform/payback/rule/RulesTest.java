package com.fb.platform.payback.rule;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.payback.rule.PointsRule;
import com.fb.platform.payback.rule.RuleConfigItem;
import com.fb.platform.payback.rule.RuleConfiguration;
import com.fb.platform.payback.rule.impl.BuyProductXEarnYPoints;
import com.fb.platform.payback.rule.impl.BuyWorthXEarnYBonusPoints;
import com.fb.platform.payback.rule.impl.EarnXPointsOnYDay;
import com.fb.platform.payback.rule.impl.EnterLoyaltyCardEarnXPoints;
import com.fb.platform.payback.to.OrderItemRequest;
import com.fb.platform.payback.to.OrderRequest;
import com.fb.platform.payback.util.PointsUtil;


public class RulesTest extends BaseTestCase{

	
	@Test
	public void testRule(){
	
		PointsUtil pointsUtil = new PointsUtil();
		
		List<RuleConfigItem> configItems = new ArrayList<RuleConfigItem>();
		RuleConfigItem e1 = new RuleConfigItem("VALID_FROM", pointsUtil.convertDateToFormat(DateTime.now(), "yyyy-MM-dd"));
		RuleConfigItem e2 = new RuleConfigItem("VALID_TILL", pointsUtil.convertDateToFormat(DateTime.now(), "yyyy-MM-dd"));
		
		RuleConfigItem e3 = new RuleConfigItem("POINTS_FACTOR", "2");
		RuleConfigItem e4 = new RuleConfigItem("EARN_RATIO", "0.03");
		RuleConfigItem e5 = new RuleConfigItem("OFFER_DAY",  DateTime.now().dayOfWeek().getAsText());
		RuleConfigItem e6 = new RuleConfigItem("EXCLUDED_CATEGORY_LIST", "");
		RuleConfigItem e7 = new RuleConfigItem("INCLUDED_CATEGORY_LIST", "1234");
		RuleConfigItem e8 = new RuleConfigItem("MIN_ORDER_VALUE", "2000");
		RuleConfigItem e9 = new RuleConfigItem("BONUS_POINTS", "200");
		
		configItems.add(e1); 
		configItems.add(e2); 
		configItems.add(e3); 
		configItems.add(e4);
		configItems.add(e5); 
		configItems.add(e6);
		configItems.add(e7);
		configItems.add(e8);
		configItems.add(e9);
		
		OrderRequest request  = new OrderRequest();
		request.setAmount(new BigDecimal(2000));
		request.setTxnTimestamp(DateTime.now());
		List<OrderItemRequest> orderItemRequest = new ArrayList<OrderItemRequest>();
		
		OrderItemRequest orderItem1 = new OrderItemRequest();
		orderItem1.setAmount(new BigDecimal(2000));
		orderItemRequest.add(orderItem1);
		request.setOrderItemRequest(orderItemRequest);
		RuleConfiguration ruleConfig = new RuleConfiguration(configItems);
		
		PointsRule rule1 = new EarnXPointsOnYDay();
		rule1.init(ruleConfig);
		assertTrue(rule1.isApplicable(request, orderItem1));
		assertEquals(rule1.execute(request, orderItem1).setScale(0, BigDecimal.ROUND_HALF_DOWN).intValue(), 120);
		
		PointsRule rule2 = new BuyProductXEarnYPoints();
		OrderItemRequest orderItem2 = new OrderItemRequest();
		orderItem2.setAmount(new BigDecimal(2000));
		orderItem2.setCategoryId(1234);
		orderItemRequest.add(orderItem2);
		request.setOrderItemRequest(orderItemRequest);
		rule2.init(ruleConfig);
		assertTrue(rule2.isApplicable(request, orderItem2));
		assertEquals(rule2.execute(request, orderItem2).setScale(0, BigDecimal.ROUND_HALF_DOWN).intValue(), 120);
		
		PointsRule rule3 = new EnterLoyaltyCardEarnXPoints();
		rule3.init(ruleConfig);
		assertTrue(rule3.isApplicable(request, orderItem2));
		assertEquals(rule3.execute(request, orderItem2).setScale(0, BigDecimal.ROUND_HALF_DOWN).intValue(), 60);
		
		PointsRule rule4 = new BuyWorthXEarnYBonusPoints();
		rule4.init(ruleConfig);
		assertTrue(rule4.isApplicable(request, null));
		assertEquals(rule4.execute(request, null).setScale(0, BigDecimal.ROUND_HALF_DOWN), BigDecimal.ZERO);
		//assertEquals(request.getAmount(), actual)
	}
	
}
