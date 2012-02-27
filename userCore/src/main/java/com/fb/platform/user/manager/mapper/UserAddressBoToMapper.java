package com.fb.platform.user.manager.mapper;

import com.fb.platform.user.domain.UserAddressBo;
import com.fb.platform.user.manager.model.UserAddressTO;

public class UserAddressBoToMapper {
	
	public UserAddressTO userAddressBotoTo (UserAddressBo userAddressBo){
		UserAddressTO userAddressTO = new UserAddressTO();
		
		userAddressTO.setAddress(userAddressBo.getAddress());
		userAddressTO.setAddressid(userAddressBo.getAddressid());
		userAddressTO.setAddresstype(userAddressBo.getAddresstype());
		userAddressTO.setCity(userAddressBo.getCity());
		userAddressTO.setCountry(userAddressBo.getCountry());
		userAddressTO.setPincode(userAddressBo.getPincode());
		userAddressTO.setState(userAddressBo.getState());
		userAddressTO.setUserid(userAddressBo.getUserid());
		return userAddressTO;
	}

	public UserAddressBo userAddressTOtoBo(UserAddressTO userAddressTO) {
		UserAddressBo userAddressBo = new UserAddressBo();
		
		userAddressBo.setAddress(userAddressTO.getAddress());
		userAddressBo.setAddressid(userAddressTO.getAddressid());
		userAddressBo.setAddresstype(userAddressTO.getAddresstype());
		userAddressBo.setCity(userAddressTO.getCity());
		userAddressBo.setCountry(userAddressTO.getCountry());
		userAddressBo.setPincode(userAddressTO.getPincode());
		userAddressBo.setState(userAddressTO.getState());
		userAddressBo.setUserid(userAddressTO.getUserid());
		
		return userAddressBo;
	}
}
