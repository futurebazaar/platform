package com.fb.platform.sap.bapi.test;

import com.fb.platform.sap.bapi.BapiConnector;
import com.fb.platform.sap.bapi.handler.impl.SapBapiHandler;
import com.fb.platform.sap.bapi.to.BapiTO;

public class TestBapiConnector {
	public static void main(String[] args) {
		BapiTO bapiTO = new TestBapiTO().getBapiTO();
		
		BapiConnector bapiConnector = new BapiConnector();
		SapBapiHandler bh = new SapBapiHandler();
		bh.setBapiConnector(bapiConnector);
		System.out.println(bh.execute("", bapiTO));;
	}
}
