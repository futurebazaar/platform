package com.fb.commons.mom.to;

<<<<<<< HEAD
import org.apache.commons.lang.builder.ToStringBuilder;

=======
>>>>>>> sapConnector
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
<<<<<<< HEAD
		return new ToStringBuilder(this)
			.append("itemTo", super.toString())
			.append("cancelInvoiceNumber", this.cancelInvoiceNumber)
			.toString();
=======
		String cancelItem = super.toString()
				+ "\ncancel invoice number : " + cancelInvoiceNumber;
		return cancelItem;
>>>>>>> sapConnector
	}
}
