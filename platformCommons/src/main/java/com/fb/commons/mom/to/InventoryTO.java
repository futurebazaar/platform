/**
 * 
 */
package com.fb.commons.mom.to;

import java.io.Serializable;

/**
 * @author nehaga
 *
 */
public class InventoryTO implements Serializable {
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
		String inventory = "transactionCode:" + transactionCode
				+ "\narticleId:" + articleId
				+ "\nissuingSite:" + issuingSite
				+ "\nreceivingSite:" + receivingSite
				+ "\nissuingStorageLoc:" + issuingStorageLoc
				+ "\nreceivingStorageLoc:" + receivingStorageLoc
				+ "\nmovementType:"+ movementType
				+ "\nsellingUnit:"+ sellingUnit
				+ "\nquantity:" + quantity;
		if(sapIdoc != null) {
			inventory += "\n" + sapIdoc.toString();
		}
		return inventory;
		
	}
		   
		   
}
