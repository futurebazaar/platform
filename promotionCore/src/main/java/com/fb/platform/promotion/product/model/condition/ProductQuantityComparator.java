/**
 * 
 */
package com.fb.platform.promotion.product.model.condition;

import java.util.Comparator;

/**
 * @author nehaga
 *
 */
public class ProductQuantityComparator implements Comparator<ProductCondition>{

	@Override
	public int compare(ProductCondition productCondition1, ProductCondition productCondition2) {
		int quantityDiff = productCondition2.getQuantity() - productCondition1.getQuantity();
		return quantityDiff;
	}

}
