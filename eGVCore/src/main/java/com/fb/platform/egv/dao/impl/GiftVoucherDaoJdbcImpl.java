/**
 * 
 */
package com.fb.platform.egv.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.fb.commons.PlatformException;
import com.fb.commons.to.Money;
import com.fb.platform.egv.dao.GiftVoucherDao;
import com.fb.platform.egv.exception.GiftVoucherException;
import com.fb.platform.egv.exception.GiftVoucherNotFoundException;
import com.fb.platform.egv.model.GiftVoucher;
import com.fb.platform.egv.model.GiftVoucherDates;
import com.fb.platform.egv.model.GiftVoucherStatusEnum;
import com.fb.platform.egv.model.GiftVoucherUse;

/**
 * @author keith
 *
 */
public class GiftVoucherDaoJdbcImpl implements GiftVoucherDao {

	private JdbcTemplate jdbcTemplate;

	private Log log = LogFactory.getLog(GiftVoucherDaoJdbcImpl.class);

	private static final String GET_GIFT_VOUCHER_BY_ID_QUERY = 
			"SELECT " +
			"	id, " +
			"	valid_from, " +
			"	valid_till, " +
			"	number, " +
			"	pin, " +
			"	status," +
			"	order_item_id, " +
			"	user_id, " +
			"	email, " +
			"	amount, " +
			"	created_on, " +
			"	last_modified_on " +
			"FROM gift_voucher where id = ?";

	private static final String GET_GIFT_VOUCHER_BY_NUMBER_QUERY = 
			"SELECT " +
			"	id, " +
			"	valid_from, " +
			"	valid_till, " +
			"	number, " +
			"	pin, " +
			"	status," +
			"	order_item_id, " +
			"	user_id, " +
			"	email, " +
			"	amount, " +
			"	created_on, " +
			"	last_modified_on " +
			"FROM gift_voucher where number = ?";

	
	private static final String GET_GIFT_VOUCHER_BY_NUMBER_PIN_QUERY = 
			"SELECT " +
			"	id, " +
			"	valid_from, " +
			"	valid_till, " +
			"	number, " +
			"	pin, " +
			"	status," +
			"	order_item_id, " +
			"	user_id, " +
			"	email, " +
			"	amount, " +
			"	created_on, " +
			"	last_modified_on " +
			"FROM gift_voucher where number = ? and pin = ?";

	
	private static final String CREATE_GIFT_VOUCHER_QUERY = 
			"INSERT INTO " +
			"	gift_voucher(" +
			"	number,"	+
			"	pin,"	+
			"	email,"	+
			"	status,"	+
			"	amount,"	+
			"	user_id,"	+
			"	order_item_id,"	+
			"	created_on,"	+
			"	last_modified_on,"	+
			"	valid_from,"	+
			"	valid_till"	+
			"	) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,? )";

	private static final String UPDATE_GIFT_VOUCHER_STATE_QUERY = 
			"UPDATE " +
			"	gift_voucher set " +
			"	last_modified_on = ? , "	+
			"	status = ? "	+
			" where number = ? ";

	private static final String GET_GIFT_VOUCHER_USAGE_BY_NUMBER_QUERY = 
			"SELECT " +
			"	gift_voucher_number, " +
			"	used_by, " +
			"	order_id," +
			"	used_on, " +
			"	amount_used " +
			"FROM gift_voucher_usage where gift_voucher_number = ?";

	
	private static final String CREATE_GIFT_VOUCHER_USAGE_QUERY = 
			"INSERT INTO " +
			"	gift_voucher_usage(" +
			"	gift_voucher_number,"	+
			"	used_by,"	+
			"	order_id,"	+
			"	used_on,"	+
			"	amount_used"	+
			"	) VALUES ( ?, ?, ?, ?, ? )";
	
	private static final String DELETE_GIFT_VOUCHER_USAGE_QUERY = 
			"DELETE FROM " +
			"	gift_voucher_usage where " +
			"	gift_voucher_number = ?  "	+
			"	AND used_by = ? "	+
			"	AND order_id = ?";

	
	@Override
	public GiftVoucher load(int giftVoucherId) {
		
		if(log.isDebugEnabled()) {
			log.debug("Geting the details for the Gift Voucher id : " + giftVoucherId);
		}
		
		GiftVoucher eGV = null;
		GiftVoucherRowCallBackHandler gvrcbh = new GiftVoucherRowCallBackHandler(); 
		jdbcTemplate.query(GET_GIFT_VOUCHER_BY_ID_QUERY, gvrcbh, giftVoucherId);
		if (gvrcbh.giftVoucher == null) {
			log.error("No Gift Voucher found for the id - " + giftVoucherId);
			return null;
		}
		eGV = gvrcbh.giftVoucher;

		return eGV;
	}
	
	@Override
	public GiftVoucher load(long giftVoucherNumber) {
		
		if(log.isDebugEnabled()) {
			log.debug("Geting the details for the Gift Voucher Number : " + giftVoucherNumber);
		}
		
		GiftVoucher eGV = null;
		GiftVoucherRowCallBackHandler gvrcbh = new GiftVoucherRowCallBackHandler(); 
		jdbcTemplate.query(GET_GIFT_VOUCHER_BY_NUMBER_QUERY, gvrcbh, new Object[] {giftVoucherNumber});
		if(gvrcbh.giftVoucher == null) {
			//no gift voucher in the database for this number
			log.error("No Gift Voucher found for the eGV " + giftVoucherNumber );
			throw new GiftVoucherNotFoundException();
		}
		eGV = gvrcbh.giftVoucher;

		return eGV;
	}
	
	@Override
	public boolean createGiftVoucher(final long gvNumber, final String pin,final String email, final int userId, final BigDecimal amount, final GiftVoucherStatusEnum status, final int orderItemId) {
		
		if(log.isDebugEnabled()) {
			log.debug("Insert in the gift_voucher table ");
		}
		int rowAffected = 0;
		KeyHolder giftVoucherKeyHolder = new GeneratedKeyHolder();
		
		try {
			rowAffected = jdbcTemplate.update(new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(CREATE_GIFT_VOUCHER_QUERY, new String [] {"id"});
					ps.setLong(1,gvNumber);
					ps.setString(2,pin);
					ps.setString(3,email);
					ps.setString(4,status.toString());
					ps.setBigDecimal(5, amount);
					ps.setInt(6, userId);
					ps.setInt(7, orderItemId);
					
					Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
					
					ps.setTimestamp(8, timestamp);
					ps.setTimestamp(9, timestamp);
					ps.setTimestamp(10, timestamp);
					ps.setTimestamp(11, new java.sql.Timestamp(DateTime.now().plusMonths(6).getMillis()));
					return ps;
				}
			}, giftVoucherKeyHolder);
		} catch (DuplicateKeyException e) {
			log.error("Duplicate key insertion exception " + e);
			throw new PlatformException("Duplicate key insertion exception "+e);
		}
		
		return rowAffected>0 ? true : false;
	}
	
	@Override
	public boolean createGiftVoucherUse(final long gvNumber, final int userId, final int orderId,final BigDecimal amountUsed) {
		
		if(log.isDebugEnabled()) {
			log.debug("Insert in the gift_voucher table ");
		}
		int rowAffected = 0;
		KeyHolder gvUsesKeyHolder = new GeneratedKeyHolder();
		
		try {
			rowAffected = jdbcTemplate.update(new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(CREATE_GIFT_VOUCHER_USAGE_QUERY, new String [] {"id"});
					
					ps.setLong(1,gvNumber);
					ps.setInt(2, userId);
					ps.setInt(3, orderId);
					Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
					ps.setTimestamp(4, timestamp);
					ps.setBigDecimal(5, amountUsed);
					
					return ps;
				}
			}, gvUsesKeyHolder);
		} catch (DuplicateKeyException e) {
			log.error("Duplicate key insertion exception " + e);
			throw new PlatformException("Duplicate key insertion exception "+e);
		}
		
		return rowAffected>0 ? true : false;
	}

	@Override
	public GiftVoucherUse loadUse(long giftVoucherNumber) {
		if(log.isDebugEnabled()) {
			log.debug("Geting the details for the Gift Voucher : GV NUmber " + giftVoucherNumber);
		}
		
		GiftVoucherUse gvUse = null;
		GiftVoucherUseRowCallBackHandler gvurcbh = new GiftVoucherUseRowCallBackHandler(); 
		jdbcTemplate.query(GET_GIFT_VOUCHER_USAGE_BY_NUMBER_QUERY, gvurcbh, giftVoucherNumber);
		if (gvurcbh.giftVoucherUse == null) {
			log.error("No Gift Voucher found : GV NUmber - " + giftVoucherNumber);
			throw new GiftVoucherException("eGV Error : Unable to find any usage entry for eGV num : " + giftVoucherNumber);
		}
		gvUse = gvurcbh.giftVoucherUse;

		return gvUse;

	}

	@Override
	public void changeState(long giftVoucherNumber,
			GiftVoucherStatusEnum newState) {
		
		log.info("Update state of Gift Voucher " +giftVoucherNumber + " to => " + newState);
		
		Timestamp modifiedOnTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
		int gvUpdated = jdbcTemplate.update(UPDATE_GIFT_VOUCHER_STATE_QUERY, new Object[] {modifiedOnTimestamp, newState.toString(),Long.toString(giftVoucherNumber)});
		if (gvUpdated != 1) {
			log.error("Error while updating the Gift Voucher : " + giftVoucherNumber);
			throw new PlatformException("Error while updating the Gift Voucher : " + giftVoucherNumber);
		}
	}
	
	@Override
	public boolean deleteGiftVoucher(long gvNumber, int userId, int orderItemId) {
		return false;
	}

	
	private static class GiftVoucherRowCallBackHandler implements RowCallbackHandler {

		private GiftVoucher giftVoucher;

		@Override
		public void processRow(ResultSet rs) throws SQLException {
			giftVoucher = new GiftVoucher();
			giftVoucher.setId(rs.getInt("id"));
			giftVoucher.setEmail(rs.getString("email"));
			giftVoucher.setAmount(new Money(rs.getBigDecimal("amount")));
			giftVoucher.setStatus(GiftVoucherStatusEnum.valueOf(rs.getString("status")));
			giftVoucher.setNumber(rs.getString("number"));
			giftVoucher.setPin(rs.getString("pin"));
			giftVoucher.setOrderItemId(rs.getInt("order_item_id"));
			giftVoucher.setUserId(rs.getInt("user_id"));
			
			GiftVoucherDates dates = new GiftVoucherDates();
			Timestamp validFromTS = rs.getTimestamp("valid_from");
			if (validFromTS != null) {
				dates.setValidFrom(new DateTime(validFromTS));
			}
			Timestamp validTillTS = rs.getTimestamp("valid_till");
			if (validTillTS != null) {
				dates.setValidTill(new DateTime(validTillTS));
			}
			Timestamp createdOnTS = rs.getTimestamp("created_on");
			if (createdOnTS != null) {
				dates.setCreatedOn(new DateTime(createdOnTS));
			}
			Timestamp modifiedOnTS = rs.getTimestamp("last_modified_on");
			if (modifiedOnTS != null) {
				dates.setLastModifiedOn(new DateTime(modifiedOnTS));
			}
			giftVoucher.setDates(dates);
		}
	}
	
	private static class GiftVoucherUseRowCallBackHandler implements RowCallbackHandler {

		private GiftVoucherUse giftVoucherUse;

		@Override
		public void processRow(ResultSet rs) throws SQLException {
			giftVoucherUse = new GiftVoucherUse();
			giftVoucherUse.setGiftVoucherNumber(rs.getString("gift_voucher_number"));
			giftVoucherUse.setOrderId(rs.getInt("order_id"));
			giftVoucherUse.setUsedBy(rs.getInt("used_by"));
			giftVoucherUse.setAmountRedeemed(new Money(rs.getBigDecimal("amount_used")));
			Timestamp usedOnTS = rs.getTimestamp("used_on");
			if (usedOnTS != null) {
				giftVoucherUse.setUsedOn(new DateTime(usedOnTS));
			}
		}
	}
	
	@Override
	public void deleteUse(long giftVoucherNumber, int userId,
			int orderId) {
		int rowsDeleted = jdbcTemplate.update(DELETE_GIFT_VOUCHER_USAGE_QUERY,new Object[] {giftVoucherNumber,userId,orderId} );
		if (rowsDeleted == 0) {
			log.error("eGV Rollback Error : Unable to delete the eGV Usage Entry for eGV number  : " + giftVoucherNumber);
			throw new PlatformException("eGV Rollback Error : Unable to delete the eGV Usage Entry for eGV number  : " + giftVoucherNumber);
		}
	}
	
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
