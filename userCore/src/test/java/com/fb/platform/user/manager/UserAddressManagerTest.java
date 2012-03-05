package com.fb.platform.user.manager;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.user.manager.interfaces.UserAddressManager;

public class UserAddressManagerTest extends BaseTestCase {
	
	@Autowired
	private UserAddressManager userAddressManager;
	
	@Test
	public void testGetAddress(){
		assertTrue(true);
	}
	
	

}
