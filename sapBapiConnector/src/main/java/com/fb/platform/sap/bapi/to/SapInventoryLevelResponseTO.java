package com.fb.platform.sap.bapi.to;

public class SapInventoryLevelResponseTO {

		private String site;
		private String article;
		private String storageLocation;
		private String stockQuantity;
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
		public String getStorageLocation() {
			return storageLocation;
		}
		public void setStorageLocation(String storageLocation) {
			this.storageLocation = storageLocation;
		}
		public String getStockQuantity() {
			return stockQuantity;
		}
		public void setStockQuantity(String stockQuantity) {
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
