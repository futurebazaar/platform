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

/**
 * @author vinayak
 *
 */
public class UserManagerTest extends BaseTestCase {

	/*@Autowired
	private UserManager userManager = null;*/

	@Test
	public void testLoginWithEmail() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		/*LoginResponse response = userManager.login(request);

		assertNotNull(response);
		assertEquals(LoginStatusEnum.LOGIN_SUCCESS, response.getLoginStatus());
		assertNotNull(response.getSessionToken());
		assertEquals(1, response.getUserId().intValue());*/
	}
}
