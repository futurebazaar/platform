package com.fb.platform.franchise.manager.impl;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.franchise.dao.interfaces.IFranchiseDAO;
import com.fb.platform.franchise.domain.FranchiseBO;
import com.fb.platform.franchise.manager.model.FranchiseTO;
import com.fb.platform.test.manager.mapper.UserBoToMapper;
import com.fb.platform.user.manager.interfaces.UserManager;


public class FranchiseManagerImpl implements UserManager {
	
	private IFranchiseDAO franchiseDao;
	
	/**
	 * @param userDao the userDao to set
	 */
	public void setFranchiseDAO(IFranchiseDAO franchiseDao) {
		this.franchiseDao = franchiseDao;
	}

	private static Logger logger = Logger.getLogger(FranchiseManagerImpl.class);

	@Override
	@Transactional
	public FranchiseTO getFranchise(String key) {
		// TODO Auto-generated method stub
		FranchiseBO franchiseBO = franchiseDao.getFranchise(key);
		return new UserBoToMapper().fromBOtoTO(franchiseBO);
	}

}
