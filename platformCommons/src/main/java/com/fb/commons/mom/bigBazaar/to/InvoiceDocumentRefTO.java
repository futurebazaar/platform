/**
 * 
 */
package com.fb.commons.mom.bigBazaar.to;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

/**
 * @author nehaga
 *
 */
public class InvoiceDocumentRefTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2455311189065296318L;
	private int qualifier;
	private String idocDocumentNumber;
	private DateTime idocDate;
	
	public int getQualifier() {
		return qualifier;
	}
	public void setQualifier(int qualifier) {
		this.qualifier = qualifier;
	}
	public String getIdocDocumentNumber() {
		return idocDocumentNumber;
	}
	public void setIdocDocumentNumber(String idocDocumentNumber) {
		this.idocDocumentNumber = idocDocumentNumber;
	}
	public DateTime getIdocDate() {
		return idocDate;
	}
	public void setIdocDate(DateTime idocDate) {
		this.idocDate = idocDate;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("qualifier", this.qualifier)
			.append("idocDocumentNumber", this.idocDocumentNumber)
			.append("idocDate", this.idocDate)
			.toString();
	}
}
