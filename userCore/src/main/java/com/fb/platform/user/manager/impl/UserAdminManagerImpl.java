/**
 *
 */
package com.fb.platform.user.manager.impl;

import java.util.ArrayList;
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
import com.fb.platform.sso.SSOSessionId;
import com.fb.platform.sso.SSOSessionTO;
import com.fb.platform.sso.SSOToken;
import com.fb.platform.sso.caching.SessionTokenCacheAccess;
import com.fb.platform.user.dao.interfaces.UserAdminDao;
import com.fb.platform.user.domain.UserBo;
import com.fb.platform.user.domain.UserEmailBo;
import com.fb.platform.user.domain.UserPhoneBo;
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
import com.fb.platform.user.manager.model.admin.UpdateUserRequest;
import com.fb.platform.user.manager.model.admin.UpdateUserResponse;
import com.fb.platform.user.manager.model.admin.UpdateUserStatusEnum;
import com.fb.platform.user.manager.model.admin.email.AddUserEmailRequest;
import com.fb.platform.user.manager.model.admin.email.AddUserEmailResponse;
import com.fb.platform.user.manager.model.admin.email.AddUserEmailStatusEnum;
import com.fb.platform.user.manager.model.admin.email.DeleteUserEmailRequest;
import com.fb.platform.user.manager.model.admin.email.DeleteUserEmailResponse;
import com.fb.platform.user.manager.model.admin.email.DeleteUserEmailStatusEnum;
import com.fb.platform.user.manager.model.admin.email.GetUserEmailRequest;
import com.fb.platform.user.manager.model.admin.email.GetUserEmailResponse;
import com.fb.platform.user.manager.model.admin.email.GetUserEmailStatusEnum;
import com.fb.platform.user.manager.model.admin.email.UserEmail;
import com.fb.platform.user.manager.model.admin.email.VerifyUserEmailRequest;
import com.fb.platform.user.manager.model.admin.email.VerifyUserEmailResponse;
import com.fb.platform.user.manager.model.admin.phone.AddUserPhoneRequest;
import com.fb.platform.user.manager.model.admin.phone.AddUserPhoneResponse;
import com.fb.platform.user.manager.model.admin.phone.AddUserPhoneStatusEnum;
import com.fb.platform.user.manager.model.admin.phone.DeleteUserPhoneRequest;
import com.fb.platform.user.manager.model.admin.phone.DeleteUserPhoneResponse;
import com.fb.platform.user.manager.model.admin.phone.DeleteUserPhoneStatusEnum;
import com.fb.platform.user.manager.model.admin.phone.GetUserPhoneRequest;
import com.fb.platform.user.manager.model.admin.phone.GetUserPhoneResponse;
import com.fb.platform.user.manager.model.admin.phone.GetUserPhoneStatusEnum;
import com.fb.platform.user.manager.model.admin.phone.UserPhone;
import com.fb.platform.user.manager.model.admin.phone.VerifyUserPhoneRequest;
import com.fb.platform.user.manager.model.admin.phone.VerifyUserPhoneResponse;
import com.fb.platform.user.util.ValidatorUtil;

/**
 * @author vinayak
 * @author kislay
 */
public class UserAdminManagerImpl implements UserAdminManager {

	private static final Log logger = LogFactory.getLog(UserAdminManagerImpl.class);

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
		if(logger.isDebugEnabled()) {
			logger.debug("Getting user details for : " + getUserRequest.getSessionToken());
		}
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
		if(logger.isDebugEnabled()) {
			logger.debug("Add user with username : " + addUserRequest.getUserName() );
		}
		AddUserResponse addUserResponse = new AddUserResponse();

		if (addUserRequest == null || StringUtils.isBlank(addUserRequest.getUserName())) {
			addUserResponse.setStatus(AddUserStatusEnum.NO_USER_PROVIDED);
			addUserResponse.setUserId(0);
			return addUserResponse;
		}
		boolean isEmail = ValidatorUtil.isValidEmailAddress(addUserRequest.getUserName());
		boolean isPhone = ValidatorUtil.isValidPhoneNumber(addUserRequest.getUserName());
		if (!(isEmail | isPhone )){
			addUserResponse.setStatus(AddUserStatusEnum.INVALID_USER_NAME);
			addUserResponse.setUserId(0);
			return addUserResponse;
		}
		try {
			UserBo userPresent = userAdminDao.load(addUserRequest.getUserName());
			if(userPresent != null && userPresent.getUserid() > 0){
				addUserResponse.setStatus(AddUserStatusEnum.USER_ALREADY_EXISTS);
				addUserResponse.setUserId(userPresent.getUserid());
				return addUserResponse;
			}
			UserBo userBo = new UserBo();
			userBo.setUsername(addUserRequest.getUserName());
			userBo.setPassword(addUserRequest.getPassword());
			if(isEmail){
				UserEmailBo userEmailBo = new UserEmailBo();
				userEmailBo.setEmail(addUserRequest.getUserName());
				userEmailBo.setType("primary");
				List<UserEmailBo> userEmailBos = new ArrayList<UserEmailBo>();
				userEmailBos.add(userEmailBo);
				userBo.setUserEmail(userEmailBos);
			}
			if(isPhone){
				UserPhoneBo userPhoneBo = new UserPhoneBo();
				userPhoneBo.setPhoneno(addUserRequest.getUserName());
				userPhoneBo.setType("primary");
				List<UserPhoneBo> userPhoneBos = new ArrayList<UserPhoneBo>();
				userPhoneBos.add(userPhoneBo);
				userBo.setUserPhone(userPhoneBos);
			}
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
	public UpdateUserResponse updateUser(UpdateUserRequest updateUserRequest) {
		if(logger.isDebugEnabled()) {
			logger.debug("Update user with session token : " + updateUserRequest.getSessionToken() );
		}
		UpdateUserResponse updateUserReponse = new UpdateUserResponse();
		if (updateUserRequest == null) {
			updateUserReponse.setStatus(UpdateUserStatusEnum.NO_USER_PROVIDED);
			return updateUserReponse;
		}

		if (StringUtils.isBlank(updateUserRequest.getSessionToken())) {
			updateUserReponse.setStatus(UpdateUserStatusEnum.NO_SESSION);
			return updateUserReponse;
		}

		try {
			AuthenticationTO authentication = authenticationService.authenticate(updateUserRequest.getSessionToken());
			if (authentication == null) {
				updateUserReponse.setStatus(UpdateUserStatusEnum.NO_SESSION);
				return updateUserReponse;
			}
			UserBo userBo = userAdminDao.loadByUserId(authentication.getUserID());
			if (userBo == null) {
				updateUserReponse.setStatus(UpdateUserStatusEnum.INVALID_USER);
				return updateUserReponse;
			}
			if(updateUserRequest.getDateOfBirth() != null){
				userBo.setDateofbirth(updateUserRequest.getDateOfBirth());
			}
			if(!(StringUtils.isBlank(updateUserRequest.getFirstName()))){
				userBo.setFirstname(updateUserRequest.getFirstName());
			}
			if(!(StringUtils.isBlank(updateUserRequest.getLastName()))){
				userBo.setLastname(updateUserRequest.getLastName());
			}
			if(!(StringUtils.isBlank(updateUserRequest.getGender()))){
				userBo.setGender(updateUserRequest.getGender());
			}
			if(!(StringUtils.isBlank(updateUserRequest.getSalutation()))){
				userBo.setSalutation(updateUserRequest.getSalutation());
			}
			UserBo user = userAdminDao.update(userBo);
			updateUserReponse.setSessionToken(authentication.getToken());
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
		if(logger.isDebugEnabled()) {
			logger.debug("Checking if user is valid : " + isValidUserRequest.getUserName() );
		}
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
			UserBo userBo = userAdminDao.loadByUserId(getUserEmailRequest.getUserId());
			
			if (userBo.getUserEmail() != null){
				List<UserEmail> userEmails = new ArrayList<UserEmail>();
				for(UserEmailBo userEmailBo : userBo.getUserEmail()){
					UserEmail userEmail = new UserEmail();
					userEmail.setEmail(userEmailBo.getEmail());
					userEmail.setType(userEmailBo.getType());
					userEmails.add(userEmail);
				}
				getUserEmailResponse.setUserEmail(userEmails);
				getUserEmailResponse.setGetUserEmailStatus(GetUserEmailStatusEnum.SUCCESS);
			}else{
				getUserEmailResponse.setGetUserEmailStatus(GetUserEmailStatusEnum.NO_EMAIL_ID);
			}			
			return getUserEmailResponse;
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
		if(logger.isDebugEnabled()) {
			logger.debug("Adding emails for user : " + addUserEmailRequest.getUserId() );
		}
		
		if (StringUtils.isBlank(addUserEmailRequest.getSessionToken())) {
			addUserEmailResponse.setAddUserEmailStatus(AddUserEmailStatusEnum.NO_SESSION);
			return addUserEmailResponse;
		}
		if(!ValidatorUtil.isValidEmailAddress(addUserEmailRequest.getUserEmail().getEmail())){
			addUserEmailResponse.setAddUserEmailStatus(AddUserEmailStatusEnum.INVALID_EMAIL);
			return addUserEmailResponse;
		}
		try {
			AuthenticationTO authentication = authenticationService.authenticate(addUserEmailRequest.getSessionToken());
			if (authentication == null) {
				addUserEmailResponse.setAddUserEmailStatus(AddUserEmailStatusEnum.NO_SESSION);
				return addUserEmailResponse;
			}
			UserBo userBo = userAdminDao.loadByUserId(addUserEmailRequest.getUserId());
			UserEmailBo userEmailBo = new UserEmailBo();
			userEmailBo.setEmail(addUserEmailRequest.getUserEmail().getEmail());
			userEmailBo.setType(addUserEmailRequest.getUserEmail().getType());
			if (userBo.getUserEmail() != null){
				for(UserEmailBo userEmail : userBo.getUserEmail()){
					if(userEmail.equals(userEmailBo)){
						addUserEmailResponse.setSessionToken(authentication.getToken());
						addUserEmailResponse.setAddUserEmailStatus(AddUserEmailStatusEnum.ALREADY_PRESENT);
						return addUserEmailResponse;
					}
				}
			}
			
			boolean success = userAdminDao.addUserEmail(addUserEmailRequest.getUserId(), userEmailBo);
			if(success){
				addUserEmailResponse.setSessionToken(authentication.getToken());
				addUserEmailResponse.setAddUserEmailStatus(AddUserEmailStatusEnum.SUCCESS);
			}else{
				logger.error("Error while adding the user emails: " + addUserEmailRequest.getUserId());
				addUserEmailResponse.setSessionToken(authentication.getToken());
				addUserEmailResponse.setAddUserEmailStatus(AddUserEmailStatusEnum.ERROR_ADDING_EMAIL);
			}
			return addUserEmailResponse;
		} catch (PlatformException pe) {
			logger.error("Error while adding the user emails: " + addUserEmailRequest.getUserId(), pe);
			addUserEmailResponse.setAddUserEmailStatus(AddUserEmailStatusEnum.ERROR_ADDING_EMAIL);
		}
		return addUserEmailResponse;
	}

	@Override
	public VerifyUserEmailResponse verifyUserEmail(
			VerifyUserEmailRequest verifyUserEmailRequest) {
		// TODO Auto-generated method stub
		return null;
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
			
			boolean success = userAdminDao.deleteUserEmail(deleteUserEmailRequest.getUserId(), deleteUserEmailRequest.getEmailId());
			
			if(success){
				deleteUserEmailResponse.setSessionToken(authentication.getToken());
				deleteUserEmailResponse.setDeleteUserEmailStatus(DeleteUserEmailStatusEnum.SUCCESS);
			}else{
				deleteUserEmailResponse.setSessionToken(authentication.getToken());
				deleteUserEmailResponse.setDeleteUserEmailStatus(DeleteUserEmailStatusEnum.NO_EMAIL_ID);
			}
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
			UserBo userBo = userAdminDao.loadByUserId(getUserPhoneRequest.getUserId());
			
			if (userBo.getUserPhone() != null){
				List<UserPhone> userPhones = new ArrayList<UserPhone>();
				for(UserPhoneBo userPhoneBo : userBo.getUserPhone()){
					UserPhone userPhone = new UserPhone();
					userPhone.setPhone(userPhoneBo.getPhoneno());
					userPhone.setType(userPhoneBo.getType());
					userPhones.add(userPhone);
				}
				getUserPhoneResponse.setUserPhone(userPhones);
				getUserPhoneResponse.setGetUserPhoneStatus(GetUserPhoneStatusEnum.SUCCESS);
			}else{
				getUserPhoneResponse.setGetUserPhoneStatus(GetUserPhoneStatusEnum.NO_PHONE);
			}			
			return getUserPhoneResponse;
		} catch (PlatformException pe) {
			logger.error("Error while getting the user Phones: " + getUserPhoneRequest.getUserId(), pe);
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
		if(!ValidatorUtil.isValidPhoneNumber(addUserPhoneRequest.getUserPhone().getPhone())){
			addUserPhoneResponse.setAddUserPhoneStatus(AddUserPhoneStatusEnum.INVALID_PHONE);
			return addUserPhoneResponse;
		}
		try {
			AuthenticationTO authentication = authenticationService.authenticate(addUserPhoneRequest.getSessionToken());
			if (authentication == null) {
				addUserPhoneResponse.setAddUserPhoneStatus(AddUserPhoneStatusEnum.NO_SESSION);
				return addUserPhoneResponse;
			}
			UserBo userBo = userAdminDao.loadByUserId(addUserPhoneRequest.getUserId());
			UserPhoneBo userPhoneBo = new UserPhoneBo();
			userPhoneBo.setPhoneno(addUserPhoneRequest.getUserPhone().getPhone());
			userPhoneBo.setType(addUserPhoneRequest.getUserPhone().getType());
			if (userBo.getUserPhone() != null){
				for(UserPhoneBo userPhone : userBo.getUserPhone()){
					if(userPhone.equals(userPhoneBo)){
						addUserPhoneResponse.setSessionToken(authentication.getToken());
						addUserPhoneResponse.setAddUserPhoneStatus(AddUserPhoneStatusEnum.ALREADY_PRESENT);
						return addUserPhoneResponse;
					}
				}
			}			
			boolean success = userAdminDao.addUserPhone(addUserPhoneRequest.getUserId(), userPhoneBo);
			if(success){
				addUserPhoneResponse.setSessionToken(authentication.getToken());
				addUserPhoneResponse.setAddUserPhoneStatus(AddUserPhoneStatusEnum.SUCCESS);
			}else{
				logger.error("Error while adding the user Phones: " + addUserPhoneRequest.getUserId());
				addUserPhoneResponse.setSessionToken(authentication.getToken());
				addUserPhoneResponse.setAddUserPhoneStatus(AddUserPhoneStatusEnum.ERROR_ADDING_PHONE);
			}
			return addUserPhoneResponse;
		} catch (PlatformException pe) {
			logger.error("Error while adding the user Phones: " + addUserPhoneRequest.getUserId(), pe);
			addUserPhoneResponse.setAddUserPhoneStatus(AddUserPhoneStatusEnum.ERROR_ADDING_PHONE);
		}
		return addUserPhoneResponse;
	}

	@Override
	public VerifyUserPhoneResponse verifyUserPhone(
			VerifyUserPhoneRequest verifyUserPhoneRequest) {
		// TODO Auto-generated method stub
		return null;
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
			
			boolean success = userAdminDao.deleteUserPhone(deleteUserPhoneRequest.getUserId(), deleteUserPhoneRequest.getPhone());
			
			if(success){
				deleteUserPhoneResponse.setSessionToken(authentication.getToken());
				deleteUserPhoneResponse.setDeleteUserPhoneStatus(DeleteUserPhoneStatusEnum.SUCCESS);
			}else{
				deleteUserPhoneResponse.setSessionToken(authentication.getToken());
				deleteUserPhoneResponse.setDeleteUserPhoneStatus(DeleteUserPhoneStatusEnum.NO_PHONE);
			}
			return deleteUserPhoneResponse;
		}catch (PlatformException pe) {
			logger.error("Error while delete the user Phone: " + deleteUserPhoneRequest.getUserId(), pe);
			deleteUserPhoneResponse.setDeleteUserPhoneStatus(DeleteUserPhoneStatusEnum.ERROR_DELETING_PHONE);
		}
		return deleteUserPhoneResponse;
	}
	
}
