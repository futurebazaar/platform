package com.fb.commons.mail.to;

import java.io.File;
import java.util.List;

/**
 * @author nehaga
 *
 */
public class MailTO {
	private String from;
	private String[] to;
	private String[] cc;
	private String[] bcc;
	private String bounceBack;
	private String subject;
	private String message;
	private List<File> attachments;
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String[] getTo() {
		return to;
	}
	public void setTo(String[] to) {
		this.to = to;
	}
	public String[] getCc() {
		return cc;
	}
	public void setCc(String[] cc) {
		this.cc = cc;
	}
	public String[] getBcc() {
		return bcc;
	}
	public void setBcc(String[] bcc) {
		this.bcc = bcc;
	}
	public String getBounceBack() {
		return bounceBack;
	}
	public void setBounceBack(String bounceBack) {
		this.bounceBack = bounceBack;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<File> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<File> attachments) {
		this.attachments = attachments;
	}
	
}
