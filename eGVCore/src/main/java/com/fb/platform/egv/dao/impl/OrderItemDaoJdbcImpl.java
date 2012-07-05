/**
 * 
 */
package com.fb.platform.egv.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fb.platform.egv.dao.OrderItemDao;

/**
 * @author keith
 *
 */
public class OrderItemDaoJdbcImpl implements OrderItemDao{

	private JdbcTemplate jdbcTemplate;

	private Log log = LogFactory.getLog(OrderItemDaoJdbcImpl.class);
	
	private static final String GET_ORDER_ITEM_COUNT = 
			"SELECT " +
			"	count(1) " +
			"FROM orders_orderitem " +
			"WHERE " +
			"	id = ? ";


	public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate=jdbcTemplate;
	}
	
	@Override
	public boolean isValidId(int orderItemId) {
		int orderCount = getOrderItemCount(orderItemId);
		boolean isValid = false;
		if(orderCount > 0) {
			isValid = true;
		}
		return isValid;
	}
	
	public int getOrderItemCount(int orderItemId) {
		int orderCount = 0;
		try {
			orderCount = jdbcTemplate.queryForInt(GET_ORDER_ITEM_COUNT, new Object[]{orderItemId});
		} catch (EmptyResultDataAccessException e) {
			if(log.isDebugEnabled()) {
				log.debug("Invalid OrderItem Id");
			}
		}
		return orderCount;
	}
	
}
