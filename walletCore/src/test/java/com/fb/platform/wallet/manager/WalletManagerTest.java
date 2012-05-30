package com.fb.platform.wallet.manager;

import java.math.BigDecimal;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.user.manager.interfaces.UserManager;
import com.fb.platform.user.manager.model.auth.LoginRequest;
import com.fb.platform.user.manager.model.auth.LoginResponse;
import com.fb.platform.user.manager.model.auth.LoginStatusEnum;
import com.fb.platform.wallet.manager.interfaces.WalletManager;
import com.fb.platform.wallet.manager.model.access.SubWalletEnum;
import com.fb.platform.wallet.manager.model.access.WalletSummaryRequest;
import com.fb.platform.wallet.manager.model.access.WalletSummaryResponse;

import com.fb.platform.wallet.manager.model.access.WalletHistoryRequest;
import com.fb.platform.wallet.manager.model.access.WalletHistoryResponse;
import com.fb.platform.wallet.manager.model.access.WalletHistoryStatusEnum;
import com.fb.platform.wallet.to.WalletTransaction;
import com.fb.platform.wallet.to.WalletSubTransaction;

import com.fb.platform.wallet.manager.model.access.FillWalletRequest;
import com.fb.platform.wallet.manager.model.access.FillWalletResponse;

import com.fb.platform.wallet.manager.model.access.PayRequest;
import com.fb.platform.wallet.manager.model.access.PayResponse;

import com.fb.platform.wallet.manager.model.access.RefundRequest;
import com.fb.platform.wallet.manager.model.access.RefundResponse;
import com.fb.platform.wallet.manager.model.access.RefundStatusEnum;

import com.fb.platform.wallet.manager.model.access.RevertRequest;
import com.fb.platform.wallet.manager.model.access.RevertResponse;
import com.fb.platform.wallet.manager.model.access.RevertStatusEnum;
import com.fb.platform.wallet.model.Wallet;


/**
 * 
 * @author kaushik
 *
 */
public class WalletManagerTest extends BaseTestCase{

	@Autowired
	private WalletManager walletManager = null;
	
	@Autowired
	private UserManager userManager = null;

	private String sessionToken = null;
	private int userId;
	
	@Before
	@Test
	public void testLoginWithEmail() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		
		sessionToken = response.getSessionToken();
		userId = response.getUserId();

		assertNotNull(response);
		assertEquals(LoginStatusEnum.LOGIN_SUCCESS, response.getLoginStatus());
		assertNotNull(response.getSessionToken());
		assertEquals(1, response.getUserId().intValue());
	}

	@Test
	public void testWalletSummary() {
		WalletSummaryRequest request = new WalletSummaryRequest();
		request.setUserId(userId);
		request.setClientId(-5);
		request.setSessionToken(sessionToken + "abcd");

		WalletSummaryResponse response = walletManager.getWalletSummary(request);
		
		assertNotNull(response);
		assertEquals("NO SESSION", response.getWalletSummaryStatus().toString());
	}
	
	@Test
	public void testWalletSummary2() {
		WalletSummaryRequest request = new WalletSummaryRequest();
		request.setUserId(userId);
		request.setClientId(-5);
		request.setSessionToken(sessionToken);

		WalletSummaryResponse response = walletManager.getWalletSummary(request);
		
		assertNotNull(response);
		assertEquals("SUCCESS", response.getWalletSummaryStatus().toString());
		assertEquals(new BigDecimal("300.00"), response.getWalletDetails().getTotalAmount());
		assertEquals(new BigDecimal("100.00"), response.getWalletDetails().getCashAmount());
		assertEquals(new BigDecimal("100.00"), response.getWalletDetails().getGiftAmount());
		assertEquals(new BigDecimal("100.00"), response.getWalletDetails().getRefundAmount());
		assertEquals(8, response.getWalletDetails().getWalletId());
	}

	@Test
	public void testWalletSummary3() {
		WalletSummaryRequest request = new WalletSummaryRequest();
		request.setUserId(600L);
		request.setClientId(-6);
		request.setSessionToken(sessionToken);

		WalletSummaryResponse response = walletManager.getWalletSummary(request);
		
		assertNotNull(response);
		assertEquals("SUCCESS", response.getWalletSummaryStatus().toString());
		assertEquals(new BigDecimal("0.00"), response.getWalletDetails().getTotalAmount());
	}

	@Test
	public void testWalletHistory() {
		WalletHistoryRequest request = new WalletHistoryRequest();
		request.setWalletId(100);
		request.setSessionToken(sessionToken + "abcd");

		WalletHistoryResponse response = walletManager.getWalletHistory(request);
		
		assertNotNull(response);
		assertEquals("NO SESSION", response.getWalletHistoryStatus().toString());
		
		request.setWalletId(100);
		request.setSessionToken(sessionToken);
		
		response = walletManager.getWalletHistory(request);
		
		assertNotNull(response);
		assertEquals("INVALID WALLET", response.getWalletHistoryStatus().toString());
	}

	@Test
	public void testWalletHistory2() {
		WalletHistoryRequest request = new WalletHistoryRequest();
		request.setWalletId(8);
		request.setSessionToken(sessionToken);

		WalletHistoryResponse response = walletManager.getWalletHistory(request);
		
		assertNotNull(response);
		assertEquals("SUCCESS", response.getWalletHistoryStatus().toString());
		assertEquals(3,response.getTransactionList().size());
	}

	@Test
	public void testWalletCreditWithCash() {
		FillWalletRequest request = new FillWalletRequest();
		request.setAmount(new BigDecimal("100.00"));
		request.setWalletId(8);
		request.setPaymentId(1);
		request.setSubWallet(SubWalletEnum.CASH);
		request.setSessionToken(sessionToken + "abcd");

		FillWalletResponse response = walletManager.fillWallet(request);
		
		assertNotNull(response);
		assertEquals("NO SESSION", response.getStatus().toString());
		
		request.setSessionToken(sessionToken);
		
		response = walletManager.fillWallet(request);
		
		assertNotNull(response);
		assertEquals("SUCCESS", response.getStatus().toString());
		
		WalletSummaryRequest summaryRequest = new WalletSummaryRequest();
		summaryRequest.setUserId(userId);
		summaryRequest.setClientId(-5);
		summaryRequest.setSessionToken(sessionToken);

		WalletSummaryResponse summaryResponse = walletManager.getWalletSummary(summaryRequest);
		
		assertNotNull(summaryResponse);
		assertEquals("SUCCESS", summaryResponse.getWalletSummaryStatus().toString());
		assertEquals(new BigDecimal("400.00"), summaryResponse.getWalletDetails().getTotalAmount());
		assertEquals(new BigDecimal("200.00"), summaryResponse.getWalletDetails().getCashAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getGiftAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getRefundAmount());
		
	}

	@Test
	public void testWalletCreditWithRefund() {
		FillWalletRequest request = new FillWalletRequest();
		request.setAmount(new BigDecimal("100.00"));
		request.setWalletId(8);
		request.setRefundId(1);
		request.setSubWallet(SubWalletEnum.REFUND);
		request.setSessionToken(sessionToken);

		FillWalletResponse response = walletManager.fillWallet(request);
		
		assertNotNull(response);
		assertEquals("SUCCESS", response.getStatus().toString());
		
		WalletSummaryRequest summaryRequest = new WalletSummaryRequest();
		summaryRequest.setUserId(userId);
		summaryRequest.setClientId(-5);
		summaryRequest.setSessionToken(sessionToken);

		WalletSummaryResponse summaryResponse = walletManager.getWalletSummary(summaryRequest);
		
		assertNotNull(summaryResponse);
		assertEquals("SUCCESS", summaryResponse.getWalletSummaryStatus().toString());
		assertEquals(new BigDecimal("400.00"), summaryResponse.getWalletDetails().getTotalAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getCashAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getGiftAmount());
		assertEquals(new BigDecimal("200.00"), summaryResponse.getWalletDetails().getRefundAmount());
		
	}
	
	@Test
	public void testWalletCreditWithRefund1() {
		FillWalletRequest request = new FillWalletRequest();
		request.setAmount(new BigDecimal("100.00"));
		request.setWalletId(100);
		request.setRefundId(1);
		request.setSubWallet(SubWalletEnum.REFUND);
		request.setSessionToken(sessionToken);

		FillWalletResponse response = walletManager.fillWallet(request);
		
		assertNotNull(response);
		assertEquals("INVALID WALLET", response.getStatus().toString());
	}

	@Test
	public void testWalletDebit() {
		PayRequest request = new PayRequest();
		request.setAmount(new BigDecimal("100.00"));
		request.setUserId(userId);
		request.setClientId(-5);
		request.setOrderId(1);
		request.setSessionToken(sessionToken + "abcd");

		PayResponse response = walletManager.payFromWallet(request);
		
		assertNotNull(response);
		assertEquals("NO SESSION", response.getStatus().toString());

		request.setSessionToken(sessionToken);

		response = walletManager.payFromWallet(request);
		
		assertNotNull(response);
		assertEquals("SUCCESS", response.getStatus().toString());

		WalletSummaryRequest summaryRequest = new WalletSummaryRequest();
		summaryRequest.setUserId(userId);
		summaryRequest.setClientId(-5);
		summaryRequest.setSessionToken(sessionToken);

		WalletSummaryResponse summaryResponse = walletManager.getWalletSummary(summaryRequest);
		
		assertNotNull(summaryResponse);
		assertEquals("SUCCESS", summaryResponse.getWalletSummaryStatus().toString());
		assertEquals(new BigDecimal("200.00"), summaryResponse.getWalletDetails().getTotalAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getCashAmount());
		assertEquals(new BigDecimal("0.00"), summaryResponse.getWalletDetails().getGiftAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getRefundAmount());

	}
	
	@Test
	public void testWalletDebit2() {
		PayRequest request = new PayRequest();
		request.setAmount(new BigDecimal("200.00"));
		request.setUserId(userId);
		request.setClientId(-5);
		request.setOrderId(1);
		request.setSessionToken(sessionToken);

		PayResponse response = walletManager.payFromWallet(request);
		
		assertNotNull(response);
		assertEquals("SUCCESS", response.getStatus().toString());
		
		WalletSummaryRequest summaryRequest = new WalletSummaryRequest();
		summaryRequest.setUserId(userId);
		summaryRequest.setClientId(-5);
		summaryRequest.setSessionToken(sessionToken);

		WalletSummaryResponse summaryResponse = walletManager.getWalletSummary(summaryRequest);
		
		assertNotNull(summaryResponse);
		assertEquals("SUCCESS", summaryResponse.getWalletSummaryStatus().toString());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getTotalAmount());
		assertEquals(new BigDecimal("0.00"), summaryResponse.getWalletDetails().getCashAmount());
		assertEquals(new BigDecimal("0.00"), summaryResponse.getWalletDetails().getGiftAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getRefundAmount());

	}

	@Test
	public void testWalletDebit3() {
		PayRequest request = new PayRequest();
		request.setAmount(new BigDecimal("300.00"));
		request.setUserId(userId);
		request.setClientId(-5);
		request.setOrderId(1);
		request.setSessionToken(sessionToken);

		PayResponse response = walletManager.payFromWallet(request);
		
		assertNotNull(response);
		assertEquals("SUCCESS", response.getStatus().toString());
		
		WalletSummaryRequest summaryRequest = new WalletSummaryRequest();
		summaryRequest.setUserId(userId);
		summaryRequest.setClientId(-5);
		summaryRequest.setSessionToken(sessionToken);

		WalletSummaryResponse summaryResponse = walletManager.getWalletSummary(summaryRequest);
		
		assertNotNull(summaryResponse);
		assertEquals("SUCCESS", summaryResponse.getWalletSummaryStatus().toString());
		assertEquals(new BigDecimal("0.00"), summaryResponse.getWalletDetails().getTotalAmount());
		assertEquals(new BigDecimal("0.00"), summaryResponse.getWalletDetails().getCashAmount());
		assertEquals(new BigDecimal("0.00"), summaryResponse.getWalletDetails().getGiftAmount());
		assertEquals(new BigDecimal("0.00"), summaryResponse.getWalletDetails().getRefundAmount());

	}
	
	@Test
	public void testWalletDebit4() {
		PayRequest request = new PayRequest();
		request.setAmount(new BigDecimal("150.00"));
		request.setUserId(userId);
		request.setClientId(-5);
		request.setOrderId(1);
		request.setSessionToken(sessionToken);

		PayResponse response = walletManager.payFromWallet(request);
		
		assertNotNull(response);
		assertEquals("SUCCESS", response.getStatus().toString());
		
		WalletSummaryRequest summaryRequest = new WalletSummaryRequest();
		summaryRequest.setUserId(userId);
		summaryRequest.setClientId(-5);
		summaryRequest.setSessionToken(sessionToken);

		WalletSummaryResponse summaryResponse = walletManager.getWalletSummary(summaryRequest);
		
		assertNotNull(summaryResponse);
		assertEquals("SUCCESS", summaryResponse.getWalletSummaryStatus().toString());
		assertEquals(new BigDecimal("150.00"), summaryResponse.getWalletDetails().getTotalAmount());
		assertEquals(new BigDecimal("50.00"), summaryResponse.getWalletDetails().getCashAmount());
		assertEquals(new BigDecimal("0.00"), summaryResponse.getWalletDetails().getGiftAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getRefundAmount());

	}

	@Test
	public void testWalletDebit5() {
		PayRequest request = new PayRequest();
		request.setAmount(new BigDecimal("400.00"));
		request.setUserId(userId);
		request.setClientId(-5);
		request.setOrderId(1);
		request.setSessionToken(sessionToken);

		PayResponse response = walletManager.payFromWallet(request);
		
		assertNotNull(response);
		assertEquals("BALANCE UNAVAILABLE", response.getStatus().toString());
	}
	
	@Test
	public void testWalletDebit6() {
		PayRequest request = new PayRequest();
		request.setAmount(new BigDecimal("400.00"));
		request.setUserId(userId);
		request.setClientId(-155);
		request.setOrderId(1);
		request.setSessionToken(sessionToken);

		PayResponse response = walletManager.payFromWallet(request);
		
		assertNotNull(response);
		assertEquals("INVALID WALLET", response.getStatus().toString());
	}
	
	@Test
	public void testRefundFromWallet() {
		RefundRequest request = new RefundRequest();
		request.setAmount(new BigDecimal("100.00"));
		request.setUserId(userId);
		request.setClientId(-5);
		request.setRefundId(1);
		request.setIgnoreExpiry(true);
		request.setSessionToken(sessionToken + "abcd");

		RefundResponse response = walletManager.refundFromWallet(request);
		
		assertNotNull(response);
		assertEquals("NO SESSION", response.getStatus().toString());

		request.setSessionToken(sessionToken);

		response = walletManager.refundFromWallet(request);
		
		assertNotNull(response);
		assertEquals("SUCCESS", response.getStatus().toString());

		request.setAmount(new BigDecimal("100.00"));
		request.setUserId(userId);
		request.setClientId(-5);
		request.setRefundId(1);
		request.setIgnoreExpiry(true);
		request.setSessionToken(sessionToken);

		response = walletManager.refundFromWallet(request);
		
		assertNotNull(response);
		assertEquals("DUPLICATE REFUND REQUEST", response.getStatus().toString());

		WalletSummaryRequest summaryRequest = new WalletSummaryRequest();
		summaryRequest.setUserId(userId);
		summaryRequest.setClientId(-5);
		summaryRequest.setSessionToken(sessionToken);

		WalletSummaryResponse summaryResponse = walletManager.getWalletSummary(summaryRequest);
		
		assertNotNull(summaryResponse);
		assertEquals("SUCCESS", summaryResponse.getWalletSummaryStatus().toString());
		assertEquals(new BigDecimal("200.00"), summaryResponse.getWalletDetails().getTotalAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getCashAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getGiftAmount());
		assertEquals(new BigDecimal("0.00"), summaryResponse.getWalletDetails().getRefundAmount());

	}

	@Test
	public void testRefundFromWallet2() {
		RefundRequest request = new RefundRequest();
		request.setAmount(new BigDecimal("100.00"));
		request.setUserId(userId);
		request.setClientId(-5);
		request.setRefundId(1);
		request.setSessionToken(sessionToken);

		RefundResponse response = walletManager.refundFromWallet(request);
		
		assertNotNull(response);
		assertEquals("ALREADY REFUNDED", response.getStatus().toString());

		request.setAmount(new BigDecimal("200.00"));
		request.setUserId(userId);
		request.setClientId(-5);
		request.setRefundId(1);
		request.setSessionToken(sessionToken);

		response = walletManager.refundFromWallet(request);
		
		assertNotNull(response);
		assertEquals("BALANCE UNAVAILABLE", response.getStatus().toString());

	}

	@Test
	public void testRefundFromWallet3() {
		RefundRequest request = new RefundRequest();
		request.setAmount(new BigDecimal("100.00"));
		request.setUserId(userId);
		request.setClientId(-5);
		request.setRefundId(2);		// Invalid refund id
		request.setIgnoreExpiry(true);
		request.setSessionToken(sessionToken);

		RefundResponse response = walletManager.refundFromWallet(request);
		
		assertNotNull(response);
		assertEquals("FAILED TRANSACTION", response.getStatus().toString());
	}

	@Test
	public void testRefundFromWallet4() {
		RefundRequest request = new RefundRequest();
		request.setAmount(new BigDecimal("100.00"));
		request.setUserId(userId);
		request.setClientId(-155);
		request.setRefundId(2);		// Invalid refund id
		request.setIgnoreExpiry(true);
		request.setSessionToken(sessionToken);

		RefundResponse response = walletManager.refundFromWallet(request);
		
		assertNotNull(response);
		assertEquals("INVALID WALLET", response.getStatus().toString());
	}

	@Test
	public void testRevertTransaction() {
		RevertRequest request = new RevertRequest();
		request.setAmount(new BigDecimal("100.00"));
		request.setUserId(userId);
		request.setClientId(-5);
		request.setSessionToken(sessionToken);
		request.setTransactionId("akjsdasdjj");

		RevertResponse response = walletManager.revertWalletTransaction(request);
		
		assertNotNull(response);
		assertEquals("INVALID TRANSACTION ID", response.getStatus().toString());
		
		request.setSessionToken(sessionToken + "abcd");
		request.setTransactionId("akjsdasdjj");

		response = walletManager.revertWalletTransaction(request);
		
		assertNotNull(response);
		assertEquals("NO SESSION", response.getStatus().toString());

	}

	@Test
	public void testRevertTransaction1() {
		RevertRequest request = new RevertRequest();
		request.setAmount(new BigDecimal("100.00"));
		request.setUserId(userId);
		request.setClientId(-155);
		request.setSessionToken(sessionToken);
		request.setTransactionId("ABSCDFADF1231lp0bg2SASzcvab");

		RevertResponse response = walletManager.revertWalletTransaction(request);
		
		assertNotNull(response);
		assertEquals("INVALID WALLET", response.getStatus().toString());
		
		request.setClientId(-5);
		
		response = walletManager.revertWalletTransaction(request);
		
		assertNotNull(response);
		assertEquals("SUCCESS", response.getStatus().toString());
		
		WalletSummaryRequest summaryRequest = new WalletSummaryRequest();
		summaryRequest.setUserId(userId);
		summaryRequest.setClientId(-5);
		summaryRequest.setSessionToken(sessionToken);

		WalletSummaryResponse summaryResponse = walletManager.getWalletSummary(summaryRequest);
		
		assertNotNull(summaryResponse);
		assertEquals("SUCCESS", summaryResponse.getWalletSummaryStatus().toString());
		assertEquals(new BigDecimal("200.00"), summaryResponse.getWalletDetails().getTotalAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getCashAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getGiftAmount());
		assertEquals(new BigDecimal("0.00"), summaryResponse.getWalletDetails().getRefundAmount());

	}

	@Test
	public void testRevertTransaction2() {
		PayRequest request = new PayRequest();
		request.setAmount(new BigDecimal("100.00"));
		request.setUserId(userId);
		request.setClientId(-5);
		request.setOrderId(1);
		request.setSessionToken(sessionToken);

		PayResponse response = walletManager.payFromWallet(request);
		
		assertNotNull(response);
		assertEquals("SUCCESS", response.getStatus().toString());

		WalletSummaryRequest summaryRequest = new WalletSummaryRequest();
		summaryRequest.setUserId(userId);
		summaryRequest.setClientId(-5);
		summaryRequest.setSessionToken(sessionToken);

		WalletSummaryResponse summaryResponse = walletManager.getWalletSummary(summaryRequest);
		
		assertNotNull(summaryResponse);
		assertEquals("SUCCESS", summaryResponse.getWalletSummaryStatus().toString());
		assertEquals(new BigDecimal("200.00"), summaryResponse.getWalletDetails().getTotalAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getCashAmount());
		assertEquals(new BigDecimal("0.00"), summaryResponse.getWalletDetails().getGiftAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getRefundAmount());

		RevertRequest revertRequest = new RevertRequest();
		revertRequest.setAmount(new BigDecimal("100.00"));
		revertRequest.setUserId(userId);
		revertRequest.setClientId(-5);
		revertRequest.setSessionToken(sessionToken);
		revertRequest.setTransactionId(response.getTransactionId());

		RevertResponse revertResponse = walletManager.revertWalletTransaction(revertRequest);
		
		assertNotNull(response);
		assertEquals("SUCCESS", revertResponse.getStatus().toString());
		
		summaryRequest = new WalletSummaryRequest();
		summaryRequest.setUserId(userId);
		summaryRequest.setClientId(-5);
		summaryRequest.setSessionToken(sessionToken);

		summaryResponse = walletManager.getWalletSummary(summaryRequest);
		
		assertNotNull(summaryResponse);
		assertEquals("SUCCESS", summaryResponse.getWalletSummaryStatus().toString());
		assertEquals(new BigDecimal("300.00"), summaryResponse.getWalletDetails().getTotalAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getCashAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getGiftAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getRefundAmount());
	}

	@Test
	public void testRevertTransaction3() {
		PayRequest request = new PayRequest();
		request.setAmount(new BigDecimal("200.00"));
		request.setUserId(userId);
		request.setClientId(-5);
		request.setOrderId(1);
		request.setSessionToken(sessionToken);

		PayResponse response = walletManager.payFromWallet(request);
		
		assertNotNull(response);
		assertEquals("SUCCESS", response.getStatus().toString());

		WalletSummaryRequest summaryRequest = new WalletSummaryRequest();
		summaryRequest.setUserId(userId);
		summaryRequest.setClientId(-5);
		summaryRequest.setSessionToken(sessionToken);

		WalletSummaryResponse summaryResponse = walletManager.getWalletSummary(summaryRequest);
		
		assertNotNull(summaryResponse);
		assertEquals("SUCCESS", summaryResponse.getWalletSummaryStatus().toString());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getTotalAmount());
		assertEquals(new BigDecimal("0.00"), summaryResponse.getWalletDetails().getCashAmount());
		assertEquals(new BigDecimal("0.00"), summaryResponse.getWalletDetails().getGiftAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getRefundAmount());

		RevertRequest revertRequest = new RevertRequest();
		revertRequest.setAmount(new BigDecimal("200.00"));
		revertRequest.setUserId(userId);
		revertRequest.setClientId(-5);
		revertRequest.setSessionToken(sessionToken);
		revertRequest.setTransactionId(response.getTransactionId());

		RevertResponse revertResponse = walletManager.revertWalletTransaction(revertRequest);
		
		assertNotNull(response);
		assertEquals("SUCCESS", revertResponse.getStatus().toString());
		
		summaryRequest = new WalletSummaryRequest();
		summaryRequest.setUserId(userId);
		summaryRequest.setClientId(-5);
		summaryRequest.setSessionToken(sessionToken);

		summaryResponse = walletManager.getWalletSummary(summaryRequest);
		
		assertNotNull(summaryResponse);
		assertEquals("SUCCESS", summaryResponse.getWalletSummaryStatus().toString());
		assertEquals(new BigDecimal("300.00"), summaryResponse.getWalletDetails().getTotalAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getCashAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getGiftAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getRefundAmount());

	}

	@Test
	public void testRevertTransaction4() {
		PayRequest request = new PayRequest();
		request.setAmount(new BigDecimal("300.00"));
		request.setUserId(userId);
		request.setClientId(-5);
		request.setOrderId(1);
		request.setSessionToken(sessionToken);

		PayResponse response = walletManager.payFromWallet(request);
		
		assertNotNull(response);
		assertEquals("SUCCESS", response.getStatus().toString());

		WalletSummaryRequest summaryRequest = new WalletSummaryRequest();
		summaryRequest.setUserId(userId);
		summaryRequest.setClientId(-5);
		summaryRequest.setSessionToken(sessionToken);

		WalletSummaryResponse summaryResponse = walletManager.getWalletSummary(summaryRequest);
		
		assertNotNull(summaryResponse);
		assertEquals("SUCCESS", summaryResponse.getWalletSummaryStatus().toString());
		assertEquals(new BigDecimal("0.00"), summaryResponse.getWalletDetails().getTotalAmount());
		assertEquals(new BigDecimal("0.00"), summaryResponse.getWalletDetails().getCashAmount());
		assertEquals(new BigDecimal("0.00"), summaryResponse.getWalletDetails().getGiftAmount());
		assertEquals(new BigDecimal("0.00"), summaryResponse.getWalletDetails().getRefundAmount());

		RevertRequest revertRequest = new RevertRequest();
		revertRequest.setAmount(new BigDecimal("300.00"));
		revertRequest.setUserId(userId);
		revertRequest.setClientId(-5);
		revertRequest.setSessionToken(sessionToken);
		revertRequest.setTransactionId(response.getTransactionId());

		RevertResponse revertResponse = walletManager.revertWalletTransaction(revertRequest);
		
		assertNotNull(response);
		assertEquals("SUCCESS", revertResponse.getStatus().toString());
		
		summaryRequest = new WalletSummaryRequest();
		summaryRequest.setUserId(userId);
		summaryRequest.setClientId(-5);
		summaryRequest.setSessionToken(sessionToken);

		summaryResponse = walletManager.getWalletSummary(summaryRequest);
		
		assertNotNull(summaryResponse);
		assertEquals("SUCCESS", summaryResponse.getWalletSummaryStatus().toString());
		assertEquals(new BigDecimal("300.00"), summaryResponse.getWalletDetails().getTotalAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getCashAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getGiftAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getRefundAmount());
		
	}

	@Test
	public void testRevertTransaction5() {
		PayRequest request = new PayRequest();
		request.setAmount(new BigDecimal("300.00"));
		request.setUserId(userId);
		request.setClientId(-5);
		request.setOrderId(1);
		request.setSessionToken(sessionToken);

		PayResponse response = walletManager.payFromWallet(request);
		
		assertNotNull(response);
		assertEquals("SUCCESS", response.getStatus().toString());

		WalletSummaryRequest summaryRequest = new WalletSummaryRequest();
		summaryRequest.setUserId(userId);
		summaryRequest.setClientId(-5);
		summaryRequest.setSessionToken(sessionToken);

		WalletSummaryResponse summaryResponse = walletManager.getWalletSummary(summaryRequest);
		
		assertNotNull(summaryResponse);
		assertEquals("SUCCESS", summaryResponse.getWalletSummaryStatus().toString());
		assertEquals(new BigDecimal("0.00"), summaryResponse.getWalletDetails().getTotalAmount());
		assertEquals(new BigDecimal("0.00"), summaryResponse.getWalletDetails().getCashAmount());
		assertEquals(new BigDecimal("0.00"), summaryResponse.getWalletDetails().getGiftAmount());
		assertEquals(new BigDecimal("0.00"), summaryResponse.getWalletDetails().getRefundAmount());

		String transactionId = response.getTransactionId();
		
		RevertRequest revertRequest = new RevertRequest();
		revertRequest.setAmount(new BigDecimal("150.00"));
		revertRequest.setUserId(userId);
		revertRequest.setClientId(-5);
		revertRequest.setSessionToken(sessionToken);
		revertRequest.setTransactionId(transactionId);

		RevertResponse revertResponse = walletManager.revertWalletTransaction(revertRequest);
		
		assertNotNull(response);
		assertEquals("SUCCESS", revertResponse.getStatus().toString());
		
		summaryRequest = new WalletSummaryRequest();
		summaryRequest.setUserId(userId);
		summaryRequest.setClientId(-5);
		summaryRequest.setSessionToken(sessionToken);

		summaryResponse = walletManager.getWalletSummary(summaryRequest);
		
		assertNotNull(summaryResponse);
		assertEquals("SUCCESS", summaryResponse.getWalletSummaryStatus().toString());
		assertEquals(new BigDecimal("150.00"), summaryResponse.getWalletDetails().getTotalAmount());
		assertEquals(new BigDecimal("50.00"), summaryResponse.getWalletDetails().getCashAmount());
		assertEquals(new BigDecimal("0.00"), summaryResponse.getWalletDetails().getGiftAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getRefundAmount());

		revertRequest = new RevertRequest();
		revertRequest.setAmount(new BigDecimal("150.00"));
		revertRequest.setUserId(userId);
		revertRequest.setClientId(-5);
		revertRequest.setSessionToken(sessionToken);
		revertRequest.setTransactionId(transactionId);

		revertResponse = walletManager.revertWalletTransaction(revertRequest);
		
		assertNotNull(response);
		assertEquals("SUCCESS", revertResponse.getStatus().toString());
		
		summaryRequest = new WalletSummaryRequest();
		summaryRequest.setUserId(userId);
		summaryRequest.setClientId(-5);
		summaryRequest.setSessionToken(sessionToken);

		summaryResponse = walletManager.getWalletSummary(summaryRequest);
		
		assertNotNull(summaryResponse);
		assertEquals("SUCCESS", summaryResponse.getWalletSummaryStatus().toString());
		assertEquals(new BigDecimal("300.00"), summaryResponse.getWalletDetails().getTotalAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getCashAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getGiftAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getRefundAmount());

	}

	@Test
	public void testRevertTransaction6() {
		PayRequest request = new PayRequest();
		request.setAmount(new BigDecimal("100.00"));
		request.setUserId(userId);
		request.setClientId(-5);
		request.setOrderId(1);
		request.setSessionToken(sessionToken);

		PayResponse response = walletManager.payFromWallet(request);
		
		assertNotNull(response);
		assertEquals("SUCCESS", response.getStatus().toString());

		WalletSummaryRequest summaryRequest = new WalletSummaryRequest();
		summaryRequest.setUserId(userId);
		summaryRequest.setClientId(-5);
		summaryRequest.setSessionToken(sessionToken);

		WalletSummaryResponse summaryResponse = walletManager.getWalletSummary(summaryRequest);
		
		assertNotNull(summaryResponse);
		assertEquals("SUCCESS", summaryResponse.getWalletSummaryStatus().toString());
		assertEquals(new BigDecimal("200.00"), summaryResponse.getWalletDetails().getTotalAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getCashAmount());
		assertEquals(new BigDecimal("0.00"), summaryResponse.getWalletDetails().getGiftAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getRefundAmount());

		String transactionId = response.getTransactionId();
		
		RevertRequest revertRequest = new RevertRequest();
		revertRequest.setAmount(new BigDecimal("150.00"));
		revertRequest.setUserId(userId);
		revertRequest.setClientId(-5);
		revertRequest.setSessionToken(sessionToken);
		revertRequest.setTransactionId(transactionId);

		RevertResponse revertResponse = walletManager.revertWalletTransaction(revertRequest);
		
		assertNotNull(response);
		assertEquals("BALANCE UNAVAILABLE", revertResponse.getStatus().toString());
		
		summaryRequest = new WalletSummaryRequest();
		summaryRequest.setUserId(userId);
		summaryRequest.setClientId(-5);
		summaryRequest.setSessionToken(sessionToken);

		summaryResponse = walletManager.getWalletSummary(summaryRequest);
		
		assertNotNull(summaryResponse);
		assertEquals("SUCCESS", summaryResponse.getWalletSummaryStatus().toString());
		assertEquals(new BigDecimal("200.00"), summaryResponse.getWalletDetails().getTotalAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getCashAmount());
		assertEquals(new BigDecimal("0.00"), summaryResponse.getWalletDetails().getGiftAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getRefundAmount());

	}

}
