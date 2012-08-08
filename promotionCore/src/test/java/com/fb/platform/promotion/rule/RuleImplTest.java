package com.fb.platform.promotion.rule;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.promotion.dao.RuleDao;
import com.fb.platform.promotion.model.OrderDiscount;
import com.fb.platform.promotion.rule.config.RuleConfiguration;
import com.fb.platform.promotion.rule.impl.BuyWorthXGetYPercentOffRuleImpl;
import com.fb.platform.promotion.rule.impl.BuyWorthXGetYRsOffRuleImpl;
import com.fb.platform.promotion.rule.impl.BuyXBrandGetYRsOffOnZProductRuleImpl;
import com.fb.platform.promotion.rule.impl.BuyXQuantityGetVariablePercentOffRuleImpl;
import com.fb.platform.promotion.rule.impl.CategoryBasedVariablePercentOffRuleImpl;
import com.fb.platform.promotion.rule.impl.FirstPurchaseBuyWorthXGetYRsOffRuleImpl;
import com.fb.platform.promotion.rule.impl.MonthlyDiscountRsOffRuleImpl;
import com.fb.platform.promotion.to.OrderItem;
import com.fb.platform.promotion.to.OrderRequest;
import com.fb.platform.promotion.to.Product;
import com.fb.platform.promotion.to.PromotionStatusEnum;


public class RuleImplTest extends BaseTestCase {

	@Autowired
	private RuleDao ruleDao = null;

	OrderRequest orderReq1 = null;
	OrderRequest orderReq2 = null;
	OrderRequest orderReq3 = null;
	OrderRequest orderReq4 = null;
	OrderRequest orderReq5 = null;
	OrderRequest orderReq6 = null;
	OrderRequest orderReq7 = null;
	OrderRequest orderReq8 = null;
	OrderRequest orderReq9 = null;
	OrderRequest orderReq10 = null;
	OrderRequest orderReq11 = null;
	OrderRequest orderReq12 = null;
	OrderRequest orderReq13 = null;
	OrderRequest catMisMatchOrderReq = null;
	OrderRequest clientMisMatchOrderReq = null;
	OrderRequest partCatPartBrandOrderReq = null;
	OrderRequest variablePercentDiscountOrderReq1 = null;
	OrderRequest variablePercentDiscountOrderReq2 = null;
	OrderRequest variablePercentDiscountOrderReq3 = null;
	OrderRequest variablePercentDiscountOrderReq4 = null;

	OrderRequest categoryBasedDiscountOrderReq1 = null;
	OrderRequest categoryBasedDiscountOrderReq2 = null;
	OrderRequest categoryBasedDiscountOrderReq3 = null;
	OrderRequest categoryBasedDiscountOrderReq4 = null;

	int userId;
	boolean isCouponCommitted;
	
	OrderDiscount orderDiscount1;
	OrderDiscount orderDiscount3;
	
	@Before
	public void createSampleData(){
		
		userId=1;
		isCouponCommitted = false;
		
		//Create Products
		Product p1 = new Product();
		p1.setPrice(new BigDecimal(10));
		p1.setCategories(Arrays.asList(1,2));
		p1.setBrands(Arrays.asList(3));
		Product p2 = new Product();
		p2.setPrice(new BigDecimal(50));
		p2.setCategories(Arrays.asList(3,4,5,6,7,8));
		p2.setBrands(Arrays.asList(3));
		Product p3 = new Product();
		p3.setPrice(new BigDecimal(100));
		p3.setCategories(Arrays.asList(9,10));
		p3.setBrands(Arrays.asList(3));
		Product p4 = new Product();
		p4.setPrice(new BigDecimal(500));
		p4.setCategories(Arrays.asList(11,12));
		p4.setBrands(Arrays.asList(6,9));
		Product p5 = new Product();
		p5.setProductId(5);
		p5.setPrice(new BigDecimal(495));
		p5.setCategories(Arrays.asList(13,14));
		p5.setBrands(Arrays.asList(3));
		Product p6 = new Product();
		p6.setPrice(new BigDecimal(1000));
		p6.setCategories(Arrays.asList(15));
		p6.setBrands(Arrays.asList(3));
		Product p7 = new Product();
		p7.setPrice(new BigDecimal(10));
		p7.setCategories(Arrays.asList(1,2));
		p7.setBrands(Arrays.asList(1));
		
		Product catMismatchProd = new Product();
		catMismatchProd.setProductId(5);
		catMismatchProd.setPrice(new BigDecimal(1500));
		catMismatchProd.setCategories(Arrays.asList(101,102,103,104,105));
		catMismatchProd.setBrands(Arrays.asList(3));
		
		Product brandMismatchProd = new Product();
		brandMismatchProd.setProductId(5);
		brandMismatchProd.setPrice(new BigDecimal(1000));
		brandMismatchProd.setCategories(Arrays.asList(6,7,8,9,10,11,12,13,14));
		brandMismatchProd.setBrands(Arrays.asList(51));

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
		OrderItem o5kItem = new OrderItem(); //5000 Rs
		o5kItem.setQuantity(10);
		o5kItem.setProduct(p4);
		OrderItem catMismatchItem = new OrderItem(); //3000 Rs
		catMismatchItem.setQuantity(2);
		catMismatchItem.setProduct(catMismatchProd);
		OrderItem oItem12 = new OrderItem(); //495 Rs
		oItem12.setQuantity(1);
		oItem12.setProduct(p7);
		
		OrderItem brandMismatchItem = new OrderItem(); //2000 Rs
		brandMismatchItem.setQuantity(2);
		brandMismatchItem.setProduct(brandMismatchProd);
		
		OrderItem varDisOrderItem1 = new OrderItem(); //1000 Rs
		varDisOrderItem1.setQuantity(1);
		varDisOrderItem1.setProduct(p6);
		
		OrderItem varDisOrderItem2 = new OrderItem(); //1000 Rs
		varDisOrderItem2.setQuantity(2);
		varDisOrderItem2.setProduct(p6);
		
		OrderItem varDisOrderItem3 = new OrderItem(); //1000 Rs
		varDisOrderItem3.setQuantity(3);
		varDisOrderItem3.setProduct(p6);
		
		// cat based 
		OrderItem catBasedOrderItem1 = new OrderItem(); //1000 Rs
		catBasedOrderItem1.setQuantity(1);
		catBasedOrderItem1.setProduct(p6);
		
		OrderItem catBasedOrderItem2 = new OrderItem(); //1000 Rs
		catBasedOrderItem2.setQuantity(20);
		catBasedOrderItem2.setProduct(p1);
		
		OrderItem catBasedOrderItem3 = new OrderItem(); //1000 Rs
		catBasedOrderItem3.setQuantity(3);
		catBasedOrderItem3.setProduct(p2);


		//Create OrderReq
		orderReq1 = new OrderRequest(); //780 Rs
		orderReq1.setOrderId(1);
		List<OrderItem> oList1 = new ArrayList<OrderItem>();
		oList1.add(oItem1);
		oList1.add(oItem2);
		oList1.add(oItem3);
		orderReq1.setOrderItems(oList1);
		orderReq1.setClientId(5);
		orderReq2 = new OrderRequest(); // 200 Rs
		orderReq2.setOrderId(2);
		List<OrderItem> oList2 = new ArrayList<OrderItem>();
		oList2.add(oItem1);
		oList2.add(oItem5);
		oList2.add(oItem10);
		orderReq2.setOrderItems(oList2);
		orderReq2.setClientId(5);
		orderReq3 = new OrderRequest(); //
		orderReq3.setOrderId(3);
		List<OrderItem> oList3 = new ArrayList<OrderItem>();
		oList3.add(oItem7);
		oList3.add(oItem8);
		oList3.add(oItem9);
		oList3.add(oItem10);
		orderReq3.setOrderItems(oList3);
		orderReq3.setClientId(5);
		orderReq4 = new OrderRequest();
		orderReq4.setOrderId(4);
		List<OrderItem> oList4 = new ArrayList<OrderItem>();
		oList4.add(oItem3);
		oList4.add(oItem6);
		oList4.add(oItem9);
		orderReq4.setOrderItems(oList4);		
		orderReq4.setClientId(5);
		
		orderReq5 = new OrderRequest(); //1275 Rs
		orderReq5.setOrderId(5);
		List<OrderItem> oList5 = new ArrayList<OrderItem>();
		oList5.add(oItem1);
		oList5.add(oItem2);
		oList5.add(oItem3);
		oList5.add(oItem11);
		orderReq5.setOrderItems(oList5);
		orderReq5.setClientId(5);
		
		orderReq6 = new OrderRequest(); //780 Rs
		orderReq6.setOrderId(6);
		List<OrderItem> oList6 = new ArrayList<OrderItem>();
		oList6.add(oItem1);
		oList6.add(oItem2);
		oList6.add(oItem3);
		orderReq6.setOrderItems(oList6);
		orderReq6.setClientId(5);
		
		orderReq7 = new OrderRequest(); //780 Rs
		orderReq7.setOrderId(7);
		List<OrderItem> oList7 = new ArrayList<OrderItem>();
		oList7.add(oItem1);
		oList7.add(oItem11);
		orderReq7.setOrderItems(oList7);
		orderReq7.setClientId(5);
		
		orderReq8 = new OrderRequest(); //780 Rs
		orderReq8.setOrderId(8);
		List<OrderItem> oList8 = new ArrayList<OrderItem>();
		oList8.add(oItem1);
		oList8.add(oItem11);
		oList8.add(oItem8);
		orderReq8.setOrderItems(oList8);
		orderReq8.setClientId(5);
		
		orderReq9 = new OrderRequest(); //
		orderReq9.setOrderId(9);
		List<OrderItem> oList9 = new ArrayList<OrderItem>();
		oList9.add(oItem8);
		orderReq9.setOrderItems(oList9);
		orderReq9.setClientId(5);
		
		catMisMatchOrderReq = new OrderRequest(); // 3000
		catMisMatchOrderReq.setOrderId(10);
		List<OrderItem> oList10 = new ArrayList<OrderItem>();
		oList10.add(catMismatchItem);
		catMisMatchOrderReq.setOrderItems(oList10);
		catMisMatchOrderReq.setClientId(5);

		//Part Cat , Part Brand Use case
		partCatPartBrandOrderReq = new OrderRequest();
		partCatPartBrandOrderReq.setOrderId(11);
		List<OrderItem> oListPart = new ArrayList<OrderItem>();
		oListPart.add(oItem2);
		oListPart.add(oItem1);
		oListPart.add(oItem3);
		oListPart.add(brandMismatchItem);
		oListPart.add(catMismatchItem);
		partCatPartBrandOrderReq.setOrderItems(oListPart);
		partCatPartBrandOrderReq.setClientId(5);
		
		clientMisMatchOrderReq = new OrderRequest(); //
		clientMisMatchOrderReq.setOrderId(12);
		List<OrderItem> oList11 = new ArrayList<OrderItem>();
		oList11.add(oItem8);
		clientMisMatchOrderReq.setOrderItems(oList11);
		clientMisMatchOrderReq.setClientId(17);
		
		variablePercentDiscountOrderReq1 = new OrderRequest(); // Quantity 1 : price 1000
		variablePercentDiscountOrderReq1.setOrderId(13);
		List<OrderItem> oList12 = new ArrayList<OrderItem>();
		oList12.add(varDisOrderItem1);
		variablePercentDiscountOrderReq1.setOrderItems(oList12);
		variablePercentDiscountOrderReq1.setClientId(5);

		variablePercentDiscountOrderReq2 = new OrderRequest(); // Quantity 2 : price 2000
		variablePercentDiscountOrderReq2.setOrderId(14);
		List<OrderItem> oList13 = new ArrayList<OrderItem>();
		oList13.add(varDisOrderItem2);
		variablePercentDiscountOrderReq2.setOrderItems(oList13);
		variablePercentDiscountOrderReq2.setClientId(5);
		
		variablePercentDiscountOrderReq3 = new OrderRequest(); // Quantity 3 : price 3000
		variablePercentDiscountOrderReq3.setOrderId(15);
		List<OrderItem> oList14 = new ArrayList<OrderItem>();
		oList14.add(varDisOrderItem3);
		variablePercentDiscountOrderReq3.setOrderItems(oList14);
		variablePercentDiscountOrderReq3.setClientId(5);
		
		variablePercentDiscountOrderReq4 = new OrderRequest(); // Quantity 6 : price 6000
		variablePercentDiscountOrderReq4.setOrderId(16);
		List<OrderItem> oList15 = new ArrayList<OrderItem>();
		oList15.add(varDisOrderItem1);
		oList15.add(varDisOrderItem2);
		oList15.add(varDisOrderItem3);
		variablePercentDiscountOrderReq4.setOrderItems(oList15);
		variablePercentDiscountOrderReq4.setClientId(5);

		// For Category Based promotion : single coupon Variable discount on diff Cat
		categoryBasedDiscountOrderReq1 = new OrderRequest(); // Quantity 1 : price 1000
		categoryBasedDiscountOrderReq1.setOrderId(20);
		List<OrderItem> categoryBasedList1 = new ArrayList<OrderItem>();
		categoryBasedList1.add(varDisOrderItem1);
		categoryBasedDiscountOrderReq1.setOrderItems(categoryBasedList1);
		categoryBasedDiscountOrderReq1.setClientId(5);

		categoryBasedDiscountOrderReq2 = new OrderRequest(); // Quantity 2 : price 2000
		categoryBasedDiscountOrderReq2.setOrderId(14);
		List<OrderItem> categoryBasedList2 = new ArrayList<OrderItem>();
		categoryBasedList2.add(varDisOrderItem2);
		categoryBasedList2.add(oItem1);
		categoryBasedList2.add(oItem2);
		categoryBasedList2.add(oItem3);
		categoryBasedDiscountOrderReq2.setOrderItems(categoryBasedList2);
		categoryBasedDiscountOrderReq2.setClientId(5);
		
		categoryBasedDiscountOrderReq3 = new OrderRequest(); // Quantity 3 : price 3000
		categoryBasedDiscountOrderReq3.setOrderId(15);
		List<OrderItem> categoryBasedList3 = new ArrayList<OrderItem>();
		categoryBasedList3.add(varDisOrderItem3);
		categoryBasedList3.add(oItem10);
		categoryBasedDiscountOrderReq3.setOrderItems(categoryBasedList3);
		categoryBasedDiscountOrderReq3.setClientId(5);
		
		categoryBasedDiscountOrderReq4 = new OrderRequest(); // Quantity 6 : price 6000
		categoryBasedDiscountOrderReq4.setOrderId(16);
		List<OrderItem> categoryBasedList4 = new ArrayList<OrderItem>();
		categoryBasedList4.add(varDisOrderItem1);
		categoryBasedList4.add(varDisOrderItem2);
		categoryBasedList4.add(varDisOrderItem3);
		categoryBasedDiscountOrderReq4.setOrderItems(categoryBasedList4);
		categoryBasedDiscountOrderReq4.setClientId(5);

		// OrderDiscount Objects
		orderDiscount1 = new OrderDiscount();
		orderDiscount3 = new OrderDiscount();
		orderDiscount1.setOrderRequest(orderReq1);
		orderDiscount3.setOrderRequest(orderReq3);

		orderReq11 = new OrderRequest(); //
		orderReq11.setNoOfTimesInMonth(0);
		orderReq11.setOrderId(18);
		List<OrderItem> oList16 = new ArrayList<OrderItem>();
		oList16.add(oItem1);
		oList16.add(oItem2);
		oList16.add(oItem3);
		orderReq11.setOrderItems(oList16);
		orderReq11.setClientId(5);

		orderReq12 = new OrderRequest(); //
		orderReq12.setOrderId(19);
		List<OrderItem> oList17 = new ArrayList<OrderItem>();
		oList17.add(oItem7);
		oList17.add(oItem8);
		oList17.add(oItem9);
		oList17.add(oItem12);
		orderReq12.setOrderItems(oList17);
		orderReq12.setClientId(5);
		
		orderReq13 = new OrderRequest(); //
		orderReq13.setOrderId(19);
		List<OrderItem> oList18 = new ArrayList<OrderItem>();
		oList18.add(oItem12);
		orderReq13.setOrderItems(oList18);
		orderReq13.setClientId(5);
		
		orderReq13.setNoOfTimesInMonth(2);
	}
	
	@Test
	public void testBuyWorthXGetYRsOffRule() {
		
		//System.out.println("In Test testBuyWorthXGetYRsOffRule ");
		//BuyWorthXGetYRsOffRule
		PromotionRule rule = ruleDao.load(-7, -2);

		assertNotNull(rule);
		assertTrue(rule instanceof BuyWorthXGetYRsOffRuleImpl);
		
		RuleConfiguration ruleConfiguration = ruleDao.loadRuleConfiguration(-7, -2);

		assertNotNull(ruleConfiguration);
		assertEquals(3, ruleConfiguration.getConfigItems().size());

		assertEquals(rule.isApplicable(orderReq1,userId,isCouponCommitted),PromotionStatusEnum.SUCCESS);
		assertEquals(rule.isApplicable(orderReq2,userId,isCouponCommitted),PromotionStatusEnum.LESS_ORDER_AMOUNT);
		assertEquals(rule.isApplicable(orderReq3,userId,isCouponCommitted),PromotionStatusEnum.SUCCESS);;
		assertEquals(rule.isApplicable(orderReq4,userId,isCouponCommitted),PromotionStatusEnum.SUCCESS);
		
		assertEquals(0,new BigDecimal(300).compareTo(rule.execute(orderDiscount1).getOrderDiscountValue()));
		assertEquals(0,new BigDecimal(300).compareTo(rule.execute(orderDiscount3).getOrderDiscountValue()));
		
	}
	
	@Test
	public void testBuyWorthXGetYPercentOffRule() {
		
		//BuyWorthXGetYPercentOffRule
		PromotionRule rule = ruleDao.load(-8, -3);

		assertNotNull(rule);
		assertTrue(rule instanceof BuyWorthXGetYPercentOffRuleImpl);
		
		RuleConfiguration ruleConfiguration = ruleDao.loadRuleConfiguration(-8, -3);

		assertNotNull(ruleConfiguration);

		assertEquals(rule.isApplicable(orderReq1,userId,isCouponCommitted),PromotionStatusEnum.SUCCESS);
		assertEquals(rule.isApplicable(orderReq2,userId,isCouponCommitted),PromotionStatusEnum.LESS_ORDER_AMOUNT);
		assertEquals(rule.isApplicable(orderReq3,userId,isCouponCommitted),PromotionStatusEnum.SUCCESS);
		
		assertEquals(0,new BigDecimal(78).compareTo(rule.execute(orderDiscount1).getOrderDiscountValue()));
		assertEquals(0,new BigDecimal(100).compareTo(rule.execute(orderDiscount3).getOrderDiscountValue()));
		
	}
	
	@Test
	public void testBuyWorthXGetYRsOffOnZCategoryRule() {
		//BuyWorthXGetYRsOffOnZCategoryRuleImpl
		PromotionRule rule = ruleDao.load(-9, -2);

		assertNotNull(rule);
		assertTrue(rule instanceof BuyWorthXGetYRsOffRuleImpl);
		
		RuleConfiguration ruleConfiguration = ruleDao.loadRuleConfiguration(-9, -4);

		assertNotNull(ruleConfiguration);

		assertEquals(rule.isApplicable(orderReq1,userId,isCouponCommitted),PromotionStatusEnum.SUCCESS);
		assertEquals(rule.isApplicable(orderReq2,userId,isCouponCommitted),PromotionStatusEnum.LESS_ORDER_AMOUNT);
		
		assertEquals(0,new BigDecimal(150).compareTo(rule.execute(orderDiscount1).getOrderDiscountValue()));

	}

	@Test
	public void testBuyWorthXGetYRsOffOnZCategoryRuleImpl() {
		
		PromotionRule rule = ruleDao.load(-3005, -3);

		assertNotNull(rule);
		assertTrue(rule instanceof BuyWorthXGetYPercentOffRuleImpl);
		
		RuleConfiguration ruleConfiguration = ruleDao.loadRuleConfiguration(-3005, -6);

		assertNotNull(ruleConfiguration);

		assertEquals(rule.isApplicable(orderReq1,userId,isCouponCommitted),PromotionStatusEnum.SUCCESS);
		assertEquals(rule.isApplicable(orderReq2,userId,isCouponCommitted),PromotionStatusEnum.LESS_ORDER_AMOUNT);
		assertEquals(rule.isApplicable(catMisMatchOrderReq,userId,isCouponCommitted),PromotionStatusEnum.CATEGORY_MISMATCH);
		assertEquals(rule.isApplicable(clientMisMatchOrderReq,userId,isCouponCommitted),PromotionStatusEnum.INVALID_CLIENT);
		
		assertEquals(new BigDecimal(780),orderReq1.getOrderValue());
		
		assertEquals(0,new BigDecimal(195).compareTo(rule.execute(orderDiscount1).getOrderDiscountValue()));

	}
	@Test
	public void testFirstPurchaseBuyWorthXGetYRsOffRule() {
			
		PromotionRule rule = ruleDao.load(-3007, -7);

		assertNotNull(rule);
		assertTrue(rule instanceof FirstPurchaseBuyWorthXGetYRsOffRuleImpl);
		
		RuleConfiguration ruleConfig = ruleDao.loadRuleConfiguration(-3007, -7);

		assertNotNull(ruleConfig);

		assertEquals(rule.isApplicable(orderReq1,1,isCouponCommitted),PromotionStatusEnum.NOT_FIRST_PURCHASE);
		assertEquals(rule.isApplicable(orderReq2,3,isCouponCommitted),PromotionStatusEnum.LESS_ORDER_AMOUNT);
		assertEquals(rule.isApplicable(catMisMatchOrderReq,3,isCouponCommitted),PromotionStatusEnum.CATEGORY_MISMATCH);
		assertEquals(rule.isApplicable(orderReq3,3,isCouponCommitted),PromotionStatusEnum.SUCCESS);
		assertEquals(rule.isApplicable(orderReq3,4,isCouponCommitted),PromotionStatusEnum.SUCCESS);
		assertEquals(rule.isApplicable(orderReq4,2,isCouponCommitted),PromotionStatusEnum.NOT_FIRST_PURCHASE);
		assertEquals(rule.isApplicable(orderReq4,5,isCouponCommitted),PromotionStatusEnum.NOT_FIRST_PURCHASE);
		assertEquals(rule.isApplicable(clientMisMatchOrderReq,3,isCouponCommitted),PromotionStatusEnum.INVALID_CLIENT);
		
		assertEquals(0,new BigDecimal(120).compareTo(rule.execute(orderDiscount1).getOrderDiscountValue()));
		assertEquals(0,new BigDecimal(120).compareTo(rule.execute(orderDiscount3).getOrderDiscountValue()));

	}
	
	@Test
	public void testBuyWorthXGetYPercentOffOnZCategoryRuleImpl() {
		
		//BuyWorthXGetYPercentOffOnZCategoryRuleImpl : Part Cat, Part Brand Use case
		PromotionRule rule = ruleDao.load(-3005, -3);

		assertNotNull(rule);
		assertTrue(rule instanceof BuyWorthXGetYPercentOffRuleImpl);
		
		RuleConfiguration ruleConfiguration = ruleDao.loadRuleConfiguration(-3005, -6);

		assertNotNull(ruleConfiguration);

		assertEquals(rule.isApplicable(orderReq1,userId,isCouponCommitted),PromotionStatusEnum.SUCCESS);
		assertEquals(rule.isApplicable(orderReq2,userId,isCouponCommitted),PromotionStatusEnum.LESS_ORDER_AMOUNT);
		assertEquals(rule.isApplicable(catMisMatchOrderReq,userId,isCouponCommitted),PromotionStatusEnum.CATEGORY_MISMATCH);
		assertEquals(rule.isApplicable(clientMisMatchOrderReq,userId,isCouponCommitted),PromotionStatusEnum.INVALID_CLIENT);
		//Partial Cat
		assertEquals(rule.isApplicable(partCatPartBrandOrderReq,userId,isCouponCommitted),PromotionStatusEnum.SUCCESS);
		
		assertEquals(new BigDecimal(780),orderReq1.getOrderValue());
		
		assertEquals(0,new BigDecimal(195).compareTo(rule.execute(orderDiscount1).getOrderDiscountValue()));
		
		assertEquals(new BigDecimal(5780),partCatPartBrandOrderReq.getOrderValue());
		OrderDiscount partCatPartBrandOrderReqDiscout = new OrderDiscount();
		partCatPartBrandOrderReqDiscout.setOrderRequest(partCatPartBrandOrderReq);
		assertEquals(0,new BigDecimal(195).compareTo(rule.execute(partCatPartBrandOrderReqDiscout).getOrderDiscountValue()));

		
	}
	
	@Test
	public void testBuyXQuantityVariableDiscountPercentOffRuleImpl() {
		
		//BuyXQuantityVariableDiscountPercentOffRuleImpl : Part Cat, Part Brand Use case
		PromotionRule rule = ruleDao.load(-3008, -8);

		assertNotNull(rule);
		assertTrue(rule instanceof BuyXQuantityGetVariablePercentOffRuleImpl);
		
		RuleConfiguration ruleConfiguration = ruleDao.loadRuleConfiguration(-3008, -8);

		assertNotNull(ruleConfiguration);

		assertEquals(rule.isApplicable(orderReq1,userId,isCouponCommitted),PromotionStatusEnum.SUCCESS);
		assertEquals(rule.isApplicable(orderReq2,userId,isCouponCommitted),PromotionStatusEnum.LESS_ORDER_AMOUNT);
		assertEquals(rule.isApplicable(catMisMatchOrderReq,userId,isCouponCommitted),PromotionStatusEnum.CATEGORY_MISMATCH);
		assertEquals(rule.isApplicable(clientMisMatchOrderReq,userId,isCouponCommitted),PromotionStatusEnum.INVALID_CLIENT);
		//Partial Categry
		assertEquals(rule.isApplicable(partCatPartBrandOrderReq,userId,isCouponCommitted),PromotionStatusEnum.SUCCESS);
		
		assertEquals(new BigDecimal(780),orderReq1.getOrderValue());
		
		assertEquals(0,new BigDecimal(117.00).compareTo(rule.execute(orderDiscount1).getOrderDiscountValue()));
		
		assertEquals(new BigDecimal(5780),partCatPartBrandOrderReq.getOrderValue());
		OrderDiscount partCatPartBrandOrderReqDiscout = new OrderDiscount();
		partCatPartBrandOrderReqDiscout.setOrderRequest(partCatPartBrandOrderReq);
		assertEquals(0,new BigDecimal(117.0).compareTo(rule.execute(partCatPartBrandOrderReqDiscout).getOrderDiscountValue()));

		assertEquals(rule.isApplicable(variablePercentDiscountOrderReq1,userId,isCouponCommitted),PromotionStatusEnum.SUCCESS);
		assertEquals(rule.isApplicable(variablePercentDiscountOrderReq2,userId,isCouponCommitted),PromotionStatusEnum.SUCCESS);
		assertEquals(rule.isApplicable(variablePercentDiscountOrderReq3,userId,isCouponCommitted),PromotionStatusEnum.SUCCESS);
		
		assertEquals(new BigDecimal(1000),variablePercentDiscountOrderReq1.getOrderValue());
		assertEquals(new BigDecimal(2000),variablePercentDiscountOrderReq2.getOrderValue());
		assertEquals(new BigDecimal(3000),variablePercentDiscountOrderReq3.getOrderValue());
		assertEquals(new BigDecimal(6000),variablePercentDiscountOrderReq4.getOrderValue());
		
		//Quantity = 1, Percent Off = 5
		OrderDiscount variablePercentDiscountOrderReqDiscout1 = new OrderDiscount();
		variablePercentDiscountOrderReqDiscout1.setOrderRequest(variablePercentDiscountOrderReq1);
		assertEquals(0,new BigDecimal(50).compareTo(rule.execute(variablePercentDiscountOrderReqDiscout1).getOrderDiscountValue()));

		//Quantity = 2, Percent Off = 10
		OrderDiscount variablePercentDiscountOrderReqDiscout2 = new OrderDiscount();
		variablePercentDiscountOrderReqDiscout2.setOrderRequest(variablePercentDiscountOrderReq2);
		assertEquals(0,new BigDecimal(200).compareTo(rule.execute(variablePercentDiscountOrderReqDiscout2).getOrderDiscountValue()));

		// Quantity = 3, Percent Off = 15
		OrderDiscount variablePercentDiscountOrderReqDiscout3 = new OrderDiscount();
		variablePercentDiscountOrderReqDiscout3.setOrderRequest(variablePercentDiscountOrderReq3);
		assertEquals(0,new BigDecimal(450).compareTo(rule.execute(variablePercentDiscountOrderReqDiscout3).getOrderDiscountValue()));
		
		// Quantity = 6, Percent Off = 15
		OrderDiscount variablePercentDiscountOrderReqDiscout4 = new OrderDiscount();
		variablePercentDiscountOrderReqDiscout4.setOrderRequest(variablePercentDiscountOrderReq4);
		assertEquals(0,new BigDecimal(900).compareTo(rule.execute(variablePercentDiscountOrderReqDiscout4).getOrderDiscountValue()));
		
	}
	
//	@Test(expected = PlatformException.class)
//	public void BuyXQuantityVariableDiscountPercentOffRuleImplInvalidInput(){
//		
//	}

	
	@Test(expected=NotImplementedException.class)
	public void buyXBrandGetYRsOffOnZProductRuleTest() {
		
		int userId=1;
		boolean isCouponCommitted = false;
		
		//BuyXBrandGetYRsOffOnZProductRuleImpl
		PromotionRule buyXBrandGetYRsOffOnZProductRule = ruleDao.load(-3004, -5);

		assertNotNull(buyXBrandGetYRsOffOnZProductRule);
		assertTrue(buyXBrandGetYRsOffOnZProductRule instanceof BuyXBrandGetYRsOffOnZProductRuleImpl);
		
		RuleConfiguration buyXBrandGetYRsOffOnZProductRuleImplRuleConfiguration = ruleDao.loadRuleConfiguration(-3004, -5);

		assertNotNull(buyXBrandGetYRsOffOnZProductRuleImplRuleConfiguration);
		assertEquals(5, buyXBrandGetYRsOffOnZProductRuleImplRuleConfiguration.getConfigItems().size());
		assertEquals("700", buyXBrandGetYRsOffOnZProductRuleImplRuleConfiguration.getConfigItem("MIN_ORDER_VALUE").getValue());

		assertEquals(buyXBrandGetYRsOffOnZProductRule.isApplicable(orderReq5,userId,isCouponCommitted),PromotionStatusEnum.SUCCESS);
		assertEquals(buyXBrandGetYRsOffOnZProductRule.isApplicable(orderReq6,userId,isCouponCommitted),PromotionStatusEnum.PRODUCT_NOT_PRESENT);
		assertEquals(buyXBrandGetYRsOffOnZProductRule.isApplicable(orderReq7,userId,isCouponCommitted),PromotionStatusEnum.LESS_ORDER_AMOUNT_OF_BRAND_PRODUCTS);
		assertEquals(buyXBrandGetYRsOffOnZProductRule.isApplicable(orderReq8,userId,isCouponCommitted),PromotionStatusEnum.LESS_ORDER_AMOUNT_OF_BRAND_PRODUCTS);
		assertEquals(buyXBrandGetYRsOffOnZProductRule.isApplicable(orderReq9,userId,isCouponCommitted),PromotionStatusEnum.BRAND_MISMATCH);
		
		OrderDiscount orderDiscount5 = new OrderDiscount();
		orderDiscount5.setOrderRequest(orderReq5);
		assertEquals(0,new BigDecimal(445).compareTo(buyXBrandGetYRsOffOnZProductRule.execute(orderDiscount5).getOrderDiscountValue()));
	}

	@Test
	public void testCategoryBasedVariablePercentOffRuleTest() {
		
		int userId=1;
		boolean isCouponCommitted = false;
		
		//CategoryBasedVariablePercentOffRule
		PromotionRule rule = ruleDao.load(-3010, -10);

		assertNotNull(rule);
		assertTrue(rule instanceof CategoryBasedVariablePercentOffRuleImpl);
		
		RuleConfiguration ruleConfiguration = ruleDao.loadRuleConfiguration(-3010, -10);

		assertNotNull(ruleConfiguration);

		assertEquals(rule.isApplicable(orderReq1,userId,isCouponCommitted),PromotionStatusEnum.SUCCESS);
		assertEquals(rule.isApplicable(orderReq2,userId,isCouponCommitted),PromotionStatusEnum.LESS_ORDER_AMOUNT);
//		assertEquals(rule.isApplicable(catMisMatchOrderReq,userId,isCouponCommitted),PromotionStatusEnum.CATEGORY_MISMATCH);
		assertEquals(rule.isApplicable(clientMisMatchOrderReq,userId,isCouponCommitted),PromotionStatusEnum.INVALID_CLIENT);
		//Partial Category
		assertEquals(rule.isApplicable(partCatPartBrandOrderReq,userId,isCouponCommitted),PromotionStatusEnum.SUCCESS);
		
		assertEquals(new BigDecimal(780),orderReq1.getOrderValue());
		
//		assertEquals(0,new BigDecimal(117.00).compareTo(rule.execute(orderDiscount1).getOrderDiscountValue()));
		
		assertEquals(new BigDecimal(5780),partCatPartBrandOrderReq.getOrderValue());
		OrderDiscount partCatPartBrandOrderReqDiscout = new OrderDiscount();
		partCatPartBrandOrderReqDiscout.setOrderRequest(partCatPartBrandOrderReq);
//		assertEquals(0,new BigDecimal(117.0).compareTo(rule.execute(partCatPartBrandOrderReqDiscout).getOrderDiscountValue()));

		assertEquals(rule.isApplicable(categoryBasedDiscountOrderReq1,userId,isCouponCommitted),PromotionStatusEnum.SUCCESS);
		assertEquals(rule.isApplicable(categoryBasedDiscountOrderReq2,userId,isCouponCommitted),PromotionStatusEnum.SUCCESS);
		assertEquals(rule.isApplicable(categoryBasedDiscountOrderReq3,userId,isCouponCommitted),PromotionStatusEnum.SUCCESS);
		
		assertEquals(new BigDecimal(1000),categoryBasedDiscountOrderReq1.getOrderValue());
		assertEquals(new BigDecimal(2780),categoryBasedDiscountOrderReq2.getOrderValue());
		assertEquals(new BigDecimal(3150),categoryBasedDiscountOrderReq3.getOrderValue());
		assertEquals(new BigDecimal(6000),categoryBasedDiscountOrderReq4.getOrderValue());
		
		/* Test 1
		 * Cat = 15 Prod = 1, Value = 1000, disc = 18%
		 * Total Disc = 180  
		 */
		 
		OrderDiscount catBasedDiscountOrderReqDiscount1 = new OrderDiscount();
		catBasedDiscountOrderReqDiscount1.setOrderRequest(categoryBasedDiscountOrderReq1);
		OrderDiscount returnDiscount1 = new OrderDiscount();
		returnDiscount1 = rule.execute(catBasedDiscountOrderReqDiscount1);
		assertEquals(0, returnDiscount1.getOrderDiscountValue().compareTo(new BigDecimal(180)));
//		printOrderDiscount(returnDiscount1);
		
		/* Test 2 
		 * Order Details
		 *  Order Product Value : 1000*2
			Order Cat List [15] 
			Exp OrderItem Disc : 360
			
			Order Product Value : 10*3
			Order Cat List [1, 2] 
			Exp : OrderItem Disc : 2
			
			Order Product Value : 50*5
			Order Cat List [3, 4, 5, 6, 7, 8] 
			Exp OrderItem Disc : 25
			
			Order Product Value : 100*5
			Order Cat List [9, 10] 
			Exp OrderItem Disc : 75
			
			Total : 461.5
		 */
		OrderDiscount catBasedDiscountOrderReqDiscount2 = new OrderDiscount();
		catBasedDiscountOrderReqDiscount2.setOrderRequest(categoryBasedDiscountOrderReq2);
		OrderDiscount returnDiscount2 = new OrderDiscount();
		returnDiscount2 = rule.execute(catBasedDiscountOrderReqDiscount2);
		assertEquals(0, returnDiscount2.getOrderDiscountValue().compareTo(new BigDecimal(461.5)));
//		printOrderDiscount(returnDiscount2);
		
		/* Test 3
		 * Order Details
		 * Order Product Value : 1000*3
			Order Cat List [15] 
			OrderItem Disc : 540
			
			Order Product Value : 50*3
			Order Cat List [3, 4, 5, 6, 7, 8] 
			OrderItem Disc : 15
			
			Total = 555

		 */
		OrderDiscount catBasedDiscountOrderReqDiscount3 = new OrderDiscount();
		catBasedDiscountOrderReqDiscount3.setOrderRequest(categoryBasedDiscountOrderReq3);
		OrderDiscount returnDiscount3 = new OrderDiscount();
		returnDiscount3 = rule.execute(catBasedDiscountOrderReqDiscount3);
		assertEquals(0, returnDiscount3.getOrderDiscountValue().compareTo(new BigDecimal(555)));
//		printOrderDiscount(returnDiscount3);
		
		/* Test 4 
		 * Order Details 
		 * Order Product Value : 1000*1
			Order Cat List [15] 
			OrderItem Disc : 180
			
			Order Product Value : 1000*2
			Order Cat List [15] 
			OrderItem Disc : 360
			
			Order Product Value : 1000*3
			Order Cat List [15] 
			OrderItem Disc : 540

			Total = 1080
		 */
			OrderDiscount catBasedDiscountOrderReqDiscount4 = new OrderDiscount();
		catBasedDiscountOrderReqDiscount4.setOrderRequest(categoryBasedDiscountOrderReq4);
		OrderDiscount returnDiscount4 = new OrderDiscount();
		returnDiscount4 = rule.execute(catBasedDiscountOrderReqDiscount4);
		assertEquals(0, returnDiscount4.getOrderDiscountValue().compareTo(new BigDecimal(1080)));
//		printOrderDiscount(returnDiscount4);
//
//		//Quantity = 2, Percent Off = 10
//		OrderDiscount variablePercentDiscountOrderReqDiscout2 = new OrderDiscount();
//		variablePercentDiscountOrderReqDiscout2.setOrderRequest(variablePercentDiscountOrderReq2);
//		assertEquals(0,new BigDecimal(200).compareTo(rule.execute(variablePercentDiscountOrderReqDiscout2).getOrderDiscountValue()));
//
//		// Quantity = 3, Percent Off = 15
//		OrderDiscount variablePercentDiscountOrderReqDiscout3 = new OrderDiscount();
//		variablePercentDiscountOrderReqDiscout3.setOrderRequest(variablePercentDiscountOrderReq3);
//		assertEquals(0,new BigDecimal(450).compareTo(rule.execute(variablePercentDiscountOrderReqDiscout3).getOrderDiscountValue()));
//		
//		// Quantity = 6, Percent Off = 15
//		OrderDiscount variablePercentDiscountOrderReqDiscout4 = new OrderDiscount();
//		variablePercentDiscountOrderReqDiscout4.setOrderRequest(variablePercentDiscountOrderReq4);
//		assertEquals(0,new BigDecimal(900).compareTo(rule.execute(variablePercentDiscountOrderReqDiscout4).getOrderDiscountValue()));
//		
	}
	
	private void printOrderDiscount(OrderDiscount orderDisc) {
		System.out.println("\n\nTotal Order Disc = " + orderDisc.getOrderDiscountValue());
		System.out.println("\nOrder Req : \n");
		for(OrderItem orderItem : orderDisc.getOrderRequest().getOrderItems()) {
			System.out.println("\nOrder Product Value : " + orderItem.getProductPrice() + "*"+ orderItem.getQuantity() + "\nOrder Cat List " + orderItem.getProduct().getCategories().toString() + " \nOrderItem Disc : " + orderItem.getTotalDiscount());
		}
	}

	@Test
	public void testMonthlyDiscountRsOffRule() {
			
		int userId=1;
		PromotionRule monthlyDiscountRsOffRule = ruleDao.load(-5001, -9);
			
		assertNotNull(monthlyDiscountRsOffRule);
		assertTrue(monthlyDiscountRsOffRule instanceof MonthlyDiscountRsOffRuleImpl);
			
		RuleConfiguration monthlyDiscountRsOffRuleConfig = ruleDao.loadRuleConfiguration(-5001, -9);
		
		assertNotNull(monthlyDiscountRsOffRuleConfig);
		

		assertEquals(monthlyDiscountRsOffRule.isApplicable(orderReq11,userId,isCouponCommitted),PromotionStatusEnum.BRAND_MISMATCH );
		assertEquals(monthlyDiscountRsOffRule.isApplicable(catMisMatchOrderReq,userId,isCouponCommitted),PromotionStatusEnum.CATEGORY_MISMATCH);

		assertEquals(monthlyDiscountRsOffRule.isApplicable(orderReq12,2,isCouponCommitted),PromotionStatusEnum.SUCCESS);

		assertEquals(monthlyDiscountRsOffRule.isApplicable(orderReq13,userId,isCouponCommitted),PromotionStatusEnum.NUMBER_OF_MONTHLY_USE_EXCEEDED);
		
		//assertEquals(monthlyDiscountRsOffRule.isApplicable(clientMisMatchOrderReq,userId,isCouponCommitted),PromotionStatusEnum.INVALID_CLIENT);
		
	//	assertEquals(0,new BigDecimal(120).compareTo(monthlyDiscountRsOffRule.execute(orderDiscount1).getOrderDiscountValue()));
	//	assertEquals(0,new BigDecimal(120).compareTo(monthlyDiscountRsOffRule.execute(orderDiscount3).getOrderDiscountValue()));
	
	}

	@Test
	public void testMonthlyDiscountRsOffRuleTwoTimes() {
			
		int userId=1;
		PromotionRule monthlyDiscountRsOffRule = ruleDao.load(-5002, -9);
			
		assertNotNull(monthlyDiscountRsOffRule);
		assertTrue(monthlyDiscountRsOffRule instanceof MonthlyDiscountRsOffRuleImpl);
			
		RuleConfiguration monthlyDiscountRsOffRuleConfig = ruleDao.loadRuleConfiguration(-5002, -9);
		
		assertNotNull(monthlyDiscountRsOffRuleConfig);
		
		assertEquals(monthlyDiscountRsOffRule.isApplicable(orderReq11,userId,isCouponCommitted),PromotionStatusEnum.BRAND_MISMATCH );
		assertEquals(monthlyDiscountRsOffRule.isApplicable(catMisMatchOrderReq,userId,isCouponCommitted),PromotionStatusEnum.CATEGORY_MISMATCH);

		//assertEquals(monthlyDiscountRsOffRule.isApplicable(orderReq12,2,isCouponCommitted),PromotionStatusEnum.SUCCESS);

		//assertEquals(monthlyDiscountRsOffRule.isApplicable(orderReq13,userId,isCouponCommitted),PromotionStatusEnum.NUMBER_OF_MONTHLY_USE_EXCEEDED);
		
		//assertEquals(monthlyDiscountRsOffRule.isApplicable(clientMisMatchOrderReq,userId,isCouponCommitted),PromotionStatusEnum.INVALID_CLIENT);
		
	//	assertEquals(0,new BigDecimal(120).compareTo(monthlyDiscountRsOffRule.execute(orderDiscount1).getOrderDiscountValue()));
	//	assertEquals(0,new BigDecimal(120).compareTo(monthlyDiscountRsOffRule.execute(orderDiscount3).getOrderDiscountValue()));
	
	}

	
}

