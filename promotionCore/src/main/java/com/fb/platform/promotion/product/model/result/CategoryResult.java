/**
 * 
 */
package com.fb.platform.promotion.product.model.result;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vinayak
 *
 */
public class CategoryResult extends AbstractResult {

	private List<Integer> categoryIds = new ArrayList<Integer>();

	public List<Integer> getCategoryIds() {
		return categoryIds;
	}
	public void setCategoryIds(List<Integer> categoryIds) {
		this.categoryIds = categoryIds;
	}
}
