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
	private int itemNum;
	private String user;
	
	public int getItemNum() {
		return itemNum;
	}
	public void setItemNum(int itemNum) {
		this.itemNum = itemNum;
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
			.append("itemNum", this.itemNum)
			.append("user", this.user)
			.toString();
	}
}
