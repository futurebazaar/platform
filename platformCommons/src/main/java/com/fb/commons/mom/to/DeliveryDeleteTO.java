/**
 * 
 */
package com.fb.commons.mom.to;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author nehaga
 *
 */
public class DeliveryDeleteTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6393503632153591125L;
	
	private SapMomTO sapIdoc;
	private String orderNo;
	private String deliveryNo;
	private int itemNo;
	private String user;
	private String transactionCode;
	private String date;
	private String time;
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
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
		return new ToStringBuilder(this)
		.append("sapIdoc", this.sapIdoc)
		.append("itemNo", this.itemNo)
		.append("date", this.date)
		.append("deliveryNo", this.deliveryNo)
		.append("orderNo", this.orderNo)
		.append("time", this.time)
		.append("transactionCode", this.transactionCode)
		.append("user", this.user)
		.toString();
	}
}
