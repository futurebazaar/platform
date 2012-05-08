package com.fb.platform.wallet.manager.impl;

import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.platform.auth.AuthenticationService;
import com.fb.platform.auth.AuthenticationTO;
import com.fb.platform.wallet.manager.interfaces.WalletManager;
import com.fb.platform.wallet.manager.model.access.WalletSummaryDetails;
import com.fb.platform.wallet.manager.model.access.WalletSummaryRequest;
import com.fb.platform.wallet.manager.model.access.WalletSummaryResponse;
import com.fb.platform.wallet.manager.model.access.WalletSummaryStatusEnum;

/**
 * @author rajesh
 *
 */
public class WalletManagerImpl implements WalletManager {

	@Autowired
	private AuthenticationService authenticationService;
	
	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	private static Log logger = LogFactory.getLog(WalletManagerImpl.class);
	@Override
	public WalletSummaryResponse getWalletSummary(WalletSummaryRequest walletSummaryRequest) {
		if(logger.isDebugEnabled()) {
			logger.debug("Trying to retrieve wallet summary for userid : " + walletSummaryRequest.getUserId() );
		}
	
		WalletSummaryResponse walletSummaryResponse = new WalletSummaryResponse();
		
		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(walletSummaryRequest.getSessionToken());
		if (authentication == null) {
			//invalid session token
			walletSummaryResponse.setWalletSummaryStatus(WalletSummaryStatusEnum.NO_SESSION);
			return walletSummaryResponse;
		}
		
		walletSummaryResponse.setSessionToken(walletSummaryRequest.getSessionToken());
		
		int userId = authentication.getUserID();
		
		walletSummaryResponse.setWalletSummaryStatus(WalletSummaryStatusEnum.SUCCESS);
		WalletSummaryDetails walletSummaryDetails = new WalletSummaryDetails();
		walletSummaryDetails.setCashAmount(new BigDecimal(Float.toString(100.0f)));
		walletSummaryDetails.setRefundAmount(new BigDecimal(Float.toString(100.0f)));
		walletSummaryDetails.setGiftAmount(new BigDecimal(Float.toString(100.0f)));
		walletSummaryDetails.setTotalAmount(new BigDecimal(Float.toString(100.0f)));
		walletSummaryResponse.setWalletSummaryDetails(walletSummaryDetails);
		return walletSummaryResponse;
	}
	
/*
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
		if(logger.isDebugEnabled()) {
			logger.debug("Trying to login with userid : " + loginRequest.getUsername() );
		}
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setUserId(0);
		if (loginRequest == null || StringUtils.isBlank(loginRequest.getUsername())) {
			loginResponse.setLoginStatus(LoginStatusEnum.INVALID_USERNAME_PASSWORD);
			return loginResponse;
		}

		try {
			UserBo user = userAdminDao.load(loginRequest.getUsername());
			if (user == null) {
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
	*/

}
