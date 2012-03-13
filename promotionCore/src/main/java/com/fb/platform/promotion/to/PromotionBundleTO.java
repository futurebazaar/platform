/**
 * 
 */
package com.fb.platform.promotion.to;

import java.io.Serializable;
import java.util.List;

/**
 * @author vinayak
 *
 */
public class PromotionBundleTO  implements Serializable {

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
