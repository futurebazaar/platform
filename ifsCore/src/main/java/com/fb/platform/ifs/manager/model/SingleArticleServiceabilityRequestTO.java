/**
 * 
 */
package com.fb.platform.ifs.manager.model;


/**
 * 
 * @author sarvesh
 *
 */
public class SingleArticleServiceabilityRequestTO {

    private String articleId;    
    private String pincode;
    private int isCod;
    private double itemPrice;
    private String rateChartId;
    private int qty;
    private String client;
    private String vendorId;
    private String sessionToken = null;

    public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	/**
	 * @return the articleId
	 */
	public String getArticleId() {
		return articleId;
	}
	/**
	 * @param articleId the articleId to set
	 */
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	/**
	 * @return the pincode
	 */
	public String getPincode() {
		return pincode;
	}
	/**
	 * @param pincode the pincode to set
	 */
	public void setPincode(String pincode) {
		System.out.println("IFSTO.setPincode() :: "+pincode);
		this.pincode = pincode;
	}
	/**
	 * @return the isCod
	 */
	public int getIsCod() {
		return isCod;
	}
	/**
	 * @param isCod the isCod to set
	 */
	public void setIsCod(int isCod) {
		System.out.println("IFSTO.setCod() :: "+isCod);
		this.isCod = isCod;
	}
	/**
	 * @return the itemPrice
	 */
	public double getItemPrice() {
		return itemPrice;
	}
	/**
	 * @param itemPrice the itemPrice to set
	 */
	public void setItemPrice(double itemPrice) {
		this.itemPrice = itemPrice;
	}
	/**
	 * @return the rateChartId
	 */
	public String getRateChartId() {
		return rateChartId;
	}
	/**
	 * @param rateChartId the rateChartId to set
	 */
	public void setRateChartId(String rateChartId) {
		this.rateChartId = rateChartId;
	}
	/**
	 * @return the qty
	 */
	public int getQty() {
		return qty;
	}
	/**
	 * @param qty the qty to set
	 */
	public void setQty(int qty) {
		this.qty = qty;
	}
	/**
	 * @return the client
	 */
	public String getClient() {
		return client;
	}
	/**
	 * @param client the client to set
	 */
	public void setClient(String client) {
		this.client = client;
	}

}
