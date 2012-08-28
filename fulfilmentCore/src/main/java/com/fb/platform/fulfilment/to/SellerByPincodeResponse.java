package com.fb.platform.fulfilment.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fb.platform.fulfilment.model.SellerPincodeServicabilityMap;

public class SellerByPincodeResponse implements Serializable{

	private String pincode;
	private List<Integer> seller_id = new ArrayList<Integer>();
	private String statusMessage;
	private String statusCode;
	
	public void setPincode(String pincode){
		this.pincode = pincode;
	}
	
	public String getPincode(){
		return this.pincode;
	}
	
	public void setSellerId(List<Integer> seller_id){
		this.seller_id = seller_id;
	}
	
	public List<Integer> getSellerId(){
		return this.seller_id;
	}

	public String getStatusMessage(){
		return this.statusMessage;
	}
	
	public String getStatusCode(){
		return this.statusCode;
	}
	
	public void setStatus(SellerByPincodeResponseStatusEnum status) {
		this.statusMessage = status.getMesage();
		this.statusCode = status.getStatus();
	}
	
}
