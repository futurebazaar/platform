package com.fb.platform.user.manager.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.fb.platform.user.dao.interfaces.UserAddressDao;
import com.fb.platform.user.domain.UserAddressBo;
import com.fb.platform.user.manager.interfaces.UserAddressManager;
import com.fb.platform.user.manager.mapper.UserAddressBoToMapper;
import com.fb.platform.user.manager.model.UserAddressTO;
import com.sun.istack.logging.Logger;

public class UserAddressManagerImpl implements UserAddressManager {
	
	private static Logger logger = Logger.getLogger(UserAddressManagerImpl.class);

	private UserAddressDao userAddressDao;
		
	@Override
	public GetAddressResponse getAddress(GetAddressRequest getAddressRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AddAddressResponse addAddress(AddAddressRequest addAddressRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpdateAddressResponse updateAddress(
			UpdateAddressRequest updateAddressRequest) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	public UserAddressDao getUserAddressDao() {
		return userAddressDao;
	}

	public void setUserAddressDao(UserAddressDao userAddressDao) {
		this.userAddressDao = userAddressDao;
	}



}
