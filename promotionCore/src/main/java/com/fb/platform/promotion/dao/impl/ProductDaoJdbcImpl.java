/**
 * 
 */
package com.fb.platform.promotion.dao.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fb.platform.promotion.dao.ProductDao;

/**
 * @author vinayak
 *
 */
public class ProductDaoJdbcImpl implements ProductDao {

	private JdbcTemplate jdbcTemplate;

	private Log log = LogFactory.getLog(ProductDaoJdbcImpl.class);

	private static String PRODUCT_IDS_PLACEHOLDER = "PRODUCT_IDS_PLACEHOLDER";

	private static final String CLEARANCE_PRODUCT_TAG_STRING = "new_clearance_sale";

	/*
	 * SELECT DISTINCT product_id FROM catalog_producttags where product_id in (11, 62256, 12, 47758, 40184, 47562) AND type = 'new_clearance_sale'
	 */
	private static String SELECT_CLEARANCE_PRODUCT_IDS_QUERY = 
			"SELECT " +
			"	DISTINCT product_id " +
			"FROM catalog_producttags " +
			"WHERE " +
			"	product_id IN (" + PRODUCT_IDS_PLACEHOLDER + ")"  +
			"	AND type = '" + CLEARANCE_PRODUCT_TAG_STRING + "'";

	@Override
	public Set<Integer> findClearanceProductIds(Set<Integer> orderProductIds) {
		
		if (orderProductIds == null) {
			return new HashSet<Integer>();
		}
		if (orderProductIds.isEmpty()) {
			return orderProductIds;
		}

		String commaSeparatedProductIds = StringUtils.join(orderProductIds, ",");

		String query = SELECT_CLEARANCE_PRODUCT_IDS_QUERY.replace(PRODUCT_IDS_PLACEHOLDER, commaSeparatedProductIds);

		List<Integer> clearanceProductIds = jdbcTemplate.queryForList(query, Integer.class);

		Set<Integer> cleranceProductIdsSet = new HashSet<Integer>();
		cleranceProductIdsSet.addAll(clearanceProductIds);

		return cleranceProductIdsSet;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
