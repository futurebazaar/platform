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
	private String idocType;
	private String idoc;
	
	public String getIdocNumber() {
		return idocNumber;
	}
	public void setIdocNumber(String idocNumber) {
		this.idocNumber = idocNumber;
	}
	public String getIdocType() {
		return idocType;
	}
	public void setIdocType(String idocType) {
		this.idocType = idocType;
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
				+ "\nidoc type : " + idocType
				+ "\nidoc : " + idoc;
	}
	
}
