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
public class DeliveryTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 894183183536879559L;
	
	private SapMomTO sapIdoc;
	private DeliveryHeaderTO deliveryHeaderTO;
	private int begin;
	public SapMomTO getSapIdoc() {
		return sapIdoc;
	}
	public void setSapIdoc(SapMomTO sapIdoc) {
		this.sapIdoc = sapIdoc;
	}
	public DeliveryHeaderTO getDeliveryHeaderTO() {
		return deliveryHeaderTO;
	}
	public void setDeliveryHeaderTO(DeliveryHeaderTO deliveryHeaderTO) {
		this.deliveryHeaderTO = deliveryHeaderTO;
	}
	public int getBegin() {
		return begin;
	}
	public void setBegin(int begin) {
		this.begin = begin;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
		.append("sapIdoc", this.sapIdoc)
		.append("deliveryHeaderTO", this.deliveryHeaderTO)
		.append("begin", this.begin)
		.toString();
	}

}
