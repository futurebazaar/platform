/**
 * 
 */
package com.fb.commons.mom.bigBazaar.to;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

/**
 * @author nehaga
 *
 */
public class DeliveryHeaderTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6954423720123120313L;
	
	private String salesDistributionDoc;
	private int receivingPoint;
	private int salesOrganization;
	private String warehouseRef;
	private int shippingConditions;
	private BigDecimal totalWeight;
	private BigDecimal netWeight;
	private BigDecimal volume;
	private int packageCount;
	private DateTime deliveryDate;
	private DeliveryAdditionalHeaderTO deliveryAdditionalHeaderTO;
	private DeliveryControlTO deliveryControlTO;
	private DeliveryDeadlineTO deliveryDeadlineTO;
	private List<DeliveryItemTO> deliveryItemList = new ArrayList<DeliveryItemTO>();
	private int segment;
	
	public String getSalesDistributionDoc() {
		return salesDistributionDoc;
	}
	public void setSalesDistributionDoc(String salesDistributionDoc) {
		this.salesDistributionDoc = salesDistributionDoc;
	}
	public int getReceivingPoint() {
		return receivingPoint;
	}
	public void setReceivingPoint(int receivingPoint) {
		this.receivingPoint = receivingPoint;
	}
	public int getSalesOrganization() {
		return salesOrganization;
	}
	public void setSalesOrganization(int salesOrganization) {
		this.salesOrganization = salesOrganization;
	}
	public String getWarehouseRef() {
		return warehouseRef;
	}
	public void setWarehouseRef(String warehouseRef) {
		this.warehouseRef = warehouseRef;
	}
	public int getShippingConditions() {
		return shippingConditions;
	}
	public void setShippingConditions(int shippingConditions) {
		this.shippingConditions = shippingConditions;
	}
	public BigDecimal getTotalWeight() {
		return totalWeight;
	}
	public void setTotalWeight(BigDecimal totalWeight) {
		this.totalWeight = totalWeight;
	}
	public BigDecimal getNetWeight() {
		return netWeight;
	}
	public void setNetWeight(BigDecimal netWeight) {
		this.netWeight = netWeight;
	}
	public BigDecimal getVolume() {
		return volume;
	}
	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}
	public int getPackageCount() {
		return packageCount;
	}
	public void setPackageCount(int packageCount) {
		this.packageCount = packageCount;
	}
	public DateTime getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(DateTime deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public DeliveryAdditionalHeaderTO getDeliveryAdditionalHeaderTO() {
		return deliveryAdditionalHeaderTO;
	}
	public void setDeliveryAdditionalHeaderTO(
			DeliveryAdditionalHeaderTO deliveryAdditionalHeaderTO) {
		this.deliveryAdditionalHeaderTO = deliveryAdditionalHeaderTO;
	}
	public DeliveryControlTO getDeliveryControlTO() {
		return deliveryControlTO;
	}
	public void setDeliveryControlTO(DeliveryControlTO deliveryControlTO) {
		this.deliveryControlTO = deliveryControlTO;
	}
	public DeliveryDeadlineTO getDeliveryDeadlineTO() {
		return deliveryDeadlineTO;
	}
	public void setDeliveryDeadlineTO(DeliveryDeadlineTO deliveryDeadlineTO) {
		this.deliveryDeadlineTO = deliveryDeadlineTO;
	}
	public List<DeliveryItemTO> getDeliveryItemList() {
		return deliveryItemList;
	}
	public void setDeliveryItemList(List<DeliveryItemTO> deliveryItemList) {
		this.deliveryItemList = deliveryItemList;
	}
	public int getSegment() {
		return segment;
	}
	public void setSegment(int segment) {
		this.segment = segment;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("salesDistributionDoc", this.salesDistributionDoc)
			.append("receivingPoint", this.receivingPoint)
			.append("salesOrganization", this.salesOrganization)
			.append("warehouseRef", this.warehouseRef)
			.append("shippingConditions", this.shippingConditions)
			.append("totalWeight", this.totalWeight)
			.append("netWeight", this.netWeight)
			.append("volume", this.volume)
			.append("packageCount", this.packageCount)
			.append("deliveryDate", this.deliveryDate)
			.append("deliveryAdditionalHeaderTO", this.deliveryAdditionalHeaderTO)
			.append("deliveryControlTO", this.deliveryControlTO)
			.append("deliveryDeadlineTO", this.deliveryDeadlineTO)
			.append("deliveryItemList", this.deliveryItemList)
			.append("segment", this.segment)
			.toString();
	}
}
