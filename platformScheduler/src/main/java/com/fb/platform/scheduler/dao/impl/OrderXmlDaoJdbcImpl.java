/**
 * 
 */
package com.fb.platform.scheduler.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.fb.platform.scheduler.dao.OrderXmlDao;
import com.fb.platform.scheduler.to.OrderXmlTO;
import com.fb.platform.scheduler.utils.SchedulerConstants;

/**
 * @author anubhav
 *
 */
public class OrderXmlDaoJdbcImpl implements OrderXmlDao{
	
	private static Log logger = LogFactory.getLog(OrderXmlDaoJdbcImpl.class);
	private JdbcTemplate jdbcTemplate;
	
	private static final String LOAD_ORDER_XMLS = 
			"SELECT " +
			"id, " +
			"order_id, " +
			"type, " +
			"xml, " +
			"reference_order_id, " +
			"attempts " +
			"FROM orders_orderxml WHERE " +
			"status in ( ?, ? ) ORDER BY id ASC";
	
	private static final String UPDATE_ORDER_XML = 
			"UPDATE orders_orderxml SET " +
			"attempts = ?, " +
			"reason = ?, " +
			"status = ? " +
			"WHERE id = ? ";
	
	@Override
	public List<OrderXmlTO> getOrderXmlList() {
		List<OrderXmlTO> orderXmlTOList = new ArrayList<OrderXmlTO>();
		try{ 
			logger.info("Getting order Xmls");
			orderXmlTOList = jdbcTemplate.query(LOAD_ORDER_XMLS, new Object[] {SchedulerConstants.ERROR_STATUS, SchedulerConstants.NEW_STATUS}, new OrderXmlMapper());
		} catch (Exception e) {
			logger.error("Exception occured while getting XMls", e);
		}
		logger.info("order Xml lists :" + orderXmlTOList);
		return orderXmlTOList;
	}
	
	private static class OrderXmlMapper implements RowMapper<OrderXmlTO> {

		@Override
		public OrderXmlTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			OrderXmlTO orderXmlTO = new OrderXmlTO();
			orderXmlTO.setAttempts(rs.getInt("attempts"));
			orderXmlTO.setId(rs.getLong("id"));
			orderXmlTO.setOrderId(rs.getLong("order_id"));
			orderXmlTO.setXml(rs.getString("xml"));
			orderXmlTO.setType(rs.getString("type"));
			orderXmlTO.setReferenceOrderId(rs.getString("reference_order_id"));
			return orderXmlTO;
		}
	}

	@Override
	public int updateOrderXml(int attempts, String reason, String status, long id) {
		logger.info("Updating Order Xml: " + id);
		try {
			return jdbcTemplate.update(UPDATE_ORDER_XML, new Object[] {attempts, reason, status, id});
		} catch (Exception e) {
			logger.error("Excetion occured while update Id: " + id, e);
			return 0;
		}
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
