/**
 * 
 */
package com.fb.platform.promotion.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;

/**
 * @author vinayak
 *
 */
public class ProductDaoTest extends BaseTestCase {

	@Autowired
	private ProductDao productDao = null;

	@Test
	public void loadClearanceProducts() {
		Set<Integer> orderProductIds = new HashSet<Integer>();
		orderProductIds.add(100);
		orderProductIds.add(200);
		orderProductIds.add(-200); //not there in db

		Set<Integer> clearanceProductIds = productDao.findClearanceProductIds(orderProductIds);
		assertNotNull(clearanceProductIds);
		assertEquals(2, clearanceProductIds.size());
		assertTrue(clearanceProductIds.contains(100));
		assertTrue(clearanceProductIds.contains(200));
	}

	@Test
	public void noClearanceProducts() {
		Set<Integer> orderProductIds = new HashSet<Integer>();
		orderProductIds.add(-100);
		orderProductIds.add(-300);
		orderProductIds.add(-200);

		Set<Integer> clearanceProductIds = productDao.findClearanceProductIds(orderProductIds);
		assertNotNull(clearanceProductIds);
		assertEquals(0, clearanceProductIds.size());
	}
}
