package com.fb.platform.franchise.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.fb.platform.franchise.domain.FranchiseBO;

public class FranchiseMapper implements RowMapper<FranchiseBO>{

	@Override
	public FranchiseBO mapRow(ResultSet rs, int rowNum) throws SQLException {	
		FranchiseBO franchiseBO = new FranchiseBO();

		/*
			franchiseID, name, networkID, userID, isActive;
		 */
		
		franchiseBO.setFranchiseID(rs.getInt("id"));
		franchiseBO.setActive(rs.getBoolean("is_active"));
		franchiseBO.setUserID(rs.getInt("user_id"));
		franchiseBO.setRole(rs.getString("role"));
		franchiseBO.setNetworkID(rs.getInt("network_id"));
		
		return franchiseBO;
	}
}
