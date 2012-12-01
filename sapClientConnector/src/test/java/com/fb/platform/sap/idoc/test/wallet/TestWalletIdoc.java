package com.fb.platform.sap.idoc.test.wallet;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.sap.client.commons.SapResponseStatus;
import com.fb.platform.sap.client.handler.PlatformClientHandler;

public class TestWalletIdoc extends BaseTestCase {
	
	private String getWalletIdocXml() {
		String idoc = "<ZIDOC_WALLET><IDOC BEGIN=\"1\"><EDI_DC40 SEGMENT=\"1\"><TABNAM>EDI_DC40</TABNAM><MANDT>400</MANDT><DOCNUM>0000040000686440</DOCNUM><DOCREL>640</DOCREL><STATUS>62</STATUS><DIRECT>2</DIRECT><OUTMOD></OUTMOD><IDOCTYP>ZIDOC_WALLET</IDOCTYP><MESTYP>ZWALLET</MESTYP><SNDPOR>ZBB</SNDPOR><SNDPRT>LS</SNDPRT><SNDPRN>ZATGJCAPS1</SNDPRN><RCVPOR>SAPDV1</RCVPOR><RCVPRT>LS</RCVPRT><RCVPRN>DV1CLNT400</RCVPRN><CREDAT>20121001</CREDAT><CRETIM>180640</CRETIM></EDI_DC40><ZE1WALLET SEGMENT=\"1\"><WALLET_NO>1234561</WALLET_NO><NAME>TEST</NAME><PAYMODE>ICI3</PAYMODE><DATE>20121001</DATE><ORDER>6555555555</ORDER><LOGIN_REF>ABCD</LOGIN_REF><AMOUNT>400</AMOUNT><IDENTITY>WTOP</IDENTITY><REF1>TEST WLLET</REF1><REF2>REQUEST</REF2><REF3></REF3></ZE1WALLET><ZE1WALLET SEGMENT=\"1\"><WALLET_NO>1234562</WALLET_NO><NAME>TEST</NAME><PAYMODE>CITI</PAYMODE><DATE>20121001</DATE><ORDER>6555555555</ORDER><LOGIN_REF>test@test.com</LOGIN_REF><AMOUNT>700</AMOUNT><IDENTITY>WTOP</IDENTITY><REF1>TEST WLLET</REF1><REF2></REF2><REF3>HELLO</REF3></ZE1WALLET><ZE1WALLET SEGMENT=\"1\"><WALLET_NO>1234563</WALLET_NO><NAME>TEST</NAME><PAYMODE>AMEX</PAYMODE><DATE>20121001</DATE><ORDER>6555555555</ORDER><LOGIN_REF>1234567890</LOGIN_REF><AMOUNT>400</AMOUNT><IDENTITY>WOPN</IDENTITY><REF1>TEST WLLET</REF1><REF2></REF2><REF3></REF3></ZE1WALLET></IDOC></ZIDOC_WALLET>";
		return idoc;
	}
	
	@Test
	public void testWallet() {
		String idocXml = getWalletIdocXml();
		ApplicationContext context = new ClassPathXmlApplicationContext("test-applicationContext-service.xml");
		PlatformClientHandler bh = (PlatformClientHandler) context.getBean("sapClientHandler");
		SapResponseStatus status = bh.sendIdoc(idocXml);
		assertEquals(SapResponseStatus.SUCCESS, status);
	}
}
