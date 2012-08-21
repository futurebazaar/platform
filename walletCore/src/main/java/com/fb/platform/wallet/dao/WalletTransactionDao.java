package com.fb.platform.wallet.dao;

import java.util.List;

import org.joda.time.DateTime;

import com.fb.commons.to.Money;
import com.fb.platform.wallet.model.Wallet;
import com.fb.platform.wallet.model.WalletGifts;
import com.fb.platform.wallet.model.WalletTransaction;

public interface WalletTransactionDao {
	
	public String insertTransaction(WalletTransaction walletTransaction);
	
	public List<WalletTransaction> walletHistory(Wallet wallet,DateTime fromDate,DateTime toDate);
	
	public List<WalletTransaction> walletHistory(Wallet wallet,int pageNumber,int resultPerPage);
	
	public WalletTransaction transactionById(long walletId,String transactionId);

	public WalletTransaction refundTransactionByRefundId(long walletId, long refundId);

	public Money amountAlreadyReversedByTransactionId(long walletId,
			long transactionId);

	public long insertGift(long walletId, String gitfCoupon, DateTime giftExpiry,Money amount);

	public Wallet updateGiftExpiry(Wallet wallet);

	List<WalletGifts> getWalletGifts(long walletId);

	Money getWalletRefundAmount(long walletId);

}