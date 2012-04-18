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
import com.fb.platform.promotion.to.ApplyCouponRequest;
import com.fb.platform.promotion.to.ApplyCouponResponse;
import com.fb.platform.promotion.to.ApplyCouponResponseStatusEnum;
import com.fb.platform.promotion.to.ApplyScratchCardRequest;
import com.fb.platform.promotion.to.ApplyScratchCardResponse;
import com.fb.platform.promotion.to.ApplyScratchCardStatus;
import com.fb.platform.promotion.to.ClearCacheEnum;
import com.fb.platform.promotion.to.ClearCouponCacheResponse;
import com.fb.platform.promotion.to.ClearPromotionCacheRequest;
import com.fb.platform.promotion.to.ClearPromotionCacheResponse;
import com.fb.platform.promotion.to.CommitCouponRequest;
import com.fb.platform.promotion.to.CommitCouponResponse;
import com.fb.platform.promotion.to.CommitCouponStatusEnum;
import com.fb.platform.promotion.to.OrderItem;
import com.fb.platform.promotion.to.OrderRequest;
import com.fb.platform.promotion.to.Product;
import com.fb.platform.promotion.to.ReleaseCouponRequest;
import com.fb.platform.promotion.to.ReleaseCouponResponse;
import com.fb.platform.promotion.to.ReleaseCouponStatusEnum;
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
	public void testApplyCoupon(){
		
		ApplyCouponRequest couponRequest = new ApplyCouponRequest();
		couponRequest.setOrderReq(getSampleOrderRequest(639));
		couponRequest.setCouponCode("END2END_GLOBAL");
		couponRequest.setSessionToken(responseUser1.getSessionToken());
		couponRequest.setIsOrderCommitted(false);
		
		ApplyCouponResponse couponResponse = promotionManager.applyCoupon(couponRequest);
		
		assertNotNull(couponResponse);
		assertEquals(couponResponse.getCouponStatus(), ApplyCouponResponseStatusEnum.TOTAL_MAX_AMOUNT_PER_USER_EXCEEDED);
		assertNotNull(couponResponse.getSessionToken());
		assertEquals(0, new BigDecimal(120).compareTo(couponResponse.getDiscountValue()));
		assertTrue((couponResponse.getPromoName()).equals("End to End Test Promotion 1"));
		assertTrue(couponResponse.getPromoDescription().equals("end to end promo 1"));
		assertTrue(couponResponse.getStatusMessage().equals(ApplyCouponResponseStatusEnum.TOTAL_MAX_AMOUNT_EXCEEDED.getMesage()));
	}
	
	@Test
	public void testCommitCoupon(){
		
		CommitCouponResponse commitCouponResponse = placeOrder(responseUser1.getSessionToken());
		
		assertNotNull(commitCouponResponse);
		assertEquals(commitCouponResponse.getCommitCouponStatus(), CommitCouponStatusEnum.SUCCESS);
	}
	
	@Test
	public void testApplyCouponCommittedCoupon(){
		
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		
		ApplyCouponRequest couponRequest = new ApplyCouponRequest();
		couponRequest.setOrderReq(getSampleOrderRequest(6390));
		couponRequest.setCouponCode("GLOBAL_COUPON_4444");
		couponRequest.setSessionToken(response.getSessionToken());
		couponRequest.setIsOrderCommitted(false);
		
		ApplyCouponResponse couponResponse = promotionManager.applyCoupon(couponRequest);
		
		assertNotNull(couponResponse);
		assertEquals(couponResponse.getCouponStatus(), ApplyCouponResponseStatusEnum.SUCCESS);
		assertNotNull(couponResponse.getSessionToken());
		assertEquals(0, new BigDecimal(150).compareTo(couponResponse.getDiscountValue()));
		
		CommitCouponRequest commitCouponRequest = new CommitCouponRequest();
		commitCouponRequest.setCouponCode("GLOBAL_COUPON_4444");
		commitCouponRequest.setDiscountValue(new BigDecimal(150));
		commitCouponRequest.setSessionToken(couponResponse.getSessionToken());
		commitCouponRequest.setOrderId(-4444);
		
		CommitCouponResponse commitCouponResponse = promotionManager.commitCouponUse(commitCouponRequest);
		assertNotNull(couponResponse);
		assertEquals(couponResponse.getCouponStatus(), ApplyCouponResponseStatusEnum.SUCCESS);
		
		ApplyCouponRequest reCouponRequest = new ApplyCouponRequest();
		OrderRequest newOrderRequest = getSampleOrderRequest(90);
		newOrderRequest.setOrderId(-4444);
		reCouponRequest.setOrderReq(newOrderRequest);
		reCouponRequest.setCouponCode("GLOBAL_COUPON_4444");
		reCouponRequest.setSessionToken(response.getSessionToken());
		reCouponRequest.setIsOrderCommitted(true);
		
		ApplyCouponResponse reCouponResponse = promotionManager.applyCoupon(reCouponRequest);
		assertNotNull(couponResponse);
		assertEquals(couponResponse.getCouponStatus(), ApplyCouponResponseStatusEnum.SUCCESS);
		assertEquals(0, new BigDecimal(13.5).compareTo(reCouponResponse.getDiscountValue()));
	}	
	
	/*@Test
	public void testMultipleReleaseCoupon(){
		
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		
		for(int i=0; i<2;i++){
			ApplyCouponRequest couponRequest = new ApplyCouponRequest();
			couponRequest.setOrderReq(getSampleOrderRequest(6390));
			couponRequest.setCouponCode("GLOBAL_COUPON_4448");
			couponRequest.setSessionToken(response.getSessionToken());
			couponRequest.setIsOrderCommitted(false);
			
			ApplyCouponResponse couponResponse = promotionManager.applyCoupon(couponRequest);
			
			assertNotNull(couponResponse);
			assertEquals(couponResponse.getCouponStatus(), ApplyCouponResponseStatusEnum.SUCCESS);
			assertNotNull(couponResponse.getSessionToken());
			assertEquals(0, new BigDecimal(150).compareTo(couponResponse.getDiscountValue()));
			
			CommitCouponRequest commitCouponRequest = new CommitCouponRequest();
			commitCouponRequest.setCouponCode("GLOBAL_COUPON_4448");
			commitCouponRequest.setDiscountValue(new BigDecimal(150));
			commitCouponRequest.setSessionToken(couponResponse.getSessionToken());
			commitCouponRequest.setOrderId(-4448);
			
			CommitCouponResponse commitCouponResponse = promotionManager.commitCouponUse(commitCouponRequest);
			assertNotNull(commitCouponResponse);
			assertEquals(commitCouponResponse.getCommitCouponStatus(), CommitCouponStatusEnum.SUCCESS);
			
			ReleaseCouponRequest releaseCoupon = new ReleaseCouponRequest();
			releaseCoupon.setOrderId(-4448);
			releaseCoupon.setCouponCode("GLOBAL_COUPON_4448");
			releaseCoupon.setSessionToken(response.getSessionToken());
			
			ReleaseCouponResponse releaseCouponResponse = promotionManager.releaseCoupon(releaseCoupon);
			assertNotNull(releaseCouponResponse);
			assertEquals(releaseCouponResponse.getReleaseCouponStatus(), ReleaseCouponStatusEnum.SUCCESS);
		}
		
	}	*/
	
	@Test
	public void testCouponMaxUsesPerUserLimit(){
		CommitCouponResponse commitCouponResponse = null;
		for(int i=0; i>5; i++){
			commitCouponResponse = placeOrder(responseUser1.getSessionToken());
			assertNotNull(commitCouponResponse);
			assertEquals(commitCouponResponse.getCommitCouponStatus(), CommitCouponStatusEnum.SUCCESS);
		}
		
		commitCouponResponse = placeOrder(responseUser1.getSessionToken());
		
		assertNotNull(commitCouponResponse);
		assertEquals(commitCouponResponse.getCommitCouponStatus(), CommitCouponStatusEnum.SUCCESS);
	}
	
	@Test
	public void testClearCache() {
		AuthenticationTO authentication = authenticationService.authenticate(responseUser1.getSessionToken());
		int userId = authentication.getUserID();
		
		ClearPromotionCacheRequest clearPromotionCacheRequest = new ClearPromotionCacheRequest();
		clearPromotionCacheRequest.setPromotionId(-2000);
		clearPromotionCacheRequest.setSessionToken(responseUser1.getSessionToken());
		
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
	
	@Test
	public void testApplyScratchCard() {
		ApplyScratchCardRequest applyScratchCardRequest = new ApplyScratchCardRequest();
		applyScratchCardRequest.setCardNumber("NG2202BMJ");
		applyScratchCardRequest.setSessionToken(responseUser1.getSessionToken());
		ApplyScratchCardResponse applyScratchCardResponse = promotionManager.applyScratchCard(applyScratchCardRequest);
		
		//User 1 has a confirmed order.
		//The scratch card is active.
		//The user is not eligible exception should be thrown because it is not his first order.
		assertEquals(ApplyScratchCardStatus.NOT_FIRST_ORDER, applyScratchCardResponse.getApplyScratchCardStatus());
		
		applyScratchCardRequest.setCardNumber("NG1987BMJ");
		applyScratchCardRequest.setSessionToken(responseUser2.getSessionToken());
		applyScratchCardResponse = promotionManager.applyScratchCard(applyScratchCardRequest);
		
		//User 2 has orders in orders_order table but are in cancelled state.
		//The scratch card is inactive.
		//The user is eligible for the scratch card.
		assertEquals(ApplyScratchCardStatus.INVALID_SCRATCH_CARD, applyScratchCardResponse.getApplyScratchCardStatus());
		
		applyScratchCardRequest.setCardNumber("NG2287BMJ");
		applyScratchCardRequest.setSessionToken(responseUser2.getSessionToken());
		applyScratchCardResponse = promotionManager.applyScratchCard(applyScratchCardRequest);
		
		//User 2 has orders in orders_order table but are in cancelled state.
		//The scratch card is active.
		//The user is eligible for the scratch card.
		assertEquals(ApplyScratchCardStatus.SUCCESS, applyScratchCardResponse.getApplyScratchCardStatus());
		
		applyScratchCardRequest.setCardNumber("NG0287BMJ");
		applyScratchCardRequest.setSessionToken(responseUser6.getSessionToken());
		applyScratchCardResponse = promotionManager.applyScratchCard(applyScratchCardRequest);
		
		//User 6 is a new user and has no order in orders_order table.
		//The scratch card is active.
		//The user is eligible for the scratch card.
		assertEquals(ApplyScratchCardStatus.SUCCESS, applyScratchCardResponse.getApplyScratchCardStatus());
		
		applyScratchCardRequest.setCardNumber("NG2908BMJ");
		applyScratchCardRequest.setSessionToken(responseUser6.getSessionToken());
		applyScratchCardResponse = promotionManager.applyScratchCard(applyScratchCardRequest);
		
		//User 6 is a new user and has no order in orders_order table.
		//The scratch card is active.
		//A scratch card has already been given to this user.
		//The user is not eligible for the scratch card.
		assertEquals(ApplyScratchCardStatus.COUPON_ALREADY_ASSIGNED_TO_USER, applyScratchCardResponse.getApplyScratchCardStatus());
		
		applyScratchCardRequest.setCardNumber("NG2910BMJ");
		applyScratchCardRequest.setSessionToken(responseUser6.getSessionToken());
		applyScratchCardResponse = promotionManager.applyScratchCard(applyScratchCardRequest);
		
		//User 6 is a new user and has no order in orders_order table.
		//The scratch card is active.
		//The user already has a scratch card issued of a different store.
		//The user is eligible for the scratch card.
		assertEquals(ApplyScratchCardStatus.SUCCESS, applyScratchCardResponse.getApplyScratchCardStatus());
		
		applyScratchCardRequest.setCardNumber("VK0105BMJ");
		applyScratchCardRequest.setSessionToken(responseUser6.getSessionToken());
		applyScratchCardResponse = promotionManager.applyScratchCard(applyScratchCardRequest);
		
		//User 6 is a new user and has no order in orders_order table.
		//The scratch card is active.
		//The user already has a scratch card issued of a different store.
		//The user is eligible for the scratch card.
		assertEquals(ApplyScratchCardStatus.INVALID_SCRATCH_CARD, applyScratchCardResponse.getApplyScratchCardStatus());
		
		
	}

	private CommitCouponResponse placeOrder(String sessionToken) {
		ApplyCouponRequest couponRequest = new ApplyCouponRequest();
		couponRequest.setOrderReq(getSampleOrderRequest(639));
		couponRequest.setCouponCode("END2END_POST_ISSUE");
		couponRequest.setSessionToken(sessionToken);
		couponRequest.setIsOrderCommitted(false);
		
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
	
	private OrderRequest getSampleOrderRequest(int totalPrice){
		//Create Products
				Product p1 = new Product();
				p1.setPrice(new BigDecimal(totalPrice));
				p1.setCategories(Arrays.asList(1,2,3,4,5));
				p1.setBrands(Arrays.asList(1,2,3));
				
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
				orderReq1.setClientId(5);
				
				return orderReq1;
				
	}
}
