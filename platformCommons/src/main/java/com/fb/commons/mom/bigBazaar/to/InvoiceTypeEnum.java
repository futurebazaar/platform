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
public enum InvoiceTypeEnum {

	CREATE("ZFB1"),
	CANCEL("ZFBC");
	
	private String code;
	
	private static Map<String, InvoiceTypeEnum> invoiceTypeMap = new HashMap<String, InvoiceTypeEnum>();
	
	static {
		for (InvoiceTypeEnum invoiceType : InvoiceTypeEnum.values()) {
			invoiceTypeMap.put(invoiceType.getCode(), invoiceType);
		}
	}
	
	private InvoiceTypeEnum(String code) {
		this.code = code;
	}
	
	public static InvoiceTypeEnum getInstance(String invoiceType) {
		return invoiceTypeMap.get(invoiceType.trim());
	}
	
	@Override
	public String toString() {
		return this.name();
	}
	
	private String getCode() {
		return this.code;
	}
}
