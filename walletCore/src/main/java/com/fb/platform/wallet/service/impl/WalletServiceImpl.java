package com.fb.platform.wallet.service.impl;

import java.util.List;

import org.joda.time.DateTime;

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
import com.fb.platform.wallet.service.exception.InSufficientFundsException;
import com.fb.platform.wallet.service.exception.InvalidTransaction;
import com.fb.platform.wallet.service.exception.RefundExpiredException;
import com.fb.platform.wallet.service.exception.WalletNOtFoundException;

public class WalletServiceImpl implements WalletService {

	private WalletDao walletDao;
	private WalletTransactionDao walletTransactionDao;
	
	@Override
	public Wallet load(long walletId) throws WalletNOtFoundException,PlatformException {
		try{
			Wallet wallet = walletDao.load(walletId);
			if(wallet != null){
				return wallet;
			}else{
				throw new WalletNOtFoundException("No wallet found with this walletId");
			}
		}catch (PlatformException e) {
			throw new PlatformException("Exception while loading wallet");
		}
	}

	@Override
	public Wallet load(long userId, long clientId) throws PlatformException {
		try{
			Wallet wallet = walletDao.load(userId,clientId);
			return wallet;
		}catch (PlatformException e) {
			throw new PlatformException("Exception while loading wallet");
		}
	}

	@Override
	public List<WalletTransaction> walletHistory(long walletId,
			DateTime fromDate, DateTime toDate, SubWalletType subWalletType) {
		try{
			return null;			
		}catch (WalletNOtFoundException e){
			throw new WalletNOtFoundException("Exception no wallet for this wallet id");
		}catch (PlatformException e) {
			throw new PlatformException("Exception while loading wallet transactions");
		}
	}

	@Override
	public WalletTransaction credit(long walletId, Money amount,
			SubWalletType subWalletType, long paymentId, long refundId,
			String gitfCoupon) throws WalletNOtFoundException,
			PlatformException {
		try {
			WalletTransaction walletTransactionRes = new WalletTransaction();
			Wallet wallet = load(walletId);
			if (wallet != null) {
				com.fb.platform.wallet.model.WalletTransaction walletTransaction = wallet
						.credit(amount, subWalletType, paymentId, refundId,gitfCoupon);
				walletTransactionRes.setWallet(walletDao.update(wallet));
				walletTransactionRes.setTransactionId(walletTransactionDao.insertTransaction(walletTransaction));
				return walletTransactionRes;
			} else {
				throw new WalletNOtFoundException(
						"No wallet with this wallet id");
			}
		} catch (PlatformException e) {
			throw new PlatformException(
					"No wallet with this wallet id");
		}
	}

	@Override
	public WalletTransaction debit(long userId,
			long clientId, Money amount, long orderId)
			throws WalletNOtFoundException, InSufficientFundsException,
			PlatformException {
		try{
			WalletTransaction walletTransactionRes = new WalletTransaction();
			Wallet wallet = load(userId,clientId);
			if (wallet != null) {
				if(wallet.isSufficientFund(amount)){
					com.fb.platform.wallet.model.WalletTransaction walletTransaction = wallet
							.debit(amount, orderId);
					walletTransactionRes.setWallet(walletDao.update(wallet));
					walletTransactionRes.setTransactionId(walletTransactionDao.insertTransaction(walletTransaction));
					return walletTransactionRes;
				}else{
					throw new InSufficientFundsException("Insufficient fund in wallet");
				}
			} else {
				throw new WalletNOtFoundException(
						"No wallet with this wallet id");
			}			
		}catch (Exception e) {
			return null;
		}
		
	}

	@Override
	public WalletTransaction refund(long userId,
			long clientId, Money amount, long refundId, boolean ignoreExpiry)
			throws WalletNOtFoundException, InSufficientFundsException,
			PlatformException {
		try {
			WalletTransaction walletTransactionRes =  new WalletTransaction();
			Wallet wallet = load(userId,clientId);
			if (wallet != null) {
				com.fb.platform.wallet.model.WalletTransaction walletTransaction = walletTransactionDao.refundTransactionByRefundId(wallet.getId(),refundId);
				if(walletTransaction.getTransactionType().equals(TransactionType.CREDIT)){
					WalletSubTransaction walletSubTransaction = walletTransaction.getWalletSubTransaction().get(0);
					if(walletSubTransaction.getAmount().gteq(amount)){
						if(ignoreExpiry || walletTransaction.getTimeStamp().plusDays(14).isAfterNow()){
							walletTransactionRes.setTransactionId(walletTransactionDao.insertTransaction(wallet.refund(amount)));
							walletTransactionRes.setWallet(walletDao.update(wallet));							
						}else {
							throw new RefundExpiredException();
						}
					}else{
						throw new InSufficientFundsException();
					}
				}else{
					throw new PlatformException(); 
				}
			}else{
				throw new WalletNOtFoundException(
						"No wallet with this wallet id");
			}
			return walletTransactionRes;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public WalletTransaction reverseTransaction(
			long userId, long clientId, String transactionId)
			throws WalletNOtFoundException, InvalidTransaction,
			PlatformException {
		try{
			WalletTransaction walletTransactionRes = new WalletTransaction();
			Wallet wallet = load(userId,clientId);
			if (wallet != null) {
				com.fb.platform.wallet.model.WalletTransaction walletTransaction = walletTransactionDao.transactionById(wallet.getId(), transactionId);
				com.fb.platform.wallet.model.WalletTransaction walletTransactionNew = wallet.reverseTransaction(walletTransaction);
				walletTransactionRes.setWallet(walletDao.update(wallet));
				walletTransactionRes.setTransactionId(walletTransactionDao.insertTransaction(walletTransactionNew));
				return walletTransactionRes;				
			} else {
				throw new WalletNOtFoundException(
						"No wallet with this wallet id");
			}			
		}catch (Exception e) {
			return null;
		}
	}
	public void setWalletDao(WalletDao walletDao) {
		this.walletDao = walletDao;
	}
	public void setWalletTransactionDao(WalletTransactionDao walletTransactionDao) {
		this.walletTransactionDao = walletTransactionDao;
	}	
}
