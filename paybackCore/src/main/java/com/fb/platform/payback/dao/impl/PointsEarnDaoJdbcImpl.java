package com.fb.platform.payback.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.joda.time.DateTime;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.fb.platform.payback.dao.PointsEarnDao;
import com.fb.platform.payback.model.PointsItems;

public class PointsEarnDaoJdbcImpl implements PointsEarnDao{
	
	private JdbcTemplate jdbcTemplate;
	
	private static final String LOAD_EARN_DATA_QUERY = 
			"SELECT ph.order_id, ph.reference_id, " +
			"ph.loyalty_card, ph.partner_merchant_id, " +
			"ph.partner_terminal_id, ph.txn_action_code, " +
			"ph.txn_classification_code, ph.txn_payment_type, " +
			"ph.txn_date, ph.settlement_date, " +
			"ph.txn_value, ph.marketing_code, " +
			"ph.branch_id, ph.txn_points, " +
			"ph.txn_timstamp, ph.reason, " +
			"pi.quantity, pi.department_code, " +
			"pi.department_name, pi.item_amount, " +
			"pi.article_id, pi.order_item_id, " +
			"pi.points_header_id " +
			"FROM " +
			"payments_pointsheader ph, payments_pointsitems pi " +
			"WHERE ph.id = pi.points_header_id AND " +
			"ph.status = 'DONE' AND " +
			"ph.txn_action_code = ? AND " +
			"ph.settlement_date = ? AND " +
			"ph.partner_merchant_id = ?";
	
	
	
	@Override
	public Collection<PointsItems> loadEarnData(String txnActionCode, String settlementDate, String merchantId) {
		Collection<PointsItems> earnPointsList = jdbcTemplate.query(LOAD_EARN_DATA_QUERY, new Object[]{txnActionCode, settlementDate, merchantId}, new EarnDataMapper());
		return earnPointsList;
		
	}
	
	@Override
	public PointsItems getEarnData(String txnActionCode, long orderId) {
		
		return null;
	}
	
	private static class EarnDataMapper implements RowMapper<PointsItems>{

		@Override
		public PointsItems mapRow(ResultSet rs, int rowNum) throws SQLException {
			PointsItems earnPoints = new PointsItems();
			earnPoints.setArticleId(rs.getString("article_id"));
			earnPoints.setBranchId(rs.getString("branch_id"));
			earnPoints.setDepartmentCode(rs.getString("department_code"));
			earnPoints.setDepartmentName(rs.getString("department_name"));
			earnPoints.setItemAmount(rs.getInt("item_amount"));
			earnPoints.setItemId(rs.getLong("order_item_id"));
			earnPoints.setLoyaltyCard(rs.getString("loyalty_card"));
			earnPoints.setMarketingCode(rs.getString("marketing_code"));
			earnPoints.setOrderId(rs.getLong("order_id"));
			earnPoints.setPartnerMerchantId(rs.getString("partner_merchant_id"));
			earnPoints.setPartnerTerminalId(rs.getString("partner_terminal_id"));
			earnPoints.setQuantity(rs.getInt("quantity"));
			earnPoints.setReason(rs.getString("reason"));
			earnPoints.setReferenceId(rs.getString("reference_id"));
			earnPoints.setSettlementDate(new DateTime(rs.getDate("settlement_date").getTime()));
			earnPoints.setTxnActionCode(rs.getString("txn_action_code"));
			earnPoints.setTxnClassificationCode(rs.getString("txn_classification_code"));
			earnPoints.setTxnDate(new DateTime(rs.getDate("txn_date").getTime()));
			earnPoints.setTxnPaymentType(rs.getString("txn_payment_type"));
			earnPoints.setTxnPoints(rs.getInt("txn_points"));
			earnPoints.setTxnTimestamp(new DateTime(rs.getDate("txn_timstamp").getTime()));
			earnPoints.setTxnValue(rs.getInt("txn_value"));
			earnPoints.setPointsHeaderId(rs.getLong("points_header_id"));
			return earnPoints;
		}
		
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
