package com.fb.platform.user.dao;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.user.dao.interfaces.UserAddressDao;

public class UserAddressDaoTest extends BaseTestCase {
	
	@Autowired
	private UserAddressDao userAddressDao;
	
	@Test
	public void testGetAddress(){
		
		assertTrue(true);
	}

}
