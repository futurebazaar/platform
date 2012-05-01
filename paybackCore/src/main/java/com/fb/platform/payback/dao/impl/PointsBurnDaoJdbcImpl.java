package com.fb.platform.payback.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;

import com.fb.platform.payback.dao.PointsBurnDao;
import com.fb.platform.payback.model.PointsHeader;

public class PointsBurnDaoJdbcImpl implements PointsBurnDao {
	
	private JdbcTemplate jdbcTemplate;
	
	private static final String LOAD_BURN_DATA_QUERY = 
			"SELECT * FROM " +
			"payments_pointsheader WHERE " +
			"status = 'FRESH' AND " +
			"txn_action_code = ? AND " +
			"settlement_date = ? AND " +
			"partner_merchant_id = ?";
	
	@Override
	public Collection<PointsHeader> loadBurnData(String txnActionCode, String settlementDate, String merchantId) {
		Collection<PointsHeader> burnPointsList = jdbcTemplate.query(LOAD_BURN_DATA_QUERY, new Object[]{txnActionCode, settlementDate, merchantId}, new BurnDataMapper());
		return burnPointsList;
	}
	
	@Override
	public PointsHeader getBurnData(String txnActionCode, long orderId) {
		
		return null;
	}
	
	private static class BurnDataMapper implements RowMapper<PointsHeader>{

		@Override
		public PointsHeader mapRow(ResultSet rs, int rowNum) throws SQLException {
			PointsHeader burnPoints = new PointsHeader();
			burnPoints.setOrderId(rs.getLong("order_id"));
			burnPoints.setPartnerMerchantId(rs.getInt("partner_merchant_id"));
			burnPoints.setReason(rs.getString("reason"));
			burnPoints.setSettlementDate(rs.getString("settlement_date"));
			burnPoints.setTransactionId(rs.getString("reference_id"));
			burnPoints.setTxnDate(rs.getString("txn_date"));
			burnPoints.setTxnPoints(rs.getInt("txn_points"));
			burnPoints.setTxnValue(rs.getInt("txn_value"));
			
			return burnPoints;
		}
		
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


}
