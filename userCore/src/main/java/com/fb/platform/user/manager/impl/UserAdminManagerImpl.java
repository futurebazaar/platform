/**
 * 
 */
package com.fb.platform.user.manager.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.fb.platform.user.dao.interfaces.UserDao;
import com.fb.platform.user.domain.UserBo;
import com.fb.platform.user.manager.interfaces.UserAdminManager;
import com.fb.platform.user.manager.mapper.UserBoToMapper;
import com.fb.platform.user.manager.model.UserTO;

/**
 * @author vinayak
 *
 */
public class UserAdminManagerImpl implements UserAdminManager {

	private UserDao userDao;
	private UserBoToMapper userMapper = new UserBoToMapper();

	/* (non-Javadoc)
	 * @see com.fb.platform.user.manager.interfaces.UserAdminManager#getUser(java.lang.String)
	 */
	@Override
	public UserTO getUser(String key) {
		UserBo userBo = getUserDao().load(key);
		if(userBo != null) {
			return userMapper.fromBOtoTO(userBo);
		} 

		return null;

	}

	/* (non-Javadoc)
	 * @see com.fb.platform.user.manager.interfaces.UserAdminManager#addUser(com.fb.platform.user.manager.model.UserTO)
	 */
	@Override
	public void addUser(UserTO user) {
		userDao.add(userMapper.fromTOtoBO(user));
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.user.manager.interfaces.UserAdminManager#updateUser(com.fb.platform.user.manager.model.UserTO)
	 */
	@Override
	public UserTO updateUser(UserTO user) {
		return userMapper.fromBOtoTO(userDao.update(userMapper.fromTOtoBO(user)));
	}

	@Override
	public Collection<UserTO> getUsers() {
		Collection<UserBo> userBolst = userDao.getUsers();
		Collection<UserTO> userTOlst = new ArrayList<UserTO>() ;
		for(UserBo user : userBolst){
			userTOlst.add(userMapper.fromBOtoTO(user));
		}
		return userTOlst;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}
