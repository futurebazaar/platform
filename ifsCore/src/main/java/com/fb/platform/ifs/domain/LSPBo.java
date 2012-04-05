/**
 * 
 */
package com.fb.platform.ifs.domain;

import java.io.Serializable;

/**
 * @author sarvesh
 *
 */
public class LSPBo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String zipGroupId;
	String zipGroupCode;
	String lspId;
	String lspCode;
	boolean isCod;
	boolean isHighValue;
	int lspPriority;
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
	 * @return the lspId
	 */
	public String getLspId() {
		return lspId;
	}
	/**
	 * @param lspId the lspId to set
	 */
	public void setLspId(String lspId) {
		this.lspId = lspId;
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
	
	@Override
	public String toString() {
		final String TAB = "||";
		String retValue = "";

		retValue = "lspCode = "+ this.lspCode + TAB + "lspId = " + this.lspId + TAB + "lspPriority = " + this.lspPriority + TAB + 
				   "zipGroupCode = " + this.zipGroupCode + TAB + "zipGroupId = " + this.zipGroupId + TAB + "isCod = " + this.isCod + TAB +
				   "isHighValue = " + this.isHighValue;
		return retValue;
	}
	/**
	 * @return the isCod
	 */
	public boolean isCod() {
		return isCod;
	}
	/**
	 * @param isCod the isCod to set
	 */
	public void setCod(boolean isCod) {
		this.isCod = isCod;
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
}
