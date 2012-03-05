/**
 * 
 */
package com.fb.platform.user.manager.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.fb.platform.user.dao.interfaces.UserAdminDao;
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

	private UserAdminDao userAdminDao;
	private UserBoToMapper userMapper = new UserBoToMapper();

	/* (non-Javadoc)
	 * @see com.fb.platform.user.manager.interfaces.UserAdminManager#getUser(java.lang.String)
	 */
	@Override
	public UserTO getUser(String key) {
		UserBo userBo = getUserAdminDao().load(key);
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
		userAdminDao.add(userMapper.fromTOtoBO(user));
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.user.manager.interfaces.UserAdminManager#updateUser(com.fb.platform.user.manager.model.UserTO)
	 */
	@Override
	public UserTO updateUser(UserTO user) {
		return userMapper.fromBOtoTO(userAdminDao.update(userMapper.fromTOtoBO(user)));
	}

	@Override
	public Collection<UserTO> getUsers() {
		Collection<UserBo> userBolst = userAdminDao.getUsers();
		Collection<UserTO> userTOlst = new ArrayList<UserTO>() ;
		for(UserBo user : userBolst){
			userTOlst.add(userMapper.fromBOtoTO(user));
		}
		return userTOlst;
	}

	public UserAdminDao getUserAdminDao() {
		return userAdminDao;
	}

	public void setUserDao(UserAdminDao userAdminDao) {
		this.userAdminDao = userAdminDao;
	}

}
