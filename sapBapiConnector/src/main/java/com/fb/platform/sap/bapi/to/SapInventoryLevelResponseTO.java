package com.fb.platform.sap.bapi.to;

import java.math.BigDecimal;

public class SapInventoryLevelResponseTO {

		private String site;
		private String article;
		private int storageLocation;
		private BigDecimal stockQuantity;
		private String unit;
		
		public String getSite() {
			return site;
		}
		public void setSite(String site) {
			this.site = site;
		}
		public String getArticle() {
			return article;
		}
		public void setArticle(String article) {
			this.article = article;
		}
		public int getStorageLocation() {
			return storageLocation;
		}
		public void setStorageLocation(int storageLocation) {
			this.storageLocation = storageLocation;
		}
		public BigDecimal getStockQuantity() {
			return stockQuantity;
		}
		public void setStockQuantity(BigDecimal stockQuantity) {
			this.stockQuantity = stockQuantity;
		}
		public String getUnit() {
			return unit;
		}
		public void setUnit(String unit) {
			this.unit = unit;
		}
		
		@Override
		public String toString() {
			return "SapInventoryLevelResponseTO [site=" + site + ", article="
					+ article + ", storageLocation=" + storageLocation
					+ ", stockQuantity=" + stockQuantity + ", unit=" + unit
					+ "]";
		}
		
}
