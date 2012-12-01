package com.fb.platform.sap.bapi.to;

public class SapLspAwbUpdateRequestTO {
	
	private String deliveryNumber;
	private String lspCode;
	private String awb;
	
	public String getDeliveryNumber() {
		return deliveryNumber;
	}
	public void setDeliveryNumber(String deliveryNumber) {
		this.deliveryNumber = deliveryNumber;
	}
	public String getLspCode() {
		return lspCode;
	}
	public void setLspCode(String lspCode) {
		this.lspCode = lspCode;
	}
	public String getAwb() {
		return awb;
	}
	public void setAwb(String awb) {
		this.awb = awb;
	}
	
	@Override
	public String toString() {
		return "SapLspAwbUpdateRequestTO [deliveryNumber=" + deliveryNumber
				+ ", lspCode=" + lspCode + ", awb=" + awb + "]";
	}

}
