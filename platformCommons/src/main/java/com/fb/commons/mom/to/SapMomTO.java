/**
 * 
 */
package com.fb.commons.mom.to;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

/**
 * @author nehaga
 *
 */
public class SapMomTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1094796082185921675L;
	
	private String idocNumber;
	private String refUID;
	private String idoc;
	private int segmentNumber;
	private String poNumber;
	private String canGr;
	private long ackUID;
	private DateTime timestamp;

	public SapMomTO(long ackUID) {
		this.ackUID = ackUID;
		this.timestamp = new DateTime();
	}

	public long getAckUID() {
		return ackUID;
	}
	
	public String getIdocNumber() {
		return idocNumber;
	}
	public void setIdocNumber(String idocNumber) {
		this.idocNumber = idocNumber;
	}
	
	public String getRefUID() {
		return refUID;
	}
	public void setRefUID(String refUID) {
		this.refUID = refUID;
	}
	public String getIdoc() {
		return idoc;
	}
	public void setIdoc(String idoc) {
		this.idoc = idoc;
	}
	
	public int getSegmentNumber() {
		return segmentNumber;
	}
	public void setSegmentNumber(int segmentNumber) {
		this.segmentNumber = segmentNumber;
	}
	public String getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}
	public String getCanGr() {
		return canGr;
	}
	public void setCanGr(String canGr) {
		this.canGr = canGr;
	}
	public DateTime getTimestamp() {
		return timestamp;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
		.append("idocNumber", this.idocNumber)
		.append("segmentNumber", this.segmentNumber)
		.append("canGr", this.canGr)
		.append("poNumber", this.poNumber)
		.append("refUID", this.refUID)
		.append("timestamp", this.timestamp)
		.append("ackUID", this.ackUID)
		.toString();
	}
}
