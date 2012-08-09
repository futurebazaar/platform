/**
 * 
 */
package com.fb.platform.promotion.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.auth.AuthenticationService;
import com.fb.platform.promotion.model.Promotion;
import com.fb.platform.promotion.product.model.promotion.AutoPromotion;
import com.fb.platform.promotion.product.to.ApplyAutoPromotionRequest;
import com.fb.platform.promotion.product.to.ApplyAutoPromotionResponse;
import com.fb.platform.promotion.product.to.ApplyAutoPromotionResponseStatusEnum;
import com.fb.platform.promotion.product.to.CommitAutoPromotionRequest;
import com.fb.platform.promotion.product.to.CommitAutoPromotionResponse;
import com.fb.platform.promotion.product.to.CommitAutoPromotionResponseStatusEnum;
import com.fb.platform.promotion.service.AutoPromotionManager;
import com.fb.platform.promotion.service.PromotionManager;
import com.fb.platform.promotion.service.PromotionService;
import com.fb.platform.promotion.to.OrderItem;
import com.fb.platform.promotion.to.OrderRequest;
import com.fb.platform.promotion.to.Product;
import com.fb.platform.user.manager.interfaces.UserManager;
import com.fb.platform.user.manager.model.auth.LoginRequest;
import com.fb.platform.user.manager.model.auth.LoginResponse;

/**
 * @author vinayak
 *
 */
public class AutoPromotionManagerImplTest extends BaseTestCase {

	@Autowired
	private UserManager userManager = null;
	
	@Autowired
	AutoPromotionManager autoPromotionManager = null;
	
	LoginResponse responseUser1 = null;
	
	LoginResponse responseUser2 = null;
	
	LoginResponse responseUser6 = null;

	@Before
	public void loginUser1() {
		
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		responseUser1 = userManager.login(request);
		
		request.setUsername("test@test.com");
		request.setPassword("testpass");

		responseUser2 = userManager.login(request);
		
		request.setUsername("neha.garani@gmail.com");
		request.setPassword("testpass");

		responseUser6 = userManager.login(request);
	}

	@Test
	public void applyMultiplePromotions() {
		Product p1 = new Product();
		p1.setPrice(new BigDecimal(700));
		p1.setMrpPrice(new BigDecimal(1200));
		p1.setProductId(133568);

		//Create OrderItems
		OrderItem oItem1 = new OrderItem();
		oItem1.setQuantity(1);
		oItem1.setProduct(p1);

		Product p2 = new Product();
		p2.setPrice(new BigDecimal(600));
		p2.setMrpPrice(new BigDecimal(1100));
		p2.setProductId(92631);

		//Create OrderItems
		OrderItem oItem2 = new OrderItem();
		oItem2.setQuantity(2);
		oItem2.setProduct(p2);

		Product p3 = new Product();
		p3.setPrice(new BigDecimal(400));
		p3.setMrpPrice(new BigDecimal(500));
		p3.setProductId(100);

		//Create OrderItems
		OrderItem oItem3 = new OrderItem();
		oItem3.setQuantity(3);
		oItem3.setProduct(p3);

		Product p4 = new Product();
		p4.setPrice(new BigDecimal(300));
		p4.setMrpPrice(new BigDecimal(350));
		p4.setProductId(200);

		//Create OrderItems
		OrderItem oItem4 = new OrderItem();
		oItem4.setQuantity(2);
		oItem4.setProduct(p4);

		//Create OrderReq
		OrderRequest orderReq1 = new OrderRequest();
		orderReq1.setOrderId(1);
		List<OrderItem> oList1 = new ArrayList<OrderItem>();
		oList1.add(oItem1);
		oList1.add(oItem2);
		oList1.add(oItem3);
		oList1.add(oItem4);
		orderReq1.setOrderItems(oList1);

		ApplyAutoPromotionRequest request = new ApplyAutoPromotionRequest();
		request.setOrderReq(orderReq1);

		ApplyAutoPromotionResponse response = autoPromotionManager.apply(request);

		assertNotNull(response);
		assertEquals(ApplyAutoPromotionResponseStatusEnum.SUCCESS, response.getApplyAutoPromotionStatus());
		assertNotNull(response.getAppliedPromotions());
		List<AutoPromotion> appliedPromotions = response.getAppliedPromotions();
		assertEquals(2, appliedPromotions.size());
		assertEquals(8000, appliedPromotions.get(0).getId());
		assertEquals(8100, appliedPromotions.get(1).getId());
		assertNotNull(response.getOrderDiscount());
	}

	@Test
	public void modification() {
		Product p1 = new Product();
		p1.setPrice(new BigDecimal(700));
		p1.setMrpPrice(new BigDecimal(1200));
		p1.setProductId(133568);

		//Create OrderItems
		OrderItem oItem1 = new OrderItem();
		oItem1.setQuantity(1);
		oItem1.setProduct(p1);

		Product p2 = new Product();
		p2.setPrice(new BigDecimal(600));
		p2.setMrpPrice(new BigDecimal(1100));
		p2.setProductId(92631);

		//Create OrderItems
		OrderItem oItem2 = new OrderItem();
		oItem2.setQuantity(2);
		oItem2.setProduct(p2);

		Product p3 = new Product();
		p3.setPrice(new BigDecimal(400));
		p3.setMrpPrice(new BigDecimal(500));
		p3.setProductId(100);

		//Create OrderItems
		OrderItem oItem3 = new OrderItem();
		oItem3.setQuantity(3);
		oItem3.setProduct(p3);

		Product p4 = new Product();
		p4.setPrice(new BigDecimal(300));
		p4.setMrpPrice(new BigDecimal(350));
		p4.setProductId(200);

		//Create OrderItems
		OrderItem oItem4 = new OrderItem();
		oItem4.setQuantity(2);
		oItem4.setProduct(p4);

		//Create OrderReq
		OrderRequest orderReq1 = new OrderRequest();
		orderReq1.setOrderId(1);
		List<OrderItem> oList1 = new ArrayList<OrderItem>();
		oList1.add(oItem1);
		oList1.add(oItem2);
		oList1.add(oItem3);
		oList1.add(oItem4);
		orderReq1.setOrderItems(oList1);

		ApplyAutoPromotionRequest request = new ApplyAutoPromotionRequest();
		request.setOrderReq(orderReq1);
		List<Integer> appliedPromotionsReqList = new ArrayList<Integer>();
		appliedPromotionsReqList.add(8100);
		request.setAppliedPromotions(appliedPromotionsReqList);

		ApplyAutoPromotionResponse response = autoPromotionManager.apply(request);

		assertNotNull(response);
		assertEquals(ApplyAutoPromotionResponseStatusEnum.SUCCESS, response.getApplyAutoPromotionStatus());
		assertNotNull(response.getAppliedPromotions());
		List<AutoPromotion> appliedPromotions = response.getAppliedPromotions();
		assertEquals(1, appliedPromotions.size());
		assertEquals(8100, appliedPromotions.get(0).getId());
		assertNotNull(response.getOrderDiscount());
	}

	@Test
	public void failedModify() {
		Product p1 = new Product();
		p1.setPrice(new BigDecimal(700));
		p1.setMrpPrice(new BigDecimal(1200));
		p1.setProductId(100);

		//Create OrderItems
		OrderItem oItem1 = new OrderItem();
		oItem1.setQuantity(1);
		oItem1.setProduct(p1);

		//Create OrderReq
		OrderRequest orderReq1 = new OrderRequest();
		orderReq1.setOrderId(1);
		List<OrderItem> oList1 = new ArrayList<OrderItem>();
		oList1.add(oItem1);
		orderReq1.setOrderItems(oList1);

		ApplyAutoPromotionRequest request = new ApplyAutoPromotionRequest();
		request.setSessionToken(responseUser1.getSessionToken());
		request.setOrderReq(orderReq1);
		List<Integer> appliedPromotionsReqList = new ArrayList<Integer>();
		appliedPromotionsReqList.add(8000);
		request.setAppliedPromotions(appliedPromotionsReqList);

		ApplyAutoPromotionResponse response = autoPromotionManager.apply(request);

		assertNotNull(response);
		assertEquals(ApplyAutoPromotionResponseStatusEnum.SUCCESS, response.getApplyAutoPromotionStatus());
		assertNotNull(response.getAppliedPromotions());
		List<AutoPromotion> appliedPromotions = response.getAppliedPromotions();
		assertEquals(1, appliedPromotions.size());
		assertEquals(8000, appliedPromotions.get(0).getId());
		//assertFalse(appliedPromotions.get(0).isApplied());
		assertFalse(response.getAppliedPromotionStatuses().get(8000).booleanValue());
		assertNotNull(response.getOrderDiscount());
	}

	@Test
	public void apply() {
		Product p1 = new Product();
		p1.setPrice(new BigDecimal(700));
		p1.setMrpPrice(new BigDecimal(1200));
		p1.setProductId(100);

		//Create OrderItems
		OrderItem oItem1 = new OrderItem();
		oItem1.setQuantity(3);
		oItem1.setProduct(p1);

		Product p2 = new Product();
		p2.setPrice(new BigDecimal(600));
		p2.setMrpPrice(new BigDecimal(1100));
		p2.setProductId(200);

		//Create OrderItems
		OrderItem oItem2 = new OrderItem();
		oItem2.setQuantity(2);
		oItem2.setProduct(p2);

		//Create OrderReq
		OrderRequest orderReq1 = new OrderRequest();
		orderReq1.setOrderId(1);
		List<OrderItem> oList1 = new ArrayList<OrderItem>();
		oList1.add(oItem1);
		oList1.add(oItem2);
		orderReq1.setOrderItems(oList1);

		ApplyAutoPromotionRequest request = new ApplyAutoPromotionRequest();
		request.setSessionToken(responseUser1.getSessionToken());
		request.setOrderReq(orderReq1);

		ApplyAutoPromotionResponse response = autoPromotionManager.apply(request);

		assertNotNull(response);
		assertEquals(ApplyAutoPromotionResponseStatusEnum.SUCCESS, response.getApplyAutoPromotionStatus());
		assertNotNull(response.getAppliedPromotions());
		assertNotNull(response.getOrderDiscount());
	}

	@Test
	public void applyBuyXGetYFree() {
		Product p1 = new Product();
		p1.setPrice(new BigDecimal(700));
		p1.setMrpPrice(new BigDecimal(1200));
		p1.setProductId(133568);

		//Create OrderItems
		OrderItem oItem1 = new OrderItem();
		oItem1.setQuantity(1);
		oItem1.setProduct(p1);

		Product p2 = new Product();
		p2.setPrice(new BigDecimal(600));
		p2.setMrpPrice(new BigDecimal(1100));
		p2.setProductId(92631);

		//Create OrderItems
		OrderItem oItem2 = new OrderItem();
		oItem2.setQuantity(2);
		oItem2.setProduct(p2);

		//Create OrderReq
		OrderRequest orderReq1 = new OrderRequest();
		orderReq1.setOrderId(1);
		List<OrderItem> oList1 = new ArrayList<OrderItem>();
		oList1.add(oItem1);
		oList1.add(oItem2);
		orderReq1.setOrderItems(oList1);

		ApplyAutoPromotionRequest request = new ApplyAutoPromotionRequest();
		request.setOrderReq(orderReq1);

		ApplyAutoPromotionResponse response = autoPromotionManager.apply(request);

		assertNotNull(response);
		assertEquals(ApplyAutoPromotionResponseStatusEnum.SUCCESS, response.getApplyAutoPromotionStatus());
		assertNotNull(response.getAppliedPromotions());
		List<AutoPromotion> appliedPromotions = response.getAppliedPromotions();
		assertEquals(1, appliedPromotions.size());
		assertEquals(8100, appliedPromotions.get(0).getId());
		assertEquals(1, response.getAppliedPromotionStatuses().size());
		assertTrue(response.getAppliedPromotionStatuses().get(8100).booleanValue());
		assertNotNull(response.getOrderDiscount());
	}
	
	@Test
	public void commit() {
		CommitAutoPromotionRequest request  = new CommitAutoPromotionRequest(); 
		request.setSessionToken(responseUser1.getSessionToken());
		request.setOrderId(123);
		List<Integer> promotionList = new ArrayList<Integer>();
		promotionList.add(100);
		request.setAppliedPromotionsList(promotionList);
		CommitAutoPromotionResponse response = autoPromotionManager.commit(request);
		assertNotNull(response);
		assertEquals(response.getCommitAutoPromotionStatus(), CommitAutoPromotionResponseStatusEnum.SUCCESS);
		
		promotionList = new ArrayList<Integer>();
		promotionList.add(200);
		request.setAppliedPromotionsList(promotionList);
		response = autoPromotionManager.commit(request);
		assertNotNull(response);
		assertEquals(response.getCommitAutoPromotionStatus(), CommitAutoPromotionResponseStatusEnum.SUCCESS);
	}

	@Test
	public void applyBuy2At10Percent() {
		Product p1 = new Product();
		p1.setPrice(new BigDecimal(98));
		p1.setMrpPrice(new BigDecimal(100));
		p1.setProductId(80210);

		//Create OrderItems
		OrderItem oItem1 = new OrderItem();
		oItem1.setQuantity(2);
		oItem1.setProduct(p1);


		//Create OrderReq
		OrderRequest orderReq1 = new OrderRequest();
		orderReq1.setOrderId(1);
		List<OrderItem> oList1 = new ArrayList<OrderItem>();
		oList1.add(oItem1);
		orderReq1.setOrderItems(oList1);

		ApplyAutoPromotionRequest request = new ApplyAutoPromotionRequest();
		request.setOrderReq(orderReq1);

		ApplyAutoPromotionResponse response = autoPromotionManager.apply(request);

		assertNotNull(response);
		assertEquals(ApplyAutoPromotionResponseStatusEnum.SUCCESS, response.getApplyAutoPromotionStatus());
		assertNotNull(response.getAppliedPromotions());
		List<AutoPromotion> appliedPromotions = response.getAppliedPromotions();
		assertEquals(1, appliedPromotions.size());
		assertEquals(8200, appliedPromotions.get(0).getId());
		assertNotNull(response.getOrderDiscount());
	}

	@Test
	public void applyBuy5At20PercentOff() {
		Product p1 = new Product();
		p1.setPrice(new BigDecimal(900));
		p1.setMrpPrice(new BigDecimal(1000));
		p1.setProductId(85985);

		//Create OrderItems
		OrderItem oItem1 = new OrderItem();
		oItem1.setQuantity(2);
		oItem1.setProduct(p1);

		Product p2 = new Product();
		p2.setPrice(new BigDecimal(99));
		p2.setMrpPrice(new BigDecimal(100));
		p2.setProductId(78214);

		//Create OrderItems
		OrderItem oItem2 = new OrderItem();
		oItem2.setQuantity(5);
		oItem2.setProduct(p2);

		//Create OrderReq
		OrderRequest orderReq1 = new OrderRequest();
		orderReq1.setOrderId(1);
		List<OrderItem> oList1 = new ArrayList<OrderItem>();
		oList1.add(oItem1);
		oList1.add(oItem2);
		orderReq1.setOrderItems(oList1);

		ApplyAutoPromotionRequest request = new ApplyAutoPromotionRequest();
		request.setOrderReq(orderReq1);

		ApplyAutoPromotionResponse response = autoPromotionManager.apply(request);

		assertNotNull(response);
		assertEquals(ApplyAutoPromotionResponseStatusEnum.SUCCESS, response.getApplyAutoPromotionStatus());
		assertNotNull(response.getAppliedPromotions());
		List<AutoPromotion> appliedPromotions = response.getAppliedPromotions();
		assertEquals(1, appliedPromotions.size());
		assertEquals(8200, appliedPromotions.get(0).getId());
		assertNotNull(response.getOrderDiscount());
	}
}
