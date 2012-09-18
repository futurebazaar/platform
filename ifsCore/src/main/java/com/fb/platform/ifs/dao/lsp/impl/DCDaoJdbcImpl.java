package com.fb.platform.ifs.dao.lsp.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.fb.commons.to.Money;
import com.fb.platform.ifs.dao.lsp.DCDao;
import com.fb.platform.ifs.model.DCAvailability;
import com.fb.platform.ifs.to.lsp.DeliveryCenter;

public class DCDaoJdbcImpl implements DCDao{
	
	private JdbcTemplate jdbcTemplate;

	private Log log = LogFactory.getLog(DCDaoJdbcImpl.class);

	private static final String STOCK_AVAILABILITY_QUERY = 
		"SELECT " +
		"	inv.dc_id, " + 
		"	dc.code, " + 
		"	dc.cod_flag, " +
		"	dc.seller_id, " +
		"	inv.type, " + 
		"	(inv.stock - inv.bookings - inv.stock_adjustment - inv.outward - inv.threshold) as ats " +
		"FROM " +
		"	inventory_inventory as inv inner join fulfillment_dc as dc on (inv.dc_id = dc.id) " +
		"WHERE " +
		"	inv.rate_chart_id = ? " +
		" 	and (inv.stock - inv.bookings - inv.stock_adjustment - inv.outward - inv.threshold) >= ? " +
		"	and inv.is_active = 1 " + 
		"	and inv.starts_on <= now() " + 
		"	and inv.ends_on >= now()";
	
	@Override
	public List<DeliveryCenter> getAvailability(int sellerChartId, int quantity) {
		if(log.isDebugEnabled()) {
			log.debug("Getting stock avilability for sellerChartId : " + sellerChartId);
		}
		List<DeliveryCenter> dcs = null;
		try {
			dcs = jdbcTemplate.query(STOCK_AVAILABILITY_QUERY, new DCAvailabilityMapper(), sellerChartId, quantity);
		} catch (IncorrectResultSizeDataAccessException e) {
			if (log.isDebugEnabled()) {
				log.debug("No stock found for sellerChartId : " + sellerChartId);
			}
			return null;
		}
		
		return dcs;
	}

	private static class DCAvailabilityMapper implements RowMapper<DeliveryCenter> {

		@Override
		public DeliveryCenter mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliveryCenter dc = new DeliveryCenter();
			dc.setId(rs.getInt("dc_id"));
			dc.setCode(rs.getString("code"));
			dc.setType(rs.getString("type"));
			dc.setSellerId(rs.getInt("seller_id"));
			dc.setCodFlag(rs.getBoolean("cod_flag"));
			
			return dc;
		}
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


}
