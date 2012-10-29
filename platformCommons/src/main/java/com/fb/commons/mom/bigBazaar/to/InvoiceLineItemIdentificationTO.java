/**
 * 
 */
package com.fb.commons.mom.bigBazaar.to;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author nehaga
 *
 */
public class InvoiceLineItemIdentificationTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6653936794927748517L;
	private String qualifier;
	private int materialId;
	private String idocShortText;
	private int segment;
	
	public String getQualifier() {
		return qualifier;
	}
	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}
	public int getMaterialId() {
		return materialId;
	}
	public void setMaterialId(int materialId) {
		this.materialId = materialId;
	}
	public String getIdocShortText() {
		return idocShortText;
	}
	public void setIdocShortText(String idocShortText) {
		this.idocShortText = idocShortText;
	}
	public int getSegment() {
		return segment;
	}
	public void setSegment(int segment) {
		this.segment = segment;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("qualifier", this.qualifier)
			.append("materialId", this.materialId)
			.append("idocShortText", this.idocShortText)
			.append("segment", this.segment)
			.toString();
	}
}
