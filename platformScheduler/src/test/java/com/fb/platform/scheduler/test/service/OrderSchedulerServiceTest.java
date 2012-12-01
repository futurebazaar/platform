package com.fb.platform.scheduler.test.service;

import static org.junit.Assert.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.scheduler.dao.OrderXmlDao;
import com.fb.platform.scheduler.service.OrderSchedulerService;
import com.fb.platform.scheduler.to.OrderXmlTO;

public class OrderSchedulerServiceTest extends BaseTestCase {

		@Autowired
		private OrderSchedulerService orderSchedulerService;
		
		@Autowired
		private OrderXmlDao orderXmlDao;

		private static Log logger = LogFactory.getLog(OrderSchedulerServiceTest.class);
		
		@Test
		public void testPostXmlsToBapi() {
			logger.info("Testing Xmls");
			orderSchedulerService.postXmlsTOBapi();
			for (OrderXmlTO orderXmlTO : orderXmlDao.getOrderXmlList()) {
				assertEquals(1, orderXmlTO.getAttempts());
			}
			//assertEquals(1, orderXmlDao.getOrderXmlList().size());
		}
		
}
