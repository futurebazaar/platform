/**
 * 
 */
package com.fb.platform.dao.catagory.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fb.platform.catagory.Category;
import com.fb.platform.dao.catagory.CategoryDao;

/**
 * @author vinayak
 *
 */
public class CategoryDaoJdbcImpl implements CategoryDao {

	private JdbcTemplate jdbcTemplate;

	private Log log = LogFactory.getLog(CategoryDaoJdbcImpl.class);

	/* (non-Javadoc)
	 * @see com.fb.platform.dao.catagory.CategoryDao#get(java.lang.Integer)
	 */
	@Override
	public Category get(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.dao.catagory.CategoryDao#create(com.fb.platform.catagory.Category)
	 */
	@Override
	public void create(Category category) {
		// TODO Auto-generated method stub

	}

}
