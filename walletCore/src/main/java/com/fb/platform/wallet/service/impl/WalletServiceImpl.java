package com.fb.platform.wallet.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.PlatformException;
import com.fb.commons.to.Money;
import com.fb.platform.wallet.dao.WalletDao;
import com.fb.platform.wallet.dao.WalletTransactionDao;
import com.fb.platform.wallet.model.SubWalletType;
import com.fb.platform.wallet.model.TransactionType;
import com.fb.platform.wallet.model.Wallet;
import com.fb.platform.wallet.model.WalletSubTransaction;
import com.fb.platform.wallet.to.WalletTransaction;
import com.fb.platform.wallet.service.WalletService;
import com.fb.platform.wallet.service.exception.AlreadyRefundedException;
import com.fb.platform.wallet.service.exception.InSufficientFundsException;
import com.fb.platform.wallet.service.exception.InvalidTransactionIdException;
import com.fb.platform.wallet.service.exception.RefundExpiredException;
import com.fb.platform.wallet.service.exception.WalletNotFoundException;

public class WalletServiceImpl implements WalletService {

	@Autowired
	private WalletDao walletDao;
	@Autowired
	private WalletTransactionDao walletTransactionDao;
	
	private int refundExpiryDays;
	
	@Override
	public Wallet load(long walletId) throws WalletNotFoundException,PlatformException {
		try{
			Wallet wallet = walletDao.load(walletId);
			return wallet;
		}catch (WalletNotFoundException e){
			throw new WalletNotFoundException("No wallet found with this walletId");
		}	
	}

	@Override
	public Wallet load(long userId, long clientId) throws PlatformException {
		Wallet wallet = load(userId,clientId,true);
        return walletTransactionDao.updateGiftExpiry(wallet);			
	}
	
	private Wallet load(long userId,long clientId,boolean createNew) 
			throws WalletNotFoundException,PlatformException {
		try{
			Wallet wallet = walletDao.load(userId,clientId,createNew);
			if(wallet != null){
				return wallet;
			}else{
				throw new PlatformException("No wallet found or created with this userId:" + userId + " and clientId ::" + clientId);
			}
		}catch (WalletNotFoundException e) {
			throw new WalletNotFoundException();
		}catch (PlatformException e){
			throw new PlatformException();
		}
	}

	@Override
	public List<WalletTransaction> walletHistory(long walletId,
			DateTime fromDate, DateTime toDate, SubWalletType subWalletType) {
		try{
			Wallet wallet = load(walletId);
			List<WalletTransaction> walletTransactions = new ArrayList<WalletTransaction>();
			for (com.fb.platform.wallet.model.WalletTransaction walletTransaction : walletTransactionDao.walletHistory(wallet, fromDate, toDate)){
				walletTransactions.add(walletTransactionModeltoTO(walletTransaction));
			}
			return walletTransactions;
		}catch (WalletNotFoundException e){
			throw new WalletNotFoundException("Exception no wallet for this wallet id");
		}catch (PlatformException e) {
			throw new PlatformException("Exception while loading wallet transactions");
		}
	}

	@Override
	public WalletTransaction credit(long walletId, Money amount,
			String subWalletType, long paymentId, long refundId,
			String gitfCoupon,DateTime giftExpiry) throws WalletNotFoundException,
			PlatformException {
		try {
			WalletTransaction walletTransactionRes = new WalletTransaction();
			Wallet wallet = load(walletId);
			long gift_id = 0L;
			if (SubWalletType.valueOf(subWalletType).equals(SubWalletType.GIFT)){
				gift_id = walletTransactionDao.insertGift(wallet.getId(),gitfCoupon,giftExpiry,amount);
			}
			com.fb.platform.wallet.model.WalletTransaction walletTransaction = wallet
					.credit(amount, SubWalletType.valueOf(subWalletType), paymentId, refundId,gift_id);
			walletTransactionRes.setWallet(walletDao.update(wallet));
			walletTransactionRes.setTransactionId(walletTransactionDao.insertTransaction(walletTransaction));
			return walletTransactionRes;
		} catch (WalletNotFoundException e){
			throw new WalletNotFoundException("No wallet with this wallet id");
		}
		catch (PlatformException e) {
			throw new PlatformException("No wallet with this wallet id");
		}
	}

	@Override
	public WalletTransaction debit(long userId,
			long clientId, Money amount, long orderId)
			throws WalletNotFoundException, InSufficientFundsException,
			PlatformException {
		try{
			WalletTransaction walletTransactionRes = new WalletTransaction();
			Wallet wallet = load(userId,clientId,false);
			// update expiry data for gifts prior to debiting the account
			wallet = walletTransactionDao.updateGiftExpiry(wallet);
			if(wallet.isSufficientFund(amount)){
				com.fb.platform.wallet.model.WalletTransaction walletTransaction = wallet
						.debit(amount, orderId);
				walletTransactionRes.setWallet(walletDao.update(wallet));
				walletTransactionRes.setTransactionId(walletTransactionDao.insertTransaction(walletTransaction));
				return walletTransactionRes;
			}else{
				throw new InSufficientFundsException("Insufficient fund in wallet");
			} 			
		} catch (InSufficientFundsException e){
			throw new InSufficientFundsException("Not enough fund in the wallet");
		} 
		catch (WalletNotFoundException e){
			throw new WalletNotFoundException("No wallet with this wallet id");
		}
		catch (PlatformException e) {
			throw new PlatformException("No wallet with this wallet id");
		}
		
	}

	@Override
	public WalletTransaction refund(long userId,
			long clientId, Money amount, long refundId, boolean ignoreExpiry)
			throws WalletNotFoundException, AlreadyRefundedException,RefundExpiredException,InSufficientFundsException,
			PlatformException {
		try {
			WalletTransaction walletTransactionRes =  new WalletTransaction();
			Wallet wallet = load(userId,clientId,false);
			com.fb.platform.wallet.model.WalletTransaction walletTransaction = walletTransactionDao.refundTransactionByRefundId(wallet.getId(),refundId);
			if(walletTransaction.getTransactionType().equals(TransactionType.CREDIT)){
				WalletSubTransaction walletSubTransaction = walletTransaction.getWalletSubTransaction().get(0);
				if(walletSubTransaction.getAmount().eq(amount)){
					if(ignoreExpiry || walletTransaction.getTimeStamp().plusDays(refundExpiryDays).isAfterNow()){
						walletTransactionRes.setTransactionId(walletTransactionDao.insertTransaction(wallet.refund(amount,refundId)));
						walletTransactionRes.setWallet(walletDao.update(wallet));							
					}else {
						throw new RefundExpiredException();
					}
				}else{
					throw new InSufficientFundsException();
				}
			}else{
				throw new AlreadyRefundedException(); 
			}
			return walletTransactionRes;
		} catch (AlreadyRefundedException e){
			throw new AlreadyRefundedException("This Refund has already been refunded");
		} catch (RefundExpiredException e){
			throw new RefundExpiredException("Refund Period has expired");
		} catch (InSufficientFundsException e){
			throw new InSufficientFundsException("Not enough fund in the wallet");
		} catch (WalletNotFoundException e){
			throw new WalletNotFoundException("No wallet with this wallet id");
		} catch (PlatformException e) {
			throw new PlatformException("An unrecoverable exception has occured while refund");
		}
	}

	@Override
	public WalletTransaction reverseTransaction(
			long userId, long clientId, String transactionId,Money amount)
			throws WalletNotFoundException, InvalidTransactionIdException,
			PlatformException {
		try{
			WalletTransaction walletTransactionRes = new WalletTransaction();
			Wallet wallet = load(userId,clientId,false);
			com.fb.platform.wallet.model.WalletTransaction walletTransaction = walletTransactionDao.transactionById(wallet.getId(), transactionId);
			Money moneyAlreadyReversed = walletTransactionDao.amountAlreadyReversedByTransactionId(wallet.getId(), walletTransaction.getId());
			Money amountToBeReversed = amount != null ? amount : walletTransaction.getAmount();
			if(walletTransaction.getAmount().gteq(amountToBeReversed.plus(moneyAlreadyReversed))){
				com.fb.platform.wallet.model.WalletTransaction walletTransactionNew = wallet.reverseTransaction(walletTransaction,amountToBeReversed,moneyAlreadyReversed);
				walletTransactionRes.setWallet(walletDao.update(wallet));
				walletTransactionRes.setTransactionId(walletTransactionDao.insertTransaction(walletTransactionNew));
				return walletTransactionRes;
			}else{
				throw new InSufficientFundsException();
			}
		}  catch (InSufficientFundsException e){
			throw new InSufficientFundsException("The amount is invalid for reversing the transaction");		
		} catch (WalletNotFoundException e){
			throw new WalletNotFoundException("No wallet with this wallet id");		
		}  catch (InvalidTransactionIdException e) {
			throw new InvalidTransactionIdException("This is an invalid transaction Id");
		} 	catch (PlatformException e) {
			e.printStackTrace();
			throw new PlatformException("an unhandeled exception has occured while trnasaction reversal");
		}
	}
	public void setWalletDao(WalletDao walletDao) {
		this.walletDao = walletDao;
	}
	public void setWalletTransactionDao(WalletTransactionDao walletTransactionDao) {
		this.walletTransactionDao = walletTransactionDao;
	}
	public void setRefundExpiryDays(int refundExpiryDays) {
		this.refundExpiryDays = refundExpiryDays;
	}

	private WalletTransaction walletTransactionModeltoTO (com.fb.platform.wallet.model.WalletTransaction walletTransaction){
		WalletTransaction walletTransactionRes = new WalletTransaction();
		walletTransactionRes.setTransactionId(walletTransaction.getTransactionId());
		walletTransactionRes.setWallet(walletTransaction.getWallet());
		walletTransactionRes.setAmount(walletTransaction.getAmount());
		walletTransactionRes.setTimeStamp(walletTransaction.getTimeStamp());
		walletTransactionRes.setTransactionType(walletTransaction.getTransactionType());
		List<com.fb.platform.wallet.to.WalletSubTransaction> subTransactionList = new ArrayList<com.fb.platform.wallet.to.WalletSubTransaction>();
		for(com.fb.platform.wallet.model.WalletSubTransaction subTransaction: walletTransaction.getWalletSubTransaction()){
			com.fb.platform.wallet.to.WalletSubTransaction sTransaction = new com.fb.platform.wallet.to.WalletSubTransaction();
			sTransaction.setAmount(subTransaction.getAmount());
			//sTransaction.setGiftCode(subTransaction.getGiftCode()); //TO DO :: get gift code from gift id
			sTransaction.setOrderId(subTransaction.getOrderId());
			sTransaction.setPaymentId(subTransaction.getPaymentId());
			sTransaction.setPaymentReversalId(subTransaction.getPaymentReversalId());
			sTransaction.setRefundId(subTransaction.getRefundId());
			sTransaction.setSubWalletType(subTransaction.getSubWalletType());
			
			subTransactionList.add(sTransaction);
		}
		walletTransactionRes.setWalletSubTransaction(subTransactionList);
		return walletTransactionRes;
	}
}
