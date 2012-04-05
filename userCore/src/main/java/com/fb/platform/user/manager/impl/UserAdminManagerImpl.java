/**
 *
 */
package com.fb.platform.user.manager.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.PlatformException;
import com.fb.platform.auth.AuthenticationService;
import com.fb.platform.auth.AuthenticationTO;
import com.fb.platform.sso.SSOMasterService;
import com.fb.platform.sso.SSOSessionId;
import com.fb.platform.sso.SSOSessionTO;
import com.fb.platform.sso.SSOToken;
import com.fb.platform.sso.caching.SessionTokenCacheAccess;
import com.fb.platform.user.dao.interfaces.UserAdminDao;
import com.fb.platform.user.domain.UserBo;
import com.fb.platform.user.manager.interfaces.UserAdminManager;
import com.fb.platform.user.manager.model.admin.AddUserRequest;
import com.fb.platform.user.manager.model.admin.AddUserResponse;
import com.fb.platform.user.manager.model.admin.AddUserStatusEnum;
import com.fb.platform.user.manager.model.admin.GetUserRequest;
import com.fb.platform.user.manager.model.admin.GetUserResponse;
import com.fb.platform.user.manager.model.admin.GetUserStatusEnum;
import com.fb.platform.user.manager.model.admin.IsValidUserEnum;
import com.fb.platform.user.manager.model.admin.IsValidUserRequest;
import com.fb.platform.user.manager.model.admin.IsValidUserResponse;
import com.fb.platform.user.manager.model.admin.UpdateUserReponse;
import com.fb.platform.user.manager.model.admin.UpdateUserRequest;
import com.fb.platform.user.manager.model.admin.UpdateUserStatusEnum;

/**
 * @author vinayak
 *
 */
public class UserAdminManagerImpl implements UserAdminManager {

	private static final Logger logger = Logger.getLogger(UserAdminManagerImpl.class);

	private UserAdminDao userAdminDao;
	
	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private SSOMasterService ssoMasterService = null;

	@Autowired
	private SessionTokenCacheAccess sessionTokenCacheAccess = null;


	/* (non-Javadoc)
	 * @see com.fb.platform.user.manager.interfaces #getUser(java.lang.String)
	 */
	@Override
	public GetUserResponse getUser(GetUserRequest getUserRequest) {
		GetUserResponse getUserResponse = new GetUserResponse();
		if (getUserRequest == null || StringUtils.isBlank(getUserRequest.getKey())) {
			getUserResponse.setStatus(GetUserStatusEnum.NO_USER_KEY);
			return getUserResponse;
		}
		if (StringUtils.isBlank(getUserRequest.getSessionToken())) {
			getUserResponse.setStatus(GetUserStatusEnum.NO_SESSION);
			return getUserResponse;
		}

		try {
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
			getUserResponse.setUserName(user.getName());
			return getUserResponse;

		} catch (PlatformException pe) {
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

		if (addUserRequest == null || StringUtils.isBlank(addUserRequest.getUserName())) {
			addUserResponse.setStatus(AddUserStatusEnum.NO_USER_PROVIDED);
			return addUserResponse;
		}
		try {
			UserBo userBo = new UserBo();
			userBo.setUsername(addUserRequest.getUserName());
			userBo.setPassword(addUserRequest.getPassword());
			UserBo user = userAdminDao.add(userBo);
			addUserResponse.setStatus(AddUserStatusEnum.SUCCESS);

			SSOSessionTO ssoSession = new SSOSessionTO();
			ssoSession.setUserId(user.getUserid());

			//create the global sso session
			SSOSessionId ssoSessionId = ssoMasterService.createSSOSession(ssoSession);

			//create the session token to be included in the response and cache it for the use of next request
			SSOToken ssoToken = ssoMasterService.createSessionToken(ssoSessionId);

			sessionTokenCacheAccess.put(ssoToken, ssoSessionId);

			addUserResponse.setSessionToken(ssoToken.getToken());
			addUserResponse.setUserId(user.getUserid());

			return addUserResponse;

		} catch (PlatformException pe) {
			logger.error("Error while adding the user : " + addUserRequest.getUserName(), pe);
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
		if (updateUserRequest == null) {
			updateUserReponse.setStatus(UpdateUserStatusEnum.NO_USER_PROVIDED);
			return updateUserReponse;
		}

		if (StringUtils.isBlank(updateUserReponse.getSessionToken())) {
			updateUserReponse.setStatus(UpdateUserStatusEnum.NO_SESSION);
			return updateUserReponse;
		}

		try {
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

		} catch (PlatformException pe) {
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

	@Override
	public IsValidUserResponse isValidUser(IsValidUserRequest isValidUserRequest) {
		IsValidUserResponse isValidUserResponse = new IsValidUserResponse();
		if (isValidUserRequest == null || StringUtils.isBlank(isValidUserRequest.getUserName())) {
			isValidUserResponse.setIsValidUserStatus(IsValidUserEnum.INVALID_USER);
			return isValidUserResponse;
		}

		try {

			UserBo user = userAdminDao.load(isValidUserRequest.getUserName());
			if (user == null) {
				isValidUserResponse.setIsValidUserStatus(IsValidUserEnum.INVALID_USER);
				return isValidUserResponse;
			}
			isValidUserResponse.setUserId(user.getUserid());
			isValidUserResponse.setIsValidUserStatus(IsValidUserEnum.VALID_USER);
			return isValidUserResponse;

		} catch (PlatformException pe) {
			logger.error("Error while getting the user : " + isValidUserRequest.getUserName(), pe);
			isValidUserResponse.setIsValidUserStatus(IsValidUserEnum.ERROR);
		}

		return isValidUserResponse;
	}

}
