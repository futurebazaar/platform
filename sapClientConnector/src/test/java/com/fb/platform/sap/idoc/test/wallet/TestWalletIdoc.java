package com.fb.platform.sap.idoc.test.wallet;

public class TestWalletIdoc {
	
	public String getWalletIdocXml() {
		String idoc = "<ZABHIINB><IDOC BEGIN=\"1\"><EDI_DC40 SEGMENT=\"1\"><TABNAM>EDI_DC40</TABNAM><MANDT>400</MANDT><DOCNUM>0000040000686171</DOCNUM><DOCREL>640</DOCREL><STATUS>62</STATUS><DIRECT>2</DIRECT><OUTMOD></OUTMOD><IDOCTYP>ZABHIINB</IDOCTYP><MESTYP>ZABHI_INB</MESTYP><SNDPOR>ZBB</SNDPOR><SNDPRT>LS</SNDPRT><SNDPRN>ZATGJCAPS1</SNDPRN><RCVPOR>SAPDV1</RCVPOR><RCVPRT>LS</RCVPRT><RCVPRN>DV1CLNT400</RCVPRN><CREDAT>20120925</CREDAT><CRETIM>164754</CRETIM></EDI_DC40><ZABHI_INB SEGMENT=\"1\"><ARTICLE>000000000300000561</ARTICLE></ZABHI_INB></IDOC></ZABHIINB>";
		return idoc;
	}

}
