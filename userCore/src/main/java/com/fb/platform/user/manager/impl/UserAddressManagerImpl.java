package com.fb.platform.user.manager.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.PlatformException;
import com.fb.platform.auth.AuthenticationService;
import com.fb.platform.auth.AuthenticationTO;
import com.fb.platform.user.manager.exception.AddressNotFoundException;
import com.fb.platform.user.manager.exception.UserNotFoundException;
import com.fb.platform.user.manager.interfaces.UserAddressManager;
import com.fb.platform.user.manager.interfaces.UserAddressService;
import com.fb.platform.user.manager.model.address.AddAddressRequest;
import com.fb.platform.user.manager.model.address.AddAddressResponse;
import com.fb.platform.user.manager.model.address.AddAddressStatusEnum;
import com.fb.platform.user.manager.model.address.DeleteAddressRequest;
import com.fb.platform.user.manager.model.address.DeleteAddressResponse;
import com.fb.platform.user.manager.model.address.DeleteAddressStatusEnum;
import com.fb.platform.user.manager.model.address.GetAddressByIdRequest;
import com.fb.platform.user.manager.model.address.GetAddressByIdResponse;
import com.fb.platform.user.manager.model.address.GetAddressByIdStatusEnum;
import com.fb.platform.user.manager.model.address.GetAddressRequest;
import com.fb.platform.user.manager.model.address.GetAddressResponse;
import com.fb.platform.user.manager.model.address.GetAddressStatusEnum;
import com.fb.platform.user.manager.model.address.UpdateAddressRequest;
import com.fb.platform.user.manager.model.address.UpdateAddressResponse;
import com.fb.platform.user.manager.model.address.UpdateAddressStatusEnum;

public class UserAddressManagerImpl implements UserAddressManager {

	private static final Log logger = LogFactory.getLog(UserAddressManagerImpl.class);

	@Autowired
	private UserAddressService userAddressService;
	
	@Autowired
	private AuthenticationService authenticationService;

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
			getAddressResponse.setUserAddress(userAddressService.getAddress(getAddressRequest.getUserId()));
			getAddressResponse.setGetAddressStatus(GetAddressStatusEnum.SUCCESS);
			return getAddressResponse;
		} catch (UserNotFoundException e) {
			getAddressResponse.setGetAddressStatus(GetAddressStatusEnum.INVALID_USER);
		} catch (AddressNotFoundException e) {
			getAddressResponse.setGetAddressStatus(GetAddressStatusEnum.NO_ADDRESSES);
		} catch (PlatformException pe) {
			logger.error("Error while getting address for the user : " + getAddressRequest.getUserId(), pe);
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
			addAddressResponse.setAddressId(userAddressService.addAddress(addAddressRequest.getUserId(), addAddressRequest.getUserAddress()).getAddressId());
			addAddressResponse.setAddAddressStatus(AddAddressStatusEnum.SUCCESS);
			return addAddressResponse;
		} catch (UserNotFoundException e) {
			addAddressResponse.setAddAddressStatus(AddAddressStatusEnum.INVALID_USER);
		} catch (AddressNotFoundException e) {
			addAddressResponse.setAddAddressStatus(AddAddressStatusEnum.EMPTY_ADDRESS);
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
		
		try {
			AuthenticationTO authentication = authenticationService.authenticate(updateAddressRequest.getSessionToken());
			if (authentication == null) {
				updateAddressResponse.setUpdateAddressStatus(UpdateAddressStatusEnum.NO_SESSION);
				return updateAddressResponse;
			}
			updateAddressResponse.setSessionToken(authentication.getToken());
			updateAddressResponse.setUpdateAddressStatus(userAddressService.updateAddress(updateAddressRequest.getUserId(), updateAddressRequest.getUserAddress()));
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
		try {
			AuthenticationTO authentication = authenticationService.authenticate(deleteAddressRequest.getSessionToken());
			if (authentication == null) {
				deleteAddressResponse.setDeleteAddressStatus(DeleteAddressStatusEnum.NO_SESSION);
				return deleteAddressResponse;
			}
			deleteAddressResponse.setSessionToken(authentication.getToken());
			deleteAddressResponse.setDeleteAddressStatus(userAddressService.deleteAddress(deleteAddressRequest.getUserId(), deleteAddressRequest.getAddressId()));
			return deleteAddressResponse;
		} catch (PlatformException pe) {
			logger.error("Error while adding address for user : " + deleteAddressRequest.getUserId(), pe);
			deleteAddressResponse.setDeleteAddressStatus(DeleteAddressStatusEnum.ERROR_DELETING_ADDRESS);
		}
		return deleteAddressResponse;
	}

	public UserAddressService getUserAddressService() {
		return userAddressService;
	}

	public void setUserAddressService(UserAddressService userAddressService) {
		this.userAddressService = userAddressService;
	}

	public AuthenticationService getAuthenticationService() {
		return authenticationService;
	}

	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@Override
	public GetAddressByIdResponse getAddress(
			GetAddressByIdRequest getAddressByIdRequest) {
		GetAddressByIdResponse getAddressByIdResponse = new GetAddressByIdResponse();
		if (getAddressByIdRequest == null) {
			getAddressByIdResponse.setGetAddressStatus(GetAddressByIdStatusEnum.INVALID_ADDRESS_ID);
			return getAddressByIdResponse;
		}
		if (getAddressByIdRequest.getAddressId() <= 0) {
			getAddressByIdResponse.setGetAddressStatus(GetAddressByIdStatusEnum.INVALID_ADDRESS_ID);
			return getAddressByIdResponse;
		}
		if(logger.isDebugEnabled()) {
			logger.debug("Getting user address for : " + getAddressByIdRequest.getAddressId());
		}
		if (StringUtils.isBlank(getAddressByIdRequest.getSessionToken())) {
			getAddressByIdResponse.setGetAddressStatus(GetAddressByIdStatusEnum.NO_SESSION);
			return getAddressByIdResponse;
		}
		try {
			AuthenticationTO authentication = authenticationService.authenticate(getAddressByIdRequest.getSessionToken());
			if (authentication == null) {
				getAddressByIdResponse.setGetAddressStatus(GetAddressByIdStatusEnum.NO_SESSION);
				return getAddressByIdResponse;
			}
			getAddressByIdResponse.setSessionToken(authentication.getToken());
			getAddressByIdResponse.setUserAddress(userAddressService.getAddress(getAddressByIdRequest.getAddressId()));
			getAddressByIdResponse.setGetAddressStatus(GetAddressByIdStatusEnum.SUCCESS);
			return getAddressByIdResponse;
		} catch (AddressNotFoundException e) {
			getAddressByIdResponse.setGetAddressStatus(GetAddressByIdStatusEnum.INVALID_ADDRESS_ID);
		} catch (PlatformException pe) {
			logger.error("Error while getting address for the user : " + getAddressByIdRequest.getAddressId(), pe);
			getAddressByIdResponse.setGetAddressStatus(GetAddressByIdStatusEnum.ERROR_RETRIVING_ADDRESS);
		}
		return getAddressByIdResponse;
	}	
}
