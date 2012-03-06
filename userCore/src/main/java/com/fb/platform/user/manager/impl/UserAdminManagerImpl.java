/**
 * 
 */
package com.fb.platform.user.manager.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fb.commons.PlatformException;
import com.fb.platform.auth.AuthenticationService;
import com.fb.platform.auth.AuthenticationTO;
import com.fb.platform.user.dao.interfaces.UserAdminDao;
import com.fb.platform.user.dao.interfaces.UserDao;
import com.fb.platform.user.domain.UserBo;
import com.fb.platform.user.domain.UserEmailBo;
import com.fb.platform.user.manager.interfaces.UserAdminManager;
import com.fb.platform.user.manager.mapper.UserBoToMapper;
import com.fb.platform.user.manager.model.UserTO;

import com.fb.platform.user.manager.model.admin.AddUserRequest;
import com.fb.platform.user.manager.model.admin.AddUserResponse;
import com.fb.platform.user.manager.model.admin.AddUserStatusEnum;
import com.fb.platform.user.manager.model.admin.GetUserRequest;
import com.fb.platform.user.manager.model.admin.GetUserResponse;
import com.fb.platform.user.manager.model.admin.GetUserStatusEnum;
import com.fb.platform.user.manager.model.admin.UpdateUserReponse;
import com.fb.platform.user.manager.model.admin.UpdateUserRequest;
import com.fb.platform.user.manager.model.admin.UpdateUserStatusEnum;
import com.fb.platform.user.manager.model.auth.LoginStatusEnum;
import com.fb.platform.user.manager.model.auth.LogoutStatusEnum;

/**
 * @author vinayak
 *
 */
public class UserAdminManagerImpl implements UserAdminManager {
	
	private static Logger logger = Logger.getLogger(UserAdminManagerImpl.class);

	private UserAdminDao userAdminDao;
	private UserBoToMapper userMapper = new UserBoToMapper();
	private AuthenticationService authenticationService;

	/* (non-Javadoc)
	 * @see com.fb.platform.user.manager.interfaces #getUser(java.lang.String)
	 */
	@Override
	public GetUserResponse getUser(GetUserRequest getUserRequest) {
		GetUserResponse getUserResponse = new GetUserResponse();
		if (StringUtils.isBlank(getUserRequest.getSessionToken())){
			getUserResponse.setStatus(GetUserStatusEnum.NO_SESSION);
			return getUserResponse;
		}
		if (getUserRequest == null || StringUtils.isBlank(getUserRequest.getKey()) ) {
			getUserResponse.setStatus(GetUserStatusEnum.NO_USER_KEY);
			return getUserResponse;
		}
		try{
			AuthenticationTO authentication = authenticationService.authenticate(getUserRequest.getSessionToken());
			if (authentication == null) {
				getUserResponse.setStatus(GetUserStatusEnum.NO_SESSION);
				return getUserResponse;
			}
			UserBo user = userAdminDao.load(getUserRequest.getKey());
			if (user == null) {
				getUserResponse.setStatus(GetUserStatusEnum.INVALID_USER);
				return getUserResponse;
			}
			getUserResponse.setStatus(GetUserStatusEnum.SUCCESS);
			getUserResponse.setUsername(user.getName());
			return getUserResponse;
			
		}catch(PlatformException pe){
			logger.error("Error while getting the user : " + getUserRequest.getKey(), pe);
			getUserResponse.setStatus(GetUserStatusEnum.ERROR_RETRIVING_USER);
		}
		
		return getUserResponse;
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.user.manager.interfaces.UserAdminManager#addUser(com.fb.platform.user.manager.model.UserTO)
	 */
	@Override
	public AddUserResponse addUser(AddUserRequest addUserRequest) {
		AddUserResponse addUserResponse = new AddUserResponse();
		
		if (addUserRequest == null || StringUtils.isBlank(addUserRequest.getUsername()) ) {
			addUserResponse.setStatus(AddUserStatusEnum.NO_USER_PROVIDED);
			return addUserResponse;
		}
		try{
			UserBo userBo = new UserBo();
			userBo.setUsername(addUserRequest.getUsername());
			userBo.setPassword(addUserRequest.getPassword());
			userAdminDao.add(userBo);			
			addUserResponse.setStatus(AddUserStatusEnum.SUCCESS);			
			return addUserResponse;
			
		}catch(PlatformException pe){
			logger.error("Error while adding the user : " + addUserRequest.getUsername(), pe);
			addUserResponse.setStatus(AddUserStatusEnum.ADD_USER_FAILED);
		}
		
		return addUserResponse;
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.user.manager.interfaces.UserAdminManager#updateUser(com.fb.platform.user.manager.model.UserTO)
	 */
	@Override
	public UpdateUserReponse updateUser(UpdateUserRequest updateUserRequest) {
		
		UpdateUserReponse updateUserReponse = new UpdateUserReponse();
		if (StringUtils.isBlank(updateUserReponse.getSessionToken())){
			updateUserReponse.setStatus(UpdateUserStatusEnum.NO_SESSION);
			return updateUserReponse;
		}
		if (updateUserRequest == null ) {
			updateUserReponse.setStatus(UpdateUserStatusEnum.NO_USER_PROVIDED);
			return updateUserReponse;
		}
		try{
			AuthenticationTO authentication = authenticationService.authenticate(updateUserRequest.getSessionToken());
			if (authentication == null) {
				updateUserReponse.setStatus(UpdateUserStatusEnum.NO_SESSION);
				return updateUserReponse;
			}
			UserBo userBo = new UserBo();
			userBo.setDateofbirth(updateUserRequest.getDateOfBirth());
			userBo.setFirstname(updateUserRequest.getFirstName());
			userBo.setGender(updateUserRequest.getGender());
			userBo.setLastname(updateUserRequest.getLastName());
			UserBo user = userAdminDao.update(userBo);
			if (user == null) {
				updateUserReponse.setStatus(UpdateUserStatusEnum.INVALID_USER);
				return updateUserReponse;
			}
			updateUserReponse.setStatus(UpdateUserStatusEnum.SUCCESS);			
			return updateUserReponse;
			
		}catch(PlatformException pe){
			logger.error("Error while getting the user : " + updateUserRequest.getFirstName(), pe);
			updateUserReponse.setStatus(UpdateUserStatusEnum.UPDATE_USER_FAILED);
		}
		
		return updateUserReponse;
	}

	public UserAdminDao getUserAdminDao() {
		return userAdminDao;
	}

	public void setUserAdminDao(UserAdminDao userAdminDao) {
		this.userAdminDao = userAdminDao;
	}

}
