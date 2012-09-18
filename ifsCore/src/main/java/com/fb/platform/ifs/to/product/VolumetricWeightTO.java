/**
 * 
 */
package com.fb.platform.ifs.to.product;

import java.math.BigDecimal;

/**
 * @author vinayak
 *
 */
public class VolumetricWeightTO {

	private long articleId = 0;
	private long productId = 0;
	private BigDecimal weight = BigDecimal.ZERO;

	private ProductVolumeTO volume = null;

	/**
	 * There are 2 types of weights associated with a product. Weight calculated based on volume and real weight. 
	 * The highest of these two is used by LSP's for cost calculations. 
	 * The weight based on volume is calculated as follows. 
	 * Height in feet * width in feet * breadth in feet * 6 kgs.
	 * @return returns the max of weight based on volume and real weight.
	 */
	public BigDecimal getMaxWeight() {
		BigDecimal volumetricWeight = volume.getVolumetricWeight();
		if (volumetricWeight.compareTo(weight) > 0) {
			return volumetricWeight;
		} else {
			return weight;
		}
	}

	public long getArticleId() {
		return articleId;
	}

	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public ProductVolumeTO getVolume() {
		return volume;
	}

	public void setVolume(ProductVolumeTO volume) {
		this.volume = volume;
	}
}
