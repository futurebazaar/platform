/**
 * 
 */
package com.fb.commons.mom.to;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author nehaga
 *
 */
public class InventoryTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3989991060720328001L;
	
	
	private SapMomTO sapIdoc;
	private String transactionCode;
	private String articleId;
	private String issuingSite;
	private String receivingSite;
	private String issuingStorageLoc;
	private String receivingStorageLoc;
	private String movementType;
	private String quantity;
	private String sellingUnit;
	
	public String getTransactionCode() {
		return transactionCode;
	}
	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	public String getIssuingSite() {
		return issuingSite;
	}
	public void setIssuingSite(String issuingSite) {
		this.issuingSite = issuingSite;
	}
	public String getReceivingSite() {
		return receivingSite;
	}
	public void setReceivingSite(String receivingSite) {
		this.receivingSite = receivingSite;
	}
	public String getIssuingStorageLoc() {
		return issuingStorageLoc;
	}
	public void setIssuingStorageLoc(String issuingStorageLoc) {
		this.issuingStorageLoc = issuingStorageLoc;
	}
	public String getReceivingStorageLoc() {
		return receivingStorageLoc;
	}
	public void setReceivingStorageLoc(String receivingStorageLoc) {
		this.receivingStorageLoc = receivingStorageLoc;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	
	public String getMovementType() {
		return movementType;
	}
	public void setMovementType(String movementType) {
		this.movementType = movementType;
	}
	public String getSellingUnit() {
		return sellingUnit;
	}
	public void setSellingUnit(String sellingUnit) {
		this.sellingUnit = sellingUnit;
	}
	public SapMomTO getSapIdoc() {
		return sapIdoc;
	}
	public void setSapIdoc(SapMomTO sapIdoc) {
		this.sapIdoc = sapIdoc;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
		.append("sapIdoc", this.sapIdoc)
		.append("articleId", this.articleId)
		.append("issuingSite", this.issuingSite)
		.append("issuingStorageLoc", this.issuingStorageLoc)
		.append("movementType", this.movementType)
		.append("quantity", this.quantity)
		.append("receivingSite", this.receivingSite)
		.append("receivingStorageLoc", this.receivingStorageLoc)
		.append("sellingUnit", this.sellingUnit)
		.append("transactionCode", this.transactionCode)
		.toString();
	}
		   
		   
}
