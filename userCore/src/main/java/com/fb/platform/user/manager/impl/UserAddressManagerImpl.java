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
import com.fb.platform.user.manager.model.address.AddAddressStatusEnum;
import com.fb.platform.user.manager.model.address.DeleteAddressRequest;
import com.fb.platform.user.manager.model.address.DeleteAddressResponse;
import com.fb.platform.user.manager.model.address.DeleteAddressStatusEnum;
import com.fb.platform.user.manager.model.address.GetAddressRequest;
import com.fb.platform.user.manager.model.address.GetAddressResponse;
import com.fb.platform.user.manager.model.address.GetAddressStatusEnum;
import com.fb.platform.user.manager.model.address.UpdateAddressRequest;
import com.fb.platform.user.manager.model.address.UpdateAddressResponse;
import com.fb.platform.user.manager.model.address.UpdateAddressStatusEnum;
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
			getAddressResponse.setSessionToken(authentication.getToken());
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
		AddAddressResponse addAddressResponse = new AddAddressResponse();
		if (addAddressRequest == null) {
			addAddressResponse.setAddAddressStatus(AddAddressStatusEnum.INVALID_USER);
			return addAddressResponse;
		}
		if(logger.isDebugEnabled()) {
			logger.debug("Adding user address for : " + addAddressRequest.getUserId());
		}
		if (StringUtils.isBlank(addAddressRequest.getSessionToken())) {
			addAddressResponse.setAddAddressStatus(AddAddressStatusEnum.NO_SESSION);
			return addAddressResponse;
		}
		try {
			AuthenticationTO authentication = authenticationService.authenticate(addAddressRequest.getSessionToken());
			if (authentication == null) {
				addAddressResponse.setAddAddressStatus(AddAddressStatusEnum.NO_SESSION);
				return addAddressResponse;
			}
			addAddressResponse.setSessionToken(authentication.getToken());
			UserBo user = userAdminDao.loadByUserId(addAddressRequest.getUserId());
			if (user == null) {
				addAddressResponse.setAddAddressStatus(AddAddressStatusEnum.INVALID_USER);
				return addAddressResponse;
			}
			if(StringUtils.isBlank(addAddressRequest.getUserAddress().getAddress())){
				addAddressResponse.setAddAddressStatus(AddAddressStatusEnum.EMPTY_ADDRESS);
				return addAddressResponse;
			}
			UserAddressBo userAddressBo = new UserAddressBo();
			userAddressBo.setAddress(addAddressRequest.getUserAddress().getAddress());
			userAddressBo.setAddresstype(addAddressRequest.getUserAddress().getAddressType());
			userAddressBo.setCity(addAddressRequest.getUserAddress().getCity());
			userAddressBo.setState(addAddressRequest.getUserAddress().getState());
			userAddressBo.setCountry(addAddressRequest.getUserAddress().getCountry());
			userAddressBo.setPincode(addAddressRequest.getUserAddress().getPinCode());
			userAddressBo.setUserid(addAddressRequest.getUserId());
			UserAddressBo userAddressBoAdd = userAddressDao.add(userAddressBo);
			addAddressResponse.setAddressId(userAddressBoAdd.getAddressid());
			addAddressResponse.setAddAddressStatus(AddAddressStatusEnum.SUCCESS);
			return addAddressResponse;
		} catch (PlatformException pe) {
			logger.error("Error while adding address for user : " + addAddressRequest.getUserId(), pe);
			addAddressResponse.setAddAddressStatus(AddAddressStatusEnum.ERROR_ADDING_ADDRESS);
		}
		return addAddressResponse;
	}

	@Override
	public UpdateAddressResponse updateAddress(
			UpdateAddressRequest updateAddressRequest) {
		UpdateAddressResponse updateAddressResponse = new UpdateAddressResponse();
		if (updateAddressRequest == null) {
			updateAddressResponse.setUpdateAddressStatus(UpdateAddressStatusEnum.INVALID_USER);
			return updateAddressResponse;
		}
		if(logger.isDebugEnabled()) {
			logger.debug("Updateing user address for : " + updateAddressRequest.getUserId());
		}
		if (StringUtils.isBlank(updateAddressRequest.getSessionToken())) {
			updateAddressResponse.setUpdateAddressStatus(UpdateAddressStatusEnum.NO_SESSION);
			return updateAddressResponse;
		}
		if (updateAddressRequest.getUserAddress() == null) {
			updateAddressResponse.setUpdateAddressStatus(UpdateAddressStatusEnum.ERROR_UPDATING_ADDRESS);
			return updateAddressResponse;
		}
		if (updateAddressRequest.getUserAddress().getAddressId() <= 0) {
			updateAddressResponse.setUpdateAddressStatus(UpdateAddressStatusEnum.ADDRESSID_ABSENT);
			return updateAddressResponse;
		}
		try {
			AuthenticationTO authentication = authenticationService.authenticate(updateAddressRequest.getSessionToken());
			if (authentication == null) {
				updateAddressResponse.setUpdateAddressStatus(UpdateAddressStatusEnum.NO_SESSION);
				return updateAddressResponse;
			}
			updateAddressResponse.setSessionToken(authentication.getToken());
			UserBo user = userAdminDao.loadByUserId(updateAddressRequest.getUserId());
			if (user == null) {
				updateAddressResponse.setUpdateAddressStatus(UpdateAddressStatusEnum.INVALID_USER);
				return updateAddressResponse;
			}
			UserAddressBo userAddressBo = userAddressDao.getAddressById(updateAddressRequest.getUserAddress().getAddressId());
			if (userAddressBo.getUserid() != updateAddressRequest.getUserId()){
				updateAddressResponse.setUpdateAddressStatus(UpdateAddressStatusEnum.USER_ADDRESSID_MISMATCH);
				return updateAddressResponse;
			}
			if(!StringUtils.isBlank(updateAddressRequest.getUserAddress().getAddress())){
				userAddressBo.setAddress(updateAddressRequest.getUserAddress().getAddress());
			}
			if(!StringUtils.isBlank(updateAddressRequest.getUserAddress().getAddressType())){
				userAddressBo.setAddresstype(updateAddressRequest.getUserAddress().getAddressType());
			}
			if(!StringUtils.isBlank(updateAddressRequest.getUserAddress().getCity())){
				userAddressBo.setCity(updateAddressRequest.getUserAddress().getCity());
			}
			if(!StringUtils.isBlank(updateAddressRequest.getUserAddress().getState())){
				userAddressBo.setState(updateAddressRequest.getUserAddress().getState());
			}
			if(!StringUtils.isBlank(updateAddressRequest.getUserAddress().getCountry())){
				userAddressBo.setCountry(updateAddressRequest.getUserAddress().getCountry());
			}
			if(!StringUtils.isBlank(updateAddressRequest.getUserAddress().getPinCode())){
				userAddressBo.setPincode(updateAddressRequest.getUserAddress().getPinCode());
			}
			
			userAddressDao.update(userAddressBo);
			updateAddressResponse.setUpdateAddressStatus(UpdateAddressStatusEnum.SUCCESS);
			
			return updateAddressResponse;
		} catch (PlatformException pe) {
			logger.error("Error while adding address for user : " + updateAddressRequest.getUserId(), pe);
			updateAddressResponse.setUpdateAddressStatus(UpdateAddressStatusEnum.ERROR_UPDATING_ADDRESS);
		}
		return updateAddressResponse;
	}
	
	@Override
	public DeleteAddressResponse deleteAddress(
			DeleteAddressRequest deleteAddressRequest) {
		DeleteAddressResponse deleteAddressResponse = new DeleteAddressResponse();
		if (deleteAddressRequest == null) {
			deleteAddressResponse.setDeleteAddressStatus(DeleteAddressStatusEnum.INVALID_USER);
			return deleteAddressResponse;
		}
		if(logger.isDebugEnabled()) {
			logger.debug("Deleteing user address for : " + deleteAddressRequest.getUserId());
		}
		if (StringUtils.isBlank(deleteAddressRequest.getSessionToken())) {
			deleteAddressResponse.setDeleteAddressStatus(DeleteAddressStatusEnum.NO_SESSION);
			return deleteAddressResponse;
		}
		if (deleteAddressRequest.getAddressId() <=0) {
			deleteAddressResponse.setDeleteAddressStatus(DeleteAddressStatusEnum.ADDRESSID_ABSENT);
			return deleteAddressResponse;
		}
		try {
			AuthenticationTO authentication = authenticationService.authenticate(deleteAddressRequest.getSessionToken());
			if (authentication == null) {
				deleteAddressResponse.setDeleteAddressStatus(DeleteAddressStatusEnum.NO_SESSION);
				return deleteAddressResponse;
			}
			deleteAddressResponse.setSessionToken(authentication.getToken());
			UserBo user = userAdminDao.loadByUserId(deleteAddressRequest.getUserId());
			if (user == null) {
				deleteAddressResponse.setDeleteAddressStatus(DeleteAddressStatusEnum.INVALID_USER);
				return deleteAddressResponse;
			}
			UserAddressBo userAddressBo = userAddressDao.getAddressById(deleteAddressRequest.getAddressId());
			if(userAddressBo == null){
				deleteAddressResponse.setDeleteAddressStatus(DeleteAddressStatusEnum.ADDRESSID_ABSENT);
				return deleteAddressResponse;
			}
			if (userAddressBo.getUserid() != deleteAddressRequest.getUserId()){
				deleteAddressResponse.setDeleteAddressStatus(DeleteAddressStatusEnum.USER_ADDRESSID_MISMATCH);
				return deleteAddressResponse;
			}
			
			boolean success = userAddressDao.deleteAddressById(deleteAddressRequest.getAddressId());
			if (success){
				deleteAddressResponse.setDeleteAddressStatus(DeleteAddressStatusEnum.SUCCESS);
			}else{
				deleteAddressResponse.setDeleteAddressStatus(DeleteAddressStatusEnum.ERROR_DELETING_ADDRESS);
			}		
			
			return deleteAddressResponse;
		} catch (PlatformException pe) {
			logger.error("Error while adding address for user : " + deleteAddressRequest.getUserId(), pe);
			deleteAddressResponse.setDeleteAddressStatus(DeleteAddressStatusEnum.ERROR_DELETING_ADDRESS);
		}
		return deleteAddressResponse;
	}

	public UserAddressDao getUserAddressDao() {
		return userAddressDao;
	}

	public void setUserAddressDao(UserAddressDao userAddressDao) {
		this.userAddressDao = userAddressDao;
	}

	
}
