package com.fb.platform.ifs.manager;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.ifs.manager.model.SingleArticleServiceabilityRequestTO;
import com.fb.platform.ifs.manager.model.SingleArticleServiceabilityResponseTO;
import com.fb.platform.user.manager.interfaces.UserManager;
import com.fb.platform.user.manager.model.auth.LoginRequest;
import com.fb.platform.user.manager.model.auth.LoginResponse;


public class IFSManagerTest extends BaseTestCase {

	@Autowired
	private UserManager userManager = null;

	@Autowired
	private IFSManager ifsManager;
	
	@Test
	public void testIFSManager()
    {
		LoginRequest request = new LoginRequest();
		request.setUsername("removingjas@test.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		
	    SingleArticleServiceabilityRequestTO requestTO = new SingleArticleServiceabilityRequestTO();
	    requestTO.setArticleId("106613786");
	    requestTO.setClient("5");
	    requestTO.setIsCod(0);
	    requestTO.setItemPrice(0);
	    requestTO.setPincode("400060");
	    requestTO.setQty(1);
	    requestTO.setRateChartId("49776");
	    requestTO.setVendorId("87");
	    requestTO.setSessionToken(response.getSessionToken());
	    try {
	    	SingleArticleServiceabilityResponseTO resultTO = ifsManager.getSingleArticleServiceabilityInfo(requestTO);	
		    assertNotNull(resultTO);
	    } catch (Exception e) {
			e.printStackTrace();
		}
    }
}
