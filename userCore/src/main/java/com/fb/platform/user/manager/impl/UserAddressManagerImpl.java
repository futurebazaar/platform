package com.fb.platform.user.manager.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.fb.platform.user.dao.interfaces.UserAddressDao;
import com.fb.platform.user.domain.UserAddressBo;
import com.fb.platform.user.manager.interfaces.UserAddressManager;
import com.fb.platform.user.manager.mapper.UserAddressBoToMapper;
import com.fb.platform.user.manager.model.UserAddressTO;

public class UserAddressManagerImpl implements UserAddressManager {

	private UserAddressDao userAddressDao;
	private UserAddressBoToMapper userAddressBoToMapper;
	
	
	@Override
	public void addAddress(UserAddressTO userAddressTO) {
		getUserAddressDao().add(userAddressBoToMapper.userAddressTOtoBo(userAddressTO));

	}

	@Override
	public void updateAddress(UserAddressTO userAddressTO) {
		getUserAddressDao().update(userAddressBoToMapper.userAddressTOtoBo(userAddressTO));

	}

	@Override
	public Collection<UserAddressTO> getAddress(int userid) {
		Collection<UserAddressBo> userAddressBos = getUserAddressDao().load(userid);
		Collection<UserAddressTO> userAddressTOs = new ArrayList<UserAddressTO>();
		for (UserAddressBo userAddressBo : userAddressBos){
			userAddressTOs.add(userAddressBoToMapper.userAddressBotoTo(userAddressBo));
		}
		return userAddressTOs;
	}
	
	
	public UserAddressDao getUserAddressDao() {
		return userAddressDao;
	}

	public void setUserAddressDao(UserAddressDao userAddressDao) {
		this.userAddressDao = userAddressDao;
	}

}
