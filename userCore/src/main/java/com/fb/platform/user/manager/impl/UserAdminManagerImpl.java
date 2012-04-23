/**
 *
 */
package com.fb.platform.user.manager.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.PlatformException;
import com.fb.platform.auth.AuthenticationService;
import com.fb.platform.auth.AuthenticationTO;
import com.fb.platform.sso.SSOMasterService;
import com.fb.platform.sso.SSOSessionId;
import com.fb.platform.sso.SSOSessionTO;
import com.fb.platform.sso.SSOToken;
import com.fb.platform.sso.caching.SessionTokenCacheAccess;

import com.fb.platform.user.manager.exception.EmailNotFoundException;
import com.fb.platform.user.manager.exception.InvalidUserNameException;
import com.fb.platform.user.manager.exception.PhoneNotFoundException;
import com.fb.platform.user.manager.exception.UserAlreadyExistsException;
import com.fb.platform.user.manager.exception.UserNotFoundException;
import com.fb.platform.user.manager.interfaces.UserAdminManager;
import com.fb.platform.user.manager.interfaces.UserAdminService;
import com.fb.platform.user.manager.model.admin.AddUserRequest;
import com.fb.platform.user.manager.model.admin.AddUserResponse;
import com.fb.platform.user.manager.model.admin.AddUserStatusEnum;
import com.fb.platform.user.manager.model.admin.GetUserRequest;
import com.fb.platform.user.manager.model.admin.GetUserResponse;
import com.fb.platform.user.manager.model.admin.GetUserStatusEnum;
import com.fb.platform.user.manager.model.admin.IsValidUserEnum;
import com.fb.platform.user.manager.model.admin.IsValidUserRequest;
import com.fb.platform.user.manager.model.admin.IsValidUserResponse;
import com.fb.platform.user.manager.model.admin.UpdateUserRequest;
import com.fb.platform.user.manager.model.admin.UpdateUserResponse;
import com.fb.platform.user.manager.model.admin.UpdateUserStatusEnum;
import com.fb.platform.user.manager.model.admin.User;
import com.fb.platform.user.manager.model.admin.email.AddUserEmailRequest;
import com.fb.platform.user.manager.model.admin.email.AddUserEmailResponse;
import com.fb.platform.user.manager.model.admin.email.AddUserEmailStatusEnum;
import com.fb.platform.user.manager.model.admin.email.DeleteUserEmailRequest;
import com.fb.platform.user.manager.model.admin.email.DeleteUserEmailResponse;
import com.fb.platform.user.manager.model.admin.email.DeleteUserEmailStatusEnum;
import com.fb.platform.user.manager.model.admin.email.GetUserEmailRequest;
import com.fb.platform.user.manager.model.admin.email.GetUserEmailResponse;
import com.fb.platform.user.manager.model.admin.email.GetUserEmailStatusEnum;
import com.fb.platform.user.manager.model.admin.email.VerifyUserEmailRequest;
import com.fb.platform.user.manager.model.admin.email.VerifyUserEmailResponse;
import com.fb.platform.user.manager.model.admin.email.VerifyUserEmailStatusEnum;
import com.fb.platform.user.manager.model.admin.phone.AddUserPhoneRequest;
import com.fb.platform.user.manager.model.admin.phone.AddUserPhoneResponse;
import com.fb.platform.user.manager.model.admin.phone.AddUserPhoneStatusEnum;
import com.fb.platform.user.manager.model.admin.phone.DeleteUserPhoneRequest;
import com.fb.platform.user.manager.model.admin.phone.DeleteUserPhoneResponse;
import com.fb.platform.user.manager.model.admin.phone.DeleteUserPhoneStatusEnum;
import com.fb.platform.user.manager.model.admin.phone.GetUserPhoneRequest;
import com.fb.platform.user.manager.model.admin.phone.GetUserPhoneResponse;
import com.fb.platform.user.manager.model.admin.phone.GetUserPhoneStatusEnum;
import com.fb.platform.user.manager.model.admin.phone.VerifyUserPhoneRequest;
import com.fb.platform.user.manager.model.admin.phone.VerifyUserPhoneResponse;
import com.fb.platform.user.manager.model.admin.phone.VerifyUserPhoneStatusEnum;

/**
 * @author vinayak
 * @author kislay
 */
public class UserAdminManagerImpl implements UserAdminManager {

	private static final Log logger = LogFactory.getLog(UserAdminManagerImpl.class);

	@Autowired
	private UserAdminService userAdminService;
	
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
		if(logger.isDebugEnabled()) {
			logger.debug("Getting user details for : " + getUserRequest.getSessionToken());
		}
		try {
			AuthenticationTO authentication = authenticationService.authenticate(getUserRequest.getSessionToken());
			if (authentication == null) {
				getUserResponse.setStatus(GetUserStatusEnum.NO_SESSION);
				return getUserResponse;
			}
			getUserResponse.setSessionToken(authentication.getToken());
			User user = userAdminService.getUser(getUserRequest.getKey());
			getUserResponse.setStatus(GetUserStatusEnum.SUCCESS);
			getUserResponse.setUserName(user.getUserName());
			return getUserResponse;

		} catch (UserNotFoundException e){
			getUserResponse.setStatus(GetUserStatusEnum.INVALID_USER);
		}catch (PlatformException pe) {
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
			addUserResponse.setUserId(0);
			return addUserResponse;
		}
		if(logger.isDebugEnabled()) {
			logger.debug("Add user with username : " + addUserRequest.getUserName() );
		}
		try {
			User user = new User();
			user.setUserName(addUserRequest.getUserName());
			user.setPassword(addUserRequest.getPassword());
			User userRes = userAdminService.addUser(user);
			addUserResponse.setStatus(AddUserStatusEnum.SUCCESS);
			SSOSessionTO ssoSession = new SSOSessionTO();
			ssoSession.setUserId(userRes.getUserId());
			//create the global sso session
			SSOSessionId ssoSessionId = ssoMasterService.createSSOSession(ssoSession);
			//create the session token to be included in the response and cache it for the use of next request
			SSOToken ssoToken = ssoMasterService.createSessionToken(ssoSessionId);
			sessionTokenCacheAccess.put(ssoToken, ssoSessionId);
			addUserResponse.setSessionToken(ssoToken.getToken());
			addUserResponse.setUserId(userRes.getUserId());
		} catch (InvalidUserNameException e) {
			addUserResponse.setStatus(AddUserStatusEnum.INVALID_USER_NAME);
			addUserResponse.setUserId(0);
		}catch (UserAlreadyExistsException e) {
			addUserResponse.setStatus(AddUserStatusEnum.USER_ALREADY_EXISTS);
			addUserResponse.setUserId(0);
		}catch (PlatformException pe) {
			logger.error("Error while adding the user : " + addUserRequest.getUserName(), pe);
			addUserResponse.setStatus(AddUserStatusEnum.ADD_USER_FAILED);
		}
		return addUserResponse;
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.user.manager.interfaces.UserAdminManager#updateUser(com.fb.platform.user.manager.model.UserTO)
	 */
	@Override
	public UpdateUserResponse updateUser(UpdateUserRequest updateUserRequest) {
		
		UpdateUserResponse updateUserReponse = new UpdateUserResponse();
		if (updateUserRequest == null) {
			updateUserReponse.setStatus(UpdateUserStatusEnum.NO_USER_PROVIDED);
			return updateUserReponse;
		}

		if (StringUtils.isBlank(updateUserRequest.getSessionToken())) {
			updateUserReponse.setStatus(UpdateUserStatusEnum.NO_SESSION);
			return updateUserReponse;
		}
		if(logger.isDebugEnabled()) {
			logger.debug("Update user with session token : " + updateUserRequest.getSessionToken() );
		}

		try {
			AuthenticationTO authentication = authenticationService.authenticate(updateUserRequest.getSessionToken());
			if (authentication == null) {
				updateUserReponse.setStatus(UpdateUserStatusEnum.NO_SESSION);
				return updateUserReponse;
			}
			User user  = new User();
			user.setUserId(authentication.getUserID());
			user.setDateOfBirth(updateUserRequest.getDateOfBirth());
			user.setFirstName(updateUserRequest.getFirstName());
			user.setLastName(updateUserRequest.getLastName());
			user.setGender(updateUserRequest.getGender());
			user.setSalutation(updateUserRequest.getSalutation());			
			updateUserReponse.setSessionToken(authentication.getToken());
			updateUserReponse.setStatus(userAdminService.updateUser(user));
			return updateUserReponse;
		} catch (UserNotFoundException e){
			logger.error("Error while getting the user : " + updateUserRequest.getFirstName(), e);
			updateUserReponse.setStatus(UpdateUserStatusEnum.UPDATE_USER_FAILED);
		}catch (PlatformException pe) {
			logger.error("Error while getting the user : " + updateUserRequest.getFirstName(), pe);
			updateUserReponse.setStatus(UpdateUserStatusEnum.UPDATE_USER_FAILED);
		}
		return updateUserReponse;
	}

	@Override
	public IsValidUserResponse isValidUser(IsValidUserRequest isValidUserRequest) {
		IsValidUserResponse isValidUserResponse = new IsValidUserResponse();
		if (isValidUserRequest == null || StringUtils.isBlank(isValidUserRequest.getUserName())) {
			isValidUserResponse.setIsValidUserStatus(IsValidUserEnum.INVALID_USER);
			return isValidUserResponse;
		}
		if(logger.isDebugEnabled()) {
			logger.debug("Checking if user is valid : " + isValidUserRequest.getUserName() );
		}

		try {
			User user = userAdminService.getUser(isValidUserRequest.getUserName());
			isValidUserResponse.setUserId(user.getUserId());
			isValidUserResponse.setIsValidUserStatus(IsValidUserEnum.VALID_USER);
			return isValidUserResponse;
		}catch (UserNotFoundException e){
			isValidUserResponse.setIsValidUserStatus(IsValidUserEnum.INVALID_USER);
		} catch (PlatformException pe) {
			logger.error("Error while getting the user : " + isValidUserRequest.getUserName(), pe);
			isValidUserResponse.setIsValidUserStatus(IsValidUserEnum.ERROR);
		}
		return isValidUserResponse;
	}

	@Override
	public GetUserEmailResponse getUserEmail(
			GetUserEmailRequest getUserEmailRequest) {
		GetUserEmailResponse getUserEmailResponse = new GetUserEmailResponse();
		if (getUserEmailRequest == null) {
			getUserEmailResponse.setGetUserEmailStatus(GetUserEmailStatusEnum.INVALID_USER);
			return getUserEmailResponse;
		}
		if(logger.isDebugEnabled()) {
			logger.debug("Getting emails for user : " + getUserEmailRequest.getUserId() );
		}		
		if (StringUtils.isBlank(getUserEmailRequest.getSessionToken())) {
			getUserEmailResponse.setGetUserEmailStatus(GetUserEmailStatusEnum.NO_SESSION);
			return getUserEmailResponse;
		}
		try {
			AuthenticationTO authentication = authenticationService.authenticate(getUserEmailRequest.getSessionToken());
			if (authentication == null) {
				getUserEmailResponse.setGetUserEmailStatus(GetUserEmailStatusEnum.NO_SESSION);
				return getUserEmailResponse;
			}
			getUserEmailResponse.setUserId(getUserEmailRequest.getUserId());
			getUserEmailResponse.setSessionToken(authentication.getToken());
			getUserEmailResponse.setUserEmail(userAdminService.getUserEmail(getUserEmailRequest.getUserId()));
			getUserEmailResponse.setGetUserEmailStatus(GetUserEmailStatusEnum.SUCCESS);
			return getUserEmailResponse;
		} catch(UserNotFoundException e){
			getUserEmailResponse.setGetUserEmailStatus(GetUserEmailStatusEnum.INVALID_USER);
		} catch(EmailNotFoundException e){
			getUserEmailResponse.setGetUserEmailStatus(GetUserEmailStatusEnum.NO_EMAIL_ID);
		} catch (PlatformException pe) {
			logger.error("Error while getting the user emails: " + getUserEmailRequest.getUserId(), pe);
			getUserEmailResponse.setGetUserEmailStatus(GetUserEmailStatusEnum.ERROR_RETRIVING_EMAIL);
		}
		return getUserEmailResponse;
	}

	@Override
	public AddUserEmailResponse addUserEmail(
			AddUserEmailRequest addUserEmailRequest) {		
		AddUserEmailResponse addUserEmailResponse = new AddUserEmailResponse();
		if (addUserEmailRequest == null) {
			addUserEmailResponse.setAddUserEmailStatus(AddUserEmailStatusEnum.INVALID_EMAIL);
			return addUserEmailResponse;
		}
		if (StringUtils.isBlank(addUserEmailRequest.getSessionToken())) {
			addUserEmailResponse.setAddUserEmailStatus(AddUserEmailStatusEnum.NO_SESSION);
			return addUserEmailResponse;
		}
		try {
			AuthenticationTO authentication = authenticationService.authenticate(addUserEmailRequest.getSessionToken());
			if (authentication == null) {
				addUserEmailResponse.setAddUserEmailStatus(AddUserEmailStatusEnum.NO_SESSION);
				return addUserEmailResponse;
			}
			addUserEmailResponse.setSessionToken(authentication.getToken());
			addUserEmailResponse.setAddUserEmailStatus(userAdminService.addUserEmail(addUserEmailRequest.getUserId(), addUserEmailRequest.getUserEmail()));
		}catch (UserNotFoundException e) {
			addUserEmailResponse.setAddUserEmailStatus(AddUserEmailStatusEnum.ERROR_ADDING_EMAIL);
		}catch (PlatformException pe) {
			logger.error("Error while adding the user emails: " + addUserEmailRequest.getUserId(), pe);
			addUserEmailResponse.setAddUserEmailStatus(AddUserEmailStatusEnum.ERROR_ADDING_EMAIL);
		}
		return addUserEmailResponse;
	}

	@Override
	public VerifyUserEmailResponse verifyUserEmail(
			VerifyUserEmailRequest verifyUserEmailRequest) {
		VerifyUserEmailResponse verifyUserEmailResponse = new VerifyUserEmailResponse();
		if (verifyUserEmailRequest == null) {
			verifyUserEmailResponse.setVerifyUserEmailStatus(VerifyUserEmailStatusEnum.INVALID_USER);
			return verifyUserEmailResponse;
		}
		if(logger.isDebugEnabled()) {
			logger.debug("Verify Email for user : " + verifyUserEmailRequest.getUserId()+ "::" + verifyUserEmailRequest.getEmail());
		}
		if (StringUtils.isBlank(verifyUserEmailRequest.getSessionToken())) {
			verifyUserEmailResponse.setVerifyUserEmailStatus(VerifyUserEmailStatusEnum.NO_SESSION);
			return verifyUserEmailResponse;
		}
		try {
			AuthenticationTO authentication = authenticationService.authenticate(verifyUserEmailRequest.getSessionToken());
			if (authentication == null) {
				verifyUserEmailResponse.setVerifyUserEmailStatus(VerifyUserEmailStatusEnum.NO_SESSION);
				return verifyUserEmailResponse;
			}
			verifyUserEmailResponse.setSessionToken(authentication.getToken());
			verifyUserEmailResponse.setVerifyUserEmailStatus(userAdminService.verifyUserEmail(verifyUserEmailRequest.getUserId(), verifyUserEmailRequest.getEmail(),verifyUserEmailRequest.getVerificationCode()));
			return verifyUserEmailResponse;
		}catch (PlatformException pe) {
			logger.error("Error while verify the user Phone: " + verifyUserEmailRequest.getUserId(), pe);
			verifyUserEmailResponse.setVerifyUserEmailStatus(VerifyUserEmailStatusEnum.ERROR_VERIFYING_EMAIL);
		}
		return verifyUserEmailResponse;
	}

	@Override
	public DeleteUserEmailResponse deleteUserEmail(
			DeleteUserEmailRequest deleteUserEmailRequest) {
		
		DeleteUserEmailResponse deleteUserEmailResponse = new DeleteUserEmailResponse();
		if (deleteUserEmailRequest == null) {
			deleteUserEmailResponse.setDeleteUserEmailStatus(DeleteUserEmailStatusEnum.INVALID_REQUEST);
			return deleteUserEmailResponse;
		}
		if(logger.isDebugEnabled()) {
			logger.debug("Delete email for user : " + deleteUserEmailRequest.getUserId()+ "::" + deleteUserEmailRequest.getEmailId());
		}		
		if (StringUtils.isBlank(deleteUserEmailRequest.getSessionToken())) {
			deleteUserEmailResponse.setDeleteUserEmailStatus(DeleteUserEmailStatusEnum.NO_SESSION);
			return deleteUserEmailResponse;
		}
		try {
			AuthenticationTO authentication = authenticationService.authenticate(deleteUserEmailRequest.getSessionToken());
			if (authentication == null) {
				deleteUserEmailResponse.setDeleteUserEmailStatus(DeleteUserEmailStatusEnum.NO_SESSION);
				return deleteUserEmailResponse;
			}
			deleteUserEmailResponse.setSessionToken(authentication.getToken());
			deleteUserEmailResponse.setDeleteUserEmailStatus(userAdminService.deleteUserEmail(deleteUserEmailRequest.getUserId(), deleteUserEmailRequest.getEmailId()));
			return deleteUserEmailResponse;
		}catch (PlatformException pe) {
			logger.error("Error while delete the user email: " + deleteUserEmailRequest.getUserId(), pe);
			deleteUserEmailResponse.setDeleteUserEmailStatus(DeleteUserEmailStatusEnum.ERROR_DELETING_EMAIL);
		}
		return deleteUserEmailResponse;
	}

	@Override
	public GetUserPhoneResponse getUserPhone(
			GetUserPhoneRequest getUserPhoneRequest) {
		GetUserPhoneResponse getUserPhoneResponse = new GetUserPhoneResponse();
		if (getUserPhoneRequest == null) {
			getUserPhoneResponse.setGetUserPhoneStatus(GetUserPhoneStatusEnum.INVALID_USER);
			return getUserPhoneResponse;
		}
		if(logger.isDebugEnabled()) {
			logger.debug("Getting Phones for user : " + getUserPhoneRequest.getUserId() );
		}
		if (StringUtils.isBlank(getUserPhoneRequest.getSessionToken())) {
			getUserPhoneResponse.setGetUserPhoneStatus(GetUserPhoneStatusEnum.NO_SESSION);
			return getUserPhoneResponse;
		}
		try {
			AuthenticationTO authentication = authenticationService.authenticate(getUserPhoneRequest.getSessionToken());
			if (authentication == null) {
				getUserPhoneResponse.setGetUserPhoneStatus(GetUserPhoneStatusEnum.NO_SESSION);
				return getUserPhoneResponse;
			}
			getUserPhoneResponse.setUserId(getUserPhoneRequest.getUserId());
			getUserPhoneResponse.setSessionToken(authentication.getToken());
			getUserPhoneResponse.setUserPhone(userAdminService.getUserPhone(getUserPhoneRequest.getUserId()));
			getUserPhoneResponse.setGetUserPhoneStatus(GetUserPhoneStatusEnum.SUCCESS);
			return getUserPhoneResponse;
		} catch(UserNotFoundException e){
			getUserPhoneResponse.setGetUserPhoneStatus(GetUserPhoneStatusEnum.INVALID_USER);
		} catch(PhoneNotFoundException e){
			getUserPhoneResponse.setGetUserPhoneStatus(GetUserPhoneStatusEnum.NO_PHONE);
		} catch (PlatformException pe) {
			logger.error("Error while getting the user emails: " + getUserPhoneRequest.getUserId(), pe);
			getUserPhoneResponse.setGetUserPhoneStatus(GetUserPhoneStatusEnum.ERROR_RETRIVING_PHONE);
		}
		return getUserPhoneResponse;
	}

	@Override
	public AddUserPhoneResponse addUserPhone(
			AddUserPhoneRequest addUserPhoneRequest) {
		AddUserPhoneResponse addUserPhoneResponse = new AddUserPhoneResponse();
		if (addUserPhoneRequest == null) {
			addUserPhoneResponse.setAddUserPhoneStatus(AddUserPhoneStatusEnum.INVALID_PHONE);
			return addUserPhoneResponse;
		}
		if(logger.isDebugEnabled()) {
			logger.debug("Adding Phones for user : " + addUserPhoneRequest.getUserId() );
		}
		
		
		if (StringUtils.isBlank(addUserPhoneRequest.getSessionToken())) {
			addUserPhoneResponse.setAddUserPhoneStatus(AddUserPhoneStatusEnum.NO_SESSION);
			return addUserPhoneResponse;
		}
		try {
			AuthenticationTO authentication = authenticationService.authenticate(addUserPhoneRequest.getSessionToken());
			if (authentication == null) {
				addUserPhoneResponse.setAddUserPhoneStatus(AddUserPhoneStatusEnum.NO_SESSION);
				return addUserPhoneResponse;
			}
			addUserPhoneResponse.setSessionToken(authentication.getToken());
			addUserPhoneResponse.setAddUserPhoneStatus(userAdminService.addUserPhone(addUserPhoneRequest.getUserId(), addUserPhoneRequest.getUserPhone()));
		}catch (UserNotFoundException e) {
			addUserPhoneResponse.setAddUserPhoneStatus(AddUserPhoneStatusEnum.ERROR_ADDING_PHONE);
		}catch (PlatformException pe) {
			logger.error("Error while adding the user emails: " + addUserPhoneRequest.getUserId(), pe);
			addUserPhoneResponse.setAddUserPhoneStatus(AddUserPhoneStatusEnum.ERROR_ADDING_PHONE);
		}
		return addUserPhoneResponse;
	}

	@Override
	public VerifyUserPhoneResponse verifyUserPhone(
			VerifyUserPhoneRequest verifyUserPhoneRequest) {
		VerifyUserPhoneResponse verifyUserPhoneResponse = new VerifyUserPhoneResponse();
		if (verifyUserPhoneRequest == null) {
			verifyUserPhoneResponse.setVerifyUserPhoneStatus(VerifyUserPhoneStatusEnum.INVALID_USER);
			return verifyUserPhoneResponse;
		}
		if(logger.isDebugEnabled()) {
			logger.debug("Verify Phone for user : " + verifyUserPhoneRequest.getUserId()+ "::" + verifyUserPhoneRequest.getPhone());
		}
		if (StringUtils.isBlank(verifyUserPhoneRequest.getSessionToken())) {
			verifyUserPhoneResponse.setVerifyUserPhoneStatus(VerifyUserPhoneStatusEnum.NO_SESSION);
			return verifyUserPhoneResponse;
		}
		try {
			AuthenticationTO authentication = authenticationService.authenticate(verifyUserPhoneRequest.getSessionToken());
			if (authentication == null) {
				verifyUserPhoneResponse.setVerifyUserPhoneStatus(VerifyUserPhoneStatusEnum.NO_SESSION);
				return verifyUserPhoneResponse;
			}
			verifyUserPhoneResponse.setSessionToken(authentication.getToken());
			verifyUserPhoneResponse.setVerifyUserPhoneStatus(userAdminService.verifyUserPhone(verifyUserPhoneRequest.getUserId(), verifyUserPhoneRequest.getPhone(),verifyUserPhoneRequest.getVerificationCode()));
			return verifyUserPhoneResponse;
		}catch (PlatformException pe) {
			logger.error("Error while verify the user Phone: " + verifyUserPhoneRequest.getUserId(), pe);
			verifyUserPhoneResponse.setVerifyUserPhoneStatus(VerifyUserPhoneStatusEnum.ERROR_VERIFYING_PHONE);
		}
		return verifyUserPhoneResponse;
	}
	@Override
	public DeleteUserPhoneResponse deleteUserPhone(
			DeleteUserPhoneRequest deleteUserPhoneRequest) {
		
		DeleteUserPhoneResponse deleteUserPhoneResponse = new DeleteUserPhoneResponse();
		if (deleteUserPhoneRequest == null) {
			deleteUserPhoneResponse.setDeleteUserPhoneStatus(DeleteUserPhoneStatusEnum.INVALID_REQUEST);
			return deleteUserPhoneResponse;
		}
		if(logger.isDebugEnabled()) {
			logger.debug("Delete Phone for user : " + deleteUserPhoneRequest.getUserId()+ "::" + deleteUserPhoneRequest.getPhone());
		}
		
		
		
		if (StringUtils.isBlank(deleteUserPhoneRequest.getSessionToken())) {
			deleteUserPhoneResponse.setDeleteUserPhoneStatus(DeleteUserPhoneStatusEnum.NO_SESSION);
			return deleteUserPhoneResponse;
		}
		try {
			AuthenticationTO authentication = authenticationService.authenticate(deleteUserPhoneRequest.getSessionToken());
			if (authentication == null) {
				deleteUserPhoneResponse.setDeleteUserPhoneStatus(DeleteUserPhoneStatusEnum.NO_SESSION);
				return deleteUserPhoneResponse;
			}
			deleteUserPhoneResponse.setSessionToken(authentication.getToken());
			deleteUserPhoneResponse.setDeleteUserPhoneStatus(userAdminService.deleteUserPhone(deleteUserPhoneRequest.getUserId(), deleteUserPhoneRequest.getPhone()));
			return deleteUserPhoneResponse;
		}catch (PlatformException pe) {
			logger.error("Error while delete the user phone: " + deleteUserPhoneRequest.getUserId(), pe);
			deleteUserPhoneResponse.setDeleteUserPhoneStatus(DeleteUserPhoneStatusEnum.ERROR_DELETING_PHONE);
		}
		return deleteUserPhoneResponse;
	}

	/**
	 * @return the userAdminService
	 */
	public UserAdminService getUserAdminService() {
		return userAdminService;
	}

	/**
	 * @param userAdminService the userAdminService to set
	 */
	public void setUserAdminService(UserAdminService userAdminService) {
		this.userAdminService = userAdminService;
	}

	/**
	 * @return the authenticationService
	 */
	public AuthenticationService getAuthenticationService() {
		return authenticationService;
	}

	/**
	 * @param authenticationService the authenticationService to set
	 */
	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	/**
	 * @return the ssoMasterService
	 */
	public SSOMasterService getSsoMasterService() {
		return ssoMasterService;
	}

	/**
	 * @param ssoMasterService the ssoMasterService to set
	 */
	public void setSsoMasterService(SSOMasterService ssoMasterService) {
		this.ssoMasterService = ssoMasterService;
	}

	/**
	 * @return the sessionTokenCacheAccess
	 */
	public SessionTokenCacheAccess getSessionTokenCacheAccess() {
		return sessionTokenCacheAccess;
	}

	/**
	 * @param sessionTokenCacheAccess the sessionTokenCacheAccess to set
	 */
	public void setSessionTokenCacheAccess(
			SessionTokenCacheAccess sessionTokenCacheAccess) {
		this.sessionTokenCacheAccess = sessionTokenCacheAccess;
	}
	
	
}
