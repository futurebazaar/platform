package com.fb.platform.user.dao;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.user.dao.interfaces.UserAddressDao;
import com.fb.platform.user.domain.UserAddressBo;
/**
 * @author kislay
 *
 */

public class UserAddressDaoTest extends BaseTestCase {

	@Autowired
	private UserAddressDao userAddressDao;

	@Test
	public void testGetAddress() {

		Collection<UserAddressBo> userAddressBos = userAddressDao.load(1);
		assertNotNull(userAddressBos);
		Iterator<UserAddressBo> itr = userAddressBos.iterator();
		while (itr.hasNext()) {
			UserAddressBo userAddressBo = itr.next();
			assertEquals(userAddressBo.getPincode(), "400001");
		}
	}

	@Test
	public void testAddAddress() {
		UserAddressBo userAddressBo = new UserAddressBo();
		userAddressBo.setAddress("Testing Ad Address");
		userAddressBo.setAddresstype("Delivery");
		userAddressBo.setCity("Mumbai");
		userAddressBo.setCountry("India");
		userAddressBo.setPincode("400001");
		userAddressBo.setState("Maharastra");
		userAddressBo.setUserid(2);
		userAddressDao.add(userAddressBo);
		Collection<UserAddressBo> userAddressBos = userAddressDao.load(2);
		assertNotNull(userAddressBos);
		Iterator<UserAddressBo> itr = userAddressBos.iterator();
		while (itr.hasNext()) {
			UserAddressBo userAddressBoadd = itr.next();
			assertNotNull(userAddressBoadd);
		}

	}

}
