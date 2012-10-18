package com.fb.platform.sap.bapi.test.lsp;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.sap.bapi.to.SapInventoryLevelResponseTO;
import com.fb.platform.sap.bapi.to.SapLspAwbUpdateRequestTO;
import com.fb.platform.sap.bapi.to.SapLspAwbUpdateResponseTO;
import com.fb.platform.sap.client.handler.PlatformClientHandler;

public class TestLspAwbUpdateTO extends BaseTestCase {
	
	private SapLspAwbUpdateRequestTO getLspAwbUpdateRequestTO() {
		SapLspAwbUpdateRequestTO lspAwbUpdateRequestTO = new SapLspAwbUpdateRequestTO();
		lspAwbUpdateRequestTO.setAwb("1234");
		lspAwbUpdateRequestTO.setDeliveryNumber("8000018246");
		lspAwbUpdateRequestTO.setLspCode("1234");
		return lspAwbUpdateRequestTO;
	}
	
	@Test
	public void testLspAwbUpdate() {
		SapLspAwbUpdateRequestTO lspAwbUpdateRequestTO = getLspAwbUpdateRequestTO();
		ApplicationContext context = new ClassPathXmlApplicationContext("test-applicationContext-service.xml");
		PlatformClientHandler bh = (PlatformClientHandler) context.getBean("sapClientHandler");
		SapLspAwbUpdateResponseTO sapLspAwbUpdateResponseTO = bh.processLspAwbUpdate(lspAwbUpdateRequestTO);
		assertEquals("FBG TEST MESSAGE", sapLspAwbUpdateResponseTO.getMessage());
		
	}
}
