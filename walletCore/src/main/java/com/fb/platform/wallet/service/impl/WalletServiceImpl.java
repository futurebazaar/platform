package com.fb.platform.wallet.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.PlatformException;
import com.fb.commons.to.Money;
import com.fb.platform.wallet.cache.WalletCacheAccess;
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
import com.fb.platform.wallet.service.exception.InvalidTransaction;
import com.fb.platform.wallet.service.exception.RefundExpiredException;
import com.fb.platform.wallet.service.exception.WalletNotFoundException;

public class WalletServiceImpl implements WalletService {

	@Autowired
	private WalletDao walletDao;
	@Autowired
	private WalletTransactionDao walletTransactionDao;
	@Autowired
	private WalletCacheAccess walletCacheAccess;
	
	@Override
	public Wallet load(long walletId) throws WalletNotFoundException,PlatformException {
		Wallet wallet = walletCacheAccess.get(walletId);
		try{
			wallet = walletDao.load(walletId);
		}catch (WalletNotFoundException e){
			throw new WalletNotFoundException("No wallet found with this walletId");
		}
		if (wallet != null){
			cacheWallet(walletId, wallet);
		}else{
			throw new WalletNotFoundException("No wallet found with this walletId");
		}
		return wallet;
	}

	@Override
	public Wallet load(long userId, long clientId) throws PlatformException {
		return (load(userId,clientId,true));			
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
			SubWalletType subWalletType, long paymentId, long refundId,
			String gitfCoupon) throws WalletNotFoundException,
			PlatformException {
		try {
			WalletTransaction walletTransactionRes = new WalletTransaction();
			Wallet wallet = load(walletId);
			com.fb.platform.wallet.model.WalletTransaction walletTransaction = wallet
					.credit(amount, subWalletType, paymentId, refundId,gitfCoupon);
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
			long clientId, Money amount, long refundId, boolean ignoreExpiry,int expiryDays)
			throws WalletNotFoundException, AlreadyRefundedException,RefundExpiredException,InSufficientFundsException,
			PlatformException {
		try {
			WalletTransaction walletTransactionRes =  new WalletTransaction();
			Wallet wallet = load(userId,clientId,false);
			com.fb.platform.wallet.model.WalletTransaction walletTransaction = walletTransactionDao.refundTransactionByRefundId(wallet.getId(),refundId);
			if(walletTransaction.getTransactionType().equals(TransactionType.CREDIT)){
				WalletSubTransaction walletSubTransaction = walletTransaction.getWalletSubTransaction().get(0);
				if(walletSubTransaction.getAmount().eq(amount)){
					if(ignoreExpiry || walletTransaction.getTimeStamp().plusDays(expiryDays).isAfterNow()){
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
			long userId, long clientId, String transactionId)
			throws WalletNotFoundException, InvalidTransaction,
			PlatformException {
		try{
			WalletTransaction walletTransactionRes = new WalletTransaction();
			Wallet wallet = load(userId,clientId,false);
			com.fb.platform.wallet.model.WalletTransaction walletTransaction = walletTransactionDao.transactionById(wallet.getId(), transactionId);
			com.fb.platform.wallet.model.WalletTransaction walletTransactionNew = wallet.reverseTransaction(walletTransaction);
			walletTransactionRes.setWallet(walletDao.update(wallet));
			walletTransactionRes.setTransactionId(walletTransactionDao.insertTransaction(walletTransactionNew));
			return walletTransactionRes;				
		} catch (WalletNotFoundException e){
			throw new WalletNotFoundException("No wallet with this wallet id");		
		} catch (PlatformException e) {
			throw new PlatformException("an unhandeled exception has occured while trnasaction reversal");
		}
	}
	public void setWalletDao(WalletDao walletDao) {
		this.walletDao = walletDao;
	}
	public void setWalletTransactionDao(WalletTransactionDao walletTransactionDao) {
		this.walletTransactionDao = walletTransactionDao;
	}
	
	private WalletTransaction walletTransactionModeltoTO (com.fb.platform.wallet.model.WalletTransaction walletTransaction){
		WalletTransaction walletTransactionRes = new WalletTransaction();
		walletTransactionRes.setTransactionId(walletTransaction.getTransactionId());
		walletTransactionRes.setWallet(walletTransaction.getWallet());
		return walletTransactionRes;
	}
	
	private void cacheWallet(long walletId, Wallet wallet) {
		try {
			walletCacheAccess.lock(walletId);
			if (walletCacheAccess.get(walletId) == null) {
				walletCacheAccess.put(walletId, wallet);
			}
		} finally {
			walletCacheAccess.unlock(walletId);
		}
	}
}
