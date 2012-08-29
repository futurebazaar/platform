/**
 * 
 */
package com.fb.commons.mom.to;

import org.joda.time.DateTime;

/**
 * @author nehaga
 *
 */
public class ReturnDeliveryTO extends ItemTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4093465291534799187L;
	
	private String number;
	private String type;
	private DateTime returnCreatedDate;
	private String returnCreatedBy;
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public DateTime getReturnCreatedDate() {
		return returnCreatedDate;
	}
	public void setReturnCreatedDate(DateTime returnCreatedDate) {
		this.returnCreatedDate = returnCreatedDate;
	}
	public String getReturnCreatedBy() {
		return returnCreatedBy;
	}
	public void setReturnCreatedBy(String returnCreatedBy) {
		this.returnCreatedBy = returnCreatedBy;
	}
	@Override
	public String toString(){
		String returnDelivery = super.toString()
				+ "\nreturn delivery number : " + number
				+ "\nreturn delivery type : " + type
				+ "\nreturn created date : " + returnCreatedDate
				+ "\nreturn created by : " + returnCreatedBy;
		return returnDelivery;
	}
	
}
