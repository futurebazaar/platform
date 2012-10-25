/**
 * 
 */
package com.fb.commons.mom.bigBazaar.to;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nehaga
 *
 */
public enum InvoiceItemIdentificationEnum {
	ORDER("002"),
	INVOICE("009"),
	DELIVERY("012");
	
	private String code;
	
	private static Map<String, InvoiceItemIdentificationEnum> invoiceIdentificationMap = new HashMap<String, InvoiceItemIdentificationEnum>();
	
	static {
		for (InvoiceItemIdentificationEnum invoiceType : InvoiceItemIdentificationEnum.values()) {
			invoiceIdentificationMap.put(invoiceType.getCode(), invoiceType);
		}
	}
	
	private InvoiceItemIdentificationEnum(String code) {
		this.code = code;
	}
	
	public static InvoiceItemIdentificationEnum getInstance(String invoiceType) {
		return invoiceIdentificationMap.get(invoiceType.trim());
	}
	
	@Override
	public String toString() {
		return this.name();
	}
	
	private String getCode() {
		return this.code;
	}
}
