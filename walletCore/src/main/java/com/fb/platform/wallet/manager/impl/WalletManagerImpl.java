package com.fb.platform.wallet.manager.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.PlatformException;
import com.fb.commons.to.Money;
import com.fb.platform.auth.AuthenticationService;
import com.fb.platform.auth.AuthenticationTO;
import com.fb.platform.user.manager.exception.UserNotFoundException;

import com.fb.platform.wallet.service.exception.InvalidTransaction;
import com.fb.platform.wallet.service.exception.WalletNotFoundException;
import com.fb.platform.wallet.service.exception.InSufficientFundsException;
import com.fb.platform.wallet.service.exception.AlreadyRefundedException;
import com.fb.platform.wallet.service.exception.RefundExpiredException;
import com.fb.platform.wallet.service.exception.InvalidTransactionIdException;
import com.fb.platform.wallet.manager.interfaces.WalletManager;
import com.fb.platform.wallet.manager.model.access.WalletDetails;
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
import com.fb.platform.wallet.manager.model.access.FillWalletStatusEnum;

import com.fb.platform.wallet.manager.model.access.PayRequest;
import com.fb.platform.wallet.manager.model.access.PayResponse;
import com.fb.platform.wallet.manager.model.access.PayStatusEnum;

import com.fb.platform.wallet.manager.model.access.RefundRequest;
import com.fb.platform.wallet.manager.model.access.RefundResponse;
import com.fb.platform.wallet.manager.model.access.RefundStatusEnum;

import com.fb.platform.wallet.manager.model.access.RevertRequest;
import com.fb.platform.wallet.manager.model.access.RevertResponse;
import com.fb.platform.wallet.manager.model.access.RevertStatusEnum;
import com.fb.platform.wallet.model.Wallet;

import com.fb.platform.wallet.service.WalletService;


/**
 * @author rajesh
 * 
 */
public class WalletManagerImpl implements WalletManager {

	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private WalletService walletService;

	public void setAuthenticationService(
			AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	private static Log logger = LogFactory.getLog(WalletManagerImpl.class);

	@Override
	public WalletSummaryResponse getWalletSummary(
			WalletSummaryRequest walletSummaryRequest) {
		if (logger.isDebugEnabled()) {
			logger.debug("Trying to retrieve wallet summary for userid : "
					+ walletSummaryRequest.getUserId());
		}

		WalletSummaryResponse walletSummaryResponse = new WalletSummaryResponse();

		// authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService
				.authenticate(walletSummaryRequest.getSessionToken());
		if (authentication == null) {
			// invalid session token
			walletSummaryResponse.setWalletSummaryStatus(WalletSummaryStatusEnum.NO_SESSION);
			return walletSummaryResponse;
		}

		walletSummaryResponse.setSessionToken(walletSummaryRequest.getSessionToken());

		long userId =  walletSummaryRequest.getUserId();
		long clientId = walletSummaryRequest.getClientId();
		
		try {
			Wallet wallet = walletService.load(userId, clientId);
			
			walletSummaryResponse.setWalletSummaryStatus(WalletSummaryStatusEnum.SUCCESS);
			WalletDetails walletDetails = new WalletDetails();
			walletDetails.setWalletId(wallet.getId());
			walletDetails.setCashAmount(wallet.getCashSubWallet().getAmount());
			walletDetails.setRefundAmount(wallet.getRefundSubWallet().getAmount());
			walletDetails.setGiftAmount(wallet.getGiftSubWallet().getAmount());
			walletDetails.setTotalAmount(wallet.getTotalAmount().getAmount());
			walletSummaryResponse.setWalletDetails(walletDetails);

		} catch (UserNotFoundException e) {
			walletSummaryResponse.setWalletSummaryStatus(WalletSummaryStatusEnum.INVALID_USER);
		} catch (PlatformException pe) {
			logger.error("Error while getting wallet summary for the user : " + walletSummaryRequest.getUserId(), pe);
			walletSummaryResponse.setWalletSummaryStatus(WalletSummaryStatusEnum.ERROR_RETRIVING_WALLET_SUMMARY);
		}
		return walletSummaryResponse;
	}

	@Override
	public WalletHistoryResponse getWalletHistory(
			WalletHistoryRequest walletHistoryRequest) {
		if (logger.isDebugEnabled()) {
			logger.debug("Trying to retrieve wallet history for wallet id : "
					+ walletHistoryRequest.getWalletId());
		}
		WalletHistoryResponse response = new WalletHistoryResponse();

		// authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(walletHistoryRequest.getSessionToken());
		if (authentication == null) {
			// invalid session token
			response.setWalletHistoryStatus(WalletHistoryStatusEnum.NO_SESSION);
			return response;
		}

		long walletId = walletHistoryRequest.getWalletId();
		
		try {
			//Wallet wallet = walletService.load(walletId);
			
			List<WalletTransaction> walletTransactionList = walletService.walletHistory(walletId, walletHistoryRequest.getFromDate(), walletHistoryRequest.getToDate(), null);
			response.setTransactionList(walletTransactionList);
			response.setWalletHistoryStatus(WalletHistoryStatusEnum.SUCCESS);

		} catch (WalletNotFoundException e) {
			response.setWalletHistoryStatus(WalletHistoryStatusEnum.INVALID_WALLET);
		} catch (PlatformException pe) {
			logger.error("Error while getting wallet history for walletId : " + walletHistoryRequest.getWalletId(), pe);
			response.setWalletHistoryStatus(WalletHistoryStatusEnum.ERROR_RETRIVING_WALLET_HISTORY);
		}

		response.setSessionToken(walletHistoryRequest.getSessionToken());

		return response;
	}
	
	@Override
	public FillWalletResponse fillWallet(
			FillWalletRequest fillWalletRequest) {
		if (logger.isDebugEnabled()) {
			logger.debug("Trying to fill wallet : "
					+ fillWalletRequest.getWalletId());
		}
		FillWalletResponse response = new FillWalletResponse();

		// authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(fillWalletRequest.getSessionToken());
		if (authentication == null) {
			// invalid session token
			response.setStatus(FillWalletStatusEnum.NO_SESSION);
			return response;
		}

		try {
			//Wallet wallet = walletService.load(fillWalletRequest.getWalletId());
			
			Money amount = new Money(fillWalletRequest.getAmount());
			
			WalletTransaction transaction = walletService.credit(fillWalletRequest.getWalletId(), amount, fillWalletRequest.getSubWallet().toString(), fillWalletRequest.getPaymentId(), fillWalletRequest.getRefundId(), null);
			
			response.setWalletId(transaction.getWallet().getId());
			response.setTransactionId(transaction.getTransactionId());
			response.setStatus(FillWalletStatusEnum.SUCCESS);
			
		} catch (WalletNotFoundException pe) {
			logger.error("No wallet exists with id : " + fillWalletRequest.getWalletId(), pe);
			response.setStatus(FillWalletStatusEnum.FAILED_TRANSACTION);
		} catch (PlatformException pe) {
			logger.error("Exception in fillwallet for wallet id: " + fillWalletRequest.getWalletId(), pe);
			response.setStatus(FillWalletStatusEnum.FAILED_TRANSACTION);
		}

		response.setSessionToken(fillWalletRequest.getSessionToken());

		return response;
	}

	@Override
	public PayResponse payFromWallet(
			PayRequest payRequest) {
		if (logger.isDebugEnabled()) {
			logger.debug("Trying to pay from wallet for order "
					+ payRequest.getOrderId());
		}
		PayResponse response = new PayResponse();

		// authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(payRequest.getSessionToken());
		if (authentication == null) {
			// invalid session token
			response.setStatus(PayStatusEnum.NO_SESSION);
			return response;
		}
		try {
			//Wallet wallet = walletService.load(fillWalletRequest.getWalletId());
			
			Money amount = new Money(payRequest.getAmount());
			
			WalletTransaction transaction = walletService.debit(payRequest.getUserId(), payRequest.getClientId(), amount, payRequest.getOrderId());
			
			response.setTransactionId(transaction.getTransactionId());
			response.setStatus(PayStatusEnum.SUCCESS);
			
		} catch (WalletNotFoundException pe) {
			logger.error("No wallet exists for user : " + payRequest.getUserId(), pe);
			response.setStatus(PayStatusEnum.INVALID_WALLET);
		} catch (InSufficientFundsException pe) {
			logger.error("Balance unavailable in wallet for user id : " + payRequest.getUserId(), pe);
			response.setStatus(PayStatusEnum.BALANCE_UNAVAILABLE);
		} catch (PlatformException pe) {
			logger.error("Exception in fillwallet for user id: " + payRequest.getUserId(), pe);
			response.setStatus(PayStatusEnum.FAILED_TRANSACTION);
		}

		response.setSessionToken(payRequest.getSessionToken());

		return response;
	}

	@Override
	public RefundResponse refundFromWallet(
			RefundRequest refundRequest) {
		if (logger.isDebugEnabled()) {
			logger.debug("Trying to refund from wallet for amount "
					+ refundRequest.getAmount());
		}
		RefundResponse response = new RefundResponse();

		// authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(refundRequest.getSessionToken());
		if (authentication == null) {
			// invalid session token
			response.setStatus(RefundStatusEnum.NO_SESSION);
			return response;
		}
		
		try {
			//Wallet wallet = walletService.load(fillWalletRequest.getWalletId());
			
			Money amount = new Money(refundRequest.getAmount());
			int refundExpiryLimit = 14;		// number of days
			WalletTransaction transaction = walletService.refund(refundRequest.getUserId(), refundRequest.getClientId(), amount, refundRequest.getRefundId(), refundRequest.getIgnoreExpiry(), refundExpiryLimit);
			response.setTransactionId(transaction.getTransactionId());
			response.setStatus(RefundStatusEnum.SUCCESS);
			
		} catch (WalletNotFoundException pe) {
			logger.error("No wallet exists for user : " + refundRequest.getUserId(), pe);
			response.setStatus(RefundStatusEnum.INVALID_WALLET);
		} catch (AlreadyRefundedException pe) {
			logger.error("No wallet exists for user : " + refundRequest.getUserId(), pe);
			response.setStatus(RefundStatusEnum.DUPLICATE_REFUND_REQUEST);
		} catch (RefundExpiredException pe) {
			logger.error("No wallet exists for user : " + refundRequest.getUserId(), pe);
			response.setStatus(RefundStatusEnum.ALREADY_REFUNDED);
		} catch (InSufficientFundsException pe) {
			logger.error("Balance unavailable in wallet for user id : " + refundRequest.getUserId(), pe);
			response.setStatus(RefundStatusEnum.BALANCE_UNAVAILABLE);
		} catch (PlatformException pe) {
			logger.error("Exception in fillwallet for user id: " + refundRequest.getUserId(), pe);
			response.setStatus(RefundStatusEnum.FAILED_TRANSACTION);
		}

		response.setSessionToken(refundRequest.getSessionToken());

		return response;
	}
	
	@Override
	public RevertResponse revertWalletTransaction(
			RevertRequest revertRequest) {
		if (logger.isDebugEnabled()) {
			logger.debug("Trying to revert wallet transaction "
					+ revertRequest.getTransactionId());
		}
		RevertResponse response = new RevertResponse();

		// authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(revertRequest.getSessionToken());
		if (authentication == null) {
			// invalid session token
			response.setStatus(RevertStatusEnum.NO_SESSION);
			return response;
		}
		try {
			//Wallet wallet = walletService.load(fillWalletRequest.getWalletId());
			
			Money amount = new Money(revertRequest.getAmount());
			
			WalletTransaction transaction = walletService.reverseTransaction(revertRequest.getUserId(), revertRequest.getClientId(), revertRequest.getTransactionId(), amount);
			response.setTransactionId(transaction.getTransactionId());
			response.setStatus(RevertStatusEnum.SUCCESS);
			
		} catch (WalletNotFoundException pe) {
			logger.error("No wallet exists for user : " + revertRequest.getUserId(), pe);
			response.setStatus(RevertStatusEnum.INVALID_WALLET);
		} catch (InvalidTransactionIdException pe) {
			logger.error("No wallet transaction with id: " + revertRequest.getTransactionId(), pe);
			response.setStatus(RevertStatusEnum.INVALID_TRANSACTION_ID);
		} catch (InSufficientFundsException pe) {
			logger.error("Balance unavailable in wallet for user id : " + revertRequest.getUserId(), pe);
			response.setStatus(RevertStatusEnum.BALANCE_UNAVAILABLE);
		} catch (PlatformException pe) {
			logger.error("Exception in fillwallet for user id: " + revertRequest.getUserId(), pe);
			response.setStatus(RevertStatusEnum.FAILED_TRANSACTION);
		}

		response.setSessionToken(revertRequest.getSessionToken());

		return response;
	}

}
