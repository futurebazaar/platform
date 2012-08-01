package com.fb.commons.mom.to;

/**
 * 
 */

/**
 * @author nehaga
 *
 */
public class CancelItemTO extends ItemTO {
	private String cancelInvoiceNumber;

	public String getCancelInvoiceNumber() {
		return cancelInvoiceNumber;
	}

	public void setCancelInvoiceNumber(String cancelInvoiceNumber) {
		this.cancelInvoiceNumber = cancelInvoiceNumber;
	}
	@Override
	public String toString(){
		String cancelItem = super.toString()
				+ "\ncancel invoice number : " + cancelInvoiceNumber;
		return cancelItem;
	}
}
