/**
*
 */
package com.fb.platform.user;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.user.manager.interfaces.UserManager;
import com.fb.platform.user.manager.model.auth.LoginResponse;
import com.fb.platform.user.manager.model.auth.LoginStatusEnum;

/**
 * @author vinayak
 *
 */
public class AuthUserTest extends BaseTestCase {

	@Autowired
	private UserManager userManager = null;

	@Test
	public void testLogin() {
		com.fb.platform.user.manager.model.auth.LoginRequest apiLoginReq = new com.fb.platform.user.manager.model.auth.LoginRequest();
		apiLoginReq.setIpAddress(null);
		apiLoginReq.setPassword("testpass");
		apiLoginReq.setUsername("test@test.com");

		LoginResponse loginResponse = userManager.login(apiLoginReq);
		assertNotNull(loginResponse);
		assertEquals(LoginStatusEnum.LOGIN_SUCCESS, loginResponse.getLoginStatus());
	}
}
