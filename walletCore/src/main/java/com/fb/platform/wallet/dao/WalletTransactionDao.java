package com.fb.platform.wallet.dao;

import java.util.List;

import org.joda.time.DateTime;

import com.fb.platform.wallet.model.Wallet;
import com.fb.platform.wallet.model.WalletTransaction;

public interface WalletTransactionDao {
	
	public String insertTransaction(WalletTransaction walletTransaction);
	
	public List<WalletTransaction> walletHistory(Wallet wallet,DateTime fromDate,DateTime toDate);
	
	public WalletTransaction transactionById(long walletId,String transactionId);

	public WalletTransaction refundTransactionByRefundId(long walletId, long refundId);

}