package com.fb.platform.payback.to;

import java.math.BigDecimal;

public class OrderItemRequest {
	private BigDecimal amount;
	private long id;
	private String articleId;
	private int quantity;
	private String departmentName;
	private long departmentCode;
	private long categoryId;
	
	public long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public long getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(long departmentCode) {
		this.departmentCode = departmentCode;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
