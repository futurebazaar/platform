/**
 * 
 */
package com.fb.commons.mom.bigBazaar.to;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author nehaga
 *
 */
public class InvoiceLineItemTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8843845046752105613L;
	private int itemNumber;
	private BigDecimal quantity;
	private String unitOfMeasurement;
	private String weightUnit;
	private String itemCategory;
	private String plant;
	private List<InvoiceLineItemIdentificationTO> lineItemIdentificationTO = new ArrayList<InvoiceLineItemIdentificationTO>();
	private int segment;
	
	public int getItemNumber() {
		return itemNumber;
	}
	public void setItemNumber(int itemNumber) {
		this.itemNumber = itemNumber;
	}
	public BigDecimal getQuantity() {
		return quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	public String getUnitOfMeasurement() {
		return unitOfMeasurement;
	}
	public void setUnitOfMeasurement(String unitOfMeasurement) {
		this.unitOfMeasurement = unitOfMeasurement;
	}
	public String getWeightUnit() {
		return weightUnit;
	}
	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}
	public String getItemCategory() {
		return itemCategory;
	}
	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}
	public String getPlant() {
		return plant;
	}
	public void setPlant(String plant) {
		this.plant = plant;
	}
	public List<InvoiceLineItemIdentificationTO> getLineItemIdentificationTO() {
		return lineItemIdentificationTO;
	}
	public void setLineItemIdentificationTO(
			List<InvoiceLineItemIdentificationTO> lineItemIdentificationTO) {
		this.lineItemIdentificationTO = lineItemIdentificationTO;
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
			.append("itemNumber", this.itemNumber)
			.append("quantity", this.quantity)
			.append("unitOfMeasurement", this.unitOfMeasurement)
			.append("weightUnit", this.weightUnit)
			.append("itemCategory", this.itemCategory)
			.append("plant", this.plant)
			.append("lineItemIdentificationTO", this.lineItemIdentificationTO)
			.append("segment", this.segment)
			.toString();
	}
}
