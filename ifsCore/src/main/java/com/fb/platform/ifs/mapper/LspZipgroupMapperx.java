/**
 * 
 */
package com.fb.platform.ifs.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.fb.platform.ifs.domain.LspZipgroupBox;

/**
 * @author sarvesh
 *
 */
public class LspZipgroupMapperx implements RowMapper<LspZipgroupBox> {

	/* (non-Javadoc)
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	@Override
	public LspZipgroupBox mapRow(ResultSet rs, int rowNum) throws SQLException {
		LspZipgroupBox lspZipgroupBo = new LspZipgroupBox();
		lspZipgroupBo.setZipGroupId(rs.getString("zipgroup_id"));
		lspZipgroupBo.setLsp(rs.getString("lsp_id"));
		return lspZipgroupBo;
	}

}
