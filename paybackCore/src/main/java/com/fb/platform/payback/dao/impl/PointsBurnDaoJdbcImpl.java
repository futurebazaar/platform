package com.fb.platform.payback.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.fb.platform.payback.dao.PointsBurnDao;
import com.fb.platform.payback.model.PaymentDetail;
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
	
	private static final String GET_BURN_TXN_QUERY = 
			"SELECT * FROM " +	
			"payments_paymentattempt p, orders_order o WHERE" +
			"p.order_id = o.id AND " +
			"p.status = 'paid' AND " +
			"p.payment_mode = 'payback' AND " +
			"o.id = ?";
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public Collection<PointsHeader> loadBurnData(String txnActionCode, String settlementDate, String merchantId) {
		Collection<PointsHeader> burnPointsList = jdbcTemplate.query(LOAD_BURN_DATA_QUERY, new Object[]{txnActionCode, settlementDate, merchantId}, new BurnDataMapper());
		return burnPointsList;
	}
	
	private static class BurnDataMapper implements RowMapper<PointsHeader>{

		@Override
		public PointsHeader mapRow(ResultSet rs, int rowNum) throws SQLException {
			PointsHeader burnPoints = new PointsHeader();
			burnPoints.setOrderId(rs.getLong("order_id"));
			burnPoints.setPartnerMerchantId(rs.getString("partner_merchant_id"));
			burnPoints.setReason(rs.getString("reason"));
			burnPoints.setSettlementDate(new DateTime(rs.getDate("settlement_date").getTime()));
			burnPoints.setTransactionId(rs.getString("reference_id"));
			burnPoints.setTxnDate(new DateTime(rs.getDate("txn_date").getTime()));
			burnPoints.setTxnPoints(rs.getInt("txn_points"));
			burnPoints.setTxnValue(rs.getInt("txn_value"));
			
			return burnPoints;
		}
		
	}

	@Override
	public PaymentDetail getPaymentDetails(long orderId) {
		List<PaymentDetail> paymentDetails = jdbcTemplate.query(GET_BURN_TXN_QUERY, new Object[]{orderId}, new PaymentMapper());
		if (paymentDetails.size() >= 1){
			return paymentDetails.get(0);
		}
		return null;
	}
	
	private class PaymentMapper implements RowMapper<PaymentDetail>{

		@Override
		public PaymentDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			PaymentDetail paymentDetail = new PaymentDetail();
			paymentDetail.setAmount(rs.getBigDecimal("amount"));
			paymentDetail.setId(rs.getLong("id"));
			paymentDetail.setOrderId(rs.getLong("order_id"));
			paymentDetail.setPaymentMode(rs.getString("paymentMode"));
			paymentDetail.setStatus(rs.getString("payment_mode"));
			paymentDetail.setTransactionId(rs.getString("transaction_id"));
			paymentDetail.setTxnDate(new DateTime(rs.getDate("created_on").getTime()));
			return paymentDetail;
		}
		
	}
	
}
