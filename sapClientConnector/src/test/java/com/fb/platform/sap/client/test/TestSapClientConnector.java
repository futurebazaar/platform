package com.fb.platform.sap.client.test;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.sap.bapi.test.inventory.TestInventoryDashboardTO;
import com.fb.platform.sap.bapi.test.inventory.TestInventoryLevelTO;
import com.fb.platform.sap.bapi.test.lsp.TestLspAwbUpdateTO;
import com.fb.platform.sap.bapi.test.order.TestBBBapiOrderTO;
import com.fb.platform.sap.bapi.test.order.TestBapiOrderTO;
import com.fb.platform.sap.bapi.to.SapInventoryDashboardRequestTO;
import com.fb.platform.sap.bapi.to.SapInventoryLevelRequestTO;
import com.fb.platform.sap.bapi.to.SapLspAwbUpdateRequestTO;
import com.fb.platform.sap.bapi.to.SapOrderRequestTO;
import com.fb.platform.sap.client.connector.SapClientConnector;
import com.fb.platform.sap.client.handler.impl.SapClientHandler;
import com.sap.conn.idoc.IDocParseException;
import com.sap.conn.jco.JCoException;

public class TestSapClientConnector {
	
	private static Log logger = LogFactory.getLog(TestSapClientConnector.class);
	
	public static void main(String[] args) {
		SapClientConnector bapiConnector = new SapClientConnector();
//		String idoc = "<ZABHIINB><IDOC BEGIN=\"1\"><EDI_DC40 SEGMENT=\"1\"><TABNAM>EDI_DC40</TABNAM><MANDT>400</MANDT><DOCNUM>0000040000686171</DOCNUM><DOCREL>640</DOCREL><STATUS>62</STATUS><DIRECT>2</DIRECT><OUTMOD></OUTMOD><IDOCTYP>ZABHIINB</IDOCTYP><MESTYP>ZABHI_INB</MESTYP><SNDPOR>ZBB</SNDPOR><SNDPRT>LS</SNDPRT><SNDPRN>ZATGJCAPS1</SNDPRN><RCVPOR>SAPDV1</RCVPOR><RCVPRT>LS</RCVPRT><RCVPRN>DV1CLNT400</RCVPRN><CREDAT>20120925</CREDAT><CRETIM>164754</CRETIM></EDI_DC40><ZABHI_INB SEGMENT=\"1\"><ARTICLE>300000561</ARTICLE></ZABHI_INB></IDOC></ZABHIINB>";
//		try {
//			bapiConnector.connectIdoc(idoc);
//		} catch (IDocParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JCoException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		SapClientHandler bh = new SapClientHandler();
		bh.setBapiConnector(bapiConnector);
		
		//Test Order
		SapOrderRequestTO bapiOrderTO = new TestBapiOrderTO().getBapiTO();
		//SapOrderRequestTO bapiOrderTO = new TestBBBapiOrderTO().getBapiTO();
		System.out.println(bh.processOrder(bapiOrderTO));
		
		//Test Inventory Dashboard
		SapInventoryDashboardRequestTO inventoryDashboardRequestTO = new TestInventoryDashboardTO().getRequestTO();
		//System.out.println(bh.processInventoryDashboard(inventoryDashboardRequestTO));
		
		SapInventoryLevelRequestTO inventoryLevelRequestTO = new TestInventoryLevelTO().getInventoryLevelRequestTO();
		//System.out.println(bh.processInventoryLevel(inventoryLevelRequestTO));
		
		SapLspAwbUpdateRequestTO lspAwbUpdateRequestTO = new TestLspAwbUpdateTO().getLspAwbUpdateRequestTO();
		//System.out.println(bh.processLspAwbUpdate(lspAwbUpdateRequestTO));
	}
	
}
