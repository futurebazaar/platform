package com.fb.platform.promotion.rule;


import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.promotion.dao.RuleDao;
import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.RuleConfiguration;
import com.fb.platform.promotion.rule.impl.BuyWorthXGetYRsOffRuleImpl;
import com.fb.platform.promotion.rule.impl.BuyXGetYFreeRuleImpl;
import com.fb.platform.promotion.to.OrderItem;
import com.fb.platform.promotion.to.OrderRequest;
import com.fb.platform.promotion.to.Product;


public class RuleImpTest extends BaseTestCase {

	@Autowired
	private RuleDao ruleDao = null;

	@Test
	public void load() {
		//load rule
		PromotionRule rule = ruleDao.load(-7, -2);

		assertNotNull(rule);
		assertTrue(rule instanceof BuyWorthXGetYRsOffRuleImpl);
		
		//load config
		RuleConfiguration ruleConfiguration = ruleDao.loadRuleConfiguration(-7, -2);

		assertNotNull(ruleConfiguration);
		assertEquals(2, ruleConfiguration.getConfigItems().size());
		
		
		//Create Products
		Product p1 = new Product();
		p1.setPrice(new BigDecimal(100));
		p1.setCategories(Arrays.asList(1,2,3,4,5));
		p1.setBrands(Arrays.asList(1,2,3));
		Product p2 = new Product();
		p2.setPrice(new BigDecimal(200));
		p2.setCategories(Arrays.asList(2,3,4,5,6,7,8,9,10));
		p2.setBrands(Arrays.asList(2,3,4,5));
		Product p3 = new Product();
		p3.setPrice(new BigDecimal(300));
		p3.setCategories(Arrays.asList(6,7,8,9,10));
		p3.setBrands(Arrays.asList(4,5,6));
		Product p4 = new Product();
		p4.setPrice(new BigDecimal(400));
		p4.setCategories(Arrays.asList(6,7,8,9,10,11,12,13,14));
		p4.setBrands(Arrays.asList(3,6,9));

		//Create OrderItems
		OrderItem oItem1 = new OrderItem();
		oItem1.setQuantity(3);
		oItem1.setProduct(p1);
		OrderItem oItem2 = new OrderItem();
		oItem1.setQuantity(5);
		oItem1.setProduct(p2);
		OrderItem oItem3 = new OrderItem();
		oItem1.setQuantity(10);
		oItem1.setProduct(p3);
		OrderItem oItem4 = new OrderItem();
		oItem1.setQuantity(1);
		oItem1.setProduct(p4);
		OrderItem oItem5 = new OrderItem();
		oItem1.setQuantity(2);
		oItem1.setProduct(p1);
		OrderItem oItem6 = new OrderItem();
		oItem1.setQuantity(8);
		oItem1.setProduct(p2);
		OrderItem oItem7 = new OrderItem();
		oItem1.setQuantity(5);
		oItem1.setProduct(p3);
		OrderItem oItem8 = new OrderItem();
		oItem1.setQuantity(4);
		oItem1.setProduct(p4);
		OrderItem oItem9 = new OrderItem();
		oItem1.setQuantity(10);
		oItem1.setProduct(p3);
		OrderItem oItem10 = new OrderItem();
		oItem1.setQuantity(3);
		oItem1.setProduct(p2);

		//Create OrderReq
		OrderRequest orderReq1 = new OrderRequest();
		orderReq1.setOrderId(1);
		orderReq1.setOrderValue(new BigDecimal(500));
		List<OrderItem> oList1 = new ArrayList<OrderItem>();
		oList1.add(oItem1);
		oList1.add(oItem2);
		oList1.add(oItem3);
		orderReq1.setOrderItems(oList1);
		OrderRequest orderReq2 = new OrderRequest();
		orderReq1.setOrderId(2);
		orderReq1.setOrderValue(new BigDecimal(1000));
		List<OrderItem> oList2 = new ArrayList<OrderItem>();
		oList1.add(oItem4);
		oList1.add(oItem5);
		oList1.add(oItem6);
		orderReq1.setOrderItems(oList2);
		OrderRequest orderReq3 = new OrderRequest();
		orderReq1.setOrderId(3);
		orderReq1.setOrderValue(new BigDecimal(1500));
		List<OrderItem> oList3 = new ArrayList<OrderItem>();
		oList3.add(oItem7);
		oList3.add(oItem8);
		oList3.add(oItem9);
		oList3.add(oItem10);
		orderReq1.setOrderItems(oList3);
		
		
	}

}
