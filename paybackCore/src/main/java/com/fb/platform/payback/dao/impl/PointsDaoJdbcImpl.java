package com.fb.platform.payback.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;

import com.fb.platform.payback.dao.PointsDao;
import com.fb.platform.payback.model.OrderDetail;
import com.fb.platform.payback.model.PaymentDetail;
import com.fb.platform.payback.model.PointsHeader;
import com.fb.platform.payback.model.PointsItems;

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
	
	private static final String LOAD_POINTS_HEADER_SQL = 
			" SELECT * FROM payments_pointsheader WHERE " +
			"txn_action_code = ? AND " +
			"order_id = ? AND " +
			"txn_classification_code = ?";
	
	private static final String GET_ORDER_QUERY = 
			"SELECT o.id, c.name , " +
			"o.payment_mode, o.timestamp, " +
			"o.reference_order_id, o.support_state, " +
			"c.id, cd.type FROM " +
			"orders_order o, accounts_client c, " +
			"accounts_clientdomain cd  " +
			"WHERE o.client_id = c.id AND " +
			"o.id = ? ";
	
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
				ps.setString(3, pointsHeader.getPartnerMerchantId());
				ps.setString(4, pointsHeader.getPartnerTerminalId());
				ps.setString(5, pointsHeader.getTxnActionCode());
				ps.setString(6, pointsHeader.getTxnClassificationCode());
				ps.setString(7, pointsHeader.getTxnPaymentType());
				ps.setDate(8, new java.sql.Date(pointsHeader.getTxnDate().getMillis()));
				ps.setDate(9, new java.sql.Date(pointsHeader.getSettlementDate().getMillis()));
			
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
	public void insertPointsItemsData(final PointsItems pointsItems) {
		int rowAffected = jdbcTemplate.update(new PreparedStatementCreator() {
		
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(INSERT_POINTS_ITEMS_SQL, new String [] {"id"});
				ps.setLong(1, pointsItems.getPointsHeaderId());
				ps.setInt(2, pointsItems.getQuantity());
				ps.setString(3, pointsItems.getDepartmentCode());
				ps.setString(4, pointsItems.getDepartmentName());
				ps.setInt(5, pointsItems.getItemAmount());
				ps.setString(6, pointsItems.getArticleId());
				ps.setString(7, pointsItems.getTxnActionCode());
				ps.setLong(8, pointsItems.getItemId());
				
				return ps;
			}
		});
		
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
	
	@Override
	public OrderDetail getOrderDetail(long orderId){
		List<OrderDetail> orderDetail = jdbcTemplate.query(GET_ORDER_QUERY, new Object[]{orderId}, new OrderMapper());
		if (orderDetail.size() >= 1){
			return orderDetail.get(0);
		}
		return new OrderDetail();
	}
	
	private class OrderMapper implements RowMapper<OrderDetail>{

		@Override
		public OrderDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setId(rs.getLong("id"));
			orderDetail.setClientDomainType(rs.getString("type"));
			orderDetail.setClientId(rs.getLong("id"));
			orderDetail.setClientName(rs.getString("name"));
			orderDetail.setOrderDate(rs.getDate("timestamp").toString());
			orderDetail.setPaymentMode(rs.getString("payment_mode"));
			orderDetail.setReference_order_id(rs.getString("reference_order_id"));
			orderDetail.setSupportState(rs.getString("support_state"));
			return orderDetail;
		}
		
	}
	
	@Override
	public PointsHeader getHeaderDetails(long orderId, String txnActionCode, String txnClassificationCode){
		List<PointsHeader> pointsHeader = jdbcTemplate.query(LOAD_POINTS_HEADER_SQL, 
				new Object[]{txnActionCode, orderId, txnClassificationCode}, new HeaderMapper());
		if (pointsHeader.size() >= 1){
			return pointsHeader.get(0);
		}
		return null;
	}
	
	private class HeaderMapper implements RowMapper<PointsHeader>{

		public PointsHeader mapRow(ResultSet rs, int rowNum) throws SQLException {
			PointsHeader pointsHeader = new PointsHeader();
			pointsHeader.setBurnRatio(rs.getBigDecimal("burn_ratio"));
			pointsHeader.setEarnRatio(rs.getBigDecimal("earn_ratio"));
			pointsHeader.setOrderId(rs.getLong("order_id"));
			pointsHeader.setTxnActionCode(rs.getString("txn_action_code"));
			pointsHeader.setTxnTimestamp(new DateTime(rs.getTimestamp("txn_timstamp").getTime()));
			pointsHeader.setSettlementDate(new DateTime(rs.getDate("settlement_date").getTime()));
			pointsHeader.setTxnDate(new DateTime(rs.getDate("txn_date").getTime()));
			//pointsHeader.setTxnPoints(txnPoints)
			return pointsHeader;
		}
		
	}
	
}
