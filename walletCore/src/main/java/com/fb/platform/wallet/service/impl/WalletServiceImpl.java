package com.fb.platform.wallet.service.impl;

import java.util.List;

import org.joda.time.DateTime;

import com.fb.commons.PlatformException;
import com.fb.commons.to.Money;
import com.fb.platform.wallet.dao.WalletDao;
import com.fb.platform.wallet.dao.WalletTransactionDao;
import com.fb.platform.wallet.model.SubWalletType;
import com.fb.platform.wallet.model.Wallet;
import com.fb.platform.wallet.to.WalletTransaction;
import com.fb.platform.wallet.service.WalletService;
import com.fb.platform.wallet.service.exception.InSufficientFundsException;
import com.fb.platform.wallet.service.exception.InvalidTransaction;
import com.fb.platform.wallet.service.exception.WalletNOtFoundException;
import com.fb.platform.wallet.to.CreditWalletStatus;

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
	public CreditWalletStatus credit(long walletId, Money amount,
			SubWalletType subWalletType, long paymentId, long refundId,
			String gitfCoupon) throws WalletNOtFoundException,
			PlatformException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WalletTransaction debit(long userId,
			long clientId, Money amount, long orderId)
			throws WalletNOtFoundException, InSufficientFundsException,
			PlatformException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WalletTransaction refund(long userId,
			long clientId, Money amount, long refundId, boolean ignoreExpiry)
			throws WalletNOtFoundException, InSufficientFundsException,
			PlatformException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WalletTransaction reverseTransaction(
			long userId, long clientId, String transactionId)
			throws WalletNOtFoundException, InvalidTransaction,
			PlatformException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
