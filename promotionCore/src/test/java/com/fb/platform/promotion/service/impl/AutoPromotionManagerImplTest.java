/**
 * 
 */
package com.fb.platform.promotion.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.auth.AuthenticationService;
import com.fb.platform.promotion.product.to.ApplyAutoPromotionRequest;
import com.fb.platform.promotion.product.to.ApplyAutoPromotionResponse;
import com.fb.platform.promotion.product.to.ApplyAutoPromotionResponseStatusEnum;
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
		p2.setPrice(new BigDecimal(700));
		p2.setMrpPrice(new BigDecimal(1200));
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
}
