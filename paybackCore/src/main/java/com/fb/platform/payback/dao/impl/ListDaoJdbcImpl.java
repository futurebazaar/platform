package com.fb.platform.payback.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.fb.platform.payback.dao.ListDao;

public class ListDaoJdbcImpl implements ListDao {
	
	private static String GET_DOD_QUERY = 
			"SELECT li.sku  FROM " +
			"lists_list ll, lists_listitem li WHERE " +
			"ll.id = li.list_id AND " +
			"DATE(ll.starts_on) == DATE(NOW()) AND " +
			"DATE(ll.ends_on) == DATE(NOW()) AND " +
			"type = 'hero_deal' ";
	
	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public Long getHeroDealSellerRateChart(){
		List<Long> sellerRateChartId = jdbcTemplate.query(GET_DOD_QUERY, new Object[]{}, new ListMapper());
		if (sellerRateChartId != null && !sellerRateChartId.isEmpty()){
			return sellerRateChartId.get(0);
		}
		return null;
		
	}
	
	private class ListMapper implements RowMapper<Long>{
			
		public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getLong("sku");
		}
	
	}

}
