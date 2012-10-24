/**
 * 
 */
package com.fb.commons.mom.bigBazaar.to;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fb.commons.mom.to.SapMomTO;

/**
 * @author nehaga
 *
 */
public class DeliveryDeleteBBTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5568266197502838574L;
	
	private SapMomTO sapIdoc;
	private DeliveryDeleteBBHeaderTO deliveryDeleteHeader;
	
	public DeliveryDeleteBBHeaderTO getDeliveryDeleteHeader() {
		return deliveryDeleteHeader;
	}

	public void setDeliveryDeleteHeader(
			DeliveryDeleteBBHeaderTO deliveryDeleteHeader) {
		this.deliveryDeleteHeader = deliveryDeleteHeader;
	}

	public SapMomTO getSapIdoc() {
		return sapIdoc;
	}

	public void setSapIdoc(SapMomTO sapIdoc) {
		this.sapIdoc = sapIdoc;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
		.append("sapIdoc", this.sapIdoc)
		.append("deliveryDeleteHeader", this.deliveryDeleteHeader)
		.toString();
	}
}
