/**
 * 
 */
package com.fb.commons.mom.to;

<<<<<<< HEAD
import org.apache.commons.lang.builder.ToStringBuilder;
=======
>>>>>>> sapConnector
import org.joda.time.DateTime;

/**
 * @author nehaga
 *
 */
public class ItemInvoiceTO extends ItemTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9094226309070598897L;
	
	private String invoiceNumber;
	private DateTime invoiceDate;
	private String billingType;
	
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
	public String getBillingType() {
		return billingType;
	}
	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}
	@Override
	public String toString(){
<<<<<<< HEAD
		return new ToStringBuilder(this)
		.append("itemTo", super.toString())
		.append("invoiceNumber", this.invoiceNumber)
		.append("billingType", this.billingType)
		.append("invoiceDate", this.invoiceDate)
		.toString();
=======
		String itemInvoice = super.toString()
				+ "\ninvoice number : " + invoiceNumber
				+ "\ninvoice date : " + invoiceDate
				+ "\nbilling type : " + billingType;
		return itemInvoice;
>>>>>>> sapConnector
	}
	
}
