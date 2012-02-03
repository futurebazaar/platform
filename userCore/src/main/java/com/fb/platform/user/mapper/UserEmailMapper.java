package com.fb.platform.user.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.fb.platform.user.domain.UserEmailBo;

public class UserEmailMapper implements RowMapper<UserEmailBo>{

	@Override
	public UserEmailBo mapRow(ResultSet rs, int rowNum) throws SQLException {
		try {
			UserEmailBo userEmailBo = new UserEmailBo();
			userEmailBo.setEmail(rs.getString("email"));
			userEmailBo.setType(rs.getString("type"));
			return userEmailBo;
		} catch (Exception e) {
			return null;
		}
	}
}
