/**
*
 */
package com.fb.platform.user.manager;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
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

/**
 * @author vinayak
 * @author kislaya
 */
public class UserManagerTest extends BaseTestCase {

	@Autowired
	private UserManager userManager = null;

	@Test
	public void testLoginWithEmail() {
		LoginRequest request = new LoginRequest();
		request.setUsername("removingjas@test.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);

		assertNotNull(response);
		assertEquals(LoginStatusEnum.LOGIN_SUCCESS, response.getLoginStatus());
		assertNotNull(response.getSessionToken());
		assertEquals(1, response.getUserId().intValue());
	}

	@Test
	public void testLoginWithOnlyAtuthUsername() {
		LoginRequest request = new LoginRequest();
		request.setUsername("testonlyusername");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);

		assertNotNull(response);
		assertEquals(LoginStatusEnum.LOGIN_SUCCESS, response.getLoginStatus());
		assertNotNull(response.getSessionToken());
		assertEquals(-5, response.getUserId().intValue());
	}
	@Test
	public void testLoginInvalidPassword() {
		LoginRequest request = new LoginRequest();
		request.setUsername("removingjas@test.com");
		request.setPassword("invalid");

		LoginResponse response = userManager.login(request);

		assertNotNull(response);
		assertEquals(LoginStatusEnum.INVALID_USERNAME_PASSWORD, response.getLoginStatus());
		assertNull(response.getSessionToken());
		assertEquals(0,response.getUserId().intValue());
	}

	@Test
	public void testLoginInvalidUser() {
		LoginRequest request = new LoginRequest();
		request.setUsername("iAmNotThere@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);

		assertNotNull(response);
		assertEquals(LoginStatusEnum.INVALID_USERNAME_PASSWORD, response.getLoginStatus());
		assertNull(response.getSessionToken());
		assertEquals(0,response.getUserId().intValue());
	}

	@Test
	public void testLoginWithPhone() {
		LoginRequest request = new LoginRequest();
		request.setUsername("9326164025");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);

		assertNotNull(response);
		assertEquals(LoginStatusEnum.LOGIN_SUCCESS, response.getLoginStatus());
		assertNotNull(response.getSessionToken());
		assertEquals(1, response.getUserId().intValue());
	}

	@Test
	public void testLoginWithSecondPhone() {
		LoginRequest request = new LoginRequest();
		request.setUsername("9870587074");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);

		assertNotNull(response);
		assertEquals(LoginStatusEnum.LOGIN_SUCCESS, response.getLoginStatus());
		assertNotNull(response.getSessionToken());
		assertEquals(2, response.getUserId().intValue());
	}

	@Test
	public void testLogout() {
		LoginRequest request = new LoginRequest();
		request.setUsername("9326164025");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);

		assertNotNull(response);
		assertEquals(LoginStatusEnum.LOGIN_SUCCESS, response.getLoginStatus());
		assertNotNull(response.getSessionToken());

		LogoutRequest logoutRequest = new LogoutRequest();
		logoutRequest.setSessionToken(response.getSessionToken());

		LogoutResponse logoutResponse = userManager.logout(logoutRequest);

		assertNotNull(logoutResponse);
		assertEquals(LogoutStatusEnum.LOGOUT_SUCCESS, logoutResponse.getLogoutStatus());
	}

	@Test
	public void testLogoutInvalidSessionToken() {
		LogoutRequest logoutRequest = new LogoutRequest();
		logoutRequest.setSessionToken("InvalidToken");

		LogoutResponse logoutResponse = userManager.logout(logoutRequest);

		assertNotNull(logoutResponse);
		assertEquals(LogoutStatusEnum.NO_SESSION, logoutResponse.getLogoutStatus());
	}

	@Test
	public void testDoubleLogout() {
		LoginRequest request = new LoginRequest();
		request.setUsername("9326164025");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);

		assertNotNull(response);
		assertEquals(LoginStatusEnum.LOGIN_SUCCESS, response.getLoginStatus());
		assertNotNull(response.getSessionToken());

		LogoutRequest logoutRequest = new LogoutRequest();
		logoutRequest.setSessionToken(response.getSessionToken());

		LogoutResponse logoutResponse = userManager.logout(logoutRequest);

		assertNotNull(logoutResponse);
		assertEquals(LogoutStatusEnum.LOGOUT_SUCCESS, logoutResponse.getLogoutStatus());

		//now try logout with logged out session, should not work.
		logoutRequest = new LogoutRequest();
		logoutRequest.setSessionToken(response.getSessionToken());

		logoutResponse = userManager.logout(logoutRequest);

		assertNotNull(logoutResponse);
		assertEquals(LogoutStatusEnum.NO_SESSION, logoutResponse.getLogoutStatus());
	}

	@Test
	public void testChangePassword() {
		LoginRequest request = new LoginRequest();
		request.setUsername("9326164025");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);

		assertNotNull(response);
		assertEquals(LoginStatusEnum.LOGIN_SUCCESS, response.getLoginStatus());
		assertNotNull(response.getSessionToken());

		ChangePasswordRequest cpRequest = new ChangePasswordRequest();
		cpRequest.setNewPassword("newPassword");
		cpRequest.setOldPassword("testpass");
		cpRequest.setSessionToken(response.getSessionToken());

		ChangePasswordResponse cpResponse = userManager.changePassword(cpRequest);

		assertNotNull(cpResponse);
		assertNotNull(cpResponse.getSessionToken());
		assertEquals(ChangePasswordStatusEnum.SUCCESS, cpResponse.getStatus());

		//now try login with the new password
		request = new LoginRequest();
		request.setUsername("9326164025");
		request.setPassword("newPassword");

		response = userManager.login(request);

		assertNotNull(response);
		assertEquals(LoginStatusEnum.LOGIN_SUCCESS, response.getLoginStatus());
		assertNotNull(response.getSessionToken());

		//try login with old password
		request = new LoginRequest();
		request.setUsername("9326164025");
		request.setPassword("testpass");

		LoginResponse invalidResponse = userManager.login(request);

		assertNotNull(invalidResponse);
		assertEquals(LoginStatusEnum.INVALID_USERNAME_PASSWORD, invalidResponse.getLoginStatus());
		assertNull(invalidResponse.getSessionToken());

		//reset password to original value so that other junit tests run through eclipse can work
		cpRequest = new ChangePasswordRequest();
		cpRequest.setNewPassword("testpass");
		cpRequest.setOldPassword("newPassword");
		cpRequest.setSessionToken(response.getSessionToken());

		cpResponse = userManager.changePassword(cpRequest);

		assertNotNull(cpResponse);
		//assertNotNull(cpResponse.getSessionToken()); TODO
		assertEquals(ChangePasswordStatusEnum.SUCCESS, cpResponse.getStatus());
	}

	@Test
	public void testChangePasswordNoSession() {
		ChangePasswordRequest cpRequest = new ChangePasswordRequest();
		cpRequest.setNewPassword("newPassword");
		cpRequest.setOldPassword("testpass");

		ChangePasswordResponse cpResponse = userManager.changePassword(cpRequest);

		assertNotNull(cpResponse);
		assertEquals(ChangePasswordStatusEnum.NO_SESSION, cpResponse.getStatus());
	}

	@Test
	public void testInvalidOldPassword() {
		LoginRequest request = new LoginRequest();
		request.setUsername("9326164025");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);

		assertNotNull(response);
		assertEquals(LoginStatusEnum.LOGIN_SUCCESS, response.getLoginStatus());
		assertNotNull(response.getSessionToken());

		ChangePasswordRequest cpRequest = new ChangePasswordRequest();
		cpRequest.setNewPassword("newPassword");
		cpRequest.setOldPassword("invalidOldPassword");
		cpRequest.setSessionToken(response.getSessionToken());

		ChangePasswordResponse cpResponse = userManager.changePassword(cpRequest);

		assertNotNull(cpResponse);
		assertEquals(ChangePasswordStatusEnum.CHANGE_PASSWORD_FAILED, cpResponse.getStatus());
	}

	@Test
	public void loginWithoutPassword() {
		LoginRequest request = new LoginRequest();
		request.setUsername("removingjas@test.com");

		LoginResponse response = userManager.login(request);

		assertNotNull(response);
		assertEquals(LoginStatusEnum.GUEST_LOGIN_SUCCESS, response.getLoginStatus());
		assertNotNull(response.getSessionToken());
		assertEquals(1, response.getUserId().intValue());
	}

	@Test
	public void guestLogout() {
		LoginRequest request = new LoginRequest();
		request.setUsername("9326164025");
		LoginResponse response = userManager.login(request);

		assertNotNull(response);
		assertEquals(LoginStatusEnum.GUEST_LOGIN_SUCCESS, response.getLoginStatus());

		assertNotNull(response.getSessionToken());
		assertEquals(1, response.getUserId().intValue());

		LogoutRequest logoutRequest = new LogoutRequest();
		logoutRequest.setSessionToken(response.getSessionToken());

		LogoutResponse logoutResponse = userManager.logout(logoutRequest);

		assertNotNull(logoutResponse);
		assertEquals(LogoutStatusEnum.LOGOUT_SUCCESS, logoutResponse.getLogoutStatus());
	}

	@Test 
	public void testKeepAlive(){
		LoginRequest request = new LoginRequest();
		request.setUsername("9326164025");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);

		assertNotNull(response);
		assertEquals(LoginStatusEnum.LOGIN_SUCCESS, response.getLoginStatus());
		assertNotNull(response.getSessionToken());

		KeepAliveRequest keepAliveRequest = new KeepAliveRequest();
		keepAliveRequest.setSessionToken(response.getSessionToken());
		
		KeepAliveResponse keepAliveResponse = userManager.keepAlive(keepAliveRequest);
		assertNotNull(keepAliveResponse);
		assertEquals(KeepAliveStatusEnum.KEEPALIVE_SUCCESS, keepAliveResponse.getKeepAliveStatus());
		assertNotNull(keepAliveResponse.getSessionToken());		
	}
}
