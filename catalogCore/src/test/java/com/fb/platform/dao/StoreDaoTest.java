/**
 * 
 */
package com.fb.platform.dao;

import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.catagory.Store;
import com.fb.platform.dao.catagory.StoreDao;

/**
 * @author vinayak
 *
 */
public class StoreDaoTest extends BaseTestCase {

	@Autowired
	private StoreDao storeDao;

	@Test
	public void testGetStore() {
		Store store = storeDao.get(-1);

		assertNotNull(store);
		assertEquals("Test Store 1", store.getName());
		assertEquals("test store 1", store.getSlug());
	}
}
