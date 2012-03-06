package com.fb.platform.user.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.user.dao.interfaces.UserAdminDao;
import com.fb.platform.user.domain.UserBo;
import com.fb.platform.user.util.PasswordUtil;

public class UserAdminDaoTest extends BaseTestCase {
	
	@Autowired
	private UserAdminDao userAdminDao;

	private static final String key  = "jasvipul@gmail.com";

	@Test
	public void testload(){
		UserBo record = new UserBo();
		record = userAdminDao.load(key);
		assertNotNull(record);
	    System.out.println("Test is running for user DAO ::::: " + record.getName());
	    System.out.println("Test is running for user DAO user email::::: " + record.getUserEmail().get(0).getEmail());
	    System.out.println("Test is running for user DAO user phone::::: " + record.getUserPhone().get(0).getPhoneno());
	}

	@Test
	public void testLoadWithEmail() {
		UserBo user = userAdminDao.load(key);

		assertNotNull(user);

		assertNotNull(user.getPassword());
		assertEquals("sha1$526aa$dSrE+t3dJd3LxYGT8FkRt1p2iiI=", user.getPassword());
		assertEquals(true, PasswordUtil.checkPassword("testpass", user.getPassword()));
	}
}
