/**
 * 
 */
package com.fb.platform.egv.model;

import com.sun.xml.bind.v2.runtime.RuntimeUtil.ToStringAdapter;

/**
 * @author keith
 *
 */
public enum GiftVoucherStatusEnum {
	
	INACTIVE("INACTIVE"),
	CONFIRMED("CONFIRMED"),
	CANCELLED("CANCELLED"),
	USED("USED");

	private String status = null; 
	
	
	GiftVoucherStatusEnum(String status) {
		this.status = status;
	}
	
	@Override
	public String toString(){
		return this.status;
	}
	
	public String getStatus(){
		return this.status;
	}
	

}
