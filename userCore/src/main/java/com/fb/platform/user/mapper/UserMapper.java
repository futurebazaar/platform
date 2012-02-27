package com.fb.platform.user.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.fb.platform.user.domain.UserBo;
import com.fb.platform.user.domain.UserEmailBo;
import com.fb.platform.user.domain.UserPhoneBo;

public class UserMapper implements RowMapper<UserBo>{

	private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public UserBo mapRow(ResultSet rs, int rowNum) throws SQLException {
		try{
			UserBo user = new UserBo();
			user.setUserid(rs.getInt("id"));
			user.setName(rs.getString("full_name"));
			user.setGender(rs.getString("gender"));
			user.setDateofbirth(rs.getDate("date_of_birth"));
			user.setPassword(rs.getString("password"));
			user.setSalutation(rs.getString("salutation"));
			
			return user;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	

}
