package com.fb.platform.promotion.rule;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.commons.to.Money;
import com.fb.platform.promotion.dao.RuleDao;
import com.fb.platform.promotion.rule.impl.BuyWorthXGetYPercentOffRuleImpl;
import com.fb.platform.promotion.rule.impl.BuyWorthXGetYRsOffOnZCategoryRuleImpl;
import com.fb.platform.promotion.rule.impl.BuyWorthXGetYRsOffRuleImpl;
import com.fb.platform.promotion.rule.impl.BuyXBrandGetYRsOffOnZProductRuleImpl;
import com.fb.platform.promotion.to.OrderItem;
import com.fb.platform.promotion.to.OrderRequest;
import com.fb.platform.promotion.to.Product;
import com.fb.platform.promotion.to.PromotionStatusEnum;


public class RuleImplTest extends BaseTestCase {

	@Autowired
	private RuleDao ruleDao = null;

	@Test
	public void testRules() {
		
		//Create Products
		Product p1 = new Product();
		p1.setPrice(new BigDecimal(10));
		p1.setCategories(Arrays.asList(1,2,3,4,5));
		p1.setBrands(Arrays.asList(3));
		Product p2 = new Product();
		p2.setPrice(new BigDecimal(50));
		p2.setCategories(Arrays.asList(2,3,4,5,6,7,8,9,10));
		p2.setBrands(Arrays.asList(3));
		Product p3 = new Product();
		p3.setPrice(new BigDecimal(100));
		p3.setCategories(Arrays.asList(6,7,8,9,10));
		p3.setBrands(Arrays.asList(3));
		Product p4 = new Product();
		p4.setPrice(new BigDecimal(500));
		p4.setCategories(Arrays.asList(6,7,8,9,10,11,12,13,14));
		p4.setBrands(Arrays.asList(6,9));
		Product p5 = new Product();
		p5.setProductId(5);
		p5.setPrice(new BigDecimal(495));
		p5.setCategories(Arrays.asList(6,7,8,9,10,11,12,13,14));
		p5.setBrands(Arrays.asList(3));

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
		OrderItem oItem11 = new OrderItem(); //495 Rs
		oItem11.setQuantity(1);
		oItem11.setProduct(p5);

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
		
		OrderRequest orderReq5 = new OrderRequest(); //1275 Rs
		orderReq5.setOrderId(5);
		List<OrderItem> oList5 = new ArrayList<OrderItem>();
		oList5.add(oItem1);
		oList5.add(oItem2);
		oList5.add(oItem3);
		oList5.add(oItem11);
		orderReq5.setOrderItems(oList5);
		orderReq5.setClientId(5);
		
		OrderRequest orderReq6 = new OrderRequest(); //780 Rs
		orderReq5.setOrderId(6);
		List<OrderItem> oList6 = new ArrayList<OrderItem>();
		oList6.add(oItem1);
		oList6.add(oItem2);
		oList6.add(oItem3);
		orderReq6.setOrderItems(oList6);
		orderReq6.setClientId(5);
		
		OrderRequest orderReq7 = new OrderRequest(); //780 Rs
		orderReq5.setOrderId(7);
		List<OrderItem> oList7 = new ArrayList<OrderItem>();
		oList7.add(oItem1);
		oList7.add(oItem11);
		orderReq7.setOrderItems(oList7);
		orderReq7.setClientId(5);
		
		OrderRequest orderReq8 = new OrderRequest(); //780 Rs
		orderReq5.setOrderId(8);
		List<OrderItem> oList8 = new ArrayList<OrderItem>();
		oList8.add(oItem1);
		oList8.add(oItem11);
		oList8.add(oItem8);
		orderReq8.setOrderItems(oList8);
		orderReq8.setClientId(5);
		
		OrderRequest orderReq9 = new OrderRequest(); //
		orderReq9.setOrderId(9);
		List<OrderItem> oList9 = new ArrayList<OrderItem>();
		oList9.add(oItem8);
		orderReq9.setOrderItems(oList9);
		orderReq9.setClientId(5);
		
		//BuyWorthXGetYRsOffRule
		PromotionRule buyWorthXGetYRsOffRule = ruleDao.load(-7, -2);

		assertNotNull(buyWorthXGetYRsOffRule);
		assertTrue(buyWorthXGetYRsOffRule instanceof BuyWorthXGetYRsOffRuleImpl);
		
		RuleConfiguration yRsOffRuleConfiguration = ruleDao.loadRuleConfiguration(-7, -2);

		assertNotNull(yRsOffRuleConfiguration);
		assertEquals(3, yRsOffRuleConfiguration.getConfigItems().size());

		assertEquals(buyWorthXGetYRsOffRule.isApplicable(orderReq1),PromotionStatusEnum.SUCCESS);
		assertEquals(buyWorthXGetYRsOffRule.isApplicable(orderReq2),PromotionStatusEnum.LESS_ORDER_AMOUNT);
		assertEquals(buyWorthXGetYRsOffRule.isApplicable(orderReq3),PromotionStatusEnum.SUCCESS);;
		assertEquals(buyWorthXGetYRsOffRule.isApplicable(orderReq4),PromotionStatusEnum.SUCCESS);
		
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

		assertEquals(buyWorthXGetYPercentOffRule.isApplicable(orderReq1),PromotionStatusEnum.SUCCESS);
		assertEquals(buyWorthXGetYPercentOffRule.isApplicable(orderReq2),PromotionStatusEnum.LESS_ORDER_AMOUNT);
		assertEquals(buyWorthXGetYPercentOffRule.isApplicable(orderReq3),PromotionStatusEnum.SUCCESS);
		
		assertTrue(new Money(new BigDecimal(78)).eq(buyWorthXGetYPercentOffRule.execute(orderReq1)));
//		assertTrue(new Money(new BigDecimal(100)).eq(buyWorthXGetYPercentOffRule.execute(orderReq2)));
		assertTrue(new Money(new BigDecimal(100)).eq(buyWorthXGetYPercentOffRule.execute(orderReq3)));
		
//		BuyWorthXGetYRsOffOnZCategoryRuleImpl
//		PromotionRule buyWorthXGetYRsOffOnZCategoryRule = ruleDao.load(-9, -4);
//
//		assertNotNull(buyWorthXGetYRsOffOnZCategoryRule);
//		assertTrue(buyWorthXGetYRsOffOnZCategoryRule instanceof BuyWorthXGetYRsOffRuleImpl);
//		
//		RuleConfiguration yRsOffOnZCategoryRuleConfiguration = ruleDao.loadRuleConfiguration(-9, -4);
//
//		assertNotNull(yRsOffOnZCategoryRuleConfiguration);
//		assertEquals(4, yRsOffOnZCategoryRuleConfiguration.getConfigItems().size());
//
//		assertEquals(buyWorthXGetYRsOffOnZCategoryRule.isApplicable(orderReq1),PromotionStatusEnum.SUCCESS);
//		assertEquals(buyWorthXGetYRsOffOnZCategoryRule.isApplicable(orderReq2),PromotionStatusEnum.LESS_ORDER_AMOUNT);
//		
//		assertTrue(new Money(new BigDecimal(150)).eq(buyWorthXGetYRsOffOnZCategoryRule.execute(orderReq1)));
		
		
		
		//BuyXBrandGetYRsOffOnZProductRuleImpl
		PromotionRule buyXBrandGetYRsOffOnZProductRule = ruleDao.load(-3004, -5);

		assertNotNull(buyXBrandGetYRsOffOnZProductRule);
		assertTrue(buyXBrandGetYRsOffOnZProductRule instanceof BuyXBrandGetYRsOffOnZProductRuleImpl);
		
		RuleConfiguration buyXBrandGetYRsOffOnZProductRuleImplRuleConfiguration = ruleDao.loadRuleConfiguration(-3004, -5);

		assertNotNull(buyXBrandGetYRsOffOnZProductRuleImplRuleConfiguration);
		assertEquals(5, buyXBrandGetYRsOffOnZProductRuleImplRuleConfiguration.getConfigItems().size());
		assertEquals("700", buyXBrandGetYRsOffOnZProductRuleImplRuleConfiguration.getConfigItem("MIN_ORDER_VALUE").getValue());

		assertEquals(buyXBrandGetYRsOffOnZProductRule.isApplicable(orderReq5),PromotionStatusEnum.SUCCESS);
		assertEquals(buyXBrandGetYRsOffOnZProductRule.isApplicable(orderReq6),PromotionStatusEnum.PRODUCT_NOT_PRESENT);
		assertEquals(buyXBrandGetYRsOffOnZProductRule.isApplicable(orderReq7),PromotionStatusEnum.LESS_ORDER_AMOUNT);
		assertEquals(buyXBrandGetYRsOffOnZProductRule.isApplicable(orderReq8),PromotionStatusEnum.LESS_ORDER_AMOUNT_OF_BRAND_PRODUCTS);
		assertEquals(buyXBrandGetYRsOffOnZProductRule.isApplicable(orderReq9),PromotionStatusEnum.BRAND_MISMATCH);
		
		assertTrue(new Money(new BigDecimal(445)).eq(buyXBrandGetYRsOffOnZProductRule.execute(orderReq5)));

		
		
	}

}
