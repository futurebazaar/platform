package com.fb.platform.scheduler.test.mbean;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.scheduler.mbean.PlatformSchedulerMbean;
import com.fb.platform.scheduler.mbean.impl.OrderSchedulerMbeanImpl;

public class TestMbean extends BaseTestCase {
	
	private PlatformSchedulerMbean mbean = new OrderSchedulerMbeanImpl();
	
	@Test
	public void testOperation() throws InterruptedException {
		assertTrue(mbean.start());
		assertFalse(mbean.start());
		assertTrue(mbean.stop());
		assertFalse(mbean.stop());
	}
	
}
