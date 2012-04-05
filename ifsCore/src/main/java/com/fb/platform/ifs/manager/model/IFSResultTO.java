/**
 * 
 */
package com.fb.platform.ifs.manager.model;

import com.fb.platform.ifs.util.Jsonizable;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 
 * @author sarvesh
 *
 */
public class IFSResultTO implements Jsonizable {
	
	private static final Gson gson = new Gson();
	
	String articleId;
	
	String productGroup;
	
	boolean isHighValue;

	String primaryDCLSP;

	String dcLspSequence;
	
	String dcStockString;
	
	String dcPhysicalStockString;

	//LSPPlantDataBean lspInfo;

	int totalDeliveryTime;
	
	int deliveryTime;
	
	int inventoryTime;

	boolean isAllQuantityFulfilled;

	boolean isShipLocalOnly;
	
	boolean isInvCheck;
	
	Long totalQuantityFound;

	String modeOfTransport;
	
	String flfMessages;
	
	String defaultDC;
	
	boolean isBackorderable;
	
	String shippingTimeMap;
	
	String processingTimeForVIMap;
	
	String orderTypeMap;
	
	String processingTimeForPI;
	
	boolean isThirdPartyProduct;
 
	public boolean isThirdPartyProduct() {
		return isThirdPartyProduct;
	}
	public void setThirdPartyProduct(boolean isThirdPartyProduct) {
		this.isThirdPartyProduct = isThirdPartyProduct;
	}
	public String getDcLspSequence() {
		return dcLspSequence;
	}
	public void setDcLspSequence(String dcLspSequence) {
		this.dcLspSequence = dcLspSequence;
	}
	public String getFlfMessages() {
		return flfMessages;
	}
	public void setFlfMessages(String flfMessages) {
		this.flfMessages = flfMessages;
	}
	public boolean isAllQuantityFulfilled() {
		return isAllQuantityFulfilled;
	}
	public void setAllQuantityFulfilled(boolean isAllQuantityFulfilled) {
		this.isAllQuantityFulfilled = isAllQuantityFulfilled;
	}
	public boolean isInvCheck() {
		return isInvCheck;
	}
	public void setInvCheck(boolean isInvCheck) {
		this.isInvCheck = isInvCheck;
	}
	public boolean isShipLocalOnly() {
		return isShipLocalOnly;
	}
	public void setShipLocalOnly(boolean isShipLocalOnly) {
		this.isShipLocalOnly = isShipLocalOnly;
	}
	public String getPrimaryDCLSP() {
		return primaryDCLSP;
	}
	public void setPrimaryDCLSP(String primaryDCId) {
		this.primaryDCLSP = primaryDCId;
	}
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	public int getTotalDeliveryTime() {
		return totalDeliveryTime;
	}
	public void setTotalDeliveryTime(int totalDeliveryTime) {
		this.totalDeliveryTime = totalDeliveryTime;
	}
	public Long getTotalQuantityFound() {
		return totalQuantityFound;
	}
	public void setTotalQuantityFound(Long totalQuantityFound) {
		this.totalQuantityFound = totalQuantityFound;
	}
	public String getModOfTransport() {
		return modeOfTransport;
	}
	public void setModOfTransport(String modOfTransport) {
		this.modeOfTransport = modOfTransport;
	}
	
	@Override
	public JsonObject toJson() throws Exception {
		String jsonString = gson.toJson(this);
		JsonParser parser = new JsonParser();
		JsonObject jsonObject = (JsonObject) parser.parse(jsonString);
		return jsonObject;
	}
	/**
	 * @return the productGroup
	 */
	public String getProductGroup() {
		return productGroup;
	}
	/**
	 * @param productGroup the productGroup to set
	 */
	public void setProductGroup(String productGroup) {
		this.productGroup = productGroup;
	}
	/**
	 * @return the isHighValue
	 */
	public boolean isHighValue() {
		return isHighValue;
	}
	/**
	 * @param isHighValue the isHighValue to set
	 */
	public void setHighValue(boolean isHighValue) {
		this.isHighValue = isHighValue;
	}
	/**
	 * @return the dcStockString
	 */
	public String getDcStockString() {
		return dcStockString;
	}
	/**
	 * @param dcStockString the dcStockString to set
	 */
	public void setDcStockString(String dcStockString) {
		this.dcStockString = dcStockString;
	}
	/**
	 * @return the dcPhysicalStockString
	 */
	public String getDcPhysicalStockString() {
		return dcPhysicalStockString;
	}
	/**
	 * @param dcPhysicalStockString the dcPhysicalStockString to set
	 */
	public void setDcPhysicalStockString(String dcPhysicalStockString) {
		this.dcPhysicalStockString = dcPhysicalStockString;
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
	 * @return the defaultDC
	 */
	public String getDefaultDC() {
		return defaultDC;
	}
	/**
	 * @param defaultDC the defaultDC to set
	 */
	public void setDefaultDC(String defaultDC) {
		this.defaultDC = defaultDC;
	}
	/**
	 * @return the isBackorderable
	 */
	public boolean isBackorderable() {
		return isBackorderable;
	}
	/**
	 * @param isBackorderable the isBackorderable to set
	 */
	public void setBackorderable(boolean isBackorderable) {
		this.isBackorderable = isBackorderable;
	}
	/**
	 * @return the shippingTimeMap
	 */
	public String getShippingTimeMap() {
		return shippingTimeMap;
	}
	/**
	 * @param shippingTimeMap the shippingTimeMap to set
	 */
	public void setShippingTimeMap(String shippingTimeMap) {
		this.shippingTimeMap = shippingTimeMap;
	}
	/**
	 * @return the processingTimeForVIMap
	 */
	public String getProcessingTimeForVIMap() {
		return processingTimeForVIMap;
	}
	/**
	 * @param processingTimeForVIMap the processingTimeForVIMap to set
	 */
	public void setProcessingTimeForVIMap(String processingTimeForVIMap) {
		this.processingTimeForVIMap = processingTimeForVIMap;
	}
	/**
	 * @return the orderTypeMap
	 */
	public String getOrderTypeMap() {
		return orderTypeMap;
	}
	/**
	 * @param orderTypeMap the orderTypeMap to set
	 */
	public void setOrderTypeMap(String orderTypeMap) {
		this.orderTypeMap = orderTypeMap;
	}
	/**
	 * @return the processingTimeForPI
	 */
	public String getProcessingTimeForPI() {
		return processingTimeForPI;
	}
	/**
	 * @param processingTimeForPI the processingTimeForPI to set
	 */
	public void setProcessingTimeForPI(String processingTimeForPI) {
		this.processingTimeForPI = processingTimeForPI;
	}
}
