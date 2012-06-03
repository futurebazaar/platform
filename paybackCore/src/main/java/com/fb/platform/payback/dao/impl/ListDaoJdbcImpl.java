package com.fb.platform.payback.dao.impl;

import java.sql.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fb.platform.payback.dao.ListDao;

public class ListDaoJdbcImpl implements ListDao {

	private static Log logger = LogFactory.getLog(ListDaoJdbcImpl.class);

	private static String GET_DOD_QUERY = "SELECT li.sku_id  FROM "
			+ "lists_list ll, lists_listitem li WHERE "
			+ "ll.id = li.list_id AND " + "DATE(ll.starts_on) <= ? AND "
			+ "DATE(ll.ends_on) >= ? AND " + "type = 'hero_deal' ";

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Long getHeroDealSellerRateChart(DateTime orderDate) {
		Date date = new Date(orderDate.getMillis());
		logger.info("Getting Hero Deail for date  : " + date);
		return jdbcTemplate.queryForLong(GET_DOD_QUERY, new Object[] { date,
				date });
	}

}
