/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fb.platform.ifs.domain;


/**
 * 
 * @author Sarvesh
 */
public class IFSResultBo {
	
	String articleId;

	String primaryDCLSP;

	String dcLspSequenceString;

	Long totalDeliveryTime;

	boolean isAllQuantityFulfilled;

	boolean isShipLocalOnly;
	
	boolean isInvCheck;
	
	Long totalQuantityFound;

	String modeOfTransport;
	
	String flfMessages;
    
 
	public String getDcSequenceString() {
		return dcLspSequenceString;
	}
	public void setDcSequenceString(String dcSequenceString) {
		this.dcLspSequenceString = dcSequenceString;
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
	public String getPrimaryDCId() {
		return primaryDCLSP;
	}
	public void setPrimaryDCId(String primaryDCId) {
		this.primaryDCLSP = primaryDCId;
	}
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	public Long getTotalDeliveryTime() {
		return totalDeliveryTime;
	}
	public void setTotalDeliveryTime(Long totalDeliveryTime) {
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

}
