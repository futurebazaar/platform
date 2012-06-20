package com.fb.platform.shipment.to;

import java.math.BigDecimal;

import com.fb.commons.to.Money;


/**
 * @author nehaga
 *
 */
public class GatePassItem {
	
	private String orderReferenceId;
    private String delNo;
    private String deece;
    private String awbNo;
    private int quantity;
    private String itemDescription;
    private BigDecimal delWt;
    
	public String getOrderReferenceId() {
		return orderReferenceId;
	}




	public void setOrderReferenceId(String orderReferenceId) {
		this.orderReferenceId = orderReferenceId;
	}




	public String getDelNo() {
		return delNo;
	}




	public void setDelNo(String delNo) {
		this.delNo = delNo;
	}




	public String getDeece() {
		return deece;
	}




	public void setDeece(String deece) {
		this.deece = deece;
	}




	public String getAwbNo() {
		return awbNo;
	}




	public void setAwbNo(String awbNo) {
		this.awbNo = awbNo;
	}




	public int getQuantity() {
		return quantity;
	}




	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}




	public String getItemDescription() {
		return itemDescription;
	}




	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}




	public BigDecimal getDelWt() {
		return delWt;
	}




	public void setDelWt(BigDecimal delWt) {
		this.delWt = delWt;
	}




	@Override
	public String toString() {
		return 	"order refernce id : " + getOrderReferenceId() + "\n" +
				"deece : " + getDeece() + "\n" +
				"delno : " + getDelNo() + "\n" +
				"deldt : " + getDelWt() + "\n" +
				"quantity : " + getQuantity() + "\n" +
				"item description : " + getItemDescription() + "\n" +
				"awbno : " + getAwbNo() ;
		
	}
    

}
