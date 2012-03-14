package com.fb.platform.promotion.model;

import java.util.List;
import java.util.Set;

public class PromotionBundle {

	private int bundleId;

	private List<PromotionBundleProduct> productList;
	
	public int getBundleId() {
		return bundleId;
	}

	public void setBundleId(int bundleId) {
		this.bundleId = bundleId;
	}

	public List<PromotionBundleProduct> getProductList() {
		return productList;
	}
	public void setProductList(List<PromotionBundleProduct> productList) {
		this.productList = productList;
	}
}
