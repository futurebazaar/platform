package com.fb.platform.promotion.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.promotion.service.PromotionManager;
import com.fb.platform.promotion.to.CommitCouponRequest;
import com.fb.platform.promotion.to.CommitCouponResponse;
import com.fb.platform.promotion.to.CommitCouponStatusEnum;
import com.fb.platform.promotion.to.CouponRequest;
import com.fb.platform.promotion.to.CouponResponse;
import com.fb.platform.promotion.to.CouponResponseStatusEnum;
import com.fb.platform.promotion.to.OrderItem;
import com.fb.platform.promotion.to.OrderRequest;
import com.fb.platform.promotion.to.Product;
import com.fb.platform.user.manager.interfaces.UserManager;
import com.fb.platform.user.manager.model.auth.LoginRequest;
import com.fb.platform.user.manager.model.auth.LoginResponse;

public class PromotionManagerImplTest extends BaseTestCase{

	@Autowired
	private UserManager userManager = null;
	
	@Autowired
	PromotionManager promotionManager = null;
	
	@Test
	public void testApplyCoupon(){
		
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		
		CouponRequest couponRequest = new CouponRequest();
		couponRequest.setClientId(5);
		couponRequest.setOrderReq(getSampleOrderRequest());
		couponRequest.setCouponCode("END2END_GLOBAL");
		couponRequest.setSessionToken(response.getSessionToken());
		
		CouponResponse couponResponse = promotionManager.applyCoupon(couponRequest);
		
		assertNotNull(couponResponse);
		assertEquals(couponResponse.getCouponStatus(), CouponResponseStatusEnum.SUCCESS);
		assertNotNull(couponResponse.getSessionToken());
		assertEquals(0, new BigDecimal(120).compareTo(couponResponse.getDiscountValue()));
	}
	
	@Test
	public void testCommitCoupon(){
		
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		
		CommitCouponResponse commitCouponResponse = placeOrder(response.getSessionToken());
		
		assertNotNull(commitCouponResponse);
		assertEquals(commitCouponResponse.getCommitCouponStatus(), CommitCouponStatusEnum.SUCCESS);
	}
	
	@Test
	public void testCouponMaxUsesPerUserLimit(){
		
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		CommitCouponResponse commitCouponResponse = null;
		for(int i=0; i>20; i++){
			commitCouponResponse = placeOrder(response.getSessionToken());
		}
		
		commitCouponResponse = placeOrder(response.getSessionToken());
		
		assertNotNull(commitCouponResponse);
		assertEquals(commitCouponResponse.getCommitCouponStatus(), CommitCouponStatusEnum.SUCCESS);
	}

	private CommitCouponResponse placeOrder(String sessionToken) {
		CouponRequest couponRequest = new CouponRequest();
		couponRequest.setClientId(5);
		couponRequest.setOrderReq(getSampleOrderRequest());
		couponRequest.setCouponCode("END2END_POST_ISSUE");
		couponRequest.setSessionToken(sessionToken);
		
		CouponResponse couponResponse = promotionManager.applyCoupon(couponRequest);
		
		assertNotNull(couponResponse);
		assertEquals(couponResponse.getCouponStatus(), CouponResponseStatusEnum.SUCCESS);
		assertNotNull(couponResponse.getSessionToken());
		assertEquals(0, new BigDecimal(50).compareTo(couponResponse.getDiscountValue()));
		
		CommitCouponRequest commitCouponRequest = new CommitCouponRequest();
		commitCouponRequest.setCouponCode("END2END_POST_ISSUE");
		commitCouponRequest.setDiscountValue(new BigDecimal(50));
		commitCouponRequest.setSessionToken(couponResponse.getSessionToken());
		commitCouponRequest.setOrderId(RandomUtils.nextInt());
		
		CommitCouponResponse commitCouponResponse = promotionManager.commitCouponUse(commitCouponRequest);
		return commitCouponResponse;
	}
	
	private OrderRequest getSampleOrderRequest(){
		//Create Products
				Product p1 = new Product();
				p1.setPrice(new BigDecimal(639));
				p1.setCategories(Arrays.asList(1,2,3,4,5));
				p1.setBrands(Arrays.asList(1,2,3));
				
				//Create OrderItems
				OrderItem oItem1 = new OrderItem();
				oItem1.setQuantity(3);
				oItem1.setProduct(p1);

				//Create OrderReq
				OrderRequest orderReq1 = new OrderRequest();
				orderReq1.setOrderId(1);
				List<OrderItem> oList1 = new ArrayList<OrderItem>();
				oList1.add(oItem1);
				orderReq1.setOrderItems(oList1);
				
				return orderReq1;
				
	}
}
