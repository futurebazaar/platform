package com.fb.platform.user.manager.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.user.dao.interfaces.UserDao;
import com.fb.platform.user.domain.UserBo;
import com.fb.platform.user.manager.interfaces.UserManager;
import com.fb.platform.user.manager.mapper.UserBoToMapper;
import com.fb.platform.user.manager.model.UserTO;


/**
 * @author kumar
 *
 */
public class UserManagerImpl implements UserManager {
	
	private UserDao userDao;
	
	/**
	 * @param userDao the userDao to set
	 */
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	private static Logger logger = Logger.getLogger(UserManagerImpl.class);
	private UserBoToMapper userMapper = new UserBoToMapper();

	/* (non-Javadoc)
	 * @see com.fb.platform.user.manager.interfaces.UserManager#getuser(java.lang.String)
	 */
	@Override
	@Transactional
	public UserTO getuser(String key) {
		// TODO Auto-generated method stub
		UserBo userBo = userDao.load(key);
		if(userBo != null)
			return userMapper.fromBOtoTO(userBo);
		else
			return null;
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

	@Override
	public void adduser(UserTO userTO) {
		userDao.add(userMapper.fromTOtoBO(userTO));		
	}

	@Override
	public UserTO updateuser(UserTO userTO) {
		return userMapper.fromBOtoTO(userDao.update(userMapper.fromTOtoBO(userTO)));
		
	}

	@Override
	public UserTO login(String username, String password) {
		return  userMapper.fromBOtoTO(userDao.login(username, password));
		
	}

	@Override
	public UserTO logout(UserTO userTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserTO changepassword(UserTO userTO, String newpassword) {
		// TODO Auto-generated method stub
		return userMapper.fromBOtoTO(userDao.changepassword(userMapper.fromTOtoBO(userTO), newpassword));
	}

}
