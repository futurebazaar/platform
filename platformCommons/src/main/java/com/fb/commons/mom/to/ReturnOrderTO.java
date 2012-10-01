/**
 * 
 */
package com.fb.commons.mom.to;

import java.math.BigDecimal;

<<<<<<< HEAD
import org.apache.commons.lang.builder.ToStringBuilder;

=======
>>>>>>> sapConnector
/**
 * @author nehaga
 *
 */
public class ReturnOrderTO extends ItemTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1416820810593424096L;
	
	private String returnOrderId;
	private String returnId;
	private BigDecimal returnQuantity;
	private String storageLocation;
	private String category;
	
	public String getReturnOrderId() {
		return returnOrderId;
	}

	public void setReturnOrderId(String returnOrderId) {
		this.returnOrderId = returnOrderId;
	}

	public String getReturnId() {
		return returnId;
	}

	public void setReturnId(String returnId) {
		this.returnId = returnId;
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
<<<<<<< HEAD
		return new ToStringBuilder(this)
		.append("itemTO", super.toString())
		.append("category", this.category)
		.append("returnOrderId", this.returnOrderId)
		.append("returnQuantity", this.returnQuantity)
		.append("returnId", this.returnId)
		.append("storageLocation", this.storageLocation)
		.toString();
=======
		String cancelItem = super.toString()
				+ "\nreturn order id : " + returnOrderId
				+ "\nreturn id : " + returnId
				+ "\nreturn quantity : " + returnQuantity
				+ "\nreturn storage location : " + storageLocation
				+ "\nreturn category : " + category;
		return cancelItem;
>>>>>>> sapConnector
	}
}
