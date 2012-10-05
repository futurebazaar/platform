package com.fb.platform.fulfilment.to;

import java.io.Serializable;

/**
 * 
 * @author suhas
 *
 */

public class SellerByPincodeRequest implements Serializable{

	private String pincode;
	
	public void setPincode(String pincode){
		this.pincode = pincode;
	}
	
	public String getPincode(){
		return pincode;
	}
}
