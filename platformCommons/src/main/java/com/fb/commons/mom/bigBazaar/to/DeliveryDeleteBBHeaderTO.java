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
	
	private String order;
	private int delivery;
	private String deletedCode;
	private DateTime deletedDate;
	private List<DeliveryDeleteItemBBTO> deletedItems;
	
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public int getDelivery() {
		return delivery;
	}
	public void setDelivery(int delivery) {
		this.delivery = delivery;
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
			.append("order", this.order)
			.append("delivery", this.delivery)
			.append("deletedCode", this.deletedCode)
			.append("deletedDate", this.deletedDate)
			.append("deletedItems", this.deletedItems)
			.toString();
	}

}
