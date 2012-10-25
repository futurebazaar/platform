/**
 * 
 */
package com.fb.commons.mom.bigBazaar.to;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fb.commons.mom.to.SapMomTO;

/**
 * @author nehaga
 *
 */
public class InvoiceTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7204893237202357570L;
	
	private SapMomTO sapIdoc = null;
	private InvoiceHeaderTO invoiceHeader = null;
	private List<InvoicePartnerHeaderTO> invoicePartnerHeader = null;
	private String orderNumber;
	private String invoiceNumber;
	private String deliveryNumber;
	private List<InvoiceLineItemTO> invoiceLineItem = null;
	private InvoiceTypeEnum invoiceType;
	
	public SapMomTO getSapIdoc() {
		return sapIdoc;
	}
	public void setSapIdoc(SapMomTO sapIdoc) {
		this.sapIdoc = sapIdoc;
	}
	public InvoiceHeaderTO getInvoiceHeader() {
		return invoiceHeader;
	}
	public void setInvoiceHeader(InvoiceHeaderTO invoiceHeader) {
		this.invoiceHeader = invoiceHeader;
	}
	public List<InvoicePartnerHeaderTO> getInvoicePartnerHeader() {
		return invoicePartnerHeader;
	}
	public void setInvoicePartnerHeader(List<InvoicePartnerHeaderTO> invoicePartnerHeader) {
		this.invoicePartnerHeader = invoicePartnerHeader;
	}
	public List<InvoiceLineItemTO> getInvoiceLineItem() {
		return invoiceLineItem;
	}
	public void setInvoiceLineItem(List<InvoiceLineItemTO> invoiceLineItem) {
		this.invoiceLineItem = invoiceLineItem;
	}
	public InvoiceTypeEnum getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(InvoiceTypeEnum invoiceType) {
		this.invoiceType = invoiceType;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getDeliveryNumber() {
		return deliveryNumber;
	}
	public void setDeliveryNumber(String deliveryNumber) {
		this.deliveryNumber = deliveryNumber;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("sapIdoc", this.sapIdoc)
			.append("orderNumber", this.orderNumber)
			.append("invoiceNumber", this.invoiceNumber)
			.append("deliveryNumber", this.deliveryNumber)
			.append("invoiceHeader", this.invoiceHeader)
			.append("invoicePartnerHeader", this.invoicePartnerHeader)
			.append("invoiceLineItemTO", this.invoiceLineItem)
			.append("invoiceType", this.invoiceType)
			.toString();
	}
	
}
