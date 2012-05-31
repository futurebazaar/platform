/**
 * 
 */
package com.fb.platform.egv.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fb.platform.egv.dao.OrderDao;

/**
 * @author keith
 *
 */
public class OrderDaoJdbcImpl implements OrderDao{

	private JdbcTemplate jdbcTemplate;

	private Log log = LogFactory.getLog(OrderDaoJdbcImpl.class);
	
	private static final String GET_USER_ORDER_COUNT = 
			"SELECT " +
			"	count(1) " +
			"FROM orders_order " +
			"WHERE " +
			"	user_id = ? " +
			"	AND " +
			"	support_state IS NOT NULL " +
			"	AND " +
			"	support_state NOT IN ('booked','cancelled') " +
			"GROUP BY user_id";


	public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate=jdbcTemplate;
	}
	
	@Override
	public boolean isUserFirstOrder(int userId) {
		int orderCount = getUserOrderCount(userId);
		boolean isUserEligible = true;
		if(orderCount > 0) {
			isUserEligible = false;
		}
		return isUserEligible;
	}
	
	public int getUserOrderCount(int userId) {
		int orderCount = 0;
		try {
			orderCount = jdbcTemplate.queryForInt(GET_USER_ORDER_COUNT, new Object[]{userId});
		} catch (EmptyResultDataAccessException e) {
			if(log.isDebugEnabled()) {
				log.debug("User first record.");
			}
		}
		return orderCount;
	}
	
}
