package com.fb.platform.shipment.to;

import java.math.BigDecimal;

import com.fb.commons.to.Money;


/**
 * @author nehaga
 *
 */
public class GatePassItem {
	
    private String delNo;
    private BigDecimal delWt;
    private String name;
    private String address;
    private String city;
    private String region;
    private String pincode;
    private long telnumber;
    private String itemDescription;
    private Money amount;
    private int deece;
    private String awbNo;
    private String payMod;

	public String getDelNo() {
		return delNo;
	}



	public void setDelNo(String delNo) {
		this.delNo = delNo;
	}



	public BigDecimal getDelWt() {
		return delWt;
	}



	public void setDelWt(BigDecimal delWt) {
		this.delWt = delWt;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}


	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public String getCity() {
		return city;
	}



	public void setCity(String city) {
		this.city = city;
	}



	public String getRegion() {
		return region;
	}



	public void setRegion(String region) {
		this.region = region;
	}



	public String getPincode() {
		return pincode;
	}



	public void setPincode(String pincode) {
		this.pincode = pincode;
	}



	public long getTelnumber() {
		return telnumber;
	}



	public void setTelnumber(long telnumber) {
		this.telnumber = telnumber;
	}



	public String getItemDescription() {
		return itemDescription;
	}



	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}



	public Money getAmount() {
		return amount;
	}



	public void setAmount(Money amount) {
		this.amount = amount;
	}



	public int getDeece() {
		return deece;
	}



	public void setDeece(int deece) {
		this.deece = deece;
	}



	public String getAwbNo() {
		return awbNo;
	}



	public void setAwbNo(String awbNo) {
		this.awbNo = awbNo;
	}



	public String getPayMod() {
		return payMod;
	}



	public void setPayMod(String payMod) {
		this.payMod = payMod;
	}



	@Override
	public String toString() {
		return 	"deece : " + getDeece() + "\n" +
				"delno : " + getDelNo() + "\n" +
				"deldt : " + getDelWt() + "\n" +
				"sonum : " + getName() + "\n" +
				"invno : " + getAddress() + "\n" +
				"invdt : " + getCity() + "\n" +
				"gtpas : " + getRegion() + "\n" +
				"awbno : " + getAwbNo() + "\n" +
				"sonum : " + getPincode() + "\n" +
				"invno : " + getTelnumber() + "\n" +
				"invdt : " + getItemDescription() + "\n" +
				"invdt : " + getPayMod() + "\n" +
				"gtpas : " + getAmount() + "\n" +
				"awbno : " + getAwbNo() ;
		
	}
    

}
