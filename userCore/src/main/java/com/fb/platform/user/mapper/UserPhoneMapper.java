package com.fb.platform.user.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.fb.platform.user.domain.UserEmailBo;
import com.fb.platform.user.domain.UserPhoneBo;

public class UserPhoneMapper implements RowMapper<UserPhoneBo> {

	@Override
	public UserPhoneBo mapRow(ResultSet rs, int rowNum) 
			throws SQLException {
		try {
			UserPhoneBo userPhoneBo = new UserPhoneBo();
			userPhoneBo.setPhoneno(rs.getString("phone"));
			userPhoneBo.setType(rs.getString("type"));
			return userPhoneBo;
		} catch (Exception e) {
			return null;
		}
	}
}