package com.fb.platform.payback.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class PointsItems implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	private String departmentName;
	private String articleId;
	private int quantity;
	private long departmentCode; //Will be long as it is product Id
	private BigDecimal itemAmount;
	private long itemId;
	private long pointsHeaderId;
	private String txnActionCode;
	private BigDecimal txnPoints;
	private BigDecimal earnRatio;
	private BigDecimal burnRatio;
	
	public BigDecimal getEarnRatio() {
		return earnRatio;
	}
	public void setEarnRatio(BigDecimal earnRatio) {
		this.earnRatio = earnRatio;
	}
	public BigDecimal getBurnRatio() {
		return burnRatio;
	}
	public void setBurnRatio(BigDecimal burnRatio) {
		this.burnRatio = burnRatio;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
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
	public long getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(long departmentCode) {
		this.departmentCode = departmentCode;
	}
	public BigDecimal getItemAmount() {
		return itemAmount;
	}
	public void setItemAmount(BigDecimal itemAmount) {
		this.itemAmount = itemAmount;
	}
	public long getItemId() {
		return itemId;
	}
	public void setItemId(long itemId) {
		this.itemId = itemId;
	}
	
	public long getPointsHeaderId() {
		return pointsHeaderId;
	}
	public void setPointsHeaderId(long pointsHeaderId) {
		this.pointsHeaderId = pointsHeaderId;
	}
	public String getTxnActionCode() {
		return txnActionCode;
	}
	public void setTxnActionCode(String txnActionCode) {
		this.txnActionCode = txnActionCode;
	}
	public BigDecimal getTxnPoints() {
		return txnPoints;
	}
	public void setTxnPoints(BigDecimal txnPoints) {
		this.txnPoints = txnPoints;
	}
}
