package com.fb.platform.wallet.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.fb.commons.PlatformException;
import com.fb.commons.to.Money;
import com.fb.platform.wallet.dao.WalletTransactionDao;
import com.fb.platform.wallet.model.SubWalletType;
import com.fb.platform.wallet.model.TransactionType;
import com.fb.platform.wallet.model.Wallet;
import com.fb.platform.wallet.model.WalletSubTransaction;
import com.fb.platform.wallet.model.WalletTransaction;
import com.fb.platform.wallet.service.exception.InvalidTransactionIdException;
import com.fb.platform.wallet.service.exception.WalletRefundMismatchException;
import com.fb.platform.wallet.service.exception.WorngRefundIdException;

public class WalletTransactionDaoImpl implements WalletTransactionDao {
	
	private JdbcTemplate jdbcTemplate;

	private final String INSERT_NEW_TRANSACTION  =  "Insert into wallets_transaction "
			+ "(transaction_id, "
			+ "wallet_id, " 
			+ "amount, " 
			+ "transaction_type, " 
			+ "transaction_date) "
			+ "values (?,?,?,?,?)";
	
	private final String INSERT_NEW_SUBTRANSACTION = "Insert into wallets_sub_transaction "
			+ "(tran_id, "
			+ "transaction_subwallet, "
			+ "amount, "
			+ "order_id, " 
			+ "refund_id, "
			+ "payment_id, "
			+ "gift_code, "
			+ "transaction_reversal_id, "
			+"transaction_description) "
			+ "values (?,?,?,?,?,?,?,?,?)";
	
	private final String GET_TRANSACTION_BY_TRANSACTIONID = "Select " 
			+ "id, " 
			+ "transaction_id, " 
			+ "wallet_id, "
			+ "amount, "
			+ "transaction_type, "
			+ "transaction_date from "
			+ "wallets_transaction where transaction_id= ? and wallet_id=?" ;
	
	private final String GET_TRANSACTION_BY_ID = "Select "
			+ "id, "
			+ "transaction_id, "
			+ "wallet_id, " 
			+ "amount, "
			+ "transaction_type, "
			+ "transaction_date "
			+ "from wallets_transaction where id= ? and wallet_id=?" ;
	
	private final String GET_SUB_TRANSACTIONS_BY_TRANID = "Select "
			+ "id, "
			+ "tran_id, "
			+ "transaction_subwallet, "
			+ "amount, "
			+ "order_id, "
			+ "refund_id, "
			+ "payment_id, "
			+ "gift_code, "
			+ "transaction_reversal_id, "
			+ "transaction_description "
			+ "from wallets_sub_transaction "
			+ "where tran_id= ?" ;
	
	private final String GET_TRANSACTION_HISTORY = "Select "
			+ "id, "
			+ "transaction_id, "
			+ "wallet_id, "
			+ "amount, "
			+ "transaction_type, "
			+ "transaction_date "
			+ "from wallets_transaction where wallet_id= ? and transaction_date between ? and ?" ;
	
	private final String GET_SUB_TRANSACTIONS_BY_REFUNDID = "Select "
			+ "id, "
			+ "tran_id, "
			+ "transaction_subwallet, "
			+ "amount, "
			+ "order_id, "
			+ "refund_id, "
			+ "payment_id,"
			+ "gift_code, "
			+ "transaction_reversal_id, "
			+ "transaction_description "
			+ "from wallets_sub_transaction "
			+ "where refund_id= ? order by id desc limit 0,1" ;
	
	private final String GET_SUB_TRANSACTIONS_BY_TRANSACTION_REVERSAL_ID = "Select "
			+ "id, "
			+ "tran_id, "
			+ "transaction_subwallet, "
			+ "amount, "
			+ "order_id, "
			+ "refund_id, "
			+ "payment_id,"
			+ "gift_code, "
			+ "transaction_reversal_id, "
			+ "transaction_description "
			+ "from wallets_sub_transaction "
			+ "where transaction_reversal_id= ?" ;
	
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
					ps.setString(4, walletTransaction.getTransactionType().toString());
					ps.setDate(5, new Date(walletTransaction.getTimeStamp().getMillis()));
					return ps;
				}
			}, generatedKeyHolder);
			long transactionId = generatedKeyHolder.getKey().longValue();
			
			for(WalletSubTransaction walletSubTransaction : walletTransaction.getWalletSubTransaction()){
				Object[] args = new Object[9];
				args[0] = transactionId;
				args[1] = walletSubTransaction.getSubWalletType().name();
				args[2] = walletSubTransaction.getAmount().getAmount();
				args[3] = walletSubTransaction.getOrderId() == 0 ? null : walletSubTransaction.getOrderId();
				args[4] = walletSubTransaction.getRefundId() == 0 ? null : walletSubTransaction.getRefundId();
				args[5] = walletSubTransaction.getPaymentId() == 0 ? null : walletSubTransaction.getPaymentId();
				args[6] = walletSubTransaction.getGiftCode();
				args[7] = walletSubTransaction.getPaymentReversalId() == 0 ? null : walletSubTransaction.getPaymentReversalId();
				args[8] = walletSubTransaction.getNotes();
				jdbcTemplate.update(INSERT_NEW_SUBTRANSACTION,args);
			}
			return transactionUniqueId;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<WalletTransaction> walletHistory(Wallet wallet,DateTime fromDateTime, DateTime toDate) {
		try {
			Date fromdate = (fromDateTime != null) ? new Date(fromDateTime.getMillis()) : new Date(DateTime.now().minusYears(1).getMillis());
			Date todate = toDate != null ? new Date(toDate.getMillis()) : new Date(DateTime.now().getMillis());
			List<WalletTransaction> walletTransactions = jdbcTemplate.query(GET_TRANSACTION_HISTORY,
					new Object[]{wallet.getId(),fromdate,todate},
					new WalletTransactionMapper());
			
			for(WalletTransaction walletTransaction : walletTransactions){
				List<WalletSubTransaction> walletSubTransactions = jdbcTemplate.query(GET_SUB_TRANSACTIONS_BY_TRANID,
						new Object[] {walletTransaction.getId()},
						new WalletSubTransactionMapper());
				walletTransaction.getWalletSubTransaction().addAll(walletSubTransactions);
			}
			return walletTransactions;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public WalletTransaction transactionById(long walletId, String transactionId) {
		try {
			WalletTransaction walletTransaction = jdbcTemplate.queryForObject(GET_TRANSACTION_BY_TRANSACTIONID,
				new Object[]{transactionId,walletId},
				new WalletTransactionMapper());	
			List<WalletSubTransaction> walletSubTransactions = jdbcTemplate.query(GET_SUB_TRANSACTIONS_BY_TRANID,
					new Object[] {walletTransaction.getId()},
					new WalletSubTransactionMapper());
			walletTransaction.getWalletSubTransaction().addAll(walletSubTransactions);			
			return walletTransaction;
		} catch (EmptyResultDataAccessException e) {
			throw new InvalidTransactionIdException();
		} catch (PlatformException e) {
			throw new PlatformException();
		}
	}
	
	private static class WalletTransactionMapper implements RowMapper<WalletTransaction>{

		@Override
		public WalletTransaction mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			WalletTransaction walletTransaction = new WalletTransaction();
			walletTransaction.setId(rs.getLong("id"));
			walletTransaction.setAmount(new Money(rs.getBigDecimal("amount")));
			walletTransaction.setTimeStamp(new DateTime(rs.getDate("transaction_date")));
			walletTransaction.setTransactionId(rs.getString("transaction_id"));
			walletTransaction.setTransactionType(TransactionType.valueOf(rs.getString("transaction_type")));
			return walletTransaction;
		}		
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
			this.jdbcTemplate = jdbcTemplate;
	}
	
	private static class WalletSubTransactionMapper implements RowMapper<WalletSubTransaction>{

		@Override
		public WalletSubTransaction mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			WalletSubTransaction walletsubTransaction = new WalletSubTransaction();
			walletsubTransaction.setTransactionId(rs.getLong("tran_id"));
			walletsubTransaction.setAmount(new Money(rs.getBigDecimal("amount")));
			walletsubTransaction.setGiftCode(rs.getString("gift_code"));
			walletsubTransaction.setOrderId(rs.getLong("order_id"));
			walletsubTransaction.setPaymentId(rs.getLong("payment_id"));
			walletsubTransaction.setRefundId(rs.getLong("refund_id"));
			walletsubTransaction.setPaymentReversalId(rs.getLong("transaction_reversal_id"));
			walletsubTransaction.setNotes(rs.getString("transaction_description"));
			walletsubTransaction.setSubWalletType(SubWalletType.valueOf(rs.getString("transaction_subwallet")));
			return walletsubTransaction;
		}		
	}
	@Override
	public WalletTransaction refundTransactionByRefundId(long walletId,
			long refundId) {		
		try{
			WalletSubTransaction walletSubTransaction = null;
			try {
				walletSubTransaction = jdbcTemplate.queryForObject(GET_SUB_TRANSACTIONS_BY_REFUNDID,
						new Object[] {refundId},
						new WalletSubTransactionMapper());
			}catch (EmptyResultDataAccessException e){
				throw new WorngRefundIdException("No available refund with this id");
			}
			try {
				WalletTransaction walletTransaction = jdbcTemplate.queryForObject(GET_TRANSACTION_BY_ID,
							new Object[]{walletSubTransaction.getTransactionId(),walletId},
							new WalletTransactionMapper());
				walletTransaction.getWalletSubTransaction().add(walletSubTransaction);
				return walletTransaction;
			}catch (EmptyResultDataAccessException e){
				throw new WalletRefundMismatchException("This refundId not associated with this wallet");
			}			
		}catch (WorngRefundIdException e) {
			throw new WorngRefundIdException("No available refund with this id");
		}catch (WalletRefundMismatchException e) {
			throw new WalletRefundMismatchException("This refundId not associated with this wallet");
		}catch (PlatformException e) {
			throw new PlatformException("No available refund with this id");
		}		
	}

	@Override
	public Money amountAlreadyReversedByTransactionId(long id,
			long transactionId) {
		try{
			Money amount = new Money(new BigDecimal("0.00"));
			List<WalletSubTransaction> walletSubTransactions = jdbcTemplate.query(GET_SUB_TRANSACTIONS_BY_TRANSACTION_REVERSAL_ID,
					new Object[] {transactionId},
					new WalletSubTransactionMapper());
			for(WalletSubTransaction walletSubTransaction : walletSubTransactions){
				amount = amount.plus(walletSubTransaction.getAmount());
			}
			return amount;
		}catch (EmptyResultDataAccessException e) {
			return new Money(new BigDecimal("0.00"));
		} catch (PlatformException e) {
			return new Money(new BigDecimal("0.00"));
		}
	}
}
