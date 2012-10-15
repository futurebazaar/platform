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
	private List<InvoiceDocumentRefTO> invoiceDocRef = null;
	private InvoiceLineItemTO invoiceLineItem = null;
	
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
	public List<InvoiceDocumentRefTO> getInvoiceDocRef() {
		return invoiceDocRef;
	}
	public void setInvoiceDocRef(List<InvoiceDocumentRefTO> invoiceDocRef) {
		this.invoiceDocRef = invoiceDocRef;
	}
	public InvoiceLineItemTO getInvoiceLineItem() {
		return invoiceLineItem;
	}
	public void setInvoiceLineItem(InvoiceLineItemTO invoiceLineItem) {
		this.invoiceLineItem = invoiceLineItem;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("sapIdoc", this.sapIdoc)
			.append("invoiceHeader", this.invoiceHeader)
			.append("invoicePartnerHeader", this.invoicePartnerHeader)
			.append("invoiceDocRef", this.invoiceDocRef)
			.append("invoiceLineItemTO", this.invoiceLineItem)
			.toString();
	}
	
}
