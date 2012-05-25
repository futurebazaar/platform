package com.fb.commons.to;

/**
 * @author nehaga
 *
 */
public class PlatformMessage {
	private String code;
	private Object[] argumentsList;
	
	public PlatformMessage() {
		super();
	}
	
	public PlatformMessage(String code, Object[] argumentsList) {
		this();
		this.code = code;
		this.argumentsList = argumentsList;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Object[] getArgumentsList() {
		return argumentsList;
	}
	public void setArgumentsList(Object[] argumentsList) {
		this.argumentsList = argumentsList;
	}
	
}
