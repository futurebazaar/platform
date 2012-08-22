/**
 * 
 */
package com.fb.commons.mom.to;

import java.io.Serializable;

/**
 * @author nehaga
 *
 */
public class SapMomTO implements Serializable {
	private String idocNumber;
	private String refUID;
	private String idoc;
	private int segmentNumber;
	private String poNumber;
	private String canGr;
	
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
	@Override
	public String toString() {
		return "idoc number : " + idocNumber
			+ "\nreference UID : " + refUID
			+ "\nsegment number : " + segmentNumber
			+ "\nidoc : " + idoc
			+ "\npo number : " + poNumber
			+ "\ncan gr : " + canGr;
	}
	
}
