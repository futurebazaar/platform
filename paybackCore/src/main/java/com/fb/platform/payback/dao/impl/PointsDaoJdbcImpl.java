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
import com.fb.platform.payback.dao.PointsDao;
import com.fb.platform.payback.model.PointsHeader;
import com.fb.platform.payback.model.PointsItems;
import com.fb.platform.payback.to.OrderItemRequest;

public class PointsDaoJdbcImpl implements PointsDao{
	
	private static Log logger = LogFactory.getLog(PointsDaoJdbcImpl.class);
	
	private JdbcTemplate jdbcTemplate;
	
	private static final String INSERT_POINTS_HEADER_SQL =
			"INSERT INTO points_header " +
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
	
	
	private static final String UPDATE_STATUS_SQL = 
			"UPDATE points_header SET " +
			"txn_timestamp = ?, " +
			"status = 'DONE' WHERE " +
			"txn_action_code = ? AND " +
			"settlement_date = ? AND " +
			"partner_merchant_id = ? ";
	
	
	private static final String LOAD_POINTS_HEADER_SQL = 
			" SELECT * FROM points_header WHERE " +
			"txn_action_code = ? AND " +
			"order_id = ? AND " +
			"txn_classification_code = ? " +
			"order by id desc";
	
	
	private static final String LOAD_HEADER_DATA_QUERY = 
			"SELECT * FROM " +
			"points_header WHERE " +
			"status = 'FRESH' AND " +
			"txn_action_code = ? AND " +
			"settlement_date = ? AND " +
			"partner_merchant_id = ?  " +
			"order by id ";
	
	private static final String LOAD_EARN_DATA_QUERY = 
			"SELECT * FROM " +
			"points_items " +
			"WHERE points_header_id = ? " +
			"order by id desc";
	
	private static final String INSERT_POINTS_ITEMS_SQL = 
			"INSERT INTO points_items " +
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

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}	
	

	
	@Override
	public Long insertPointsHeaderData(final PointsHeader pointsHeader) {
		logger.info("Inserting Points Header data for order Id : " + pointsHeader.getOrderId());
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
				ps.setString(14, "FRESH");
				ps.setString(15, pointsHeader.getReason());
				
				return ps;
			}
		}, keyHolderPointsHeader);
			
		return (Long) keyHolderPointsHeader.getKey();
	}

	@Override
	public void updateStatus(final String txnActionCode, final String settlementDate, final String merchantId) {
		logger.info("Updating Points Header data for actionCode : " + txnActionCode + " for merchant : " + merchantId + " on date : " + settlementDate);
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
	public PointsHeader getHeaderDetails(long orderId, String txnActionCode, String txnClassificationCode){
		logger.info("Getting Points Header data for order Id : " + orderId);
		return jdbcTemplate.queryForObject(LOAD_POINTS_HEADER_SQL, new Object[]{txnActionCode, orderId, txnClassificationCode}, new HeaderMapper());
	}
	
	@Override
	public Collection<PointsHeader> loadPointsHeaderData(String txnActionCode, String settlementDate, String merchantId) {
		Collection<PointsHeader> pointsHeaderList = jdbcTemplate.query(LOAD_HEADER_DATA_QUERY, new Object[]{txnActionCode, settlementDate, merchantId}, 
				new HeaderMapper());
		return pointsHeaderList;
	}
	
	private class HeaderMapper implements RowMapper<PointsHeader>{

		public PointsHeader mapRow(ResultSet rs, int rowNum) throws SQLException {
			PointsHeader pointsHeader = new PointsHeader();
			pointsHeader.setId(rs.getLong("id"));
			pointsHeader.setOrderId(rs.getLong("order_id"));
			pointsHeader.setTxnActionCode(rs.getString("txn_action_code"));
			pointsHeader.setTxnTimestamp(new DateTime(rs.getTimestamp("txn_timestamp").getTime()));
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
			
			return pointsHeader;
		}
		
	}
	
	@Override
	public Collection<PointsItems> loadPointsItemData(long pointsHeaderId) {
		logger.info("Getting Points Header data for points order Id : " + pointsHeaderId);
		Collection<PointsItems> earnPointsList = jdbcTemplate.query(LOAD_EARN_DATA_QUERY, new Object[]{pointsHeaderId}, 
				new EarnDataMapper());
		return earnPointsList;
		
	}
	
	private static class EarnDataMapper implements RowMapper<PointsItems>{

		@Override
		public PointsItems mapRow(ResultSet rs, int rowNum) throws SQLException {
			PointsItems earnPoints = new PointsItems();
			earnPoints.setArticleId(rs.getString("article_id"));
			earnPoints.setDepartmentCode(rs.getLong("department_code"));
			earnPoints.setDepartmentName(rs.getString("department_name"));
			earnPoints.setItemAmount(rs.getBigDecimal("item_amount"));
			earnPoints.setItemId(rs.getLong("order_item_id"));
			earnPoints.setQuantity(rs.getInt("quantity"));
			earnPoints.setPointsHeaderId(rs.getLong("points_header_id"));
			earnPoints.setEarnRatio(rs.getBigDecimal("earn_ratio"));
			return earnPoints;
		}
		
	}


	@Override
	public void insertPointsItemsData(final OrderItemRequest itemRequest,
			final long headerId, final int txnPoints) {
		logger.info("Inserting Points Item data for header Id : " + headerId + " and itemId : " + itemRequest.getId());
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(INSERT_POINTS_ITEMS_SQL, new String [] {"id"});
				ps.setLong(1, headerId);
				ps.setInt(2, itemRequest.getQuantity());
				ps.setLong(3, itemRequest.getDepartmentCode());
				ps.setString(4, itemRequest.getDepartmentName());
				ps.setInt(5, itemRequest.getAmount().intValue());
				ps.setString(6, itemRequest.getArticleId());
				ps.setInt(7, txnPoints);
				ps.setLong(8, itemRequest.getId());
				ps.setBigDecimal(9, itemRequest.getEarnRatio());
				ps.setBigDecimal(10, itemRequest.getBurnRatio());
				
				return ps;
			}
		});
		
	}
}
