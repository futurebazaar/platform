package com.fb.platform.promotion.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.auth.AuthenticationService;
import com.fb.platform.auth.AuthenticationTO;
import com.fb.platform.promotion.service.PromotionManager;
import com.fb.platform.promotion.service.PromotionService;
import com.fb.platform.promotion.to.ClearCacheEnum;
import com.fb.platform.promotion.to.ClearCouponCacheRequest;
import com.fb.platform.promotion.to.ClearCouponCacheResponse;
import com.fb.platform.promotion.to.ClearPromotionCacheRequest;
import com.fb.platform.promotion.to.ClearPromotionCacheResponse;
import com.fb.platform.promotion.to.CommitCouponRequest;
import com.fb.platform.promotion.to.CommitCouponResponse;
import com.fb.platform.promotion.to.CommitCouponStatusEnum;
import com.fb.platform.promotion.to.ApplyCouponRequest;
import com.fb.platform.promotion.to.ApplyCouponResponse;
import com.fb.platform.promotion.to.ApplyCouponResponseStatusEnum;
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
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private PromotionService promotionService = null;
	
	LoginResponse response = null;
	
	@Before
	public void login() {
		
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		response = userManager.login(request);
	}
	
	@Test
	public void testApplyCoupon(){
		
		ApplyCouponRequest couponRequest = new ApplyCouponRequest();
		couponRequest.setOrderReq(getSampleOrderRequest());
		couponRequest.setCouponCode("END2END_GLOBAL");
		couponRequest.setSessionToken(response.getSessionToken());
		
		ApplyCouponResponse couponResponse = promotionManager.applyCoupon(couponRequest);
		
		assertNotNull(couponResponse);
		assertEquals(couponResponse.getCouponStatus(), ApplyCouponResponseStatusEnum.SUCCESS);
		assertNotNull(couponResponse.getSessionToken());
		assertEquals(0, new BigDecimal(120).compareTo(couponResponse.getDiscountValue()));
		assertTrue((couponResponse.getPromoName()).equals("End to End Test Promotion 1"));
		assertTrue(couponResponse.getPromoDescription().equals("end to end promo 1"));
		assertTrue(couponResponse.getStatusMessage().equals("SUCCESSFULLY APPLIED"));
	}
	
	@Test
	public void testCommitCoupon(){
		
		CommitCouponResponse commitCouponResponse = placeOrder(response.getSessionToken());
		
		assertNotNull(commitCouponResponse);
		assertEquals(commitCouponResponse.getCommitCouponStatus(), CommitCouponStatusEnum.SUCCESS);
	}
	
	@Test
	public void testCouponMaxUsesPerUserLimit(){
		CommitCouponResponse commitCouponResponse = null;
		for(int i=0; i>5; i++){
			commitCouponResponse = placeOrder(response.getSessionToken());
			assertNotNull(commitCouponResponse);
			assertEquals(commitCouponResponse.getCommitCouponStatus(), CommitCouponStatusEnum.SUCCESS);
		}
		
		commitCouponResponse = placeOrder(response.getSessionToken());
		
		assertNotNull(commitCouponResponse);
		assertEquals(commitCouponResponse.getCommitCouponStatus(), CommitCouponStatusEnum.SUCCESS);
	}
	
	@Test
	public void testClearCache() {
		AuthenticationTO authentication = authenticationService.authenticate(response.getSessionToken());
		int userId = authentication.getUserID();
		
		ClearPromotionCacheRequest clearPromotionCacheRequest = new ClearPromotionCacheRequest();
		clearPromotionCacheRequest.setPromotionId(-2000);
		clearPromotionCacheRequest.setSessionToken(response.getSessionToken());
		
		promotionService.getCoupon("GlobalCoupon1000Off5", userId);
		promotionService.getCoupon("GlobalCoupon1000Off4", userId);
		promotionService.getCoupon("GlobalCoupon1000Off3", userId);
		promotionService.getCoupon("GlobalCoupon1000Off2", userId);
		promotionService.getCoupon("GlobalCoupon1000Off", userId);
		promotionService.getPromotion(-2000);
		
		ClearPromotionCacheResponse clearPromotionCacheResponse = promotionService.clearCache(clearPromotionCacheRequest);
		assertEquals(clearPromotionCacheResponse.getClearCacheEnum(), ClearCacheEnum.SUCCESS);
		
		for(ClearCouponCacheResponse clearCouponCacheResponse: clearPromotionCacheResponse.getClearCouponCacheResponse()) {
			assertEquals(clearCouponCacheResponse.getClearCacheEnum(), ClearCacheEnum.SUCCESS);
		}
	}

	private CommitCouponResponse placeOrder(String sessionToken) {
		ApplyCouponRequest couponRequest = new ApplyCouponRequest();
		couponRequest.setOrderReq(getSampleOrderRequest());
		couponRequest.setCouponCode("END2END_POST_ISSUE");
		couponRequest.setSessionToken(sessionToken);
		
		ApplyCouponResponse couponResponse = promotionManager.applyCoupon(couponRequest);
		
		assertNotNull(couponResponse);
		assertEquals(couponResponse.getCouponStatus(), ApplyCouponResponseStatusEnum.SUCCESS);
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
				orderReq1.setClientId(5);
				
				return orderReq1;
				
	}
}
