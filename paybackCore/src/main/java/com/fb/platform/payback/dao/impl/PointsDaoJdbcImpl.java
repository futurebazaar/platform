package com.fb.platform.payback.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;

import com.fb.platform.payback.dao.PointsDao;
import com.fb.platform.payback.model.PointsHeader;

public class PointsDaoJdbcImpl implements PointsDao{
	
	private JdbcTemplate jdbcTemplate;
	
	private static final String INSERT_POINTS_HEADER_SQL =
			"INSERT INTO payments_pointsheader " +
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
			"txn_value, " +
			"marketing_code, " +
			"branch_id, " +
			"txn_points, " +
			"status, " +
			"txn_timstamp, " +
			"reason, " +
			"burn_ratio, " +
			"earn_ratio) " +
			"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, " +
			"?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String INSERT_POINTS_ITEMS_SQL = 
			"INSERT INTO payments_pointsitems " +
			"(points_header_id, " +
			"quantity, " +
			"department_code, " +
			"department_name, " +
			"item_amount, " +
			"article_id, " +
			"action_code, " +
			"order_item_id) " +
			"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String UPDATE_STATUS_SQL = 
			"UPDATE payments_pointsheader SET " +
			"txn_timstamp = ?, " +
			"status = 'DONE' WHERE " +
			"txn_action_code = ? AND " +
			"settlement_date = ? AND " +
			"partner_merchant_id = ? ";
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public void insertPointsHeaderData(final PointsHeader pointsHeader) {
			int rowAffected = jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(INSERT_POINTS_HEADER_SQL, new String [] {"id"});
				ps.setLong(1, pointsHeader.getOrderId());
				ps.setString(2, pointsHeader.getTransactionId());
				ps.setInt(3, pointsHeader.getPartnerMerchantId());
				ps.setInt(4, pointsHeader.getPartnerTerminalId());
				ps.setString(5, pointsHeader.getTxnActionCode());
				ps.setString(6, pointsHeader.getTxnClassificationCode());
				ps.setString(7, pointsHeader.getTxnPaymentType());
				ps.setString(8, pointsHeader.getTxnDate());
				ps.setString(9, pointsHeader.getSettlementDate());
				ps.setTimestamp(10, new java.sql.Timestamp(System.currentTimeMillis()));				
				ps.setInt(11, pointsHeader.getTxnValue());
				ps.setString(12, pointsHeader.getMarketingCode());
				ps.setString(13, pointsHeader.getBranchId());
				ps.setInt(14, pointsHeader.getTxnPoints());
				ps.setString(15, "FRESH");
				ps.setString(16, pointsHeader.getReason());
				ps.setBigDecimal(17, pointsHeader.getBurnRatio());
				ps.setBigDecimal(18, pointsHeader.getEarnRatio());
				
				return ps;
			}
		});
	}
	

	@Override
	public void insertPointsItemsData() {
		
	}

	@Override
	public void updateStatus(final String txnActionCode, final String settlementDate, final String merchantId) {
		long rowUpdated = jdbcTemplate.update(new PreparedStatementCreator() {
			
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
		
	}

}
