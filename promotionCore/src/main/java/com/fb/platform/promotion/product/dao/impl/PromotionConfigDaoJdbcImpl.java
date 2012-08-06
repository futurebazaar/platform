/**
 * 
 */
package com.fb.platform.promotion.product.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fb.platform.promotion.product.dao.PromotionConfigDao;
import com.fb.platform.promotion.product.model.PromotionConfig;

/**
 * @author vinayak
 *
 */
public class PromotionConfigDaoJdbcImpl implements PromotionConfigDao {

	private JdbcTemplate jdbcTemplate;

	private Log log = LogFactory.getLog(PromotionConfigDaoJdbcImpl.class);

	@Override
	public PromotionConfig load(int promotionId) {
		
		return null;
	}

}
