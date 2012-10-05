package com.fb.platform.fulfilment.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/*
 * @author suhas
 */
public class SellerPincodeServicabilityMap implements Serializable{
	private int id;
	private List<Integer> seller_id = new ArrayList<Integer>();
	private String pincode;
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;
	}
	
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
}
