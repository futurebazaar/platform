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
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.fb.commons.PlatformException;
import com.fb.commons.to.Money;
import com.fb.platform.egv.dao.GiftVoucherDao;
import com.fb.platform.egv.model.GiftVoucher;
import com.fb.platform.egv.model.GiftVoucherDates;
import com.fb.platform.egv.model.GiftVoucherStatusEnum;
import com.fb.platform.egv.service.GiftVoucherNotFoundException;

/**
 * @author keith
 *
 */
public class GiftVoucherDaoJdbcImpl implements GiftVoucherDao {

	private JdbcTemplate jdbcTemplate;

	private Log log = LogFactory.getLog(GiftVoucherDaoJdbcImpl.class);

	private static final String GET_GIFT_VOUCHER__BY_ID_QUERY = 
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

	private static final String GET_GIFT_VOUCHER__BY_NUMBER_PIN_QUERY = 
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
	
	@Override
	public GiftVoucher load(int giftVoucherId) {
		
		if(log.isDebugEnabled()) {
			log.debug("Geting the details for the Gift Voucher id : " + giftVoucherId);
		}
		
		GiftVoucher eGV = null;
		GiftVoucherRowCallBackHandler prcbh = new GiftVoucherRowCallBackHandler(); 
		jdbcTemplate.query(GET_GIFT_VOUCHER__BY_ID_QUERY, prcbh, giftVoucherId);
		if (prcbh.giftVoucher == null) {
			//this means there is no promotion in the database for this id, fine
			log.error("No Gift Voucher found for the id - " + giftVoucherId);
			return null;
		}
		eGV = prcbh.giftVoucher;

		return eGV;
	}
	
	@Override
	public GiftVoucher load(long giftVoucherNumber,int giftVoucherPin) {
		
		if(log.isDebugEnabled()) {
			log.debug("Geting the details for the Gift Voucher Number : " + giftVoucherNumber);
		}
		
		GiftVoucher eGV = null;
		GiftVoucherRowCallBackHandler prcbh = new GiftVoucherRowCallBackHandler(); 
		jdbcTemplate.query(GET_GIFT_VOUCHER__BY_NUMBER_PIN_QUERY, prcbh, new Object[] {giftVoucherNumber,giftVoucherPin});
		if (prcbh.giftVoucher == null) {
			//this means there is no promotion in the database for this id, fine
			log.error("No Gift Voucher found for the id - " + giftVoucherNumber);
			throw new GiftVoucherNotFoundException();
		}
		eGV = prcbh.giftVoucher;

		return eGV;
	}

	private Money getGiftVoucherValue(int giftVoucherId, int userId, int orderId){
		if(log.isDebugEnabled()) {
//			log.debug("Get from the user promotion uses table record for user : " + userId + " , applied promotion id : " + promotionId + " , on order id : " + orderId);
		}
		return null;
	}
	
	@Override
	public boolean createGiftVoucher(final long gvNumber, final String pin,final String email, final int userId, final BigDecimal amount, final GiftVoucherStatusEnum status, final int orderItemId) {
		
		if(log.isDebugEnabled()) {
			log.debug("Insert in the gift_voucher table ");
		}
		int rowAffected = 0;
		KeyHolder userUsesKeyHolder = new GeneratedKeyHolder();
		
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
			}, userUsesKeyHolder);
		} catch (DuplicateKeyException e) {
			log.error("Duplicate key insertion exception " + e);
			throw new PlatformException("Duplicate key insertion exception "+e);
		}
		
		return rowAffected>0 ? true : false;
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
			giftVoucher.setPin(rs.getInt("pin"));
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

	
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
