/**
 * 
 */
package com.fb.platform.user.manager;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.user.manager.interfaces.UserManager;
import com.fb.platform.user.manager.model.auth.LoginRequest;
import com.fb.platform.user.manager.model.auth.LoginResponse;
import com.fb.platform.user.manager.model.auth.LoginStatusEnum;
import com.fb.platform.user.manager.model.auth.LogoutRequest;
import com.fb.platform.user.manager.model.auth.LogoutResponse;
import com.fb.platform.user.manager.model.auth.LogoutStatusEnum;

/**
 * @author vinayak
 *
 */
public class UserManagerTest extends BaseTestCase {

	@Autowired
	private UserManager userManager = null;

	@Test
	public void testLoginWithEmail() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);

		assertNotNull(response);
		assertEquals(LoginStatusEnum.LOGIN_SUCCESS, response.getLoginStatus());
		assertNotNull(response.getSessionToken());
		System.out.println("SessionToken is : " + response.getSessionToken());
		assertEquals(1, response.getUserId().intValue());
	}

	@Test
	public void testLoginInvalidPassword() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("invalid");

		LoginResponse response = userManager.login(request);

		assertNotNull(response);
		assertEquals(LoginStatusEnum.INVALID_USERNAME_PASSWORD, response.getLoginStatus());
		assertNull(response.getSessionToken());
		assertNull(response.getUserId());
		
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
		System.out.println("SessionToken is : " + response.getSessionToken());
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
		System.out.println("SessionToken is : " + response.getSessionToken());
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

		/*LogoutRequest logoutRequest = new LogoutRequest();
		logoutRequest.setSessionToken(response.getSessionToken());

		LogoutResponse logoutResponse = userManager.logout(logoutRequest);

		assertNotNull(logoutResponse);
		assertEquals(LogoutStatusEnum.LOGOUT_SUCCESS, logoutResponse.getStatus());*/
	}

}
