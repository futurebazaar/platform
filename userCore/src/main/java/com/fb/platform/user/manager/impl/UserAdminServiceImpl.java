package com.fb.platform.user.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.fb.commons.PlatformException;
import com.fb.platform.user.dao.interfaces.UserAdminDao;
import com.fb.platform.user.domain.UserBo;
import com.fb.platform.user.manager.exception.UserNotFoundException;
import com.fb.platform.user.manager.interfaces.UserAdminService;

public class UserAdminServiceImpl implements UserAdminService {
	
	@Autowired
	private UserAdminDao userAdminDao = null;

	@Override
	public UserBo getUser(String key) throws UserNotFoundException,
			PlatformException {
		UserBo user = new UserBo();
		try{
			user = userAdminDao.load(key);
		}catch (DataAccessException e) {
			throw new PlatformException("Error while loading the user. key = " + key, e);
		}
		if (user != null) {
			return user;
		} else {
			throw new UserNotFoundException("User not found with key : " + key);
		}
	}

	/**
	 * @return the userAdminDao
	 */
	public UserAdminDao getUserAdminDao() {
		return userAdminDao;
	}

	/**
	 * @param userAdminDao the userAdminDao to set
	 */
	public void setUserAdminDao(UserAdminDao userAdminDao) {
		this.userAdminDao = userAdminDao;
	}
	
	

}
