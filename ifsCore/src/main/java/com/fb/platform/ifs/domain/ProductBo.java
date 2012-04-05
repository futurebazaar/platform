package com.fb.platform.ifs.domain;

import java.io.Serializable;

/**
 * 
 * @author sarvesh
 */
public class ProductBo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String articleId;
	String productGroup;
	boolean shipLocalOnly;
	String shippingMode;
	boolean highValueFlag;
	int thresholdAmt;
	
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
	 * @return the shipLocalOnly
	 */
	public boolean isShipLocalOnly() {
		return shipLocalOnly;
	}
	/**
	 * @param shipLocalOnly the shipLocalOnly to set
	 */
	public void setShipLocalOnly(boolean shipLocalOnly) {
		this.shipLocalOnly = shipLocalOnly;
	}
	/**
	 * @return the shippingMode
	 */
	public String getShippingMode() {
		return shippingMode;
	}
	/**
	 * @param shippingMode the shippingMode to set
	 */
	public void setShippingMode(String shippingMode) {
		this.shippingMode = shippingMode;
	}
	/**
	 * @return the highValueFlag
	 */
	public boolean isHighValueFlag() {
		return highValueFlag;
	}
	/**
	 * @param highValueFlag the highValueFlag to set
	 */
	public void setHighValueFlag(boolean highValueFlag) {
		this.highValueFlag = highValueFlag;
	}
	/**
	 * @return the thresholdAmt
	 */
	public int getThresholdAmt() {
		return thresholdAmt;
	}
	/**
	 * @param thresholdAmt the thresholdAmt to set
	 */
	public void setThresholdAmt(int thresholdAmt) {
		this.thresholdAmt = thresholdAmt;
	}
	
	@Override
	public String toString() {
		final String TAB = "||";
		String retValue = "";

		retValue = "articleId = "+ this.articleId + TAB + "productGroup = " + this.productGroup + TAB + "shippingMode = " + this.shippingMode + TAB + 
				   "threshold = " + this.thresholdAmt + TAB + "highValueFlag = " + this.highValueFlag + TAB + "shipLocalOnly = " + this.shipLocalOnly;
		return retValue;
	}
	
	
	
	
}
