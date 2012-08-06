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
public class BrandResult extends AbstractResult {

	private List<Integer> brandIds = new ArrayList<Integer>();

	public List<Integer> getBrandIds() {
		return brandIds;
	}
	public void setBrandIds(List<Integer> brandIds) {
		this.brandIds = brandIds;
	}
}
