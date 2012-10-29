/**
 * 
 */
package com.fb.commons.mom.bigBazaar.to;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

/**
 * @author nehaga
 *
 */
public class DeliveryDeleteBBHeaderTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7917590473751536480L;
	
	private String orderNumber;
	private int deliveryNumber;
	private String deletedCode;
	private DateTime deletedDate;
	private List<DeliveryDeleteItemBBTO> deletedItems;
	
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public int getDeliveryNumber() {
		return deliveryNumber;
	}
	public void setDeliveryNumber(int deliveryNumber) {
		this.deliveryNumber = deliveryNumber;
	}
	public String getDeletedCode() {
		return deletedCode;
	}
	public void setDeletedCode(String deletedCode) {
		this.deletedCode = deletedCode;
	}
	public DateTime getDeletedDate() {
		return deletedDate;
	}
	public void setDeletedDate(DateTime  deletedDate) {
		this.deletedDate = deletedDate;
	}
	public List<DeliveryDeleteItemBBTO> getDeletedItems() {
		return deletedItems;
	}
	public void setDeletedItems(List<DeliveryDeleteItemBBTO> deletedItems) {
		this.deletedItems = deletedItems;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("orderNumber", this.orderNumber)
			.append("deliveryNumber", this.deliveryNumber)
			.append("deletedCode", this.deletedCode)
			.append("deletedDate", this.deletedDate)
			.append("deletedItems", this.deletedItems)
			.toString();
	}

}
