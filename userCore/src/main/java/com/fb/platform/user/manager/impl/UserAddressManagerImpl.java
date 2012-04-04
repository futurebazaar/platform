package com.fb.platform.user.manager.impl;

import org.apache.log4j.Logger;

import com.fb.platform.user.dao.interfaces.UserAddressDao;
import com.fb.platform.user.manager.interfaces.UserAddressManager;
import com.fb.platform.user.manager.model.address.AddAddressRequest;
import com.fb.platform.user.manager.model.address.AddAddressResponse;
import com.fb.platform.user.manager.model.address.GetAddressRequest;
import com.fb.platform.user.manager.model.address.GetAddressResponse;
import com.fb.platform.user.manager.model.address.UpdateAddressRequest;
import com.fb.platform.user.manager.model.address.UpdateAddressResponse;

public class UserAddressManagerImpl implements UserAddressManager {

	private static final Logger logger = Logger.getLogger(UserAddressManagerImpl.class);

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
