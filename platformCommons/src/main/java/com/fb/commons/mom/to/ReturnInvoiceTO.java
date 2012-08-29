/**
 * 
 */
package com.fb.commons.mom.to;

import org.joda.time.DateTime;

/**
 * @author nehaga
 *
 */
public class ReturnInvoiceTO extends ItemTO {
	private String number;
	private DateTime invoiceDate;
	private String type;
	private String invoiceNet;
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public DateTime getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(DateTime invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getInvoiceNet() {
		return invoiceNet;
	}
	public void setInvoiceNet(String invoiceNet) {
		this.invoiceNet = invoiceNet;
	}

	@Override
	public String toString(){
		String returnInvoice = super.toString()
				+ "\nreturn invoice number : " + number
				+ "\nreturn invoice type : " + type
				+ "\nreturn invoice date : " + invoiceDate
				+ "\nreturn invoice net : " + invoiceNet;
		return returnInvoice;
	}
}