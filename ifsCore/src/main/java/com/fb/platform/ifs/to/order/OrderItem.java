package com.fb.platform.ifs.to.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fb.platform.ifs.service.DCManager;
import com.fb.platform.ifs.to.lsp.DeliveryCenter;

public class OrderItem {

	private int id;
	private int sellerRateChartId;
	private String articleId;
	private int sellerId;
	private int quantity;
	private BigDecimal price = null;
	
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setSellerRateChartId(int sellerRateChartId) {
		this.sellerRateChartId = sellerRateChartId;
	}

	public int getSellerRateChartId() {
		return sellerRateChartId;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	
	public String getArticleId() {
		return articleId;
	}
	
	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}
	
	public int getSellerId() {
		return sellerId;
	}
	
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public BigDecimal getPrice() {
		return price;
	}
	
	//Returns list of DCs in the priority order which contains stock >= orderitem quantity
	public List<DeliveryCenter> getAvailability(){
		List<DeliveryCenter> dcs = new ArrayList<DeliveryCenter>();
		return dcs;
	}
}

