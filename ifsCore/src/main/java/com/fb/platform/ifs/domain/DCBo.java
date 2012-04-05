/**
 * 
 */
package com.fb.platform.ifs.domain;

import java.sql.Date;

/**
 * @author sarvesh
 *
 */
public class DCBo implements Comparable<DCBo> {
	String id;
	String invId;
	String dcId;
	int stockLevel;
	int physicalStock;
	int deltaDeliveryTime;
	boolean backOrderable;
	boolean madeToOrder;
	boolean preOrder;
	boolean cod;
	boolean localDC;
	int expectedInDays;
	String type;
	Date expectedOnDate;
	
	/**
	 * @return the dcId
	 */
	public String getDcId() {
		return dcId;
	}
	/**
	 * @param dcId the dcId to set
	 */
	public void setDcId(String dcId) {
		this.dcId = dcId;
	}
	/**
	 * @return the stockLevel
	 */
	public int getStockLevel() {
		return stockLevel;
	}
	/**
	 * @param stockLevel the stockLevel to set
	 */
	public void setStockLevel(int stockLevel) {
		this.stockLevel = stockLevel;
	}
	/**
	 * @return the deltaDeliveryTime
	 */
	public int getDeltaDeliveryTime() {
		return deltaDeliveryTime;
	}
	/**
	 * @param deltaDeliveryTime the deltaDeliveryTime to set
	 */
	public void setDeltaDeliveryTime(int deltaDeliveryTime) {
		this.deltaDeliveryTime = deltaDeliveryTime;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the physicalStock
	 */
	public int getPhysicalStock() {
		return physicalStock;
	}
	/**
	 * @param physicalStock the physicalStock to set
	 */
	public void setPhysicalStock(int physicalStock) {
		this.physicalStock = physicalStock;
	}
	public boolean isBackOrderable() {
		return backOrderable;
	}
	public void setBackOrderable(boolean backOrderable) {
		this.backOrderable = backOrderable;
	}
	public int getExpectedInDays() {
		return expectedInDays;
	}
	public void setExpectedInDays(int expectedInDays) {
		this.expectedInDays = expectedInDays;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the cod
	 */
	public boolean isCod() {
		return cod;
	}
	/**
	 * @param cod the cod to set
	 */
	public void setCod(boolean cod) {
		this.cod = cod;
	}
	/**
	 * @return the localDC
	 */
	public boolean isLocalDC() {
		return localDC;
	}
	/**
	 * @param localDC the localDC to set
	 */
	public void setLocalDC(boolean localDC) {
		this.localDC = localDC;
	}
	
	@Override
	public int compareTo(DCBo dcBo) {
		int result = 0;
		if((this.getStockLevel() < dcBo.getStockLevel()))
		{
			result = 1;
		}
		
		return result;
	}
	/**
	 * @return the expectedOnDate
	 */
	public Date getExpectedOnDate() {
		return expectedOnDate;
	}
	/**
	 * @param expectedOnDate the expectedOnDate to set
	 */
	public void setExpectedOnDate(Date expectedOnDate) {
		this.expectedOnDate = expectedOnDate;
	}
	/**
	 * @return the madeToOrder
	 */
	public boolean isMadeToOrder() {
		return madeToOrder;
	}
	/**
	 * @param madeToOrder the madeToOrder to set
	 */
	public void setMadeToOrder(boolean madeToOrder) {
		this.madeToOrder = madeToOrder;
	}
	/**
	 * @return the preOrder
	 */
	public boolean isPreOrder() {
		return preOrder;
	}
	/**
	 * @param preOrder the preOrder to set
	 */
	public void setPreOrder(boolean preOrder) {
		this.preOrder = preOrder;
	}
	
	@Override
	public String toString() {
		final String TAB = "||";
		String retValue = "";

		retValue = "dcId = "+ this.dcId + TAB + "deltaDeliveryTime = " + this.deltaDeliveryTime + TAB + "expectedInDays = " + this.expectedInDays + TAB + 
				   "physicalStock = " + this.physicalStock + TAB + "stockLevel = " + this.stockLevel + TAB + "type = " + this.type + TAB + 
				   "backOrderable = " + this.backOrderable + TAB + "expectedOnDate = " + this.expectedOnDate + TAB + "madeToOrder = " + this.madeToOrder + TAB + 
				   "preOrder = " + this.preOrder;
		return retValue;
	}
	/**
	 * @return the invId
	 */
	public String getInvId() {
		return invId;
	}
	/**
	 * @param invId the invId to set
	 */
	public void setInvId(String invId) {
		this.invId = invId;
	}
	
}
