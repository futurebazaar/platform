/**
 * 
 */
package com.fb.platform.promotion.to;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author vinayak
 *
 */
public class Product {

	private int productId = 0;
	private List<Integer> categories = null;
	private List<Integer> brands = null;
	//price is offer price
	private BigDecimal price = null;
	private BigDecimal mrpPrice = null;
	//this is the price that will be offered to the customer after calculating auto promotion discounts
	private BigDecimal discountedPrice = null;

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
	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getMrpPrice() {
		return mrpPrice;
	}
	public void setMrpPrice(BigDecimal mrpPrice) {
		this.mrpPrice = mrpPrice;
	}

	public BigDecimal getDiscountedPrice() {
		return discountedPrice;
	}
	public void setDiscountedPrice(BigDecimal discountedPrice) {
		this.discountedPrice = discountedPrice;
	}
	public boolean isProductOfCategory(List<Integer> catList){
		return catList.containsAll(this.categories);
	}
	
	public boolean isProductOfBrand(List<Integer> brandList){
		return brandList.containsAll(this.brands);
	}
	public boolean isProductApplicable(List<Integer> productList) {
		return productList.contains(productId);
	}
}
