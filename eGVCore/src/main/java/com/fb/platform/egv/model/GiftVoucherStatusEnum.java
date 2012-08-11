/**
 * 
 */
package com.fb.platform.egv.model;

import java.io.Serializable;

/**
 * @author keith
 *
 */
public enum GiftVoucherStatusEnum implements Serializable{
	
	INACTIVE("INACTIVE"),
	CONFIRMED("CONFIRMED"),
	CANCELLED("CANCELLED"),
	USED("USED"),
	USE_ROLLBACKED("USE_ROLLBACKED");

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
