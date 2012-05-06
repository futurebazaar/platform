package com.fb.platform.payback.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.fb.commons.PlatformException;
import com.fb.platform.payback.dao.PointsBurnDao;
import com.fb.platform.payback.model.PaymentDetail;

public class PointsBurnDaoJdbcImpl implements PointsBurnDao {
	
	private JdbcTemplate jdbcTemplate;
	
	private static final String GET_BURN_TXN_QUERY = 
			"SELECT * FROM " +	
			"payments_paymentattempt p, orders_order o WHERE " +
			"p.order_id = o.id AND " +
			"p.status = 'paid' AND " +
			"p.payment_mode = 'payback' AND " +
			"o.id = ?";
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public PaymentDetail getPaymentDetails(long orderId) {
		List<PaymentDetail> paymentDetails = jdbcTemplate.query(GET_BURN_TXN_QUERY, new Object[]{orderId}, new PaymentMapper());
		if (paymentDetails.size() >= 1){
			return paymentDetails.get(0);
		}
		throw new PlatformException("No Valid Payment Details Found");
	}
	
	private class PaymentMapper implements RowMapper<PaymentDetail>{

		@Override
		public PaymentDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			PaymentDetail paymentDetail = new PaymentDetail();
			paymentDetail.setAmount(rs.getBigDecimal("amount"));
			paymentDetail.setId(rs.getLong("id"));
			paymentDetail.setOrderId(rs.getLong("order_id"));
			paymentDetail.setPaymentMode(rs.getString("payment_mode"));
			paymentDetail.setStatus(rs.getString("status"));
			paymentDetail.setTransactionId(rs.getString("transaction_id"));
			paymentDetail.setTxnDate(new DateTime(rs.getDate("created_on").getTime()));
			return paymentDetail;
		}
		
	}
	
}
