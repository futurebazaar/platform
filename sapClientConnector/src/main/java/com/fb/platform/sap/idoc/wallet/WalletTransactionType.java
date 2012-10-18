package com.fb.platform.sap.idoc.wallet;

public enum WalletTransactionType {
	
	WOPN,
	WTOP,
	WREFND,
	WCREFND;
	
	@Override
	public String toString() {
		return this.name();
	}

}
