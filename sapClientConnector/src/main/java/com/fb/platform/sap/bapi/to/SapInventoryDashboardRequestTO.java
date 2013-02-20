package com.fb.platform.sap.bapi.to;

import org.joda.time.DateTime;

public class SapInventoryDashboardRequestTO {
	
	private String article;
	private String plant;
	private DateTime fromDateTime;
	private DateTime toDateTime;
	private String client;

	public DateTime getFromDateTime() {
		return fromDateTime;
	}
	public void setFromDateTime(DateTime fromDateTime) {
		this.fromDateTime = fromDateTime;
	}
	public DateTime getToDateTime() {
		return toDateTime;
	}
	public void setToDateTime(DateTime toDateTime) {
		this.toDateTime = toDateTime;
	}
	public String getArticle() {
		return article;
	}
	public void setArticle(String article) {
		this.article = article;
	}
	public String getPlant() {
		return plant;
	}
	public void setPlant(String plant) {
		this.plant = plant;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getClient() {
		return client;
	}
	
	@Override
	public String toString() {
		return "SapInventoryDashboardRequestTO [article=" + article
				+ ", plant=" + plant + ", fromDateTime=" + fromDateTime
				+ ", toDateTime=" + toDateTime + ", client=" + client + "]";
	}
	
}
