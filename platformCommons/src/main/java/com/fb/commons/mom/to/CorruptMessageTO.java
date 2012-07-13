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
	private Object message;
	private CorruptMessageCause cause;
	
	public Object getMessage() {
		return message;
	}
	public void setMessage(Object message) {
		this.message = message;
	}
	public CorruptMessageCause getCause() {
		return cause;
	}
	public void setCause(CorruptMessageCause cause) {
		this.cause = cause;
	}
}
