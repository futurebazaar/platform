/**
 * 
 */
package com.fb.platform.promotion.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;

import com.fb.platform.promotion.dao.PromotionBundleDao;
import com.fb.platform.promotion.model.PromotionBundle;

/**
 * @author Keith Fernandez
 *
 */
public class PromotionBundleDaoJdbcImpl implements PromotionBundleDao {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/* (non-Javadoc)
	 * @see com.fb.dao.promotion.PromotionBundleDao#get(java.lang.Integer)
	 */
	@Override
	public PromotionBundle get(Integer bundleId) {
		// TODO Auto-generated method stub
		return null;
	}
}
