package com.fb.platform.promotion.egv.impl;

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
import com.fb.platform.egv.to.CreateGiftVoucherRequest;
import com.fb.platform.egv.to.CreateGiftVoucherResponse;
import com.fb.platform.egv.to.CreateGiftVoucherResponseStatusEnum;
import com.fb.platform.egv.to.GetGiftVoucherInfoRequest;
import com.fb.platform.egv.to.GetGiftVoucherInfoResponse;
import com.fb.platform.egv.to.GetGiftVoucherInfoResponseStatusEnum;
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
		
		GetGiftVoucherInfoRequest getGiftVoucherInfoRequest = new GetGiftVoucherInfoRequest();
		getGiftVoucherInfoRequest.setGiftVoucherNumber(-12345678901L);
		getGiftVoucherInfoRequest.setGiftVoucherPin(12345);
		getGiftVoucherInfoRequest.setSessionToken(responseUser1.getSessionToken());
		
		GetGiftVoucherInfoResponse getGiftVoucherInfoResponse = giftVoucherManager.getGiftVoucherInfo(getGiftVoucherInfoRequest);
		
		assertNotNull(getGiftVoucherInfoResponse);
		assertNotNull(getGiftVoucherInfoResponse.getSessionToken());
		assertEquals(GetGiftVoucherInfoResponseStatusEnum.SUCCESS,getGiftVoucherInfoResponse.getResponseStatus());
		assertEquals(0, new BigDecimal(1000).compareTo(getGiftVoucherInfoResponse.getAmount()));
	}

	@Test
	public void testCreateGiftVoucher(){
		CreateGiftVoucherRequest createGiftVoucherRequest = new CreateGiftVoucherRequest();
		createGiftVoucherRequest.setEmail("try1@gmail.com");
		createGiftVoucherRequest.setOrderItemId(-100);
		createGiftVoucherRequest.setAmount(new BigDecimal(1000.00));
		createGiftVoucherRequest.setSessionToken(responseUser1.getSessionToken());
		
		
		CreateGiftVoucherResponse createGiftVoucherResponse = giftVoucherManager.createGiftVoucher(createGiftVoucherRequest);
		
		assertNotNull(createGiftVoucherResponse);
		assertNotNull(createGiftVoucherResponse.getSessionToken());
		assertEquals(CreateGiftVoucherResponseStatusEnum.SUCCESS,createGiftVoucherResponse.getStatus());
		
	}
}
