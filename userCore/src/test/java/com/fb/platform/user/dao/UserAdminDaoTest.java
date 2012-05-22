package com.fb.platform.user.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.user.dao.interfaces.UserAdminDao;
import com.fb.platform.user.domain.UserBo;
import com.fb.platform.user.domain.UserEmailBo;
import com.fb.platform.user.domain.UserPhoneBo;
import com.fb.platform.user.util.PasswordUtil;


public class UserAdminDaoTest extends BaseTestCase {

	@Autowired
	private UserAdminDao userAdminDao;

	private static final String key  = "jasvipul@gmail.com";

	@Test
	public void testload() {
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
		assertEquals("sha1$da42a$46b44c51df06fcfdf3eda17286ed98b34bfb4917", user.getPassword());
		assertEquals(true, PasswordUtil.checkPassword("testpass", user.getPassword()));
	}
	@Test
	public void testAddUserWithPhone() {
		UserBo user = new UserBo();
		List<UserPhoneBo> phoneLst = new ArrayList<UserPhoneBo>();
		UserPhoneBo userPhone = new UserPhoneBo();
		userPhone.setPhoneno("9876543210");
		userPhone.setType("primary");
		phoneLst.add(userPhone);
		user.setUsername("9876543210");
		user.setPassword("testpass");
		user.setUserPhone(phoneLst);
		UserBo usettest  = userAdminDao.add(user);
		assertNotNull(usettest);
		assertNotNull(usettest.getPassword());
		assertEquals(true, PasswordUtil.checkPassword("testpass", usettest.getPassword()));
	}
	
	@Test
	public void testAddUser() {
		UserBo user = new UserBo();
		List<UserEmailBo> emailLst = new ArrayList<UserEmailBo>();
		UserEmailBo userEmail = new UserEmailBo();
		userEmail.setEmail("testingemail@test.com");
		userEmail.setType("primary");
		emailLst.add(userEmail);
		user.setFirstname("testfirst");
		user.setLastname("tetslast");
		user.setPassword("testpass");
		user.setUsername("testingemail@test.com");
		user.setUserEmail(emailLst);
		userAdminDao.add(user);
		UserBo usettest = userAdminDao.load("testingemail@test.com");

		assertNotNull(usettest);
		assertNotNull(usettest.getPassword());
		assertEquals(true, PasswordUtil.checkPassword("testpass", usettest.getPassword()));
	}
	
	@Test
	public void testUpdateUser() {
		UserBo usettest = userAdminDao.load(key);
		assertNotNull(usettest);
		assertNotNull(usettest.getPassword());
		usettest.setFirstname("testfirstphoneupda");
		usettest.setLastname("tetslastphoneupdate");
		userAdminDao.update(usettest);
		UserBo usettest1 = userAdminDao.load(key);
		assertNotNull(usettest1);
		assertEquals("testfirstphoneupda tetslastphoneupdate", usettest1.getName());
	}
	
	@Test
	public void testAddUserEmail(){
		UserBo usettest = userAdminDao.load(key);
		assertNotNull(usettest);
		int userid = usettest.getUserid();
		UserEmailBo userEmail = new UserEmailBo();
		userEmail.setEmail("tetsaddingemail@tets.com");
		userEmail.setType("primary");
		assertEquals(true, userAdminDao.addUserEmail(userid, userEmail));
		UserBo usettest1 = userAdminDao.load(key);
		assertNotNull(usettest1);
		List<UserEmailBo> usEmailBos = usettest1.getUserEmail();
		assertNotNull(usEmailBos);
		boolean found = false;
		for(UserEmailBo userEmailBo : usEmailBos){
			if(userEmailBo.getEmail().equals("tetsaddingemail@tets.com")){
				found = true;
			}
		}
		assertTrue(found);
	}
	@Test
	public void testAddUserPhone(){
		UserBo usettest = userAdminDao.load(key);
		assertNotNull(usettest);
		int userid = usettest.getUserid();
		UserPhoneBo userPhone = new UserPhoneBo();
		userPhone.setPhoneno("09876654546");
		userPhone.setType("secondary");
		assertEquals(true, userAdminDao.addUserPhone(userid, userPhone));
		UserBo usettest1 = userAdminDao.load(key);
		assertNotNull(usettest1);
		List<UserPhoneBo> usPhoneBos = usettest1.getUserPhone();
		assertNotNull(usPhoneBos);
		for(UserPhoneBo userPhoneBo : usPhoneBos){
			assertNotNull(userPhoneBo);
			if(userPhoneBo.getType().equals("secondary")){
				assertEquals("09876654546", userPhoneBo.getPhoneno());
			}
		}
		
	}
	@Test
	public void testDeleteUserEmail(){
		UserBo usettest = userAdminDao.load(key);
		
		UserEmailBo userEmail = new UserEmailBo();
		userEmail.setEmail("tetsaddingemail@tets.com");
		userEmail.setType("primary");
		assertEquals(true, userAdminDao.addUserEmail(usettest.getUserid(), userEmail));
		
		assertNotNull(usettest);
		int userid = usettest.getUserid();
		assertTrue(userAdminDao.deleteUserEmail(userid, "tetsaddingemail@tets.com"));
		assertFalse(userAdminDao.deleteUserEmail(userid, "tetsaddingemail@tets.com"));
		UserBo usettest1 = userAdminDao.load(key);
		assertNotNull(usettest1);
		List<UserEmailBo> usEmailBos = usettest1.getUserEmail();
		boolean found = false;
		for(UserEmailBo userEmailBo :usEmailBos){
			if(userEmailBo.getEmail().equals("tetsaddingemail@tets.com")){
				found = true;
			}
		}
		assertFalse(found);
	}
	@Test
	public void testDeleteUserPhone(){
		UserBo usettest = userAdminDao.load(key);
		assertNotNull(usettest);
		int userid = usettest.getUserid();
		
		UserPhoneBo userPhone = new UserPhoneBo();
		userPhone.setPhoneno("09876654546");
		userPhone.setType("secondary");
		assertEquals(true, userAdminDao.addUserPhone(userid, userPhone));
		
		assertTrue( userAdminDao.deleteUserPhone(userid, "09876654546"));
		assertFalse( userAdminDao.deleteUserEmail(userid, "09876654546"));
		UserBo usettest1 = userAdminDao.load(key);
		assertNotNull(usettest1);
		boolean found = false;
		List<UserPhoneBo> usPhoneBos = usettest1.getUserPhone();
		for(UserPhoneBo userPhoneBo : usPhoneBos){
			if(userPhoneBo.getPhoneno().equals("09876654546")){
				found = true;
			}
		}
		assertFalse(found);
	}

}
