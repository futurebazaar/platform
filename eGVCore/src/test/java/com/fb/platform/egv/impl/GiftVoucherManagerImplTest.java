package com.fb.platform.egv.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.auth.AuthenticationService;
import com.fb.platform.egv.service.GiftVoucherManager;
import com.fb.platform.egv.service.GiftVoucherService;
import com.fb.platform.egv.to.ActivateRequest;
import com.fb.platform.egv.to.ActivateResponse;
import com.fb.platform.egv.to.ActivateResponseStatusEnum;
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
import com.fb.platform.egv.to.RollbackUseRequest;
import com.fb.platform.egv.to.RollbackUseResponse;
import com.fb.platform.egv.to.RollbackUseResponseStatusEnum;
import com.fb.platform.egv.to.SendPinRequest;
import com.fb.platform.egv.to.SendPinResponse;
import com.fb.platform.egv.to.SendPinResponseStatusEnum;
import com.fb.platform.egv.to.UseRequest;
import com.fb.platform.egv.to.UseResponse;
import com.fb.platform.egv.to.UseResponseStatusEnum;
import com.fb.platform.user.manager.interfaces.UserManager;
import com.fb.platform.user.manager.model.auth.LoginRequest;
import com.fb.platform.user.manager.model.auth.LoginResponse;

public class GiftVoucherManagerImplTest extends BaseTestCase {

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

	long newEGVNum;

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
	public void testGetGiftVoucherInfo() {

		GetInfoRequest getGiftVoucherInfoRequest = new GetInfoRequest();
		getGiftVoucherInfoRequest.setGiftVoucherNumber(-12345678901L);
		getGiftVoucherInfoRequest.setSessionToken(responseUser1.getSessionToken());

		GetInfoResponse getGiftVoucherInfoResponse = giftVoucherManager.getInfo(getGiftVoucherInfoRequest);

		assertNotNull(getGiftVoucherInfoResponse);
		assertNotNull(getGiftVoucherInfoResponse.getSessionToken());
		assertEquals(GetInfoResponseStatusEnum.SUCCESS, getGiftVoucherInfoResponse.getResponseStatus());
		assertEquals(0, new BigDecimal(1000).compareTo(getGiftVoucherInfoResponse.getAmount()));
	}

	@Test
	public void testCreateGiftVoucherWithEmail() {
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
		assertEquals(CreateResponseStatusEnum.SUCCESS, createGiftVoucherResponse.getResponseStatus());

	}

	@Test
	public void testCreateGiftVoucherWithMobile() {
		CreateRequest createGiftVoucherRequest = new CreateRequest();
		createGiftVoucherRequest.setMobile("917498459473");
		createGiftVoucherRequest.setOrderItemId(1);
		createGiftVoucherRequest.setAmount(new BigDecimal(1000.00));
		createGiftVoucherRequest.setSessionToken(responseUser1.getSessionToken());
		createGiftVoucherRequest.setSenderName("Keith Fernandez");
		createGiftVoucherRequest.setReceiverName("Zishaan");

		CreateResponse createGiftVoucherResponse = giftVoucherManager.create(createGiftVoucherRequest);

		assertNotNull(createGiftVoucherResponse);
		assertNotNull(createGiftVoucherResponse.getSessionToken());
		assertEquals(CreateResponseStatusEnum.SUCCESS, createGiftVoucherResponse.getResponseStatus());

	}

	@Test
	public void testCreateGiftVoucherWithInvalidMobile() {
		CreateRequest createGiftVoucherRequest = new CreateRequest();
		createGiftVoucherRequest.setMobile("91749845947");
		createGiftVoucherRequest.setOrderItemId(1);
		createGiftVoucherRequest.setAmount(new BigDecimal(1000.00));
		createGiftVoucherRequest.setSessionToken(responseUser1.getSessionToken());
		createGiftVoucherRequest.setSenderName("Keith Fernandez");
		createGiftVoucherRequest.setReceiverName("Zishaan");

		CreateResponse createGiftVoucherResponse = giftVoucherManager.create(createGiftVoucherRequest);

		assertNotNull(createGiftVoucherResponse);
		assertNotNull(createGiftVoucherResponse.getSessionToken());
		assertEquals(CreateResponseStatusEnum.SUCCESS, createGiftVoucherResponse.getResponseStatus());

	}

	@Test
	public void testCreateGiftVoucherWithEmailAndMobile() {
		CreateRequest createGiftVoucherRequest = new CreateRequest();
		createGiftVoucherRequest.setEmail("keith.fernandez@futuregroup.in");
		createGiftVoucherRequest.setMobile("917498459473");
		createGiftVoucherRequest.setOrderItemId(1);
		createGiftVoucherRequest.setAmount(new BigDecimal(1000.00));
		createGiftVoucherRequest.setSessionToken(responseUser1.getSessionToken());
		createGiftVoucherRequest.setSenderName("Keith Fernandez");
		createGiftVoucherRequest.setReceiverName("Zishaan");

		CreateResponse createGiftVoucherResponse = giftVoucherManager.create(createGiftVoucherRequest);

		assertNotNull(createGiftVoucherResponse);
		assertNotNull(createGiftVoucherResponse.getSessionToken());
		assertEquals(CreateResponseStatusEnum.SUCCESS, createGiftVoucherResponse.getResponseStatus());

	}

	@Test
	public void testApplyGiftVoucher() {
		ApplyRequest applyGiftVoucherRequest = new ApplyRequest();
		applyGiftVoucherRequest.setGiftVoucherNumber(-12345678901L);
		applyGiftVoucherRequest.setGiftVoucherPin("12345");
		applyGiftVoucherRequest.setSessionToken(responseUser1.getSessionToken());

		ApplyResponse applyGiftVoucherResponse = giftVoucherManager.apply(applyGiftVoucherRequest);

		assertNotNull(applyGiftVoucherResponse);
		assertNotNull(applyGiftVoucherResponse.getSessionToken());
		assertEquals(ApplyResponseStatusEnum.SUCCESS, applyGiftVoucherResponse.getResponseStatus());

	}

	@Test
	public void testUseGiftVoucher() {
		UseRequest useGiftVoucherRequest = new UseRequest();
		useGiftVoucherRequest.setGiftVoucherNumber(-12345678901L);
		useGiftVoucherRequest.setOrderId(-1);
		useGiftVoucherRequest.setAmount(new BigDecimal(1000.00));
		useGiftVoucherRequest.setSessionToken(responseUser1.getSessionToken());

		UseResponse useGiftVoucherResponse = giftVoucherManager.use(useGiftVoucherRequest);

		assertNotNull(useGiftVoucherResponse);
		assertNotNull(useGiftVoucherResponse.getSessionToken());
		assertEquals(UseResponseStatusEnum.SUCCESS, useGiftVoucherResponse.getResponseStatus());
	}

	@Test
	public void testRollbackUseGiftVoucher() {
		RollbackUseRequest rollbackUseGiftVoucherRequest = new RollbackUseRequest();
		rollbackUseGiftVoucherRequest.setGiftVoucherNumber(-12345678923L);
		rollbackUseGiftVoucherRequest.setOrderId(-7);
		rollbackUseGiftVoucherRequest.setSessionToken(responseUser1.getSessionToken());

		RollbackUseResponse rollbackUseGiftVoucherResponse = giftVoucherManager
				.rollbackUse(rollbackUseGiftVoucherRequest);

		assertNotNull(rollbackUseGiftVoucherResponse);
		assertNotNull(rollbackUseGiftVoucherResponse.getSessionToken());
		assertEquals(RollbackUseResponseStatusEnum.SUCCESS, rollbackUseGiftVoucherResponse.getResponseStatus());

	}

	@Test
	public void testCancelGiftVoucher() {
		CancelRequest cancelGiftVoucherRequest = new CancelRequest();
		cancelGiftVoucherRequest.setGiftVoucherNumber(-12345678901L);
		cancelGiftVoucherRequest.setOrderItemId(-1);
		cancelGiftVoucherRequest.setSessionToken(responseUser1.getSessionToken());

		CancelResponse cancelGiftVoucherResponse = giftVoucherManager.cancel(cancelGiftVoucherRequest);

		assertNotNull(cancelGiftVoucherResponse);
		assertNotNull(cancelGiftVoucherResponse.getSessionToken());
		assertEquals(CancelResponseStatusEnum.SUCCESS, cancelGiftVoucherResponse.getResponseStatus());

	}

	@Test
	public void testCreateGiftVoucherWithMobileAsInactive() {
		CreateRequest createGiftVoucherRequest = new CreateRequest();
		createGiftVoucherRequest.setMobile("917498459473");
		createGiftVoucherRequest.setOrderItemId(1);
		createGiftVoucherRequest.setAmount(new BigDecimal(1000.00));
		createGiftVoucherRequest.setSessionToken(responseUser1.getSessionToken());
		createGiftVoucherRequest.setSenderName("Keith Fernandez");
		createGiftVoucherRequest.setReceiverName("Zishaan");
		createGiftVoucherRequest.setDeferActivation(true);
		DateTime validTill = new DateTime(2012, 9, 1, 0, 0, 0);
		createGiftVoucherRequest.setValidTill(validTill);

		CreateResponse createGiftVoucherResponse = giftVoucherManager.create(createGiftVoucherRequest);

		assertNotNull(createGiftVoucherResponse);
		assertNotNull(createGiftVoucherResponse.getSessionToken());
		assertEquals(CreateResponseStatusEnum.SUCCESS, createGiftVoucherResponse.getResponseStatus());
		assertEquals(0, createGiftVoucherResponse.getValidTill().compareTo(validTill));
		newEGVNum = createGiftVoucherResponse.getGvNumber();

	}

	@Test
	public void testActivateGiftVoucherWithEmailAndMobile() {

		ActivateRequest activateGiftVoucherRequest = new ActivateRequest();
		activateGiftVoucherRequest.setAmount(new BigDecimal(1000.00));
		activateGiftVoucherRequest.setSessionToken(responseUser1.getSessionToken());
		DateTime newValidTill = new DateTime(2012, 10, 1, 0, 0, 0);
		activateGiftVoucherRequest.setValidTill(newValidTill);
		activateGiftVoucherRequest.setGiftVoucherNumber(-12345678924L);

		ActivateResponse activateGiftVoucherResponse = giftVoucherManager.activate(activateGiftVoucherRequest);

		assertNotNull(activateGiftVoucherResponse);
		assertNotNull(activateGiftVoucherResponse.getSessionToken());
		assertEquals(ActivateResponseStatusEnum.SUCCESS, activateGiftVoucherResponse.getResponseStatus());

	}

	@Test
	public void testSendPinGiftVoucherWithEmailAndMobile() {

		SendPinRequest sendPinGiftVoucherRequest = new SendPinRequest();
		sendPinGiftVoucherRequest.setSessionToken(responseUser1.getSessionToken());
		sendPinGiftVoucherRequest.setGiftVoucherNumber(-12345678924L);
		sendPinGiftVoucherRequest.setMobile("917498459473");
		sendPinGiftVoucherRequest.setEmail("keith.fernandez@futuregroup.in");
		sendPinGiftVoucherRequest.setMobile("917498459473");
		sendPinGiftVoucherRequest.setSenderName("KEITH FB");
		sendPinGiftVoucherRequest.setReceiverName("Zishaan");
		sendPinGiftVoucherRequest.setGiftMessage("This is the new Gift Message with egv.number worth egv.amount");

		SendPinResponse sendPinGiftVoucherResponse = giftVoucherManager.sendPin(sendPinGiftVoucherRequest);

		assertNotNull(sendPinGiftVoucherResponse);
		assertNotNull(sendPinGiftVoucherResponse.getSessionToken());
		assertEquals(SendPinResponseStatusEnum.SUCCESS, sendPinGiftVoucherResponse.getResponseStatus());
		assertEquals(-12345678924L, sendPinGiftVoucherResponse.getNumber());

	}

}
