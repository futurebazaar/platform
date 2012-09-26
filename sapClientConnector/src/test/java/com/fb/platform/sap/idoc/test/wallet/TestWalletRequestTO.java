package com.fb.platform.sap.idoc.test.wallet;

import java.math.BigDecimal;

import org.joda.time.DateTime;

import com.fb.platform.sap.bapi.to.SapWalletRequestTO;

public class TestWalletRequestTO {
	
	public SapWalletRequestTO getWalletRequestTO() {
		SapWalletRequestTO walletRequestTO = new SapWalletRequestTO();
		walletRequestTO.setAmount(new BigDecimal(200));
		walletRequestTO.setLoginID("test@test.com");
		walletRequestTO.setName("TEST_WALLET");
		walletRequestTO.setOrderID("5049999901");
		walletRequestTO.setPaymentMode("CREDIT CARD");
		walletRequestTO.setTransactionTimestamp(DateTime.now());
		walletRequestTO.setTransactionType("WTOP");
		walletRequestTO.setWalletNumber(1);
		return walletRequestTO;
	}

}
