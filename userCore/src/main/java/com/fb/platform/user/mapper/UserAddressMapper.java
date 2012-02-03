package com.fb.platform.user.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.fb.platform.user.domain.UserAddressBo;

public class UserAddressMapper implements RowMapper<UserAddressBo> {

	@Override
	public UserAddressBo mapRow(ResultSet rs, int rowNum) throws SQLException {
		try{
			UserAddressBo useraddress = new UserAddressBo();
			useraddress.setAddressid(rs.getLong("addressid"));
			useraddress.setUserid(rs.getInt("userid"));
			useraddress.setAddress(rs.getString("address"));
			useraddress.setAddresstype(rs.getString("type"));
			useraddress.setCity(rs.getString("city"));
			useraddress.setCountry(rs.getString("country"));
			useraddress.setState(rs.getString("state"));
			useraddress.setPincode(rs.getString("pincode"));
			return useraddress;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
