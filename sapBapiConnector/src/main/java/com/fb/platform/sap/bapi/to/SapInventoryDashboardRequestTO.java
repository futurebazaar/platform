package com.fb.platform.sap.bapi.to;

import org.joda.time.DateTime;

public class SapInventoryDashboardRequestTO {
	
	private String article;
	private String plant;
	private DateTime fromDateTime;
	private DateTime toDateTime;

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
	
	public String toString() {
		String description = "\nArticle : " + article
				+ "\nPlant : " + plant
				+  "\n From Date Time : " + fromDateTime
				+ "\n To Date Time : " + toDateTime;
		return description;
		
	}
	
}
