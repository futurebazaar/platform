package com.fb.platform.wallet.dao.impl;

import java.util.List;

import org.joda.time.DateTime;

import com.fb.platform.wallet.dao.WalletTransactionDao;
import com.fb.platform.wallet.model.Wallet;
import com.fb.platform.wallet.model.WalletTransaction;

public class WalletTransactionDaoImpl implements WalletTransactionDao {

	private final String INSERT_NEW_TRANSACTION  =  "Insert into wallets_transactions "
			+ "(transaction_id,wallet_id,amount,transaction_type,transaction_date) "
			+ "values(?,?,?,?,?)";
	
	private final String INSERT_NEW_SUBTRANSACTION = "Insert into wallets_sub_transaction "
			+ "(tran_id,transaction_subwallet,amount,order_id,refund_id,payment_id,transaction_reversal_id,transaction_description) "
			+ "(?,?,?,?,?,?,?,?)";
	
	private final String GET_TRANSACTION_HISTORY = "Select id,transaction_id,wallet_id,amount,transaction_type,transaction_date from "
			+ "wallets_transactions where wallet_id= ? and transaction_date between ? and ?" ;
	
	@Override
	public boolean insertTransaction(WalletTransaction walletTransaction) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<WalletTransaction> walletHistory(Wallet wallet,DateTime fromDateTime, DateTime toDate) {
		// TODO Auto-generated method stub
		return null;
	}

}
