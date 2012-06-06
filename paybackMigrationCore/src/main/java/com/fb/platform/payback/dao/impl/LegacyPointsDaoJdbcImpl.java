package com.fb.platform.payback.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.fb.commons.PlatformException;
import com.fb.platform.payback.dao.LegacyPointsDao;
import com.fb.platform.payback.model.LegacyPointsHeader;
import com.fb.platform.payback.model.LegacyPointsItems;

public class LegacyPointsDaoJdbcImpl implements LegacyPointsDao{
	
	private static Log logger = LogFactory.getLog(LegacyPointsDaoJdbcImpl.class);
	
	private JdbcTemplate jdbcTemplate;
	
	private static final String INSERT_POINTS_HEADER_SQL =
			"INSERT INTO platform.points_header " +
			"(order_id, " +
			"reference_id, " +
			"loyalty_card, " +
			"partner_merchant_id, " +
			"partner_terminal_id, " +
			"txn_action_code, " +
			"txn_classification_code, " +
			"txn_payment_type, " +
			"txn_date, " +
			"settlement_date, " +
			"txn_timestamp, " +
			"txn_value, " +
			"txn_points, " +
			"status, " +
			"reason) " +
			"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String LOAD_HEADER_DATA_QUERY = 
			"SELECT * FROM tinla.payments_pointsheader order by id";
	
	private static final String LOAD_EARN_DATA_QUERY = 
			"SELECT * FROM tinla.payments_pointsitems where points_header_id = ? order by id";

	
	private static final String INSERT_POINTS_ITEMS_SQL = 
			"INSERT INTO plaform.points_items " +
			"(points_header_id, " +
			"quantity, " +
			"department_code, " +
			"department_name, " +
			"item_amount, " +
			"article_id, " +
			"txn_points, " +
			"order_item_id, " +
			"earn_ratio, " +
			"burn_ratio) " +
			"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String UPDATE_STATUS_SQL = 
			"UPDATE points_header SET " +
			"txn_timestamp = ?, " +
			"status = 'DONE' WHERE " +
			"txn_action_code = ? AND " +
			"settlement_date = ? AND " +
			"partner_merchant_id = ? ";
	
	
	private static final String LOAD_HEADER_QUERY = 
			"SELECT * FROM " +
			"payments_pointsheader WHERE " +
			"status = 'FRESH' AND " +
			"txn_action_code = ? AND " +
			"settlement_date = ? AND " +
			"partner_merchant_id = ?  " +
			"order by id ";
	

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}	
	

	
	@Override
	public Long insertPointsHeaderData(final LegacyPointsHeader pointsHeader) {
		final KeyHolder keyHolderPointsHeader = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(INSERT_POINTS_HEADER_SQL, new String [] {"id"});
				
				ps.setLong(1, pointsHeader.getOrderId());
				ps.setString(2, pointsHeader.getReferenceId());
				ps.setString(3, pointsHeader.getLoyaltyCard());
				ps.setString(4, pointsHeader.getPartnerMerchantId());
				ps.setString(5, pointsHeader.getPartnerTerminalId());
				ps.setString(6, pointsHeader.getTxnActionCode());
				ps.setString(7, pointsHeader.getTxnClassificationCode());
				ps.setString(8, pointsHeader.getTxnPaymentType());
				
				ps.setDate(9, new java.sql.Date(pointsHeader.getTxnDate().getMillis()));
				ps.setDate(10, new java.sql.Date(pointsHeader.getSettlementDate().getMillis()));
				ps.setTimestamp(11, new java.sql.Timestamp(pointsHeader.getTxnTimestamp().getMillis()));				
			
				ps.setInt(12, pointsHeader.getTxnValue());
				ps.setInt(13, pointsHeader.getTxnPoints());
				ps.setString(14, pointsHeader.getStatus());
				ps.setString(15, pointsHeader.getReason());
				
				return ps;
			}
		}, keyHolderPointsHeader);
			
		return (Long) keyHolderPointsHeader.getKey();
	}

	@Override
	public Collection<LegacyPointsHeader> loadPointsHeaderData() {
		Collection<LegacyPointsHeader> pointsHeaderList = jdbcTemplate.query(LOAD_HEADER_DATA_QUERY, new Object[]{}, 
				new HeaderMapper());
		return pointsHeaderList;
	}
	
	private class HeaderMapper implements RowMapper<LegacyPointsHeader>{

		public LegacyPointsHeader mapRow(ResultSet rs, int rowNum) throws SQLException {
			LegacyPointsHeader pointsHeader = new LegacyPointsHeader();
			pointsHeader.setId(rs.getLong("id"));
			pointsHeader.setOrderId(rs.getLong("order_id"));
			pointsHeader.setTxnActionCode(rs.getString("txn_action_code"));
			pointsHeader.setTxnTimestamp(new DateTime(rs.getTimestamp("txn_timstamp").getTime()));
			pointsHeader.setSettlementDate(new DateTime(rs.getDate("settlement_date").getTime()));
			pointsHeader.setTxnDate(new DateTime(rs.getDate("txn_date").getTime()));
			pointsHeader.setTxnPoints(rs.getInt("txn_points"));
			pointsHeader.setBranchId(rs.getString("branch_id"));
			pointsHeader.setLoyaltyCard(rs.getString("loyalty_card"));
			pointsHeader.setMarketingCode(rs.getString("marketing_code"));
			pointsHeader.setPartnerMerchantId(rs.getString("partner_merchant_id"));
			pointsHeader.setPartnerTerminalId(rs.getString("partner_terminal_id"));
			pointsHeader.setReason(rs.getString("reason"));
			pointsHeader.setReferenceId(rs.getString("reference_id"));
			pointsHeader.setTxnClassificationCode(rs.getString("txn_classification_code"));
			pointsHeader.setTxnPaymentType(rs.getString("txn_payment_type"));
			pointsHeader.setTxnValue(rs.getInt("txn_value"));
			pointsHeader.setEarnRatio(rs.getBigDecimal("earn_ratio"));
			pointsHeader.setBurnRatio(rs.getBigDecimal("burn_ratio"));
			pointsHeader.setStatus(rs.getString("status"));
			
			
			return pointsHeader;
		}
		
	}
	
	@Override
	public Collection<LegacyPointsItems> loadPointsItemData(long pointsHeaderId) {
		Collection<LegacyPointsItems> earnPointsList = jdbcTemplate.query(LOAD_EARN_DATA_QUERY, new Object[]{pointsHeaderId}, 
				new EarnDataMapper());
		return earnPointsList;
		
	}
	
	private static class EarnDataMapper implements RowMapper<LegacyPointsItems>{

		@Override
		public LegacyPointsItems mapRow(ResultSet rs, int rowNum) throws SQLException {
			LegacyPointsItems earnPoints = new LegacyPointsItems();
			earnPoints.setArticleId(rs.getString("article_id"));
			earnPoints.setDepartmentCode(rs.getLong("department_code"));
			earnPoints.setDepartmentName(rs.getString("department_name"));
			earnPoints.setItemAmount(rs.getBigDecimal("item_amount"));
			earnPoints.setItemId(rs.getLong("order_item_id"));
			earnPoints.setQuantity(rs.getInt("quantity"));
			earnPoints.setPointsHeaderId(rs.getLong("points_header_id"));
			return earnPoints;
		}
		
	}


	@Override
	public void insertPointsItemsData(final LegacyPointsItems itemRequest) {
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(INSERT_POINTS_ITEMS_SQL, new String [] {"id"});
				ps.setLong(1, itemRequest.getPointsHeaderId());
				ps.setInt(2, itemRequest.getQuantity());
				ps.setLong(3, itemRequest.getDepartmentCode());
				ps.setString(4, itemRequest.getDepartmentName());
				ps.setInt(5, itemRequest.getItemAmount().intValue());
				ps.setString(6, itemRequest.getArticleId());
				ps.setInt(7, itemRequest.getTxnPoints().intValue());
				ps.setLong(8, itemRequest.getItemId());
				ps.setBigDecimal(9, itemRequest.getEarnRatio());
				ps.setBigDecimal(10, itemRequest.getBurnRatio());
				
				return ps;
			}
		});
		
	}



	@Override
	public void updateStatus(final String txnActionCode, final String settlementDate,
			final String merchantId) {
		//	logger.info("Updating Points Header data for actionCode : " + txnActionCode + " for merchant : " + merchantId + " on date : " + settlementDate);
		int rowUpdated =  jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(UPDATE_STATUS_SQL, new String [] {"id"});
				ps.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
				ps.setString(2, txnActionCode);
				ps.setString(3, settlementDate);
				ps.setString(4, merchantId);
				return ps;
			}
		});
		if (rowUpdated <= 0){
			throw new PlatformException("Unable to update the status for merchant : " + merchantId + 
					" with txnActionCode : "+ txnActionCode +  " on settlement_date : "+ settlementDate);
		}
		
	}

	@Override
	public Collection<LegacyPointsHeader> loadPointsHeaderData(String txnActionCode,
			String settlementDate, String merchantId) {
		Collection<LegacyPointsHeader> pointsHeaderList = jdbcTemplate.query(LOAD_HEADER_QUERY, new Object[]{txnActionCode, settlementDate, merchantId}, 
				new HeaderMapper());
		return pointsHeaderList;
	}
}
