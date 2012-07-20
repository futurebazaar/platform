/**
 * 
 */
package com.fb.commons.mom.to;

import java.io.Serializable;

/**
 * @author nehaga
 *
 */
public class DeliveryDeleteTO implements Serializable{
	private SapMomTO sapIdoc;
	private String deliveryNo;
	private int itemNo;
	private String user;
	private String transactionCode;
	private String date;
	private String time;
	
	public String getDeliveryNo() {
		return deliveryNo;
	}
	public void setDeliveryNo(String deliveryNo) {
		this.deliveryNo = deliveryNo;
	}
	public int getItemNo() {
		return itemNo;
	}
	public void setItemNo(int itemNo) {
		this.itemNo = itemNo;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getTransactionCode() {
		return transactionCode;
	}
	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public SapMomTO getSapIdoc() {
		return sapIdoc;
	}
	public void setSapIdoc(SapMomTO sapIdoc) {
		this.sapIdoc = sapIdoc;
	}
	
	@Override
	public String toString() {
		String deliveryDelete = "delivery number:" + deliveryNo
				+ "\nitem number:" + itemNo
				+ "\nuser:" + user
				+ "\ntransaction code:" + transactionCode
				+ "\ndate:" + date
				+ "\ntime:" + time;
		if(sapIdoc != null) {
			deliveryDelete += "\n" + sapIdoc.toString();
		}
		return deliveryDelete;
	}
	
}
