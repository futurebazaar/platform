package com.fb.platform.payback.util;

public enum PartnerMerchantIdEnum {
	
	FUTUREBAZAAR("90012970, 64217418");
	
	private String client = null;
	
	private PartnerMerchantIdEnum(String client){
		this.client = client;
	}
	
	@Override
	public String toString(){
		return this.client;
	}
}
