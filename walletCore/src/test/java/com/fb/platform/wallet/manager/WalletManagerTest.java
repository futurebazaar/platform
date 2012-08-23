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
import com.fb.platform.wallet.manager.model.access.FillWalletStatusEnum;
import com.fb.platform.wallet.manager.model.access.SubWalletEnum;
import com.fb.platform.wallet.manager.model.access.VerifyWalletRequest;
import com.fb.platform.wallet.manager.model.access.VerifyWalletResponse;
import com.fb.platform.wallet.manager.model.access.WalletSummaryRequest;
import com.fb.platform.wallet.manager.model.access.WalletSummaryResponse;
import com.fb.platform.wallet.manager.model.access.WalletSummaryStatusEnum;

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
import com.fb.platform.wallet.model.SubWalletType;
import com.fb.platform.wallet.model.TransactionType;
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
	private static String testWalletPassword = "c0mP|e*P@$$w)rD";
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
		assertEquals(4,response.getTransactionList().size());
	}
	
	@Test
	public void testWalletHistory3() {
		WalletHistoryRequest request = new WalletHistoryRequest();
		request.setUserId(1);
		request.setClientId(-5);
		request.setPageNumber(1);
		request.setResultsPerPage(5);
		request.setSessionToken(sessionToken);

		WalletHistoryResponse response = walletManager.getWalletHistoryPaged(request);
		
		assertNotNull(response);
		assertEquals("SUCCESS", response.getWalletHistoryStatus().toString());
		assertEquals(4,response.getTotalTransactionSize());
		assertNotNull(response.getTransactionList());
		assertEquals(4,response.getTransactionList().size());
	}

	@Test
	public void testWalletCreditWithCash() {
		FillWalletRequest request = new FillWalletRequest();
		request.setAmount(new BigDecimal("100.00"));
		request.setUserId(userId);
		request.setClientId(-5);
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
		request.setUserId(userId);
		request.setClientId(-5);
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
	public void testWalletDebit() {
		PayRequest request = new PayRequest();
		request.setAmount(new BigDecimal("100.00"));
		request.setUserId(userId);
		request.setClientId(-5);
		request.setOrderId(1);
		request.setWalletPassord(testWalletPassword);
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
		request.setWalletPassord(testWalletPassword);
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
		request.setWalletPassord(testWalletPassword);
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
		request.setWalletPassord(testWalletPassword);
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
		request.setAmount(new BigDecimal("500.00"));
		request.setUserId(userId);
		request.setClientId(-5);
		request.setOrderId(1);
		request.setWalletPassord(testWalletPassword);
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
		request.setWalletPassord(testWalletPassword);
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
		request.setTransactionIdToRevert("akjsdasdjj");

		RevertResponse response = walletManager.revertWalletTransaction(request);
		
		assertNotNull(response);
		assertEquals("INVALID TRANSACTION ID", response.getStatus().toString());
		
		request.setSessionToken(sessionToken + "abcd");
		request.setTransactionIdToRevert("akjsdasdjj");

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
		request.setTransactionIdToRevert("ABSCDFADF1231lp0bg2SASzcvab");

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
		request.setWalletPassord(testWalletPassword);
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
		revertRequest.setTransactionIdToRevert(response.getTransactionId());

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
		request.setAmount(new BigDecimal("300.00"));
		request.setUserId(userId);
		request.setClientId(-5);
		request.setOrderId(1);
		request.setWalletPassord(testWalletPassword);
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
		revertRequest.setTransactionIdToRevert(response.getTransactionId());

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
		request.setWalletPassord(testWalletPassword);
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
		revertRequest.setAmount(new BigDecimal("250.00"));
		revertRequest.setUserId(userId);
		revertRequest.setClientId(-5);
		revertRequest.setSessionToken(sessionToken);
		revertRequest.setTransactionIdToRevert(transactionId);

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
		assertEquals(new BigDecimal("250.00"), summaryResponse.getWalletDetails().getTotalAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getCashAmount());
		assertEquals(new BigDecimal("50.00"), summaryResponse.getWalletDetails().getGiftAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getRefundAmount());

		revertRequest = new RevertRequest();
		revertRequest.setAmount(new BigDecimal("50.00"));
		revertRequest.setUserId(userId);
		revertRequest.setClientId(-5);
		revertRequest.setSessionToken(sessionToken);
		revertRequest.setTransactionIdToRevert(transactionId);

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
		request.setAmount(new BigDecimal("200.00"));
		request.setUserId(userId);
		request.setClientId(-5);
		request.setOrderId(1);
		request.setWalletPassord(testWalletPassword);
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

		String transactionId = response.getTransactionId();
		
		RevertRequest revertRequest = new RevertRequest();
		revertRequest.setAmount(new BigDecimal("250.00"));
		revertRequest.setUserId(userId);
		revertRequest.setClientId(-5);
		revertRequest.setSessionToken(sessionToken);
		revertRequest.setTransactionIdToRevert(transactionId);

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
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getTotalAmount());
		assertEquals(new BigDecimal("0.00"), summaryResponse.getWalletDetails().getCashAmount());
		assertEquals(new BigDecimal("0.00"), summaryResponse.getWalletDetails().getGiftAmount());
		assertEquals(new BigDecimal("100.00"), summaryResponse.getWalletDetails().getRefundAmount());

	}
	
	

	@Test
	public void testWalletVerify1() {
		VerifyWalletRequest request = new VerifyWalletRequest();
		request.setAmount(new BigDecimal("0.01"));
		request.setUserId(userId);
		request.setClientId(-5);
		request.setPassword(testWalletPassword);
		request.setSessionToken(sessionToken);

		VerifyWalletResponse response = walletManager.verifyWallet(request);
		
		assertNotNull(response);
		assertEquals("SUCCESS", response.getStatus().toString());

	}
	
	@Test
	public void testWalletVerify2() {
		VerifyWalletRequest request = new VerifyWalletRequest();
		request.setAmount(new BigDecimal("10000.00"));
		request.setUserId(userId);
		request.setClientId(-5);
		request.setPassword(testWalletPassword);
		request.setSessionToken(sessionToken);

		VerifyWalletResponse response = walletManager.verifyWallet(request);
		
		assertNotNull(response);
		assertEquals("BALANCE UNAVAILABLE", response.getStatus().toString());

	}
	
	@Test
	public void testWalletVerify3() {
		VerifyWalletRequest request = new VerifyWalletRequest();
		request.setAmount(new BigDecimal("10000.00"));
		request.setUserId(userId);
		request.setClientId(-5);
		request.setPassword("adfdassa");
		request.setSessionToken(sessionToken);

		VerifyWalletResponse response = walletManager.verifyWallet(request);
		
		assertNotNull(response);
		assertEquals("WRONG PASSWORD", response.getStatus().toString());

	}
	
	@Test
	public void testWalletVerify4() {
		VerifyWalletRequest request = new VerifyWalletRequest();
		request.setAmount(new BigDecimal("10000.00"));
		request.setUserId(1000);
		request.setClientId(-50);
		request.setPassword("adfdassa");
		request.setSessionToken(sessionToken);

		VerifyWalletResponse response = walletManager.verifyWallet(request);
		
		assertNotNull(response);
		assertEquals("INVALID WALLET", response.getStatus().toString());

	}
	
	@Test 
	public void testCompleteWalletManager() {
		WalletSummaryRequest walletSummaryRequest = new WalletSummaryRequest();
		walletSummaryRequest.setClientId(5);
		walletSummaryRequest.setUserId(6);
		walletSummaryRequest.setSessionToken(sessionToken);
		WalletSummaryResponse walletSummaryResponse = walletManager.getWalletSummary(walletSummaryRequest);
		assertNotNull(walletSummaryResponse);
		assertEquals(true, walletSummaryResponse.getWalletSummaryStatus().equals(WalletSummaryStatusEnum.SUCCESS));
		assertEquals(new BigDecimal("0.00"), walletSummaryResponse.getWalletDetails().getTotalAmount());
		
		FillWalletRequest fillWalletRequest = new FillWalletRequest();
		fillWalletRequest.setAmount(new BigDecimal("300.00"));
		fillWalletRequest.setClientId(5);
		fillWalletRequest.setRefundId(1);
		fillWalletRequest.setSessionToken(sessionToken);
		fillWalletRequest.setSubWallet(SubWalletEnum.REFUND);
		fillWalletRequest.setUserId(6);
		
		FillWalletResponse fillWalletResponse = walletManager.fillWallet(fillWalletRequest);
		assertNotNull(fillWalletResponse);
		assertEquals(true, fillWalletResponse.getStatus().equals(FillWalletStatusEnum.SUCCESS));
		assertEquals(walletSummaryResponse.getWalletDetails().getWalletId(), fillWalletResponse.getWalletId());
		assertNotNull(fillWalletResponse.getTransactionId());
		String tranid1 = fillWalletResponse.getTransactionId();
		
		FillWalletRequest fillWalletRequest1 = new FillWalletRequest();
		fillWalletRequest1.setAmount(new BigDecimal("300.00"));
		fillWalletRequest1.setClientId(5);
		fillWalletRequest1.setRefundId(2);
		fillWalletRequest1.setSessionToken(sessionToken);
		fillWalletRequest1.setSubWallet(SubWalletEnum.REFUND);
		fillWalletRequest1.setUserId(6);
		
		fillWalletResponse = walletManager.fillWallet(fillWalletRequest1);
		assertNotNull(fillWalletResponse);
		assertEquals(true, fillWalletResponse.getStatus().equals(FillWalletStatusEnum.SUCCESS));
		assertEquals(walletSummaryResponse.getWalletDetails().getWalletId(), fillWalletResponse.getWalletId());
		assertNotNull(fillWalletResponse.getTransactionId());
		String tranid2 = fillWalletResponse.getTransactionId();
		
		
		FillWalletRequest fillWalletRequest2 = new FillWalletRequest();
		fillWalletRequest2.setAmount(new BigDecimal("300.00"));
		fillWalletRequest2.setClientId(5);
		fillWalletRequest2.setRefundId(3);
		fillWalletRequest2.setSessionToken(sessionToken);
		fillWalletRequest2.setSubWallet(SubWalletEnum.REFUND);
		fillWalletRequest2.setUserId(6);
		
		fillWalletResponse = walletManager.fillWallet(fillWalletRequest2);
		assertNotNull(fillWalletResponse);
		assertEquals(true, fillWalletResponse.getStatus().equals(FillWalletStatusEnum.SUCCESS));
		assertEquals(walletSummaryResponse.getWalletDetails().getWalletId(), fillWalletResponse.getWalletId());
		assertNotNull(fillWalletResponse.getTransactionId());
		String tranid3 = fillWalletResponse.getTransactionId();
		
		FillWalletRequest fillWalletRequest3 = new FillWalletRequest();
		fillWalletRequest3.setAmount(new BigDecimal("300.00"));
		fillWalletRequest3.setClientId(5);
		fillWalletRequest3.setRefundId(4);
		fillWalletRequest3.setSessionToken(sessionToken);
		fillWalletRequest3.setSubWallet(SubWalletEnum.REFUND);
		fillWalletRequest3.setUserId(6);
		
		fillWalletResponse = walletManager.fillWallet(fillWalletRequest3);
		assertNotNull(fillWalletResponse);
		assertEquals(true, fillWalletResponse.getStatus().equals(FillWalletStatusEnum.SUCCESS));
		assertEquals(walletSummaryResponse.getWalletDetails().getWalletId(), fillWalletResponse.getWalletId());
		assertNotNull(fillWalletResponse.getTransactionId());
		String tranid4 = fillWalletResponse.getTransactionId();
		
		WalletHistoryRequest walletHistoryRequest = new WalletHistoryRequest();
		walletHistoryRequest.setClientId(5);
		walletHistoryRequest.setPageNumber(1);
		walletHistoryRequest.setResultsPerPage(5);
		walletHistoryRequest.setSessionToken(sessionToken);
		walletHistoryRequest.setUserId(6);
		
		WalletHistoryResponse walletHistoryResponse = walletManager.getWalletHistoryPaged(walletHistoryRequest);
		assertNotNull(walletHistoryResponse);
		assertEquals(4, walletHistoryResponse.getTotalTransactionSize());
		assertEquals(true, walletHistoryResponse.getWalletHistoryStatus().equals(WalletHistoryStatusEnum.SUCCESS));
		for(WalletTransaction walletTransaction :walletHistoryResponse.getTransactionList()){
			assertNotNull(walletTransaction);
			assertEquals(true, walletTransaction.getTransactionType().equals(TransactionType.CREDIT));
			if(walletTransaction.getTransactionId().equals(tranid1)){
				assertEquals(new BigDecimal("300.00"), walletTransaction.getWalletBalance().getAmount());
				assertEquals(1, walletTransaction.getWalletSubTransaction().get(0).getRefundId());
				assertEquals(true, walletTransaction.getWalletSubTransaction().get(0).getSubWalletType().equals(SubWalletType.REFUND));
			}
			if(walletTransaction.getTransactionId().equals(tranid2)){
				assertEquals(new BigDecimal("600.00"), walletTransaction.getWalletBalance().getAmount());
				assertEquals(2, walletTransaction.getWalletSubTransaction().get(0).getRefundId());
				assertEquals(true, walletTransaction.getWalletSubTransaction().get(0).getSubWalletType().equals(SubWalletType.REFUND));
			}
			if(walletTransaction.getTransactionId().equals(tranid3)){
				assertEquals(new BigDecimal("900.00"), walletTransaction.getWalletBalance().getAmount());
				assertEquals(3, walletTransaction.getWalletSubTransaction().get(0).getRefundId());
				assertEquals(true, walletTransaction.getWalletSubTransaction().get(0).getSubWalletType().equals(SubWalletType.REFUND));
			}
			if(walletTransaction.getTransactionId().equals(tranid4)){
				assertEquals(new BigDecimal("1200.00"), walletTransaction.getWalletBalance().getAmount());
				assertEquals(4, walletTransaction.getWalletSubTransaction().get(0).getRefundId());
				assertEquals(true, walletTransaction.getWalletSubTransaction().get(0).getSubWalletType().equals(SubWalletType.REFUND));
			}
		}
		

	}
}
