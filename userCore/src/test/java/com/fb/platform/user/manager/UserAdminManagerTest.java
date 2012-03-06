package com.fb.platform.user.manager;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.user.manager.interfaces.UserAdminManager;
import com.fb.platform.user.manager.model.admin.GetUserRequest;
import com.fb.platform.user.manager.model.admin.GetUserResponse;

public class UserAdminManagerTest extends BaseTestCase {
	
	private static final String key  = "jasvipul@gmail.com";
	
	@Autowired
	private UserAdminManager userAdminManager;
	
	@Test
	public void testUserManager()
    {
	    GetUserResponse record = new GetUserResponse();
	    GetUserRequest putreq = new GetUserRequest();
	    putreq.setKey(key);
	    record = userAdminManager.getUser(putreq);
	    System.out.println("Test is running for user Manager ::::: " + record.getUsername());
	    System.out.println("Test is running for user Manager getuserstatus::::: " + record.getStatus());
	    System.out.println("Test is running for user Manager getusersession::::: " + record.getSessionToken());
    }

}
