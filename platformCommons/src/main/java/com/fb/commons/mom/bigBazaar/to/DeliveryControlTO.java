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
public class DeliveryControlTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 310816506214585901L;
	
	private String qualifier;
	private int segment;
	
	public String getQualifier() {
		return qualifier;
	}
	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
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
			.append("segment", this.segment)
			.toString();
	}
}
