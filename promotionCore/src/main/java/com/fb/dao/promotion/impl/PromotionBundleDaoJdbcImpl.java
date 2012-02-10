/**
 * 
 */
package com.fb.dao.promotion.impl;

import org.springframework.jdbc.core.JdbcTemplate;

import com.fb.bo.promotion.PromotionBundle;
import com.fb.dao.promotion.PromotionBundleDao;

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
