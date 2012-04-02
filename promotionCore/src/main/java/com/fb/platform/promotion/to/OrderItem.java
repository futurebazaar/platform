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
	private BigDecimal price = null;

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
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public boolean isOrderItemInCategory(List<Integer> categories){
		return product.isProductOfCategory(categories);
	}
	
}