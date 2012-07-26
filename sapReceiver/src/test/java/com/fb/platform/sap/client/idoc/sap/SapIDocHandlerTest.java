/**
 * 
 */
package com.fb.platform.sap.client.idoc.sap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * @author nehaga
 *
 */
@ContextConfiguration(
		locations={"classpath:/test-applicationContext-resources.xml",
				"classpath:/platformMom-applicationContext-resources.xml",
				"classpath:/platformMom-applicationContext-service.xml",
				"classpath:/*platformMom-applicationContext-resources.xml",
				"classpath:/*platformMom-applicationContext-service.xml",
				"classpath:/test-sapReceiver-applicationContext.xml",
				"classpath:/sapReceiver-applicationContext-service.xml",
				"classpath:/*applicationContext.xml",
				"classpath:/*applicationContext-service.xml",
				"classpath*:/*applicationContext.xml",
				"classpath*:/*applicationContext-service.xml",
				"classpath*:/*platformMom-applicationContext-resources.xml",
				"classpath*:/*platformMom-applicationContext-service.xml",
				"classpath:**/test-applicationContext*.xml"})
public class SapIDocHandlerTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	private SapIDocHandler sapIdocHandler = null;

	@Test
	public void checkDuplicate() {
		String idocXml = "<?xml version=\"1.0\"?><ZTINLA_IDOCTYP><IDOC BEGIN=\"1\"><EDI_DC40 SEGMENT=\"1\"><TABNAM>EDI_DC40</TABNAM><MANDT>239</MANDT><DOCNUM>0000000206255797</DOCNUM><DOCREL>640</DOCREL><STATUS>30</STATUS><DIRECT>1</DIRECT><OUTMOD>2</OUTMOD><IDOCTYP>ZTINLA_IDOCTYP</IDOCTYP><MESTYP>ZTINLA_MSGTYP_INV</MESTYP><SNDPOR>SAPQA3</SNDPOR><SNDPRT>LS</SNDPRT><SNDPRN>PD1CLNT239</SNDPRN><RCVPOR>ZATGJCAPS</RCVPOR><RCVPRT>LS</RCVPRT><RCVPRN>ZATGJCAPS</RCVPRN><CREDAT>20120627</CREDAT><CRETIM>134257</CRETIM><SERIAL>20120627134257</SERIAL></EDI_DC40><ZTINLA_SEG_DLVR SEGMENT=\"1\"><T_CODE>VNA01</T_CODE><MATNR>000000000100315458</MATNR><R_WERKS>2786</R_WERKS><R_LGORT>10</R_LGORT><BWART>642</BWART><MEINS>EA</MEINS><TRNSFR_QUAN>50.000</TRNSFR_QUAN><MAT_DOC>6211892032</MAT_DOC></ZTINLA_SEG_DLVR></IDOC></ZTINLA_IDOCTYP>";
		boolean isDuplicate = sapIdocHandler.checkDuplicate(idocXml);
		assertFalse(isDuplicate);
		isDuplicate = sapIdocHandler.checkDuplicate(idocXml);
		assertTrue(isDuplicate);
	}
	
	public void setSapIdocHandler(SapIDocHandler sapIdocHandler) {
		this.sapIdocHandler = sapIdocHandler;
	}
	
}
