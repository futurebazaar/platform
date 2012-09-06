/**
 * 
 */
package com.fb.commons.mom.to;

import java.io.Serializable;

/**
 * @author nehaga
 *
 */
public class CorruptMessageTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5166601589528181406L;
	
	private SapMomTO sapIdoc;
	private CorruptMessageCause cause;
	
	public CorruptMessageCause getCause() {
		return cause;
	}
	public void setCause(CorruptMessageCause cause) {
		this.cause = cause;
	}
	public SapMomTO getSapIdoc() {
		return sapIdoc;
	}
	public void setSapIdoc(SapMomTO sapIdoc) {
		this.sapIdoc = sapIdoc;
	}
	@Override
	public String toString() {
		String corruptMessage = "";
		if(cause != null) {
			corruptMessage = "cause : " + cause.toString();
		}
		if(sapIdoc != null) {
			corruptMessage += "\n" + sapIdoc.toString();
		}
		
		return corruptMessage; 
	}
	
}
