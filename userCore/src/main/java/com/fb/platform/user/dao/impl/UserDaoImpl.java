package com.fb.platform.user.dao.impl;


import com.fb.platform.user.dao.interfaces.UserAdminDao;
import com.fb.platform.user.dao.interfaces.UserDao;
import com.fb.platform.user.domain.UserBo;

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
