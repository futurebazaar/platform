/**
 * 
 */
package com.fb.commons.mom.to;

import java.io.Serializable;

import org.joda.time.DateTime;

/**
 * @author nehaga
 *
})
 */
public class OrderTO implements Serializable {
	private SapMomTO sapIdoc;
	private String orderId;
	private String orderSource;
	private String tsVar;
	private String orderDesc;
	private String id;
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderSource() {
		return orderSource;
	}
	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}
	public String getTsVar() {
		return tsVar;
	}
	public void setTsVar(String tsVar) {
		this.tsVar = tsVar;
	}
	public String getOrderDesc() {
		return orderDesc;
	}
	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public SapMomTO getSapIdoc() {
		return sapIdoc;
	}
	public void setSapIdoc(SapMomTO sapIdoc) {
		this.sapIdoc = sapIdoc;
	}
	
	@Override
	public String toString() {
		String order = "order Id : " + orderId
				+ "\norder source : " + orderSource
				+ "\nts var : " + tsVar
				+ "\norder description : " + orderDesc
				+ "\nid : " + id;
		if(sapIdoc != null) {
			order += "\n" + sapIdoc.toString();
		}
		return order;
		
	}
		   
		   
}
