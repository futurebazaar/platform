/**
 * 
 */
package com.fb.platform.ifs.to.product;

import java.math.BigDecimal;

/**
 * @author vinayak
 *
 */
public class ProductVolumeTO {

	//all units should be in inches 
	private BigDecimal length = BigDecimal.ZERO;
	private BigDecimal height = BigDecimal.ZERO;
	private BigDecimal width = BigDecimal.ZERO;

	/**
	 * The weight based on volume is calculated as follows. 
	 * Height in feet * width in feet * breadth in feet * 6 kgs.
	 * @return
	 */
	public BigDecimal getVolumetricWeight() {
		BigDecimal volumeWeight = BigDecimal.ZERO;

		BigDecimal volumeInInches = BigDecimal.ONE;
		volumeInInches = volumeInInches.multiply(length).multiply(height).multiply(width);

		if (volumeInInches.compareTo(BigDecimal.ZERO) != 0) {
			BigDecimal twelve = new BigDecimal("12.00"); //1 foot = 12 inches

			volumeWeight = volumeInInches.divide(twelve).divide(twelve).divide(twelve).multiply(new BigDecimal("6"));
		}

		return volumeWeight;
	}

	public BigDecimal getLength() {
		return length;
	}

	public void setLength(BigDecimal length) {
		this.length = length;
	}

	public BigDecimal getHeight() {
		return height;
	}

	public void setHeight(BigDecimal height) {
		this.height = height;
	}

	public BigDecimal getWidth() {
		return width;
	}

	public void setWidth(BigDecimal width) {
		this.width = width;
	}

}
