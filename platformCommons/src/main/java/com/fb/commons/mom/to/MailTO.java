package com.fb.commons.mom.to;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author nehaga
 *
 */
public class MailTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8082027411633952722L;
	
	private String from;
	private String[] to;
	private String[] cc;
	private String[] bcc;
	private String bounceBack;
	private String subject;
	private String message;
	private List<File> attachments = null;
	private boolean isHtmlText = false;
	private String fromPersonal;
	
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
	public boolean isHtmlText() {
		return isHtmlText;
	}
	public void setHtmlText(boolean isHtmlText) {
		this.isHtmlText = isHtmlText;
	}
	public String getFromPersonal() {
		return fromPersonal;
	}
	public void setFromPersonal(String fromPersonal) {
		this.fromPersonal = fromPersonal;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
		.append("", this.isHtmlText)
		.append("", this.bcc)
		.append("", this.bounceBack)
		.append("", this.cc)
		.append("", this.from)
		.append("", this.fromPersonal)
		//.append("", this.message)
		.append("", this.subject)
		.append("", this.to)
		.toString();
	}
	
}
