package com.fb.platform.sap.bapi.test.lsp;

import org.junit.Test;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.sap.bapi.to.SapLspAwbUpdateRequestTO;

public class TestLspAwbUpdateTO {
	
	public SapLspAwbUpdateRequestTO getLspAwbUpdateRequestTO() {
		SapLspAwbUpdateRequestTO lspAwbUpdateRequestTO = new SapLspAwbUpdateRequestTO();
		lspAwbUpdateRequestTO.setAwb("1234");
		lspAwbUpdateRequestTO.setDeliveryNumber("8000018246");
		lspAwbUpdateRequestTO.setLspCode("1234");
		return lspAwbUpdateRequestTO;
	}
	
	@Test
	public void dummy() {
		
	}
}
