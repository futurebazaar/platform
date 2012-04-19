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
public class OrderItem {

	private Product product = null;
	private int quantity = 0;

	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getPrice() {
		return (product.getPrice()).multiply(new BigDecimal(quantity));
	}
	
	public boolean isOrderItemInCategory(List<Integer> categories){
		return product.isProductOfCategory(categories);
	}
	
	public boolean isOrderItemInBrand(List<Integer> brands){
		return product.isProductOfBrand(brands);
	}
	
	public boolean isProductPresent(int productId){
		return (this.product.getProductId() == productId);
	}
	
	public BigDecimal getProductPrice(){
		return product.getPrice();
	}
	
}