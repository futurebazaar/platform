package com.fb.platform.promotion.rule;


import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.commons.to.Money;
import com.fb.platform.promotion.dao.RuleDao;
import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.RuleConfiguration;
import com.fb.platform.promotion.rule.impl.BuyWorthXGetYPercentOffRuleImpl;
import com.fb.platform.promotion.rule.impl.BuyWorthXGetYRsOffOnZCategoryRuleImpl;
import com.fb.platform.promotion.rule.impl.BuyWorthXGetYRsOffRuleImpl;
import com.fb.platform.promotion.rule.impl.BuyXGetYFreeRuleImpl;
import com.fb.platform.promotion.to.OrderItem;
import com.fb.platform.promotion.to.OrderRequest;
import com.fb.platform.promotion.to.Product;


public class RuleImplTest extends BaseTestCase {

	@Autowired
	private RuleDao ruleDao = null;

	@Test
	public void testRules() {
		
		//Create Products
		Product p1 = new Product();
		p1.setPrice(new BigDecimal(10));
		p1.setCategories(Arrays.asList(1,2,3,4,5));
		p1.setBrands(Arrays.asList(1,2,3));
		Product p2 = new Product();
		p2.setPrice(new BigDecimal(50));
		p2.setCategories(Arrays.asList(2,3,4,5,6,7,8,9,10));
		p2.setBrands(Arrays.asList(2,3,4,5));
		Product p3 = new Product();
		p3.setPrice(new BigDecimal(100));
		p3.setCategories(Arrays.asList(6,7,8,9,10));
		p3.setBrands(Arrays.asList(4,5,6));
		Product p4 = new Product();
		p4.setPrice(new BigDecimal(500));
		p4.setCategories(Arrays.asList(6,7,8,9,10,11,12,13,14));
		p4.setBrands(Arrays.asList(3,6,9));

		//Create OrderItems
		OrderItem oItem1 = new OrderItem(); //30 Rs
		oItem1.setQuantity(3);
		oItem1.setProduct(p1);
		OrderItem oItem2 = new OrderItem(); //250 Rs
		oItem2.setQuantity(5);
		oItem2.setProduct(p2);
		OrderItem oItem3 = new OrderItem(); //500 Rs
		oItem3.setQuantity(5);
		oItem3.setProduct(p3);
		OrderItem oItem4 = new OrderItem(); //500 Rs
		oItem4.setQuantity(1);
		oItem4.setProduct(p4);
		OrderItem oItem5 = new OrderItem(); //20 Rs
		oItem5.setQuantity(2);
		oItem5.setProduct(p1);
		OrderItem oItem6 = new OrderItem(); //400 Rs
		oItem6.setQuantity(8);
		oItem6.setProduct(p2);
		OrderItem oItem7 = new OrderItem(); //500 Rs
		oItem7.setQuantity(5);
		oItem7.setProduct(p3);
		OrderItem oItem8 = new OrderItem(); //2000 Rs
		oItem8.setQuantity(4);
		oItem8.setProduct(p4);
		OrderItem oItem9 = new OrderItem(); //300 Rs
		oItem9.setQuantity(10);
		oItem9.setProduct(p3);
		OrderItem oItem10 = new OrderItem(); //150 Rs
		oItem10.setQuantity(3);
		oItem10.setProduct(p2);

		//Create OrderReq
		OrderRequest orderReq1 = new OrderRequest(); //780 Rs
		orderReq1.setOrderId(1);
		List<OrderItem> oList1 = new ArrayList<OrderItem>();
		oList1.add(oItem1);
		oList1.add(oItem2);
		oList1.add(oItem3);
		orderReq1.setOrderItems(oList1);
		orderReq1.setClientId(5);
		OrderRequest orderReq2 = new OrderRequest(); // 200 Rs
		orderReq2.setOrderId(2);
		List<OrderItem> oList2 = new ArrayList<OrderItem>();
		oList2.add(oItem1);
		oList2.add(oItem5);
		oList2.add(oItem10);
		orderReq2.setOrderItems(oList2);
		orderReq2.setClientId(5);
		OrderRequest orderReq3 = new OrderRequest(); //
		orderReq3.setOrderId(3);
		List<OrderItem> oList3 = new ArrayList<OrderItem>();
		oList3.add(oItem7);
		oList3.add(oItem8);
		oList3.add(oItem9);
		oList3.add(oItem10);
		orderReq3.setOrderItems(oList3);
		orderReq3.setClientId(5);
		OrderRequest orderReq4 = new OrderRequest();
		orderReq4.setOrderId(4);
		List<OrderItem> oList4 = new ArrayList<OrderItem>();
		oList4.add(oItem3);
		oList4.add(oItem6);
		oList4.add(oItem9);
		orderReq4.setOrderItems(oList4);		
		orderReq4.setClientId(5);
		
		//BuyWorthXGetYRsOffRule
		PromotionRule buyWorthXGetYRsOffRule = ruleDao.load(-7, -2);

		assertNotNull(buyWorthXGetYRsOffRule);
		assertTrue(buyWorthXGetYRsOffRule instanceof BuyWorthXGetYRsOffRuleImpl);
		
		RuleConfiguration yRsOffRuleConfiguration = ruleDao.loadRuleConfiguration(-7, -2);

		assertNotNull(yRsOffRuleConfiguration);
		assertEquals(3, yRsOffRuleConfiguration.getConfigItems().size());

		assertTrue(buyWorthXGetYRsOffRule.isApplicable(orderReq1));
		assertFalse(buyWorthXGetYRsOffRule.isApplicable(orderReq2));
		assertTrue(buyWorthXGetYRsOffRule.isApplicable(orderReq3));
		assertTrue(buyWorthXGetYRsOffRule.isApplicable(orderReq4));
		
		assertTrue(new Money(new BigDecimal(300)).eq(buyWorthXGetYRsOffRule.execute(orderReq1)));
//		assertTrue(new Money(new BigDecimal(300)).eq(buyWorthXGetYRsOffRule.execute(orderReq2)));
		assertTrue(new Money(new BigDecimal(300)).eq(buyWorthXGetYRsOffRule.execute(orderReq3)));
				
		//BuyWorthXGetYPercentOffRule
		PromotionRule buyWorthXGetYPercentOffRule = ruleDao.load(-8, -3);

		assertNotNull(buyWorthXGetYPercentOffRule);
		assertTrue(buyWorthXGetYPercentOffRule instanceof BuyWorthXGetYPercentOffRuleImpl);
		
		RuleConfiguration yPercentOffRuleConfiguration = ruleDao.loadRuleConfiguration(-8, -3);

		assertNotNull(yPercentOffRuleConfiguration);
		assertEquals(4, yPercentOffRuleConfiguration.getConfigItems().size());

		assertTrue(buyWorthXGetYPercentOffRule.isApplicable(orderReq1));
		assertFalse(buyWorthXGetYPercentOffRule.isApplicable(orderReq2));
		assertTrue(buyWorthXGetYPercentOffRule.isApplicable(orderReq3));
		
		assertTrue(new Money(new BigDecimal(78)).eq(buyWorthXGetYPercentOffRule.execute(orderReq1)));
//		assertTrue(new Money(new BigDecimal(100)).eq(buyWorthXGetYPercentOffRule.execute(orderReq2)));
		assertTrue(new Money(new BigDecimal(100)).eq(buyWorthXGetYPercentOffRule.execute(orderReq3)));
		
//		BuyWorthXGetYRsOffOnZCategoryRuleImpl
		PromotionRule buyWorthXGetYRsOffOnZCategoryRule = ruleDao.load(-9, -4);

		assertNotNull(buyWorthXGetYRsOffOnZCategoryRule);
		assertTrue(buyWorthXGetYRsOffOnZCategoryRule instanceof BuyWorthXGetYRsOffOnZCategoryRuleImpl);
		
		RuleConfiguration yRsOffOnZCategoryRuleConfiguration = ruleDao.loadRuleConfiguration(-9, -4);

		assertNotNull(yRsOffOnZCategoryRuleConfiguration);
		assertEquals(4, yRsOffOnZCategoryRuleConfiguration.getConfigItems().size());

		assertTrue(buyWorthXGetYRsOffOnZCategoryRule.isApplicable(orderReq1));
		assertFalse(buyWorthXGetYRsOffOnZCategoryRule.isApplicable(orderReq2));
		
		assertTrue(new Money(new BigDecimal(150)).eq(buyWorthXGetYRsOffOnZCategoryRule.execute(orderReq1)));
		
		
	}

}
