package com.fb.platform.fulfilment.to;

import java.io.Serializable;
/**
 * @author suhas
 *
 */
public enum SellerByPincodeResponseStatusEnum implements Serializable{
	
	SUCCESS("SUCCESS", "Got the seller for given pincode"),
	NO_SESSION("NO_SESSION", "INVALID SESSION, PLEASE APPLY AGAIN OR LOGIN AGAIN AND TRY"),
	NONSERVICABLE_PINCODE("NONSERVICABLE_PINCODE", "Sorry, we currently do not provide service at your pincode."),
	INTERNAL_ERROR("INTERNAL_ERROR", "Oops, there seems to be a problem with our systems. Can you please try again?");
	
	
	private String status = null;
	private String message = null;
	
	private SellerByPincodeResponseStatusEnum(String status, String message) {
		this.status = status;
		this.message = message;
	}

	@Override
	public String toString() {
		return this.status;
	}
	
	public String getMesage() {
		return this.message;
	}
	
	public String getStatus() {
		return this.status;
	}
}
