package com.fb.platform.user.manager.impl;

import org.apache.commons.lang.StringUtils;
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
import com.fb.platform.user.dao.interfaces.UserDao;
import com.fb.platform.user.domain.UserBo;
import com.fb.platform.user.manager.interfaces.UserManager;
import com.fb.platform.user.manager.model.auth.ChangePasswordRequest;
import com.fb.platform.user.manager.model.auth.ChangePasswordResponse;
import com.fb.platform.user.manager.model.auth.ChangePasswordStatusEnum;
import com.fb.platform.user.manager.model.auth.LoginRequest;
import com.fb.platform.user.manager.model.auth.LoginResponse;
import com.fb.platform.user.manager.model.auth.LoginStatusEnum;
import com.fb.platform.user.manager.model.auth.LogoutRequest;
import com.fb.platform.user.manager.model.auth.LogoutResponse;
import com.fb.platform.user.manager.model.auth.LogoutStatusEnum;
import com.fb.platform.user.util.PasswordUtil;

/**
 * @author kumar
 * @author vinayak
 *
 */
public class UserManagerImpl implements UserManager {
	
	private static Logger logger = Logger.getLogger(UserManagerImpl.class);

	private UserDao userDao = null;
	private SSOMasterService ssoMasterService = null;
	private AuthenticationService authenticationService;

	@Autowired
	private SessionTokenCacheAccess sessionTokenCacheAccess = null;

	@Override
	public LoginResponse login(LoginRequest loginRequest) {
		LoginResponse loginResponse = new LoginResponse();

		if (loginRequest == null || StringUtils.isBlank(loginRequest.getUsername()) || StringUtils.isBlank(loginRequest.getPassword())) {
			loginResponse.setLoginStatus(LoginStatusEnum.INVALID_USERNAME_PASSWORD);
			return loginResponse;
		}

		UserBo user = userDao.load(loginRequest.getUsername());
		if (user == null) {
			loginResponse.setLoginStatus(LoginStatusEnum.INVALID_USERNAME_PASSWORD);
			return loginResponse;
		}

		boolean passwordMatch = PasswordUtil.checkPassword(loginRequest.getPassword(), user.getPassword());
		if (!passwordMatch) {
			loginResponse.setLoginStatus(LoginStatusEnum.INVALID_USERNAME_PASSWORD);
			return loginResponse;
		}

		SSOSessionTO ssoSession = new SSOSessionTO();
		ssoSession.setUserId(user.getUserid());
		ssoSession.setIpAddress(loginRequest.getIpAddress());

		//create the global sso session
		SSOSessionId ssoSessionId = ssoMasterService.createSSOSession(ssoSession);

		//create the session token to be included in the response and cache it for the use of next request
		SSOToken ssoToken = ssoMasterService.createSessionToken(ssoSessionId);

		sessionTokenCacheAccess.put(ssoToken, ssoSessionId);

		loginResponse.setLoginStatus(LoginStatusEnum.LOGIN_SUCCESS);
		loginResponse.setSessionToken(ssoToken.getToken());
		loginResponse.setUserId(user.getUserid());
		return loginResponse;
	}

	@Override
	public LogoutResponse logout(LogoutRequest logoutRequest) {
		LogoutResponse response = new LogoutResponse();

		if (logoutRequest == null || StringUtils.isBlank(logoutRequest.getSessionToken())) {
			response.setStatus(LogoutStatusEnum.NO_SESSION);
			return response;
		}

		try {
			AuthenticationTO authentication = authenticationService.authenticate(logoutRequest.getSessionToken());
			if (authentication == null) {
				response.setStatus(LogoutStatusEnum.NO_SESSION);
				return response;
			}

			ssoMasterService.removeSSOSession(authentication.getSessionId());
			response.setStatus(LogoutStatusEnum.LOGOUT_SUCCESS);
		} catch (PlatformException e) {
			logger.error("Error while logging out user.", e);
			response.setStatus(LogoutStatusEnum.LOGOUT_FAILED);
		}
		return response;
	}

	@Override
	public ChangePasswordResponse changePassword(ChangePasswordRequest request) {
		ChangePasswordResponse response = new ChangePasswordResponse();

		if (request == null || StringUtils.isBlank(request.getOldPassword()) || StringUtils.isBlank(request.getNewPassword())) {
			response.setStatus(ChangePasswordStatusEnum.CHANGE_PASSWORD_FAILED);
			return response;
		}

		if (StringUtils.equals(request.getOldPassword(), request.getNewPassword())) {
			response.setStatus(ChangePasswordStatusEnum.DIFFERENT_PASSWORDS_REQUIRED);
			return response;
		}

		try {
			AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
			if (authentication == null) {
				response.setStatus(ChangePasswordStatusEnum.NO_SESSION);
				return response;
			}

			UserBo user = userDao.loadByUserId(authentication.getUserID());
			boolean success = userDao.changePassword(user, request.getNewPassword());

			if (!success) {
				response.setStatus(ChangePasswordStatusEnum.CHANGE_PASSWORD_FAILED);
				return response;
			}

			response.setStatus(ChangePasswordStatusEnum.SUCCESS);
		} catch (PlatformException e) {
			logger.error("Error while updating users new password.", e);
			response.setStatus(ChangePasswordStatusEnum.CHANGE_PASSWORD_FAILED);
		}
		return response;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public SSOMasterService getSsoMasterService() {
		return ssoMasterService;
	}

	public void setSsoMasterService(SSOMasterService ssoMasterService) {
		this.ssoMasterService = ssoMasterService;
	}

	public AuthenticationService getAuthenticationService() {
		return authenticationService;
	}

	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
}
