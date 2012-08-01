/**
 * 
 */
package com.fb.commons.mom.to;

import java.math.BigDecimal;

/**
 * @author nehaga
 *
 */
public class ReturnOrderTO extends ItemTO {
	private String returnOrderId;
	private BigDecimal returnQuantity;
	private String storageLocation;
	private String category;
	
	public String getReturnOrderId() {
		return returnOrderId;
	}

	public void setReturnOrderId(String returnOrderId) {
		this.returnOrderId = returnOrderId;
	}

	public BigDecimal getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(BigDecimal returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public String getStorageLocation() {
		return storageLocation;
	}

	public void setStorageLocation(String storageLocation) {
		this.storageLocation = storageLocation;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString(){
		String cancelItem = super.toString()
				+ "\nreturn order id : " + returnOrderId
				+ "\nreturn quantity : " + returnQuantity
				+ "\nreturn storage location : " + storageLocation
				+ "\nreturn category : " + category;
		return cancelItem;
	}
}
