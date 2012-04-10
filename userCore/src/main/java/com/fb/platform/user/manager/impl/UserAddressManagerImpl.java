package com.fb.platform.user.manager.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.PlatformException;
import com.fb.platform.auth.AuthenticationService;
import com.fb.platform.auth.AuthenticationTO;
import com.fb.platform.sso.SSOMasterService;
import com.fb.platform.sso.caching.SessionTokenCacheAccess;
import com.fb.platform.user.dao.interfaces.UserAddressDao;
import com.fb.platform.user.dao.interfaces.UserAdminDao;
import com.fb.platform.user.domain.UserAddressBo;
import com.fb.platform.user.domain.UserBo;
import com.fb.platform.user.manager.interfaces.UserAddressManager;
import com.fb.platform.user.manager.model.address.AddAddressRequest;
import com.fb.platform.user.manager.model.address.AddAddressResponse;
import com.fb.platform.user.manager.model.address.GetAddressRequest;
import com.fb.platform.user.manager.model.address.GetAddressResponse;
import com.fb.platform.user.manager.model.address.GetAddressStatusEnum;
import com.fb.platform.user.manager.model.address.UpdateAddressRequest;
import com.fb.platform.user.manager.model.address.UpdateAddressResponse;
import com.fb.platform.user.manager.model.address.UserAddress;
import com.fb.platform.user.manager.model.admin.GetUserStatusEnum;

public class UserAddressManagerImpl implements UserAddressManager {

	private static final Log logger = LogFactory.getLog(UserAddressManagerImpl.class);

	private UserAddressDao userAddressDao;
	
	@Autowired
	private UserAdminDao userAdminDao;
	
	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private SSOMasterService ssoMasterService = null;

	@Autowired
	private SessionTokenCacheAccess sessionTokenCacheAccess = null;

	@Override
	public GetAddressResponse getAddress(GetAddressRequest getAddressRequest) {
		GetAddressResponse getAddressResponse = new GetAddressResponse();
		if (getAddressRequest == null) {
			getAddressResponse.setGetAddressStatus(GetAddressStatusEnum.INVALID_USER);
			return getAddressResponse;
		}
		if(logger.isDebugEnabled()) {
			logger.debug("Getting user address for : " + getAddressRequest.getUserId());
		}
		if (StringUtils.isBlank(getAddressRequest.getSessionToken())) {
			getAddressResponse.setGetAddressStatus(GetAddressStatusEnum.NO_SESSION);
			return getAddressResponse;
		}
		try {
			AuthenticationTO authentication = authenticationService.authenticate(getAddressRequest.getSessionToken());
			if (authentication == null) {
				getAddressResponse.setGetAddressStatus(GetAddressStatusEnum.NO_SESSION);
				return getAddressResponse;
			}
			UserBo user = userAdminDao.loadByUserId(getAddressRequest.getUserId());
			if (user == null) {
				getAddressResponse.setGetAddressStatus(GetAddressStatusEnum.INVALID_USER);
				return getAddressResponse;
			}
			Collection<UserAddressBo> userAddressBos = userAddressDao.load(getAddressRequest.getUserId());
			if(userAddressBos != null){
				if(userAddressBos.size() >0 ){
					List<UserAddress> userAddressLst = new ArrayList<UserAddress>();
					Iterator<UserAddressBo> itr = userAddressBos.iterator();
					while (itr.hasNext()){
						UserAddressBo userAddressBo = itr.next();
						UserAddress userAddress =  new UserAddress();
						userAddress.setAddress(userAddressBo.getAddress());
						userAddress.setAddressId(userAddressBo.getAddressid());
						userAddress.setAddressType(userAddressBo.getAddresstype());
						userAddress.setCity(userAddressBo.getCity());
						userAddress.setState(userAddressBo.getState());
						userAddress.setCountry(userAddressBo.getCountry());
						userAddress.setPinCode(userAddressBo.getPincode());
						userAddressLst.add(userAddress);
					}
					getAddressResponse.setSessionToken(authentication.getToken());
					getAddressResponse.setGetAddressStatus(GetAddressStatusEnum.SUCCESS);
					getAddressResponse.setUserAddress(userAddressLst);
					return getAddressResponse;
				}
				
			}
			getAddressResponse.setSessionToken(authentication.getToken());
			getAddressResponse.setGetAddressStatus(GetAddressStatusEnum.NO_ADDRESSES);
			return getAddressResponse;
		} catch (PlatformException pe) {
			logger.error("Error while getting the user : " + getAddressRequest.getUserId(), pe);
			getAddressResponse.setGetAddressStatus(GetAddressStatusEnum.ERROR_RETRIVING_ADDRESS);
		}
		return getAddressResponse;
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
