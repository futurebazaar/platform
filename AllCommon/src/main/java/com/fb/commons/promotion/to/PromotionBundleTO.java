/**
 * 
 */
package com.fb.commons.promotion.to;

import java.util.List;

/**
 * @author vinayak
 *
 */
public class PromotionBundleTO {

	private int bundleId;

	private List<BundleProductTO> productList;

	public int getBundleId() {
		return bundleId;
	}

	public void setBundleId(int bundleId) {
		this.bundleId = bundleId;
	}

	public List<BundleProductTO> getProductList() {
		return productList;
	}

	public void setProductList(List<BundleProductTO> productList) {
		this.productList = productList;
	}
}
