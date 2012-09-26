package com.fb.commons.mom.to;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 */

/**
 * @author nehaga
 *
 */
public class CancelItemTO extends ItemTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3906880439792526563L;
	
	private String cancelInvoiceNumber;

	public String getCancelInvoiceNumber() {
		return cancelInvoiceNumber;
	}

	public void setCancelInvoiceNumber(String cancelInvoiceNumber) {
		this.cancelInvoiceNumber = cancelInvoiceNumber;
	}
	@Override
	public String toString(){
		return new ToStringBuilder(this)
			.append("itemTo", super.toString())
			.append("cancelInvoiceNumber", this.cancelInvoiceNumber)
			.toString();
	}
}
