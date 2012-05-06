package com.fb.platform.payback.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;

import com.fb.platform.payback.dao.PointsEarnDao;
import com.fb.platform.payback.model.PointsItems;

public class PointsEarnDaoJdbcImpl implements PointsEarnDao{
	
	private JdbcTemplate jdbcTemplate;
	
	private static final String LOAD_EARN_DATA_QUERY = 
			"SELECT * FROM " +
			"payments_pointsitems " +
			"WHERE points_header_id = ? " +
			"order by id desc";
	
	private static final String INSERT_POINTS_ITEMS_SQL = 
			"INSERT INTO payments_pointsitems " +
			"(points_header_id, " +
			"quantity, " +
			"department_code, " +
			"department_name, " +
			"item_amount, " +
			"article_id, " +
			"action_code, " +
			"order_item_id, " +
			"status) " +
			"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}	
	
	@Override
	public Collection<PointsItems> loadPointsItemData(long pointsHeaderId) {
		Collection<PointsItems> earnPointsList = jdbcTemplate.query(LOAD_EARN_DATA_QUERY, new Object[]{pointsHeaderId}, 
				new EarnDataMapper());
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
			earnPoints.setArticleId(rs.getLong("article_id"));
			earnPoints.setDepartmentCode(rs.getLong("department_code"));
			earnPoints.setDepartmentName(rs.getString("department_name"));
			earnPoints.setItemAmount(rs.getBigDecimal("item_amount"));
			earnPoints.setItemId(rs.getLong("order_item_id"));
			earnPoints.setQuantity(rs.getInt("quantity"));
			earnPoints.setTxnActionCode(rs.getString("action_code"));
			earnPoints.setPointsHeaderId(rs.getLong("points_header_id"));
			return earnPoints;
		}
		
	}

	@Override
	public void insertPointsItemsData(final PointsItems pointsItems) {
		jdbcTemplate.update(new PreparedStatementCreator() {
		
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(INSERT_POINTS_ITEMS_SQL, new String [] {"id"});
				ps.setLong(1, pointsItems.getPointsHeaderId());
				ps.setInt(2, pointsItems.getQuantity());
				ps.setLong(3, pointsItems.getDepartmentCode());
				ps.setString(4, pointsItems.getDepartmentName());
				ps.setInt(5, pointsItems.getItemAmount().intValue());
				ps.setLong(6, pointsItems.getArticleId());
				ps.setString(7, pointsItems.getTxnActionCode());
				ps.setLong(8, pointsItems.getItemId());
				ps.setString(9, "FRESH");
				
				return ps;
			}
		});
		
	}

}
