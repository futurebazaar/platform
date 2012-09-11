/**
 * 
 */
package com.fb.platform.ifs.dao.lsp.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fb.platform.ifs.dao.lsp.LSPDao;
import com.fb.platform.ifs.to.lsp.LSP;

/**
 * @author vinayak
 *
 */
public class LSPDaoJdbcImpl implements LSPDao {

	private JdbcTemplate jdbcTemplate;

	private Log log = LogFactory.getLog(LSPDaoJdbcImpl.class);

	@Override
	public List<LSP> loadAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
