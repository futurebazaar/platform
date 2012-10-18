package com.fb.platform.wallet.dao;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.fb.commons.to.Money;
import com.fb.platform.wallet.model.Wallet;
import com.fb.platform.wallet.model.WalletGifts;
import com.fb.platform.wallet.model.WalletTransaction;
import com.fb.platform.wallet.model.WalletTransactionResultSet;

public interface WalletTransactionDao {
	
	public String insertTransaction(WalletTransaction walletTransaction);
	
	public WalletTransactionResultSet walletHistory(Wallet wallet,DateTime fromDate,DateTime toDate);
	
	public WalletTransactionResultSet walletHistory(Wallet wallet,int pageNumber,int resultPerPage);
	
	public WalletTransaction transactionById(long walletId,String transactionId);

	public WalletTransaction refundTransactionByRefundId(long walletId, long refundId);

	public Money amountAlreadyReversedByTransactionId(long walletId,
			long transactionId);

	public long insertGift(long walletId, String gitfCoupon, DateTime giftExpiry,Money amount);

	public Wallet updateGiftExpiry(Wallet wallet);

	List<WalletGifts> getWalletGifts(long walletId);

	Money getWalletRefundAmount(long walletId);

	public boolean isRefundable(Wallet wallet, long refundId, Money amount);
	
	public void createFillWalletXML(long userId, long walletId,String paymentMode, long orderId, Money amount); 

}