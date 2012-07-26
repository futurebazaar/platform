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
	
	@Override
	public String toString() {
		return "idoc number : " + idocNumber
				+ "\nreference UID : " + refUID
				+ "\nidoc : " + idoc;
	}
	
}
