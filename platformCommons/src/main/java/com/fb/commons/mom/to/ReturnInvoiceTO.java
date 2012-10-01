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
public class ReturnInvoiceTO extends ItemTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1373091425879467564L;
	
	private String number;
	private String returnId;
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
	public String getReturnId() {
		return returnId;
	}
	public void setReturnId(String returnId) {
		this.returnId = returnId;
	}

	@Override
	public String toString(){
<<<<<<< HEAD
		return new ToStringBuilder(this)
		.append("itemTO", super.toString())
		.append("pgrCreationDate", this.number)
		.append("invoiceDate", this.invoiceDate)
		.append("invoiceNet", this.invoiceNet)
		.append("returnId", this.returnId)
		.append("type", this.type)
		.toString();
=======
		String returnInvoice = super.toString()
				+ "\nreturn invoice number : " + number
				+ "\nreturn id : " + returnId
				+ "\nreturn invoice type : " + type
				+ "\nreturn invoice date : " + invoiceDate
				+ "\nreturn invoice net : " + invoiceNet;
		return returnInvoice;
>>>>>>> sapConnector
	}
}
