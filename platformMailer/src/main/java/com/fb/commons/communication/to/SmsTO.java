package com.fb.commons.communication.to;

import java.util.ArrayList;
import java.util.List;

public class SmsTO {
	
	private List<String> toList;
	private String message;
	
	
	
	public SmsTO() {
		toList = new ArrayList<String>();
		message = "";
	}
	
	public List<String> getTo() {
		return toList;
	}
	public void setTo(List<String> to) {
		this.toList = to;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void addTo(String to) {
		this.toList.add(to);
	}
	
	public String toListAsString() {
		StringBuilder toListString = new StringBuilder();
		for (int i = 0 ; i < toList.size() ; i++){
			toListString.append(toList.get(i));
			if(i>0){
				toListString.append(";");
			}
		}
		return toListString.toString();
	}
}
