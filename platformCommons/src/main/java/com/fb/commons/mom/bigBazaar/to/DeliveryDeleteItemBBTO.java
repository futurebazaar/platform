/**
 * 
 */
package com.fb.commons.mom.bigBazaar.to;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author nehaga
 *
 */
public class DeliveryDeleteItemBBTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3613892073448595007L;
	private int itemNumber;
	private String user;
	
	public int getItemNumber() {
		return itemNumber;
	}
	public void setItemNumber(int itemNumber) {
		this.itemNumber = itemNumber;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("itemNumber", this.itemNumber)
			.append("user", this.user)
			.toString();
	}
}
