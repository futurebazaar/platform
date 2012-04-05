package com.fb.platform.user.manager.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.PlatformException;
import com.fb.commons.to.LoginType;
import com.fb.platform.auth.AuthenticationService;
import com.fb.platform.auth.AuthenticationTO;
import com.fb.platform.sso.SSOMasterService;
import com.fb.platform.sso.SSOSessionAppData;
import com.fb.platform.sso.SSOSessionId;
import com.fb.platform.sso.SSOSessionTO;
import com.fb.platform.sso.SSOToken;
import com.fb.platform.sso.caching.SessionTokenCacheAccess;
import com.fb.platform.user.dao.interfaces.UserAdminDao;
import com.fb.platform.user.domain.UserBo;
import com.fb.platform.user.manager.interfaces.UserManager;
import com.fb.platform.user.manager.model.auth.ChangePasswordRequest;
import com.fb.platform.user.manager.model.auth.ChangePasswordResponse;
import com.fb.platform.user.manager.model.auth.ChangePasswordStatusEnum;
import com.fb.platform.user.manager.model.auth.KeepAliveRequest;
import com.fb.platform.user.manager.model.auth.KeepAliveResponse;
import com.fb.platform.user.manager.model.auth.KeepAliveStatusEnum;
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

	private static final Logger logger = Logger.getLogger(UserManagerImpl.class);

	@Autowired
	private UserAdminDao userAdminDao = null;
	@Autowired
	private SSOMasterService ssoMasterService = null;
	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private SessionTokenCacheAccess sessionTokenCacheAccess = null;

	@Override
	public LoginResponse login(LoginRequest loginRequest) {
		LoginResponse loginResponse = new LoginResponse();

		if (loginRequest == null || StringUtils.isBlank(loginRequest.getUsername())) {
			loginResponse.setLoginStatus(LoginStatusEnum.INVALID_USERNAME_PASSWORD);
			return loginResponse;
		}

		try {
			UserBo user = userAdminDao.load(loginRequest.getUsername());
			if (user == null) {
				loginResponse.setUserId(0);
				loginResponse.setLoginStatus(LoginStatusEnum.INVALID_USERNAME_PASSWORD);
				return loginResponse;
			}

			LoginType loginType = null;

			if (StringUtils.isBlank(loginRequest.getPassword())) {
				//this is login without password, guest login
				loginType = LoginType.GUEST_LOGIN;
			} else {
				boolean passwordMatch = PasswordUtil.checkPassword(loginRequest.getPassword(), user.getPassword());
				if (!passwordMatch) {
					loginResponse.setUserId(0);
					loginResponse.setLoginStatus(LoginStatusEnum.INVALID_USERNAME_PASSWORD);
					return loginResponse;
				}
				loginType = LoginType.REGULAR_LOGIN;
			}

			SSOSessionAppData sessionAppData = new SSOSessionAppData();
			sessionAppData.setLoginType(loginType);

			SSOSessionTO ssoSession = new SSOSessionTO();
			ssoSession.setUserId(user.getUserid());
			ssoSession.setIpAddress(loginRequest.getIpAddress());
			ssoSession.setAppData(sessionAppData.getSSOAppDataString());

			//create the global sso session
			SSOSessionId ssoSessionId = ssoMasterService.createSSOSession(ssoSession);

			//create the session token to be included in the response and cache it for the use of next request
			SSOToken ssoToken = ssoMasterService.createSessionToken(ssoSessionId);

			sessionTokenCacheAccess.put(ssoToken, ssoSessionId);

			if (loginType == LoginType.GUEST_LOGIN) {
				loginResponse.setLoginStatus(LoginStatusEnum.GUEST_LOGIN_SUCCESS);
			} else {
				loginResponse.setLoginStatus(LoginStatusEnum.LOGIN_SUCCESS);
			}

			loginResponse.setSessionToken(ssoToken.getToken());
			loginResponse.setUserId(user.getUserid());
		} catch (PlatformException e) {
			logger.error("Error while login the user : " + loginRequest.getUsername(), e);
			loginResponse.setLoginStatus(LoginStatusEnum.LOGIN_FAILURE);
		}
		return loginResponse;
	}

	@Override
	public LogoutResponse logout(LogoutRequest logoutRequest) {
		LogoutResponse response = new LogoutResponse();

		if (logoutRequest == null || StringUtils.isBlank(logoutRequest.getSessionToken())) {
			response.setLogoutStatus(LogoutStatusEnum.NO_SESSION);
			return response;
		}

		try {
			AuthenticationTO authentication = authenticationService.authenticate(logoutRequest.getSessionToken());
			if (authentication == null) {
				response.setLogoutStatus(LogoutStatusEnum.NO_SESSION);
				return response;
			}

			ssoMasterService.removeSSOSession(authentication.getSessionId());
			response.setLogoutStatus(LogoutStatusEnum.LOGOUT_SUCCESS);
		} catch (PlatformException e) {
			logger.error("Error while logging out user.", e);
			response.setLogoutStatus(LogoutStatusEnum.LOGOUT_FAILED);
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

			//validate that the user has provided correct old password
			UserBo user = userAdminDao.loadByUserId(authentication.getUserID());
			boolean validOldPassword = PasswordUtil.checkPassword(request.getOldPassword(), user.getPassword());
			if (!validOldPassword) {
				response.setStatus(ChangePasswordStatusEnum.CHANGE_PASSWORD_FAILED);
				return response;
			}

			String hashedNewPassword = PasswordUtil.getEncryptedPassword(request.getNewPassword());
			boolean success = userAdminDao.changePassword(authentication.getUserID(), hashedNewPassword);

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

	public UserAdminDao getUserAdminDao() {
		return userAdminDao;
	}

	public void setUserAdminDao(UserAdminDao userAdminDao) {
		this.userAdminDao = userAdminDao;
	}

	@Override
	public KeepAliveResponse keepAlive(KeepAliveRequest keepAliveRequest) {
		KeepAliveResponse keepAliveResponse = new KeepAliveResponse();
		if (keepAliveRequest == null || StringUtils.isBlank(keepAliveRequest.getSessionToken())) {
			keepAliveResponse.setKeepAliveStatus(KeepAliveStatusEnum.NO_SESSION);
			return keepAliveResponse;
		}
		try {
			AuthenticationTO authentication = authenticationService.authenticate(keepAliveRequest.getSessionToken());
			if (authentication == null) {
				keepAliveResponse.setKeepAliveStatus(KeepAliveStatusEnum.NO_SESSION);
				return keepAliveResponse;
			}
			ssoMasterService.keepAlive(authentication.getSessionId());
			keepAliveResponse.setSessionToken(authentication.getToken());
			keepAliveResponse.setKeepAliveStatus(KeepAliveStatusEnum.KEEPALIVE_SUCCESS);
			
		} catch (PlatformException e) {
			logger.error("Error while keep alive session.", e);
			keepAliveResponse.setKeepAliveStatus(KeepAliveStatusEnum.KEEPALIVE_FAILED);
		}
		return keepAliveResponse;
	}
}
