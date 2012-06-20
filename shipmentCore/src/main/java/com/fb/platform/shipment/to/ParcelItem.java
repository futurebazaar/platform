package com.fb.platform.shipment.to;

import java.math.BigDecimal;

import com.fb.commons.to.Money;

/**
 * @author nehaga
 *
 *	Delivery No - fulfillment_shipment || delivery_number,  
 *	Name of the customer - orders_billinginfo || first_name + last_name (order_id)
 *	Address,  
 *	city, 
 *	state, 
 *	country, 
 *	pincode, 
 *	phone    number, 
 *	price - orders_order || payable_amount
 *	article discription, 
 *	weight, 
 *	quantity, 
 *	site - fulfillment_shipment || dc_id
 *	payment mode - orders_order || payment_mode 
 *	AWB no. - fulfillment_shipment || tracking_number
 *
 */
public class ParcelItem {
	private String deliveryNumber = " ";
	private String customerName = " ";
	private String address = " ";
	private String city = " ";
	private String state = " ";
	private String country = " ";
	private String pincode;
	private long phoneNumber;
	private Money amountPayable;
	private String articleDescription = " ";
	private BigDecimal weight;
	private int quantity;
	private String deliverySiteId;
	private String paymentMode = " ";
	private String trackingNumber = " ";
	
	public String getDeliveryNumber() {
		return deliveryNumber;
	}
	public void setDeliveryNumber(String deliveryNumber) {
		this.deliveryNumber = deliveryNumber.replace("\r\n", " ");
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName.replace("\r\n", " ");
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address.replace("\r\n", " ");
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city.replace("\r\n", " ");
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state.replace("\r\n", " ");
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country.replace("\r\n", " ");
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public long getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Money getAmountPayable() {
		return amountPayable;
	}
	public void setAmountPayable(Money amountPayable) {
		this.amountPayable = amountPayable;
	}
	public String getArticleDescription() {
		return articleDescription;
	}
	public void setArticleDescription(String articleDescription) {
		this.articleDescription = articleDescription.replace("\r\n", " ");
	}
	public BigDecimal getWeight() {
		return weight;
	}
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getDeliverySiteId() {
		return deliverySiteId;
	}
	public void setDeliverySiteId(String deliverySiteId) {
		this.deliverySiteId = deliverySiteId;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode.replace("\r\n", " ");
	}
	public String getTrackingNumber() {
		return trackingNumber;
	}
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber.replace("\r\n", " ");
	}
	
	@Override
	public String toString() {
		return  "deliveryNumber : " + this.getDeliveryNumber() + "\n" +
				"customerName : " + this.getCustomerName() + "\n" +
				"address : " + this.getAddress() + "\n" +
				"city : " + this.getCity() + "\n" +
				"state : " + this.getState() + "\n" +
				"country : " + this.getCountry() + "\n" +
				"pincode : " + this.getPincode() + "\n" +
				"phoneNumber : " + this.getPhoneNumber() + "\n" +
				"amountPayable : " + this.getAmountPayable() + "\n" +
				"articleDescription : " + this.getArticleDescription() + "\n" +
				"weight : " + this.getWeight() + "\n" +
				"quantity : " + this.getQuantity() + "\n" +
				"deliverySiteId : " + this.getDeliverySiteId() + "\n" +
				"paymentMode : " + this.getPaymentMode() + "\n" +
				"trackingNumber : " + this.getTrackingNumber();
				
	}
}
