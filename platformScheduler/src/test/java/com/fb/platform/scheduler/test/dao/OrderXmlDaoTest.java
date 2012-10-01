package com.fb.platform.scheduler.test.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.scheduler.dao.OrderXmlDao;
import com.fb.platform.scheduler.to.OrderXmlTO;

public class OrderXmlDaoTest extends BaseTestCase {
	
	@Autowired
	private OrderXmlDao orderXmlDao;
	
	@Test
	public void testGetOrderXmls() {
		assertNotNull(orderXmlDao.getOrderXmlList().get(0));
		assertEquals("bb_new", orderXmlDao.getOrderXmlList().get(0).getType());
	}
	
	@Test
	public void updateOrderXml() {
		List<OrderXmlTO> list = orderXmlDao.getOrderXmlList();
		for (OrderXmlTO orderXmlTO : list) {
			assertEquals(1,orderXmlDao.updateOrderXml(1, "", "", orderXmlTO.getId()));			
		}
		assertEquals(new ArrayList<OrderXmlTO>(), orderXmlDao.getOrderXmlList());
	}

}
