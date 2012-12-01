package com.fb.platform.scheduler.to;

/**
 * @author anubhav
 *
 */
public class OrderXmlTO {
	
	private long id;
	private String xml;
	private String type;
	private int attempts;
	private long orderId;
	private String referenceOrderId;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getXml() {
		return xml;
	}
	public void setXml(String xml) {
		this.xml = xml;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getAttempts() {
		return attempts;
	}
	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public String getReferenceOrderId() {
		return referenceOrderId;
	}
	public void setReferenceOrderId(String referenceOrderId) {
		this.referenceOrderId = referenceOrderId;
	}
	
	@Override
	public String toString() {
		return "OrderXmlTO [id=" + id + ", xml=" + xml + ", type=" + type
				+ ", attempts=" + attempts + ", orderId=" + orderId
				+ ", referenceOrderId=" + referenceOrderId + "]";
	}

}
