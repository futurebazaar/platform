/**
 * 
 */
package com.fb.platform.promotion.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.fb.commons.PlatformException;
import com.fb.platform.promotion.dao.ScratchCardDao;
import com.fb.platform.promotion.exception.ScratchCardNotFoundException;
import com.fb.platform.promotion.model.scratchCard.ScratchCard;

/**
 * @author vinayak
 *
 */
public class ScratchCardDaoJdbcImpl implements ScratchCardDao {

	private JdbcTemplate jdbcTemplate;

	private Log log = LogFactory.getLog(ScratchCardDaoJdbcImpl.class);

	private static final String ACTIVE_STATUS_IN_DB = "active";
	private static final String INACTIVE_STATUS_IN_DB = "inactive";

	private static final String LOAD_SCRATCH_CARD_QUERY = 
			"SELECT " +
			"	id, " +
			"	scratch_card_no, " +
			"	coupon_code, " +
			"	status, " +
			"	store ," +
			"	used_date ,  " +
			"	email ," +
			"	mobile ," +
			"	user_id ," +
			"	timestamp, " +
			"	name " +
			"FROM promotions_scratchcard " +
			"WHERE scratch_card_no = ?";

	private static final String LOAD_COUPON_CODE_FOR_STORE_QUERY = 
			"SELECT " +
			"	coupon_code " +
			"FROM promotions_scratchcardcoupons " +
			"WHERE store = ?";

	private static final String UPDATE_SCRATCH_CARD_SQL = 
			"UPDATE promotions_scratchcard " +
			"SET " +
			"	user_id = ?, " +
			"	status = ?, " +
			"	used_date = ?, " +
			"	coupon_code = ? " +
			"WHERE id = ?";
	
	private static final String GET_USER_ORDER_COUNT = 
			"SELECT " +
			"	count(*) " +
			"FROM orders_order " +
			"WHERE " +
			"	user_id = ? " +
			"	AND " +
			"	support_state IS NOT NULL " +
			"	AND " +
			"	support_state NOT IN ('booked','cancelled','returned') " +
			"GROUP BY user_id";

	@Override
	public ScratchCard load(String cardNumber) {
		if (log.isDebugEnabled()) {
			log.debug("Loading the scratchCard from database : " + cardNumber);
		}

		ScratchCard scratchCard = null;
		try {
			scratchCard = jdbcTemplate.queryForObject(LOAD_SCRATCH_CARD_QUERY, new ScratchCardRowMapper(), cardNumber);
		} catch (IncorrectResultSizeDataAccessException e) {
			//problem. can't find the scratch card.
			throw new ScratchCardNotFoundException("ScratchCard not found. ScratchCard Number : " + cardNumber);
		}
		return scratchCard;
	}

	@Override
	public List<String> getCouponCodesForStore(String store) {
		List<String> couponCodes = jdbcTemplate.queryForList(LOAD_COUPON_CODE_FOR_STORE_QUERY, String.class, store);
		if (couponCodes == null || couponCodes.size() == 0) {
			throw new PlatformException("No CouponCode found for the store : " + store);
		}
		return couponCodes;
	}

	@Override
	public void commitUse(int id, int userId, String couponCode) {
		Timestamp currentTime = new java.sql.Timestamp(System.currentTimeMillis());
		int rowsUpdated = jdbcTemplate.update(UPDATE_SCRATCH_CARD_SQL, userId, INACTIVE_STATUS_IN_DB, currentTime, couponCode, id);
		if (rowsUpdated != 1) {
			throw new PlatformException("Unable to assgin Scratch Card to user, scratchCardId : " + id + ", userId : " + userId + ", couponCode : " + couponCode);
		}
	}
	
	@Override
	public int getUserOrderCount(int userId) {
		int orderCount = 0;
		try {
			orderCount = jdbcTemplate.queryForInt(GET_USER_ORDER_COUNT, new Object[]{userId});
		} catch (EmptyResultDataAccessException e) {
			if(log.isDebugEnabled()) {
				log.debug("User first record.");
			}
		}
		return orderCount;
	}

	private static class ScratchCardRowMapper implements RowMapper<ScratchCard> {

		@Override
		public ScratchCard mapRow(ResultSet rs, int rowNum) throws SQLException {
			ScratchCard scratchCard = new ScratchCard();

			DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); 
			
			/*DatatypeFactory df = null;
			try {
				df = DatatypeFactory.newInstance();
			} catch (DatatypeConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} ; 
			
			GregorianCalendar gc = new GregorianCalendar(); */ 
			
			String activeStr = rs.getString("status");
			if (ACTIVE_STATUS_IN_DB.equals(activeStr)) {
				scratchCard.setActive(true);
			} else {
				scratchCard.setActive(false);
			}
			scratchCard.setCardNumber(rs.getString("scratch_card_no"));
			scratchCard.setId(rs.getInt("id"));
			scratchCard.setCouponCode(rs.getString("coupon_code"));
			scratchCard.setStore(rs.getString("store"));
			scratchCard.setCardStatus( rs.getString("status"));
			scratchCard.setMobile(rs.getString("mobile"));
			scratchCard.setEmail(rs.getString("email"));
			scratchCard.setUserId(rs.getInt("user_id"));
			scratchCard.setName(rs.getString("name"));

			Timestamp ts = rs.getTimestamp("timestamp");
			scratchCard.setTimestamp( ts );
			
			Timestamp usedOnTS = rs.getTimestamp("used_date");
			scratchCard.setUsedDate( usedOnTS );
			
            /*if (usedOnTS != null) {
            	System.out.println("usedOnTS"+usedOnTS);
    			gc.setTime(usedOnTS );
    			System.out.println("df.newXMLGregorianCalendar(gc)"+df.newXMLGregorianCalendar(gc));
            	scratchCard.setUsedDate(df.newXMLGregorianCalendar(gc) );
            } */

			

			return scratchCard;
		}
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
