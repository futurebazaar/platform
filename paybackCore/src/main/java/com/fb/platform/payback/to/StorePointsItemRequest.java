package com.fb.platform.payback.to;

import java.math.BigDecimal;

public class StorePointsItemRequest {
	private BigDecimal amount;
	private long id;
	private long articleId;
	private int quantity;
	private String departmentName;
	private long departmentCode;
	
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
	public long getArticleId() {
		return articleId;
	}
	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
