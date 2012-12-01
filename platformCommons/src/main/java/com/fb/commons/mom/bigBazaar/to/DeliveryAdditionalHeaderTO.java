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
public class DeliveryAdditionalHeaderTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6614028910677887555L;
	
	private String deliveryType;
	private String deliveryTypeDesc;
	private int deliveryPriority;
	private int transportationGroup;
	private String transportationGroupDesc;
	private int segment;
	
	public String getDeliveryType() {
		return deliveryType;
	}
	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}
	public String getDeliveryTypeDesc() {
		return deliveryTypeDesc;
	}
	public void setDeliveryTypeDesc(String deliveryTypeDesc) {
		this.deliveryTypeDesc = deliveryTypeDesc;
	}
	public int getDeliveryPriority() {
		return deliveryPriority;
	}
	public void setDeliveryPriority(int deliveryPriority) {
		this.deliveryPriority = deliveryPriority;
	}
	public int getTransportationGroup() {
		return transportationGroup;
	}
	public void setTransportationGroup(int transportationGroup) {
		this.transportationGroup = transportationGroup;
	}
	public String getTransportationGroupDesc() {
		return transportationGroupDesc;
	}
	public void setTransportationGroupDesc(String transportationGroupDesc) {
		this.transportationGroupDesc = transportationGroupDesc;
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
			.append("deliveryType", this.deliveryType)
			.append("deliveryTypeDesc", this.deliveryTypeDesc)
			.append("deliveryPriority", this.deliveryPriority)
			.append("transportationGroup", this.transportationGroup)
			.append("transportationGroupDesc", this.transportationGroupDesc)
			.append("segment", this.segment)
			.toString();
	}

}
