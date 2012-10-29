/**
 * 
 */
package com.fb.commons.mom.to;

import org.apache.commons.lang.builder.ToStringBuilder;
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
	private String returnId;
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
	public String getReturnId() {
		return returnId;
	}
	public void setReturnId(String returnId) {
		this.returnId = returnId;
	}
	
	@Override
	public String toString(){
		return new ToStringBuilder(this)
		.append("itemTO", super.toString())
		.append("number", this.number)
		.append("returnCreatedBy", this.returnCreatedBy)
		.append("returnCreatedDate", this.returnCreatedDate)
		.append("returnId", this.returnId)
		.append("type", this.type)
		.toString();
	}
}