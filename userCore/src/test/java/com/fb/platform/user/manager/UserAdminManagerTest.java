package com.fb.platform.user.manager;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.user.manager.interfaces.UserAdminManager;
import com.fb.platform.user.manager.model.UserTO;

public class UserAdminManagerTest extends BaseTestCase {
	
	private static final String key  = "jasvipul@gmail.com";
	
	@Autowired
	private UserAdminManager userAdminManager;
	
	@Test
	public void testUserManager()
    {
	    UserTO record = new UserTO();
	    record = userAdminManager.getUser(key);
	    System.out.println("Test is running for user Manager ::::: " + record.getName());
	    System.out.println("Test is running for user Manager user email::::: " + record.getUserEmail().get(0).getEmail());
	    System.out.println("Test is running for user Manager user phone::::: " + record.getUserPhone().get(0).getPhoneno());
    }

}
