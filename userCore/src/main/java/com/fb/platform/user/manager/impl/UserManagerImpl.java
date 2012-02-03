package com.fb.platform.user.manager.impl;

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

	/* (non-Javadoc)
	 * @see com.fb.platform.user.manager.interfaces.UserManager#getuser(java.lang.String)
	 */
	@Override
	@Transactional
	public UserTO getuser(String key) {
		// TODO Auto-generated method stub
		UserBo userBo = userDao.load(key);
		if(userBo != null)
			return new UserBoToMapper().fromBOtoTO(userBo);
		else
			return null;
	}

}
