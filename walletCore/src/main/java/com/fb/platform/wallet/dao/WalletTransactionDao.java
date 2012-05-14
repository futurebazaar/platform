package com.fb.platform.wallet.dao;

import java.util.List;

import org.joda.time.DateTime;

import com.fb.platform.wallet.model.Wallet;
import com.fb.platform.wallet.model.WalletTransaction;

public interface WalletTransactionDao {
	
	public boolean insertTransaction(WalletTransaction walletTransaction);
	
	public List<WalletTransaction> walletHistory(Wallet wallet,DateTime fromDate,DateTime toDate);

}