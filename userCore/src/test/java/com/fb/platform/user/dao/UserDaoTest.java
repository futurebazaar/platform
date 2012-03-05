package com.fb.platform.user.dao;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.user.dao.interfaces.UserDao;

public class UserDaoTest extends BaseTestCase {
	
	@Autowired
	private UserDao userDao;
	
	
	@Test
	public void testChangePass(){
		assertTrue(true);
	}

}
