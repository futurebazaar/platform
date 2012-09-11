/**
 * 
 */
package com.fb.platform.ifs.to.lsp;

import com.fb.commons.to.Money;

/**
 * @author vinayak
 *
 */
public class LSP {

	private int id;
	private String name;

	/**
	 * Checks whether this LSP delivers at the pincode given.
	 * @param pincode
	 * @param orderValue
	 * @return
	 */
	public boolean isServiceable(String pincode, Money orderValue) {
		return false;
	}

	/**
	 * Calculates the minimum shipping charges required to ship the given delivery package at the pincode.
	 * @param pincode
	 * @param deliveryPackage
	 * @param orderValue
	 * @return
	 */
	public Money calculateShippingCharge(String pincode, Object deliveryPackage, Money orderValue) {
		return null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
