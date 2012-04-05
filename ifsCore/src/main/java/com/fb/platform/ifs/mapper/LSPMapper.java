/**
 * 
 */
package com.fb.platform.ifs.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.fb.platform.ifs.domain.LSPBo;

/**
 * @author sarvesh
 *
 */
public class LSPMapper implements RowMapper<LSPBo> {
	

	@Override
	public LSPBo mapRow(ResultSet rs, int rowNum) throws SQLException {
		LSPBo lspBo = new LSPBo();
		lspBo.setLspId(rs.getString("lsp_id"));
		lspBo.setZipGroupId(rs.getString("zipgroup_id"));
		lspBo.setZipGroupCode(rs.getString("zipgroup_code"));
		lspBo.setLspPriority(rs.getInt("lsp_priority"));
		lspBo.setLspCode(rs.getString("lsp_code"));
		lspBo.setCod(rs.getBoolean("cod_flag"));
		lspBo.setHighValue(rs.getBoolean("high_value"));
		return lspBo;
	}

}
