/**
 * 
 */
package com.fb.platform.ifs.to.lsp;

/**
 * @author vinayak
 *
 */
public class DeliveryCenter {

	private int id;
	private String code;
	private String type;
	private int sellerId;
	private boolean codFlag = false;
	private String pincode = null;
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setCodFlag(boolean codFlag) {
		this.codFlag = codFlag;
	}

	public boolean isCodFlag() {
		return codFlag;
	}

	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}

	public int getSellerId() {
		return sellerId;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getPincode() {
		return pincode;
	}
	
}
