package com.fb.platform.user.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.fb.platform.user.dao.interfaces.UserAdminDao;
import com.fb.platform.user.dao.interfaces.UserDao;
import com.fb.platform.user.domain.UserBo;
import com.fb.platform.user.domain.UserEmailBo;
import com.fb.platform.user.domain.UserPhoneBo;
import com.fb.platform.user.util.PasswordUtil;

/**
 * @author kumar
 *
 */
public class UserDaoImpl implements UserDao {
	
	private UserAdminDao userAdminDao;
	
	@Override
	public boolean changePassword(UserBo userBo,String newPassword) {
		userBo.setPassword(newPassword); //the update statement does the encryption
		userAdminDao.update(userBo);
		//TODO find a way to ensure the update actually worked, only then return true, else return false
		return true;
	}

	
}
