/**
 * 
 */
package com.fb.platform.promotion.to;

import java.util.List;

/**
 * @author vinayak
 *
 */
public class Product {

	private int productId = 0;
	private List<Integer> categories = null;
	private List<Integer> brands = null;

	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public List<Integer> getCategories() {
		return categories;
	}
	public void setCategories(List<Integer> categories) {
		this.categories = categories;
	}
	public List<Integer> getBrands() {
		return brands;
	}
	public void setBrands(List<Integer> brands) {
		this.brands = brands;
	}
}
