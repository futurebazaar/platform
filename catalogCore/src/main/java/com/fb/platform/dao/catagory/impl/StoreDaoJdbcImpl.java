/**
 * 
 */
package com.fb.platform.dao.catagory.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.fb.platform.catagory.Store;
import com.fb.platform.dao.catagory.StoreDao;

/**
 * @author vinayak
 *
 */
public class StoreDaoJdbcImpl implements StoreDao {

	private JdbcTemplate jdbcTemplate;

	private Log log = LogFactory.getLog(StoreDaoJdbcImpl.class);

	private static final String GET_STORE_QUERY = "SELECT id, name, slug FROM categories_store where id = ?";

	private static final String GET_STORE_BY_NAME_QUERY = "SELECT id, name, slug FROM categories_store where name = ?";

	private static final String CREATE_STORE_QUERY = "INSERT INTO categories_store (name, slug) VALUES (?, ?)";

	/* (non-Javadoc)
	 * @see com.fb.platform.dao.catagory.StoreDao#get(java.lang.Integer)
	 */
	@Override
	public Store get(Integer id) {
		log.info("Getting the Store with id : " + id);
		List<Store> stores = (List<Store>) jdbcTemplate.query(GET_STORE_QUERY, new Object [] {id}, new StoreMapper());

		if (stores.size() == 1) {
			return stores.get(0);
		}
		log.warn("Unable to find the Store with id : " + id);
		throw new RuntimeException(); //TODO create custom exception
	}

	private Store get(String name) {
		log.info("");
		List<Store> stores = (List<Store>) jdbcTemplate.query(GET_STORE_BY_NAME_QUERY, new Object [] {name}, new StoreMapper());

		if (stores.size() == 1) {
			return stores.get(0);
		}
		log.warn("");
		throw new RuntimeException(); //TODO create custom exception
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.dao.catagory.StoreDao#create(com.fb.platform.catagory.Store)
	 */
	@Override
	public Store create(Store store) {
		int rowsCreated = jdbcTemplate.update(CREATE_STORE_QUERY,
				store.getName(),
				store.getSlug());

		if (rowsCreated == 0) {
			throw new RuntimeException(); //TODO throw custom exception
		}

		return get(store.getName());
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private static class StoreMapper implements RowMapper<Store> {

		@Override
		public Store mapRow(ResultSet rs, int rowNum) throws SQLException {
			Store store = new Store();

			store.setId(rs.getInt("id"));
			store.setName(rs.getString("name"));
			store.setSlug(rs.getString("slug"));
			return store;
		}
	}

}
