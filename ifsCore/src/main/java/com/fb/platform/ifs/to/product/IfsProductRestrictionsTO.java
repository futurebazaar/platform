/**
 * 
 */
package com.fb.platform.ifs.to.product;

/**
 * @author vinayak
 *
 */
public class IfsProductRestrictionsTO {

	private long productId = 0;
	private String shippingMode = null;
	private String preferredLsp = null;

	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public String getShippingMode() {
		return shippingMode;
	}
	public void setShippingMode(String shippingMode) {
		this.shippingMode = shippingMode;
	}
	public String getPreferredLsp() {
		return preferredLsp;
	}
	public void setPreferredLsp(String preferredLsp) {
		this.preferredLsp = preferredLsp;
	}
}
