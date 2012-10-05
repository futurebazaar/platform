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

	private static BigDecimal TWELVE = new BigDecimal("12.00"); //1 foot = 12 inches
	private static BigDecimal volumeToKGConstant = new BigDecimal("6.00");

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
			volumeWeight = volumeInInches.divide(TWELVE).divide(TWELVE).divide(TWELVE).multiply(volumeToKGConstant);
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
