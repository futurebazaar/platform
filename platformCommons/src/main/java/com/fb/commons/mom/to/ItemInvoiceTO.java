/**
 * 
 */
package com.fb.commons.mom.to;

import org.joda.time.DateTime;

/**
 * @author nehaga
 *
 */
public class ItemInvoiceTO extends ItemTO {
	private String invoiceNumber;
	private DateTime invoiceDate;
	
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public DateTime getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(DateTime invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	@Override
	public String toString(){
		String itemInvoice = super.toString()
				+ "\ninvoice number : " + invoiceNumber
				+ "\ninvoice date : " + invoiceDate;
		return itemInvoice;
	}
	
}
