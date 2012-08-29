package com.fb.platform.wallet.dao.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.fb.commons.PlatformException;
import com.fb.commons.to.Money;
import com.fb.platform.wallet.dao.WalletDao;
import com.fb.platform.wallet.dao.WalletTransactionDao;
import com.fb.platform.wallet.model.SubWalletType;
import com.fb.platform.wallet.model.TransactionType;
import com.fb.platform.wallet.model.Wallet;
import com.fb.platform.wallet.model.WalletGifts;
import com.fb.platform.wallet.model.WalletGiftsHistory;
import com.fb.platform.wallet.model.WalletSubTransaction;
import com.fb.platform.wallet.model.WalletTransaction;
import com.fb.platform.wallet.model.WalletTransactionResultSet;
import com.fb.platform.wallet.service.exception.InSufficientFundsException;
import com.fb.platform.wallet.service.exception.InvalidTransactionIdException;
import com.fb.platform.wallet.service.exception.WalletRefundMismatchException;
import com.fb.platform.wallet.service.exception.WorngRefundIdException;

public class WalletTransactionDaoImpl implements WalletTransactionDao {
	
	private JdbcTemplate jdbcTemplate;
	private WalletDao walletDao;
	private int refundExpiryDays;
	private Log log = LogFactory.getLog(WalletTransactionDaoImpl.class);

	private final String INSERT_NEW_TRANSACTION  =  "Insert into wallets_transaction "
			+ "(transaction_id, "
			+ "wallet_id, " 
			+ "amount, " 
			+ "transaction_type, " 
			+ "transaction_date, "
			+ "transaction_note, "
			+ "wallet_balance) "
			+ "values (?,?,?,?,?,?,?)";
	
	private final String INSERT_NEW_SUBTRANSACTION = "Insert into wallets_sub_transaction "
			+ "(tran_id, "
			+ "transaction_subwallet, "
			+ "amount, "
			+ "order_id, " 
			+ "refund_id, "
			+ "payment_id, "
			+ "gift_id, "
			+ "transaction_reversal_id, "
			+"transaction_description) "
			+ "values (?,?,?,?,?,?,?,?,?)";
	
	private final String GET_TRANSACTION_BY_TRANSACTIONID = "Select " 
			+ "id, " 
			+ "transaction_id, " 
			+ "wallet_id, "
			+ "amount, "
			+ "transaction_type, "
			+ "transaction_date, "
			+ "transaction_note, "
			+ "wallet_balance "
			+ "from wallets_transaction where transaction_id= ? and wallet_id=?" ;
	
	private final String GET_TRANSACTION_BY_ID = "Select "
			+ "id, "
			+ "transaction_id, "
			+ "wallet_id, " 
			+ "amount, "
			+ "transaction_type, "
			+ "transaction_date, "
			+ "transaction_note, "
			+ "wallet_balance "
			+ "from wallets_transaction where id= ? and wallet_id=?" ;
	
	private final String GET_SUB_TRANSACTIONS_BY_TRANID = "Select "
			+ "id, "
			+ "tran_id, "
			+ "transaction_subwallet, "
			+ "amount, "
			+ "order_id, "
			+ "refund_id, "
			+ "payment_id, "
			+ "gift_id, "
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
			+ "transaction_date, "
			+ "transaction_note, "
			+ "wallet_balance "
			+ "from wallets_transaction where wallet_id= ? and transaction_date between ? and ?" ;
	
	private final String GET_TRANSACTION_HISTORY_PAGINATED = "Select "
			+ "id, "
			+ "transaction_id, "
			+ "wallet_id, "
			+ "amount, "
			+ "transaction_type, "
			+ "transaction_date, "
			+ "transaction_note, "
			+ "wallet_balance "
			+ "from wallets_transaction where wallet_id= ? order by transaction_date desc LIMIT ?,?" ;
	
	private final String GET_TOTAL_TRANSACTION_WALLETID = "Select count(*) "
			+ "from wallets_transaction where wallet_id= ?" ;
	
	private final String GET_SUB_TRANSACTIONS_BY_REFUNDID = "Select "
			+ "id, "
			+ "tran_id, "
			+ "transaction_subwallet, "
			+ "amount, "
			+ "order_id, "
			+ "refund_id, "
			+ "payment_id,"
			+ "gift_id, "
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
			+ "gift_id, "
			+ "transaction_reversal_id, "
			+ "transaction_description "
			+ "from wallets_sub_transaction "
			+ "where transaction_reversal_id= ?" ;
	
	private final String GET_SUB_TRANSACTION_ID_BY_TRANSACTION_ID_AND_SUBWALLET_TYPE = "Select "
			+ "id "
			+ "from wallets_sub_transaction "
			+ "where tran_id= ? and transaction_subwallet = ?" ;
	
	private final String INSERT_WALLET_GIFT = "Insert into wallets_gifts "
			+ "(wallet_id ,gift_code ,gift_expiry ,is_expired ,amount_remaining ) values "
			+ "(?,?,?,?,?)";
	
	private final String INSERT_WALLET_GIFT_HISTORY = "Insert into wallets_gifts_transaction_history "
			+ "(gift_id,sub_transaction_id,amount ) values "
			+ "(?,?,?)";
	
	private final String INSERT_WALLET_REFUND_CREDIT = "Insert into wallets_refunds_credit_history "
			+ "(wallet_id,sub_transaction_id,refund_id,amount,credit_date,amount_remaining,is_used) values "
			+ "(?,?,?,?,?,?,?)";
	
	private final String INSERT_WALLET_REFUND_DEBIT = "Insert into wallets_refunds_debit_history "
			+ "(wallet_id,sub_transaction_id,amount,debit_date,refund_credit_id) values "
			+ "(?,?,?,?,?)";
	
	private final String UPDATE_WALLET_REFUND_CREDIT = "Update wallets_refunds_credit_history "
			+ "set amount_remaining = ? , is_used = 1 where id = ?" ;
	
	private final String GET_WALLET_GIFT_HISTORY_BY_SUB_TRANSACTION = "select  gift_id,sub_transaction_id,amount from wallets_gifts_transaction_history "
			+ "where sub_transaction_id = ? ";
		
	private final String GET_WALLET_GIFT_WALLET_ID = "select  id,wallet_id,gift_code ,gift_expiry ,is_expired ,amount_remaining from wallets_gifts "
			+ "where wallet_id = ? and is_expired = 0 and amount_remaining > 0 "
			+ "order by gift_expiry";
	
	private final String GET_WALLET_REFUND_CREDIT_WALLET_ID = "select id,wallet_id,sub_transaction_id,refund_id ,amount,credit_date ,amount_remaining,is_used "
			+ "from wallets_refunds_credit_history "
			+ "where wallet_id = ? and amount_remaining > 0 "
			+ "order by credit_date";
	
	private final String GET_WALLETREFUNDCREDIT_BY_WALLETID_REFUNDID = "select id,wallet_id,sub_transaction_id,refund_id ,amount,credit_date ,amount_remaining,is_used "
			+ "from wallets_refunds_credit_history "
			+ "where wallet_id = ? and refund_id = ? "
			+ "order by credit_date";
	
	private final String GET_REFUND_AMOUNT_WALLET_ID = "select sum(amount_remaining) "
			+ "from wallets_refunds_credit_history "
			+ "where wallet_id = ? and amount_remaining > 0 " ;// and is_used = 0 clause removed as this can be refunded now 
			//+ "and DATE_SUB(NOW(), INTERVAL ? DAY) <= credit_date";	this clause removed as no expiry for refunds now
	
	private final String GET_WALLET_GIFT_BY_ID = "select  id,wallet_id,gift_code ,gift_expiry ,is_expired ,amount_remaining from wallets_gifts "
			+ "where id= ? ";
	
	private final String UPDATE_WALLET_GIFT_REMAINING = "update wallets_gifts set amount_remaining = ? ,is_expired = 0 where id = ?";
	
	private final String UPDATE_WALLET_GIFT_REMAINING_EXPIRE = "update wallets_gifts set amount_remaining = ? ,is_expired = 1 where id = ?";
	
	private final String UPDATE_PAYMENT_REFUNDS = "update payments_refund set amount = ? ,modified_on = NOW() , status =? where id = ?";
	
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
					ps.setTimestamp(5, new Timestamp(walletTransaction.getTimeStamp().getMillis()));
					ps.setString(6, walletTransaction.getTransactionNote());
					ps.setBigDecimal(7, walletTransaction.getWalletBalance().getAmount());
					return ps;
				}
			}, generatedKeyHolder);
			final long transactionId = generatedKeyHolder.getKey().longValue();
			final KeyHolder generatedKeyHolderSubTransaction = new GeneratedKeyHolder();
			for(final WalletSubTransaction walletSubTransaction : walletTransaction.getWalletSubTransaction()){
				jdbcTemplate.update(new PreparedStatementCreator() {
					@Override
					public PreparedStatement createPreparedStatement(Connection con)
							throws SQLException {
						PreparedStatement ps = con.prepareStatement(INSERT_NEW_SUBTRANSACTION , new String[]{"id"});
						ps.setLong(1, transactionId);
						ps.setString(2, walletSubTransaction.getSubWalletType().name());
						ps.setBigDecimal(3, walletSubTransaction.getAmount().getAmount());
						ps.setObject(4, walletSubTransaction.getOrderId() == 0 ? null : walletSubTransaction.getOrderId());
						ps.setObject(5, walletSubTransaction.getRefundId() == 0 ? null : walletSubTransaction.getRefundId());
						ps.setObject(6, walletSubTransaction.getPaymentId() == 0 ? null : walletSubTransaction.getPaymentId());
						ps.setObject(7, walletSubTransaction.getGiftId() == 0 ? null : walletSubTransaction.getGiftId());
						ps.setObject(8, walletSubTransaction.getPaymentReversalId() == 0 ? null : walletSubTransaction.getPaymentReversalId());
						ps.setString(9, walletSubTransaction.getNotes());
						return ps;
					}
				}, generatedKeyHolderSubTransaction);
				walletSubTransaction.setId(generatedKeyHolderSubTransaction.getKey().longValue());
				processSubTransaction(walletTransaction.getWallet().getId(),walletSubTransaction,walletTransaction.getTransactionType());
			}			
			return transactionUniqueId;
		}catch (Exception e) {
			e.printStackTrace();
			log.error("An exception while inserting the transaction record" + e);
			throw new PlatformException("An exception while inserting the transaction record "+e);
		}
	}
	@Override
	public WalletTransactionResultSet walletHistory(Wallet wallet,DateTime fromDateTime, DateTime toDate) {
		try {
			WalletTransactionResultSet walletTransactionResultSet = new WalletTransactionResultSet();
			Timestamp fromdate = (fromDateTime != null) ? new Timestamp(fromDateTime.getMillis()) : new Timestamp(DateTime.now().minusYears(1).getMillis());
			Timestamp todate = toDate != null ? new Timestamp(toDate.getMillis()) : new Timestamp(DateTime.now().getMillis());
			List<WalletTransaction> walletTransactions = jdbcTemplate.query(GET_TRANSACTION_HISTORY,
					new Object[]{wallet.getId(),fromdate,todate},
					new WalletTransactionMapper());
			
			for(WalletTransaction walletTransaction : walletTransactions){
				List<WalletSubTransaction> walletSubTransactions = jdbcTemplate.query(GET_SUB_TRANSACTIONS_BY_TRANID,
						new Object[] {walletTransaction.getId()},
						new WalletSubTransactionMapper());
				walletTransaction.getWalletSubTransaction().addAll(walletSubTransactions);
			}
			int resultSize = jdbcTemplate.queryForInt(GET_TOTAL_TRANSACTION_WALLETID,new Object[] {wallet.getId()}); 
			walletTransactionResultSet.setTotalNumberTransations(resultSize);
			walletTransactionResultSet.setWalletTransactions(walletTransactions);
			return walletTransactionResultSet;
		} catch (Exception e) {
			log.error("An exception while fetching the wallet history" + e);
			throw new PlatformException("An exception while fetching wallet history "+e);
		}
	}
	

	@Override
	public WalletTransactionResultSet walletHistory(Wallet wallet, int pageNumber,
			int resultPerPage) {
		try {
			WalletTransactionResultSet walletTransactionResultSet = new WalletTransactionResultSet();
			if(pageNumber <= 0){
				pageNumber = 1;
			}
			List<WalletTransaction> walletTransactions = jdbcTemplate.query(GET_TRANSACTION_HISTORY_PAGINATED,
					new Object[]{wallet.getId(),((pageNumber-1)*resultPerPage),resultPerPage},
					new WalletTransactionMapper());
			for(WalletTransaction walletTransaction : walletTransactions){
				List<WalletSubTransaction> walletSubTransactions = jdbcTemplate.query(GET_SUB_TRANSACTIONS_BY_TRANID,
						new Object[] {walletTransaction.getId()},
						new WalletSubTransactionMapper());
				walletTransaction.getWalletSubTransaction().addAll(walletSubTransactions);
			}
			int resultSize = jdbcTemplate.queryForInt(GET_TOTAL_TRANSACTION_WALLETID,new Object[] {wallet.getId()}); 
			walletTransactionResultSet.setTotalNumberTransations(resultSize);
			walletTransactionResultSet.setWalletTransactions(walletTransactions);
			return walletTransactionResultSet;
		} catch (Exception e) {
			log.error("An exception while fetching the wallet history" + e);
			throw new PlatformException("An exception while fetching wallet history "+e);
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
		}
	}
	
	private static class WalletTransactionMapper implements RowMapper<WalletTransaction>{

		@Override
		public WalletTransaction mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			WalletTransaction walletTransaction = new WalletTransaction();
			walletTransaction.setId(rs.getLong("id"));
			walletTransaction.setAmount(new Money(rs.getBigDecimal("amount")));
			walletTransaction.setTimeStamp(new DateTime(rs.getTimestamp("transaction_date")));
			walletTransaction.setTransactionId(rs.getString("transaction_id"));
			walletTransaction.setTransactionType(TransactionType.valueOf(rs.getString("transaction_type")));
			walletTransaction.setTransactionNote(rs.getString("transaction_note"));
			walletTransaction.setWalletBalance(new Money(rs.getBigDecimal("wallet_balance")));
			return walletTransaction;
		}		
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
			this.jdbcTemplate = jdbcTemplate;
	}
	
	public void setWalletDao(WalletDao walletDao) {
		this.walletDao = walletDao;
	}
	
	public void setRefundExpiryDays(int refundExpiryDays) {
		this.refundExpiryDays = refundExpiryDays;
	}
	
	private static class WalletSubTransactionMapper implements RowMapper<WalletSubTransaction>{

		@Override
		public WalletSubTransaction mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			WalletSubTransaction walletsubTransaction = new WalletSubTransaction();
			walletsubTransaction.setId(rs.getLong("id"));
			walletsubTransaction.setTransactionId(rs.getLong("tran_id"));
			walletsubTransaction.setAmount(new Money(rs.getBigDecimal("amount")));
			walletsubTransaction.setGiftId(rs.getLong("gift_id"));
			walletsubTransaction.setOrderId(rs.getLong("order_id"));
			walletsubTransaction.setPaymentId(rs.getLong("payment_id"));
			walletsubTransaction.setRefundId(rs.getLong("refund_id"));
			walletsubTransaction.setPaymentReversalId(rs.getLong("transaction_reversal_id"));
			walletsubTransaction.setNotes(rs.getString("transaction_description"));
			walletsubTransaction.setSubWalletType(SubWalletType.valueOf(rs.getString("transaction_subwallet")));
			return walletsubTransaction;
		}		
	}
	
	private static class WalletGiftsMapper implements RowMapper<WalletGifts> {

		@Override
		public WalletGifts mapRow(ResultSet rs, int rowNum) throws SQLException {
			WalletGifts walletGifts = new WalletGifts();
			walletGifts.setAmountRemaining(new Money(rs.getBigDecimal("amount_remaining")));
			walletGifts.setGiftCode(rs.getString("gift_code"));
			walletGifts.setGiftId(rs.getLong("id"));
			walletGifts.setWalletId(rs.getLong("wallet_id"));
			walletGifts.setExpired(rs.getBoolean("is_expired"));
			walletGifts.setGiftExpiry(new DateTime(rs.getTimestamp("gift_expiry")));
			return walletGifts;
		}
		
	}
	
	private static class WalletGiftsHistoryMapper implements RowMapper<WalletGiftsHistory> {

		@Override
		public WalletGiftsHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
			WalletGiftsHistory walletGiftsHistory = new WalletGiftsHistory();
			walletGiftsHistory.setAmount(new Money(rs.getBigDecimal("amount")));
			walletGiftsHistory.setGiftId(rs.getLong("gift_id"));
			walletGiftsHistory.setSubTransactionId(rs.getLong("sub_transaction_id"));
			return walletGiftsHistory;
		}
		
	}
	
	private static class WalletRefundCreditMapper implements RowMapper<WalletRefundCredit> {

		@Override
		public WalletRefundCredit mapRow(ResultSet rs, int rowNum) throws SQLException {
			WalletRefundCredit walletRefundCredit = new WalletRefundCredit(
					rs.getLong("id"),
					rs.getLong("wallet_id"),
					rs.getLong("sub_transaction_id"),
					rs.getInt("refund_id"),
					new Money(rs.getBigDecimal("amount")),
					new DateTime(rs.getTimestamp("credit_date")),
					rs.getBoolean("is_used"),
					new Money(rs.getBigDecimal("amount_remaining")));
			return walletRefundCredit;
		}
		
	}
	
	@Override
	public WalletTransaction refundTransactionByRefundId(long walletId,
			long refundId) {		
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
		}
	}

	@Override
	public long insertGift(final long walletId, final String gitfCoupon, final DateTime giftExpiry,final Money amount) {
		
		final KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(INSERT_WALLET_GIFT , new String[]{"id"});
				ps.setLong(1, walletId);
				ps.setString(2, gitfCoupon);
				ps.setTimestamp(3, new Timestamp(giftExpiry.getMillis()));
				ps.setBoolean(4, false);
				ps.setBigDecimal(5, amount.getAmount());
				return ps;
			}
		}, generatedKeyHolder);
		long giftId = generatedKeyHolder.getKey().longValue();
		return giftId;
	}
	
	private void insertWalletGiftHistory(long subTransactionId,long giftId , Money amount){
		jdbcTemplate.update(INSERT_WALLET_GIFT_HISTORY,new Object[] {giftId,subTransactionId,amount.getAmount()});
	}
	
	private void updateWalletGiftAmount(long giftId, Money amount , boolean isExpiry){
		if(isExpiry){
			jdbcTemplate.update(UPDATE_WALLET_GIFT_REMAINING_EXPIRE,new Object[] {amount.getAmount(),giftId});
		}else{
			jdbcTemplate.update(UPDATE_WALLET_GIFT_REMAINING,new Object[] {amount.getAmount(),giftId});
		}
	}
	
	private void debitWalletGift(long walletId,long subTransactionId,Money amount){
		Money amountToDebit = amount;
		List<WalletGifts> walletGifts = jdbcTemplate.query(GET_WALLET_GIFT_WALLET_ID, new Object[]{walletId},new WalletGiftsMapper());
		for(WalletGifts walletGift : walletGifts){
			if(walletGift.getAmountRemaining().gteq(amountToDebit)){
				insertWalletGiftHistory(subTransactionId, walletGift.getGiftId(), amountToDebit.negate());
				updateWalletGiftAmount(walletGift.getGiftId(), walletGift.getAmountRemaining().minus(amountToDebit),false);
				amountToDebit = amountToDebit.minus(amountToDebit);
			}else{
				insertWalletGiftHistory(subTransactionId, walletGift.getGiftId(), walletGift.getAmountRemaining().negate());
				updateWalletGiftAmount(walletGift.getGiftId(),new Money(new BigDecimal("0.00")),false);
				amountToDebit = amountToDebit.minus(walletGift.getAmountRemaining());
			}
			if(amountToDebit.lteq(new Money(new BigDecimal("0.00")))){
				break;
			}			
		}		
	}
	
	private void revertGiftBySubtransaction(Money amount,long newSubtransactionId ,long subTransactionIdtoRevert) {
		List<WalletGiftsHistory> walletGiftsHistories = jdbcTemplate.query(GET_WALLET_GIFT_HISTORY_BY_SUB_TRANSACTION, new Object[]{subTransactionIdtoRevert},new WalletGiftsHistoryMapper());
		for(WalletGiftsHistory walletGiftsHistory : walletGiftsHistories){
			WalletGifts walletGifts = jdbcTemplate.queryForObject(GET_WALLET_GIFT_BY_ID,new Object[]{walletGiftsHistory.getGiftId()}, new WalletGiftsMapper());
			insertWalletGiftHistory(newSubtransactionId, walletGiftsHistory.getGiftId(), walletGiftsHistory.getAmount().negate());
			updateWalletGiftAmount(walletGiftsHistory.getGiftId(), walletGifts.getAmountRemaining().plus(walletGiftsHistory.getAmount().negate()), false);
		}
	}

	@Override
	public Wallet updateGiftExpiry(Wallet wallet) {
		for (WalletGifts walletGift : getWalletGifts(wallet.getId())){			
			if(walletGift.getGiftExpiry().isBeforeNow()){
				WalletTransaction walletTransaction = wallet.debit(walletGift.getAmountRemaining(), walletGift.getGiftId(),"The gift : " + walletGift.getGiftCode() + " expired");
				insertTransaction(walletTransaction);
				updateWalletGiftAmount(walletGift.getGiftId(),new Money(new BigDecimal("0.00")),true);
				insertWalletGiftHistory(walletTransaction.getWalletSubTransaction().get(0).getId(), walletGift.getGiftId(), walletGift.getAmountRemaining().negate());
			}else{
				break;
			}			
		}
		return walletDao.update(wallet);
	}
	
	private void processSubTransaction (long walletId , WalletSubTransaction walletSubTransaction , TransactionType transactionType){
		if (walletSubTransaction.getSubWalletType().equals(SubWalletType.GIFT)){
			if (transactionType.equals(TransactionType.CREDIT)){
				if (walletSubTransaction.getPaymentReversalId() == 0){
					insertWalletGiftHistory(walletSubTransaction.getId(), walletSubTransaction.getGiftId(), walletSubTransaction.getAmount());
				}else{
					long subWalletIdToRevert = jdbcTemplate.queryForLong(GET_SUB_TRANSACTION_ID_BY_TRANSACTION_ID_AND_SUBWALLET_TYPE,new Object[]{walletSubTransaction.getPaymentReversalId(),SubWalletType.GIFT.toString()});
					revertGiftBySubtransaction(walletSubTransaction.getAmount(),walletSubTransaction.getId(),subWalletIdToRevert);
				}
			}
			else{
				debitWalletGift(walletId,walletSubTransaction.getId(),walletSubTransaction.getAmount());
			}
		}
		if (walletSubTransaction.getSubWalletType().equals(SubWalletType.REFUND)){
			if (transactionType.equals(TransactionType.CREDIT)){
				insertWalletRefundCredit(walletId,walletSubTransaction);
			}
			else{
				debitWalletRefund(walletId,walletSubTransaction);
			}
		}
	}
	private void debitWalletRefund(long walletId,WalletSubTransaction walletSubTransaction) {
		Money amountToDebit = walletSubTransaction.getAmount();
		List<WalletRefundCredit> walletRefundCredits = null;
		if(walletSubTransaction.getRefundId() > 0){
			walletRefundCredits = jdbcTemplate.query(GET_WALLETREFUNDCREDIT_BY_WALLETID_REFUNDID, new Object[] {walletId,walletSubTransaction.getRefundId()},new WalletRefundCreditMapper());
		}else{
			walletRefundCredits = jdbcTemplate.query(GET_WALLET_REFUND_CREDIT_WALLET_ID, new Object[] {walletId},new WalletRefundCreditMapper());
		}
		for(WalletRefundCredit walletRefundCredit : walletRefundCredits){
			if(walletRefundCredit.getAmountRemaining().gt(amountToDebit)){
				insertWalletRefundDebit(walletId,walletSubTransaction,amountToDebit,walletRefundCredit.getId());
				updateWalletRefundCredit(walletRefundCredit.getId(), walletRefundCredit.getAmountRemaining().minus(amountToDebit));
				updatePaymetRefund(walletSubTransaction.getRefundId(), walletRefundCredit.getAmountRemaining().minus(amountToDebit) ,"wallet");
				amountToDebit = amountToDebit.minus(amountToDebit);
			}else{
				insertWalletRefundDebit(walletId,walletSubTransaction,walletRefundCredit.getAmountRemaining(),walletRefundCredit.getId());
				updateWalletRefundCredit(walletRefundCredit.getId(),new Money(new BigDecimal("0.00")));
				updatePaymetRefund(walletSubTransaction.getRefundId(), new Money(new BigDecimal("0.00")) ,"closed");
				amountToDebit = amountToDebit.minus(walletRefundCredit.getAmountRemaining());
			}
			if(amountToDebit.lteq(new Money(new BigDecimal("0.00")))){
				break;
			}			
		}
	}
	private void insertWalletRefundCredit(long walletId,WalletSubTransaction walletSubTransaction) {
		jdbcTemplate.update(INSERT_WALLET_REFUND_CREDIT,new Object[] {walletId,walletSubTransaction.getId(),walletSubTransaction.getRefundId(),walletSubTransaction.getAmount().getAmount(),new Timestamp(System.currentTimeMillis()),walletSubTransaction.getAmount().getAmount(),0});
	}
	private void insertWalletRefundDebit(long walletId,WalletSubTransaction walletSubTransaction,Money amount,long walletRefundCreditId) {
		jdbcTemplate.update(INSERT_WALLET_REFUND_DEBIT,new Object[] {walletId,walletSubTransaction.getId(),amount.getAmount(),new Date(System.currentTimeMillis()),walletRefundCreditId});		
	}
	private void updateWalletRefundCredit(long walletRefundCreditId,Money amount) {
		jdbcTemplate.update(UPDATE_WALLET_REFUND_CREDIT,new Object[] {amount.getAmount(),walletRefundCreditId});		
	}
	
	private void updatePaymetRefund(long refundId, Money amount , String status) {
		jdbcTemplate.update(UPDATE_PAYMENT_REFUNDS,new Object[] {amount.getAmount(),status,refundId});		
	}
	@Override
	public List<WalletGifts> getWalletGifts(long walletId){
		return jdbcTemplate.query(GET_WALLET_GIFT_WALLET_ID, new Object[]{walletId},new WalletGiftsMapper());
	}
	
	@Override
	public Money getWalletRefundAmount(long walletId){
		try{
			return(new Money(jdbcTemplate.queryForObject(GET_REFUND_AMOUNT_WALLET_ID, new Object[]{walletId}, BigDecimal.class)));
		}catch (Exception e) {
			return(new Money(new BigDecimal("0.00")));
		}					
	}
	
	@SuppressWarnings({"serial","unused"})
	private static class WalletRefundCredit implements Serializable {
		long Id;
		long walletId;
		long subTransactionid;
		int refundId;
		Money amount;
		DateTime creditDate;
		boolean isUsed = false;
		Money amountRemaining ;
		
		public WalletRefundCredit(long id, long walletId , 
				long subTransactionid,int refundId ,Money amount,DateTime creditDate,
				boolean isUsed , Money amountRemaining){
			this.Id = id;
			this.walletId = walletId;
			this.subTransactionid = subTransactionid;
			this.refundId = refundId;
			this.amount = amount;
			this.creditDate = creditDate;
			this.isUsed = isUsed;
			this.amountRemaining = amountRemaining;
		}

		public long getId() {
			return Id;
		}
		public Money getAmountRemaining() {
			return amountRemaining;
		}
		
	}

	@Override
	public boolean isRefundable(Wallet wallet, long refundId, Money amount) {
		try{
			List<WalletRefundCredit> walletRefundCredits = jdbcTemplate.query(GET_WALLETREFUNDCREDIT_BY_WALLETID_REFUNDID, new Object[] {wallet.getId(),refundId},new WalletRefundCreditMapper());
			if(walletRefundCredits.size() > 0){
				Money moneyTobeRefunded = amount;
				for (WalletRefundCredit walletRefundCredit : walletRefundCredits){
					if (moneyTobeRefunded.isPlus() && walletRefundCredit.getAmountRemaining().isPlus()){
						moneyTobeRefunded = moneyTobeRefunded.minus(walletRefundCredit.getAmountRemaining());
					}
				}
				if(moneyTobeRefunded.isPlus()){
					throw new InSufficientFundsException();
				}else {
					return true;
				}
			} else{
				throw new WorngRefundIdException();
			}
		}catch (WorngRefundIdException e) {
			throw new WorngRefundIdException("No available refund with this id for this wallet"); 
		}
		
	}

}
