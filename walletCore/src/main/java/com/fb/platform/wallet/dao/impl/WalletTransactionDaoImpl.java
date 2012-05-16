package com.fb.platform.wallet.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.fb.platform.wallet.dao.WalletTransactionDao;
import com.fb.platform.wallet.model.Wallet;
import com.fb.platform.wallet.model.WalletTransaction;

public class WalletTransactionDaoImpl implements WalletTransactionDao {
	
	private JdbcTemplate jdbcTemplate;

	private final String INSERT_NEW_TRANSACTION  =  "Insert into wallets_transactions "
			+ "(transaction_id,wallet_id,amount,transaction_type,transaction_date) "
			+ "values(?,?,?,?,?)";
	
	private final String INSERT_NEW_SUBTRANSACTION = "Insert into wallets_sub_transaction "
			+ "(tran_id,transaction_subwallet,amount,order_id,refund_id,payment_id,transaction_reversal_id,transaction_description) "
			+ "(?,?,?,?,?,?,?,?)";
	
	private final String GET_TRANSACTION_HISTORY = "Select id,transaction_id,wallet_id,amount,transaction_type,transaction_date from "
			+ "wallets_transactions where wallet_id= ? and transaction_date between ? and ?" ;
	
	@Override
	public String insertTransaction(final WalletTransaction walletTransaction) {
		try{
			final KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
			final String transactionUniqueId = UUID.randomUUID().toString();
			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					PreparedStatement ps = con.prepareStatement(INSERT_NEW_TRANSACTION , new String[]{"id"});
					ps.setString(1, transactionUniqueId);
					ps.setLong(2, walletTransaction.getWallet().getId());
					ps.setBigDecimal(3, walletTransaction.getAmount().getAmount());
					ps.setString(4, walletTransaction.getTransactionType().name());
					ps.setDate(5, new Date(walletTransaction.getTimeStamp().getMillis()));
					return ps;
				}
			}, generatedKeyHolder);
			long transactionId = generatedKeyHolder.getKey().longValue();
			
			/*Object[] args = new Object[7];
			args[0] = transactionId;
			args[1] = walletTransaction.getSubWalletType().name();
			args[2] = walletTransaction.
			jdbcTemplate.update(INSERT_NEW_SUBTRANSACTION,new);*/
			return transactionUniqueId;
		}catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<WalletTransaction> walletHistory(Wallet wallet,DateTime fromDateTime, DateTime toDate) {
		// TODO Auto-generated method stub
		return null;
	}

}
