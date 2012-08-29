package com.fb.platform.wallet.service;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.commons.PlatformException;
import com.fb.commons.to.Money;
import com.fb.platform.wallet.model.SubWalletType;
import com.fb.platform.wallet.model.Wallet;
import com.fb.platform.wallet.to.WalletTransaction;
import com.fb.platform.wallet.to.WalletTransactionResultSet;
import com.fb.platform.wallet.service.exception.AlreadyRefundedException;
import com.fb.platform.wallet.service.exception.InSufficientFundsException;
import com.fb.platform.wallet.service.exception.InvalidTransactionIdException;
import com.fb.platform.wallet.service.exception.RefundExpiredException;
import com.fb.platform.wallet.service.exception.WalletNotFoundException;
import com.fb.platform.wallet.service.exception.WrongWalletPassword;

@Transactional
public interface WalletService {
	
	/**
	 * Returns the Wallet associated with a walletId.
	 * @param walletId
	 * @throws WalletNotFoundException When no wallet is found matching the walletId.
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return Wallet
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Wallet load(long walletId) throws WalletNotFoundException,PlatformException;
	
	/**
	 * Returns the Wallet associated with a userId,ClientId.
	 * If Not present create a new wallet.
	 * @param userId
	 * @param clientId
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return Wallet
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Wallet load(long userId,long clientId);
	
	/**
	 * Returns the Wallet Transactions associated with a wallet.
	 * @param walletId
	 * @throws WalletNotFoundException When no wallet is found matching the walletId.
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return List of Wallet Transactions
	**/
	@Transactional (propagation = Propagation.REQUIRED)
	public WalletTransactionResultSet walletHistory (long walletId,DateTime fromDate , DateTime toDate,SubWalletType subWalletType);
	
	/**
	 * Returns the Wallet Transactions associated with a wallet.
	 * @param userId
	 * @param clientId
	 * @param pageNumber
	 * @param resultPerPage
	 * @throws WalletNotFoundException When no wallet is found matching the walletId.
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return List of Wallet Transactions
	**/
	@Transactional (propagation = Propagation.REQUIRED)
	public WalletTransactionResultSet walletHistory (long userId,long clientId,int pageNumber , int resultPerPage,SubWalletType subWalletType);
	
	/**
	 * Credit the wallet with the given amount.
	 * @param walletId : Wallet Id of the wallet to be credited.
	 * @param amount : The amount to be credited to the wallet.
	 * @param subWalletType : The credited amount to be in which Sub Wallet.
	 * @param paymentId  : The payment Id of the payment for a cash transaction. 
	 * @param refundId : The refund Id if the credit via the refund mechanism.
	 * @param giftCoupon : The coupon code if filled via an client gift or EGV.
	 * @param giftExpiry : The coupon code expiry date.
	 * @throws WalletNotFoundException When no wallet is found matching the wallet.
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return WalletTransaction
	**/
	@Transactional (propagation = Propagation.REQUIRED)
	public WalletTransaction credit (long walletId, Money amount , String subWalletType , long paymentId , long refundId , String gitfCoupon , DateTime giftExpiry) throws WalletNotFoundException,PlatformException;
	
	/**
	 * Debit the wallet with the given amount.
	 * @param userId : User Id of the for whom to debit the wallet.
	 * @param clientId : Client Id though which the payment request is initiated.
	 * @param amount : The amount to be debited to the wallet.
	 * @param orderId : The order Id for which the wallet has to be credited.
	 * @param walletPassword : The wallet password is required to debit the wallet
	 * @throws WalletNotFoundException When no wallet is found matching the wallet.
	 * @throws InSufficientFundsException When  wallet not having enough funds.
	 * @throws WrongWalletPassword when the password to debit the wallet is invalid.
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return WalletTransaction 
	**/
	@Transactional (propagation = Propagation.REQUIRED)
	public WalletTransaction debit (long userId, long clientId , Money amount , long orderId,String walletPassword) throws WalletNotFoundException,InSufficientFundsException,WrongWalletPassword ,PlatformException;
	
	/**
	 * Refund requested from the wallet.
	 * @param userId : User Id of the for whom to refund from the wallet.
	 * @param clientId : Client Id though which the refund request is initiated.
	 * @param amount : The amount to be refunded to the user.
	 * @param refundId : The refundId for which the refund needs to be processed.
	 * @param ignoreExpiry :The expiry date for refund should be considered for the refund process or not. DEFAULT : True.
	 * @throws WalletNotFoundException When no wallet is found matching the wallet.
	 * @throws InSufficientFundsException When  refund not having enough funds.
	 * @throws AlreadyRefundedException When the given refund Id is already refunded.
	 * @throws RefundExpiredException refund period has expired and cannot be taken out of the wallet.
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return WalletTransaction 
	**/
	@Transactional (propagation = Propagation.REQUIRED)
	public WalletTransaction refund (long userId, long clientId , Money amount , long refundId , boolean ignoreExpiry) throws WalletNotFoundException, AlreadyRefundedException,RefundExpiredException,InSufficientFundsException,PlatformException;
	
	/**
	 * Reverse the transaction if an order is cancelled which is paid via the wallet.
	 * @param userId : User Id of the for whom to the wallet transaction to be reversed.
	 * @param clientId : Client Id though which the cancellation request is initiated.
	 * @param transactionId : The transactionId which needs to be reversed.
	 * @param amount : The amount for which the order was cancelled if only part of the order is cancelled.
	 * @throws WalletNotFoundException When no wallet is found matching the wallet.
	 * @throws InvalidTransactionIdException When the transaction doesn't exist.
	 * @throws PlatformException When an unrecoverable error happens.
	 * @return WalletTransaction 
	**/
	public WalletTransaction reverseTransaction(long userId, long clientId,String transactionId,Money amount) throws WalletNotFoundException,InvalidTransactionIdException ,PlatformException;

	
	/**
	 * Verify the wallet for given amount and password.
	 * @param userId : User Id of the for whom to debit the wallet.
	 * @param clientId : Client Id though which the payment request is initiated.
	 * @param password : The wallet password is required to debit the wallet
	 * @return Wallet
	 * @throws WalletNotFoundException When no wallet is found matching the wallet.
	 * @throws WrongWalletPassword when the password to debit the wallet is invalid.
	 * @throws PlatformException When an unrecoverable error happens.
	**/
	public Wallet verifyWallet(long userId, long clientId,String password)throws WalletNotFoundException,WrongWalletPassword ,PlatformException;

	/**
	 * Change the wallet password
	 * @param userId : User Id of the for whom to debit the wallet.
	 * @param clientId : Client Id though which the payment request is initiated.
	 * @param oldPassword : The wallet old password is required to change password
	 * @param newPassword : The wallet new password is required to change password
	 * @throws WalletNotFoundException When no wallet is found matching the wallet.
	 * @throws WrongWalletPassword when the old password for the wallet is invalid.
	 * @throws PlatformException When an unrecoverable error happens.
	**/
	public void changeWalletPassword(long userId, long clientId,String oldPassword, String newPassword) throws WalletNotFoundException,WrongWalletPassword ,PlatformException;;

	/**
	 * Reset the wallet password if wallet exists
	 * @param userId : User Id of the for whom to debit the wallet.
	 * @param clientId : Client Id though which the payment request is initiated.
	 * @throws WalletNotFoundException When no wallet is found matching the wallet.
	 * @throws PlatformException When an unrecoverable error happens.
	**/
	public void resetWalletPassword(long userId, long clientId) throws WalletNotFoundException ,PlatformException;;
	
}