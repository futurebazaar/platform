package com.fb.commons.communication.to;

import java.util.ArrayList;
import java.util.List;

public class SmsTO {

	private List<String> toList;
	private String message;
	private String bcc;

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
		for (String s : toList) {
			toListString.append(s + ";");
		}
		return toListString.toString();
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
}
