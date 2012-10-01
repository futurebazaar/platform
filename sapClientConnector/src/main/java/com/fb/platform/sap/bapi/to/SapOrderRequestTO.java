package com.fb.platform.sap.bapi.to;

import java.util.ArrayList;
import java.util.List;

import com.fb.commons.mom.to.AddressTO;
import com.fb.commons.mom.to.LineItemTO;
import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.commons.mom.to.PaymentTO;
import com.fb.platform.sap.bapi.order.TinlaOrderType;

public class SapOrderRequestTO {
	
	private TinlaOrderType orderType;
	private OrderHeaderTO orderHeaderTO;
	private List<LineItemTO> lineItemTO = new ArrayList<LineItemTO>();
	private List<PaymentTO> paymentTO = new ArrayList<PaymentTO>();
	private AddressTO billingAddressTO;
	private AddressTO defaultShippingAddressTO;
	
	public TinlaOrderType getOrderType() {
		return orderType;
	}
	public void setOrderType(TinlaOrderType orderType) {
		this.orderType = orderType;
	}
	public OrderHeaderTO getOrderHeaderTO() {
		return orderHeaderTO;
	}
	public void setOrderHeaderTO(OrderHeaderTO orderHeaderTO) {
		this.orderHeaderTO = orderHeaderTO;
	}
	public List<LineItemTO> getLineItemTO() {
		return lineItemTO;
	}
	public void setLineItemTO(List<LineItemTO> lineItemTO) {
		this.lineItemTO = lineItemTO;
	}
	public List<PaymentTO> getPaymentTO() {
		return paymentTO;
	}
	public void setPaymentTO(List<PaymentTO> paymentTO) {
		this.paymentTO = paymentTO;
	}
	public AddressTO getBillingAddressTO() {
		return billingAddressTO;
	}
	public void setBillingAddressTO(AddressTO billingAddressTO) {
		this.billingAddressTO = billingAddressTO;
	}
	public AddressTO getDefaultShippingAddressTO() {
		return defaultShippingAddressTO;
	}
	public void setDefaultShippingAddressTO(AddressTO defaultShippingAddressTO) {
		this.defaultShippingAddressTO = defaultShippingAddressTO;
	}
	
	@Override
	public String toString() {
		return "SapOrderRequestTO [orderType=" + orderType + ", orderHeaderTO="
				+ orderHeaderTO + ", lineItemTO=" + lineItemTO + ", paymentTO="
				+ paymentTO + ", billingAddressTO=" + billingAddressTO
				+ ", defaultShippingAddressTO=" + defaultShippingAddressTO
				+ "]";
	}
	
}
