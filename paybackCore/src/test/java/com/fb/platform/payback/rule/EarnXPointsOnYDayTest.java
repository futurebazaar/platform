package com.fb.platform.payback.rule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.payback.rule.impl.EarnXPointsOnYDay;
import com.fb.platform.payback.to.OrderItemRequest;
import com.fb.platform.payback.to.OrderRequest;


public class EarnXPointsOnYDayTest extends BaseTestCase{

	
	@Test
	public void testRule(){
	
		List<RuleConfigItem> configItems = new ArrayList<RuleConfigItem>();
		RuleConfigItem e1 = new RuleConfigItem("POINTS_FACTOR", "2");
		RuleConfigItem e2 = new RuleConfigItem("EARN_RATIO", "0.03");
		RuleConfigItem e3 = new RuleConfigItem("OFFER_DAY",  DateTime.now().dayOfWeek().getAsText());
		RuleConfigItem e4 = new RuleConfigItem("EXCLUDED_CATEGORY_LIST", "");
		configItems.add(e1); 
		configItems.add(e2); 
		configItems.add(e3); 
		configItems.add(e4);
		
		OrderRequest request  = new OrderRequest();
		request.setAmount(new BigDecimal(2000));
		request.setTxnTimestamp(DateTime.now());
		List<OrderItemRequest> orderItemRequest = new ArrayList<OrderItemRequest>();
		
		OrderItemRequest orderItem = new OrderItemRequest();
		orderItemRequest.add(orderItem);
		request.setOrderItemRequest(orderItemRequest);
		RuleConfiguration ruleConfig = new RuleConfiguration(configItems);
		PointsRule rule = new EarnXPointsOnYDay();
		rule.init(ruleConfig);
		//assertTrue(rule.isApplicable(request));
		BigDecimal earnRatio = new BigDecimal(e1.getValue()).multiply(new BigDecimal(e2.getValue())).multiply(request.getAmount());
		for(OrderItemRequest itemRequest : orderItemRequest){
			itemRequest.setAmount(earnRatio);
		}
		for(OrderItemRequest itemRequest : orderItemRequest){
			System.out.println("Item Amount is : " + itemRequest.getAmount());
		}
		
		//assertEquals(rule.execute(request),  earnRatio);
		
	}
	
}
