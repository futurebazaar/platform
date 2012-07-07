package com.fb.platform.egv.impl;

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
import com.fb.platform.egv.service.GiftVoucherManager;
import com.fb.platform.egv.service.GiftVoucherService;
import com.fb.platform.egv.to.ApplyRequest;
import com.fb.platform.egv.to.ApplyResponse;
import com.fb.platform.egv.to.ApplyResponseStatusEnum;
import com.fb.platform.egv.to.CancelRequest;
import com.fb.platform.egv.to.CancelResponse;
import com.fb.platform.egv.to.CancelResponseStatusEnum;
import com.fb.platform.egv.to.CreateRequest;
import com.fb.platform.egv.to.CreateResponse;
import com.fb.platform.egv.to.CreateResponseStatusEnum;
import com.fb.platform.egv.to.GetInfoRequest;
import com.fb.platform.egv.to.GetInfoResponse;
import com.fb.platform.egv.to.GetInfoResponseStatusEnum;
import com.fb.platform.egv.to.UseRequest;
import com.fb.platform.egv.to.UseResponse;
import com.fb.platform.egv.to.UseResponseStatusEnum;
import com.fb.platform.user.manager.interfaces.UserManager;
import com.fb.platform.user.manager.model.auth.LoginRequest;
import com.fb.platform.user.manager.model.auth.LoginResponse;

public class GiftVoucherManagerImplTest extends BaseTestCase{

	@Autowired
	private UserManager userManager = null;

	@Autowired
	GiftVoucherManager giftVoucherManager = null;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private GiftVoucherService giftVoucherService = null;
	
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
	public void testGetGiftVoucherInfo(){
		
		GetInfoRequest getGiftVoucherInfoRequest = new GetInfoRequest();
		getGiftVoucherInfoRequest.setGiftVoucherNumber(-12345678901L);
		getGiftVoucherInfoRequest.setSessionToken(responseUser1.getSessionToken());
		
		GetInfoResponse getGiftVoucherInfoResponse = giftVoucherManager.getInfo(getGiftVoucherInfoRequest);
		
		assertNotNull(getGiftVoucherInfoResponse);
		assertNotNull(getGiftVoucherInfoResponse.getSessionToken());
		assertEquals(GetInfoResponseStatusEnum.SUCCESS,getGiftVoucherInfoResponse.getResponseStatus());
		assertEquals(0, new BigDecimal(1000).compareTo(getGiftVoucherInfoResponse.getAmount()));
	}

	@Test
	public void testCreateGiftVoucher(){
		CreateRequest createGiftVoucherRequest = new CreateRequest();
		createGiftVoucherRequest.setEmail("keith.fernandez@futuregroup.in");
		createGiftVoucherRequest.setOrderItemId(1);
		createGiftVoucherRequest.setAmount(new BigDecimal(1000.00));
		createGiftVoucherRequest.setSessionToken(responseUser1.getSessionToken());
		createGiftVoucherRequest.setSenderName("Keith Fernandez");
		createGiftVoucherRequest.setReceiverName("Zishaan");
		
		
		CreateResponse createGiftVoucherResponse = giftVoucherManager.create(createGiftVoucherRequest);
		
		assertNotNull(createGiftVoucherResponse);
		assertNotNull(createGiftVoucherResponse.getSessionToken());
		assertEquals(CreateResponseStatusEnum.SUCCESS,createGiftVoucherResponse.getResponseStatus());
		
	}
	
	@Test
	public void testApplyGiftVoucher(){
		ApplyRequest applyGiftVoucherRequest = new ApplyRequest();
		applyGiftVoucherRequest.setGiftVoucherNumber(-12345678901L);
		applyGiftVoucherRequest.setGiftVoucherPin("12345");
		applyGiftVoucherRequest.setSessionToken(responseUser1.getSessionToken());
		
		ApplyResponse applyGiftVoucherResponse = giftVoucherManager.apply(applyGiftVoucherRequest);
		
		assertNotNull(applyGiftVoucherResponse);
		assertNotNull(applyGiftVoucherResponse.getSessionToken());
		assertEquals(ApplyResponseStatusEnum.SUCCESS,applyGiftVoucherResponse.getResponseStatus());
		
	}

	@Test
	public void testUseGiftVoucher(){
		UseRequest useGiftVoucherRequest = new UseRequest();
		useGiftVoucherRequest.setGiftVoucherNumber(-12345678901L);
		useGiftVoucherRequest.setOrderId(-1);
		useGiftVoucherRequest.setAmount(new BigDecimal(1000.00));
		useGiftVoucherRequest.setSessionToken(responseUser1.getSessionToken());
		
		UseResponse useGiftVoucherResponse = giftVoucherManager.use(useGiftVoucherRequest);
		
		assertNotNull(useGiftVoucherResponse);
		assertNotNull(useGiftVoucherResponse.getSessionToken());
		assertEquals(UseResponseStatusEnum.SUCCESS,useGiftVoucherResponse.getResponseStatus());
		
	}
	
	@Test
	public void testCancelGiftVoucher(){
		CancelRequest cancelGiftVoucherRequest = new CancelRequest();
		cancelGiftVoucherRequest.setGiftVoucherNumber(-12345678901L);
		cancelGiftVoucherRequest.setOrderItemId(-1);
		cancelGiftVoucherRequest.setSessionToken(responseUser1.getSessionToken());
		
		CancelResponse cancelGiftVoucherResponse = giftVoucherManager.cancel(cancelGiftVoucherRequest);
		
		assertNotNull(cancelGiftVoucherResponse);
		assertNotNull(cancelGiftVoucherResponse.getSessionToken());
		assertEquals(CancelResponseStatusEnum.SUCCESS,cancelGiftVoucherResponse.getResponseStatus());
		
	}
	
	}
