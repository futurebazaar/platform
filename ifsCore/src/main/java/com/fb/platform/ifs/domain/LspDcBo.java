/**
 * 
 */
package com.fb.platform.ifs.domain;


/**
 * @author sarvesh
 *
 */
public class LspDcBo implements Comparable<LspDcBo> {
	String zipGroupId;
	String zipGroupCode;
	String lspId;
	String dcId;
	String dcCode;
	String lspCode;
	int stockLevel;
	int deliveryTime;
	int inventoryTime;
	int totalDeliveryTime;
	int lspPriority;
	String processingTimeForVI;
	String orderType;
	int dcPriority;
	
	/**
	 * @return the zipGroupId
	 */
	public String getZipGroupId() {
		return zipGroupId;
	}
	/**
	 * @param zipGroupId the zipGroupId to set
	 */
	public void setZipGroupId(String zipGroupId) {
		this.zipGroupId = zipGroupId;
	}
	/**
	 * @return the lsp
	 */
	public String getLspId() {
		return lspId;
	}
	/**
	 * @param lspId the lsp to set
	 */
	public void setLspId(String lspId) {
		this.lspId = lspId;
	}
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
	 * @return the deliveryTime
	 */
	public int getDeliveryTime() {
		return deliveryTime;
	}
	/**
	 * @param deliveryTime the deliveryTime to set
	 */
	public void setDeliveryTime(int deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	
	@Override
	public int compareTo(LspDcBo lspDcBo) {
		int result = 0;
		/*if((this.lspPriority > lspDcBo.getLspPriority()) || ((this.lspPriority == lspDcBo.getLspPriority()) && (this.totalDeliveryTime > lspDcBo.getTotalDeliveryTime())))
		{
			result = 1;
		}*/
		
		if((this.dcPriority > lspDcBo.getDcPriority()) || ((this.dcPriority == lspDcBo.getDcPriority()) && (this.totalDeliveryTime > lspDcBo.getTotalDeliveryTime())))
		{
			result = 1;
		}
		
		return result;
	}
	/**
	 * @return the lspPriority
	 */
	public int getLspPriority() {
		return lspPriority;
	}
	/**
	 * @param lspPriority the lspPriority to set
	 */
	public void setLspPriority(int lspPriority) {
		this.lspPriority = lspPriority;
	}
	/**
	 * @return the dcCode
	 */
	public String getDcCode() {
		return dcCode;
	}
	/**
	 * @param dcCode the dcCode to set
	 */
	public void setDcCode(String dcCode) {
		this.dcCode = dcCode;
	}
	/**
	 * @return the lspCode
	 */
	public String getLspCode() {
		return lspCode;
	}
	/**
	 * @param lspCode the lspCode to set
	 */
	public void setLspCode(String lspCode) {
		this.lspCode = lspCode;
	}
	/**
	 * @return the zipGroupCode
	 */
	public String getZipGroupCode() {
		return zipGroupCode;
	}
	/**
	 * @param zipGroupCode the zipGroupCode to set
	 */
	public void setZipGroupCode(String zipGroupCode) {
		this.zipGroupCode = zipGroupCode;
	}
	/**
	 * @return the inventoryTime
	 */
	public int getInventoryTime() {
		return inventoryTime;
	}
	/**
	 * @param inventoryTime the inventoryTime to set
	 */
	public void setInventoryTime(int inventoryTime) {
		this.inventoryTime = inventoryTime;
	}
	/**
	 * @return the totalDeliveryTime
	 */
	public int getTotalDeliveryTime() {
		return totalDeliveryTime;
	}
	/**
	 * @param totalDeliveryTime the totalDeliveryTime to set
	 */
	public void setTotalDeliveryTime(int totalDeliveryTime) {
		this.totalDeliveryTime = totalDeliveryTime;
	}
	
	@Override
	public String toString() {
		final String TAB = "||";
		String retValue = "";

		retValue = "dcCode = "+ this.dcCode + TAB + "dcId = " + this.dcId + TAB + "deliveryTime = " + this.deliveryTime + TAB + 
				   "inventoryTime = " + this.inventoryTime + TAB + "lspCode = " + this.lspCode + TAB + "lspPriority = " + this.lspPriority + TAB + 
				   "stockLevel = " + this.stockLevel + TAB + "totalDeliveryTime = " + this.totalDeliveryTime + TAB + "zipGroupCode = " + this.zipGroupCode + TAB + 
				   "zipGroupId = " + this.zipGroupId + TAB + " orderType = " + this.orderType;
		return retValue;
	}
	/**
	 * @return the processingTimeForVI
	 */
	public String getProcessingTimeForVI() {
		return processingTimeForVI;
	}
	/**
	 * @param processingTimeForVI the processingTimeForVI to set
	 */
	public void setProcessingTimeForVI(String processingTimeForVI) {
		this.processingTimeForVI = processingTimeForVI;
	}
	/**
	 * @return the orderType
	 */
	public String getOrderType() {
		return orderType;
	}
	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	/**
	 * @return the dcPriority
	 */
	public int getDcPriority() {
		return dcPriority;
	}
	/**
	 * @param dcPriority the dcPriority to set
	 */
	public void setDcPriority(int dcPriority) {
		this.dcPriority = dcPriority;
	}
}
