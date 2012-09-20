package com.fb.platform.sap.bapi.to;

import java.util.ArrayList;
import java.util.List;

import com.fb.commons.mom.to.AddressTO;
import com.fb.commons.mom.to.LineItemTO;
import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.commons.mom.to.PaymentTO;
import com.fb.commons.mom.to.PricingTO;
import com.fb.platform.sap.bapi.order.table.TinlaOrderType;

public class SapOrderRequestTO {
	
	private TinlaOrderType orderType;
	private OrderHeaderTO orderHeaderTO;
	private List<LineItemTO> lineItemTO = new ArrayList<LineItemTO>();
	private List<PaymentTO> paymentTO = new ArrayList<PaymentTO>();
	private AddressTO addressTO;
	private PricingTO pricingTO;
	
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
	public AddressTO getAddressTO() {
		return addressTO;
	}
	public void setAddressTO(AddressTO addressTO) {
		this.addressTO = addressTO;
	}
	public PricingTO getPricingTO() {
		return pricingTO;
	}
	public void setPricingTO(PricingTO pricingTO) {
		this.pricingTO = pricingTO;
	}
	
	@Override
	public String toString() {
		return "SapOrderRequestTO [orderType=" + orderType + ", orderHeaderTO="
				+ orderHeaderTO + ", lineItemTO=" + lineItemTO + ", paymentTO="
				+ paymentTO + ", addressTO=" + addressTO + ", pricingTO="
				+ pricingTO + "]";
	}
	
}
