/**
 * 
 */
package com.fb.platform.promotion.to;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.util.ListUtil;

/**
 * @author vinayak
 *
 */
public class OrderRequest implements Serializable {
 
	private int orderId = 0;
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();
	private int clientId = 0;
	private int noOfTimesInMonth = 0;
	
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public BigDecimal getOrderValue() {
		BigDecimal orderValue = new BigDecimal(0);
		for(OrderItem oItem : orderItems){
			orderValue = orderValue.add(oItem.getPrice());
		}
				return orderValue;
	}
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	
	public boolean isAllProductsInCategory(List<Integer> categories){
		for(OrderItem o:orderItems){
			if(!o.isOrderItemInCategory(categories)){
				return false;
			}
		}
		return true;
	}
	
	public boolean isAnyProductInCategory(List<Integer> categories){
		for(OrderItem o:orderItems){
			if(o.isOrderItemInCategory(categories)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isValidClient(List<Integer> clientList){
		return clientList.contains(Integer.valueOf(clientId));
	}
	
	public boolean isAllProductsInBrand(List<Integer> brands){
		for(OrderItem o:orderItems){
			if(!o.isOrderItemInBrand(brands)){
				return false;
			}
		}
		return true;
	}
	
	public boolean isAnyProductInBrand( List<Integer> brands){
		for(OrderItem o:orderItems){
			if(o.isOrderItemInBrand(brands)){
				return true;
			}
		}
		return false;
	}
	
	public boolean hasProduct(List<Integer> products) {
		for(Integer productId : products) {
			if(isProductPresent(productId)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isProductPresent(int productId){
		for(OrderItem o:orderItems){
			if(o.isProductPresent(productId)){
				return true;
			}
		}
		return false;
	}

	public Money getOrderValueForCategory(List<Integer> catList){
		Money orderValueForCategoryProducts = new Money(new BigDecimal(0)); 
		for(OrderItem o:orderItems){
			if(o.isOrderItemInCategory(catList)){
				orderValueForCategoryProducts = orderValueForCategoryProducts.plus(new Money(o.getPrice()));
			}
		}
		return orderValueForCategoryProducts;
	}
	
	public Money getOrderValueForBrand(List<Integer> brandList){
		Money orderValueForBrandProducts = new Money(new BigDecimal(0)); 
		for(OrderItem o:orderItems){
			if(o.isOrderItemInBrand(brandList)){
				orderValueForBrandProducts = orderValueForBrandProducts.plus(new Money(o.getPrice()));
			}
		}
		return orderValueForBrandProducts;
	}
	
	public Money getProductPrice(int productId){
		for(OrderItem o:orderItems){
			if(o.isProductPresent(productId)){
				return new Money(o.getProductPrice());
			}
		}
		//TODO : Change to throw exception accordingly
		return new Money(new BigDecimal(0));
	}
	
	public Money getOrderValueForRelevantProducts(List<Integer> brandList,List<Integer> catIncludeList,List<Integer> catExcludeList){
		Money orderValueForBrandProducts = new Money(new BigDecimal(0)); 
		for(OrderItem o:orderItems){
			if( (!ListUtil.isValidList(brandList)|| o.isOrderItemInBrand(brandList))
					&& (!ListUtil.isValidList(catIncludeList) || o.isOrderItemInCategory(catIncludeList))
					&&  (!ListUtil.isValidList(catExcludeList) || !o.isOrderItemInCategory(catExcludeList))){
				orderValueForBrandProducts = orderValueForBrandProducts.plus(new Money(o.getPrice()));
			}
		}
		return orderValueForBrandProducts;
	}
	
	public Money getOrderValue(List<Integer> brandList,List<Integer> catIncludeList,List<Integer> catExcludeList, List<Integer> productList) {
		Money orderValue = new Money(new BigDecimal(0));
		for(OrderItem o:orderItems){
			if((!ListUtil.isValidList(brandList) || o.isOrderItemInBrand(brandList)) 
					&& (!ListUtil.isValidList(catIncludeList) || o.isOrderItemInCategory(catIncludeList))
					&& (!ListUtil.isValidList(catExcludeList) || !o.isOrderItemInCategory(catExcludeList))
					&& (!ListUtil.isValidList(productList) || o.isOrderItemOfProduct(productList))) {
				orderValue = orderValue.plus(new Money(o.getPrice()));
			}
		}
		return orderValue;
	}
	
	public int getOrderQuantityForBrand(List<Integer> brandList){
		int count = 0; 
		for(OrderItem o:orderItems){
			if(o.isOrderItemInBrand(brandList)){
				count++;
			}
		}
		return count;
	}
	
	public int getOrderQuantityForRelevantProducts(List<Integer> brandList, List<Integer> catIncludeList, List<Integer> catExcludeList) {
		int count = 0; 
		for(OrderItem o: orderItems) {
			if( (!ListUtil.isValidList(brandList)|| o.isOrderItemInBrand(brandList))
					&& (!ListUtil.isValidList(catIncludeList) || o.isOrderItemInCategory(catIncludeList))
					&&  (!ListUtil.isValidList(catExcludeList) || !o.isOrderItemInCategory(catExcludeList))){
				count=count+o.getQuantity();
			}
		}
		return count;
	}
	
	public int getNoOfTimesInMonth() {
		
		
		
		return noOfTimesInMonth;
	}
	
	
	public void setNoOfTimesInMonth(int noOfTimesInMonth) {
		this.noOfTimesInMonth = noOfTimesInMonth;
	}

	public void resetDiscountedPrice() {
		for (OrderItem orderItem : orderItems) {
			orderItem.getProduct().setDiscountedPrice(null);
		}
	}
}
