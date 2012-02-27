/**
 * 
 */
package com.fb.platform.sso.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.platform.dao.BaseTestCase;
import com.fb.platform.sso.SSOMasterService;
import com.fb.platform.sso.SSOSessionId;
import com.fb.platform.sso.SSOSessionTO;

/**
 * @author vinayak
 *
 */
public class SSOMasterServiceImplTest extends BaseTestCase {

	@Autowired
	private SSOMasterService ssoMasterService = null;

	@Test
	public void testCreateSsoSession() {
		SSOSessionTO ssoSession = new SSOSessionTO();
		ssoSession.setAppData("xxxxxappdata");
		ssoSession.setIpAddress("10.2.2.1");
		ssoSession.setUserId(1);

		SSOSessionId sessionId = ssoMasterService.createSSOSession(ssoSession);

		assertNotNull(sessionId);
	}

	@Test
	public void testAuthenticate() {
		SSOSessionTO ssoSession = new SSOSessionTO();
		ssoSession.setAppData("xxxxxappdata");
		ssoSession.setIpAddress("10.2.2.2");
		ssoSession.setUserId(2);

		SSOSessionId sessionId = ssoMasterService.createSSOSession(ssoSession);

		assertNotNull(sessionId);

		SSOSessionTO authSession = ssoMasterService.authenticate(sessionId);

		assertEquals(ssoSession.getAppData(), authSession.getAppData());
		assertEquals(ssoSession.getIpAddress(), authSession.getIpAddress());
		assertEquals(ssoSession.getUserId(), authSession.getUserId());
	}

	@Test
	public void testLogout() {
		SSOSessionTO ssoSession = new SSOSessionTO();
		ssoSession.setAppData("xxxxxappdata");
		ssoSession.setIpAddress("10.2.2.3");
		ssoSession.setUserId(3);

		SSOSessionId sessionId = ssoMasterService.createSSOSession(ssoSession);

		assertNotNull(sessionId);

		SSOSessionTO authSession = ssoMasterService.authenticate(sessionId);

		assertEquals(ssoSession.getAppData(), authSession.getAppData());
		assertEquals(ssoSession.getIpAddress(), authSession.getIpAddress());
		assertEquals(ssoSession.getUserId(), authSession.getUserId());

		ssoMasterService.removeSSOSession(sessionId);

		authSession = ssoMasterService.authenticate(sessionId);
		assertNull(authSession);
	}
}
