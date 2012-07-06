/**
 * 
 */
package com.fb.platform.promotion.rule.config.data;

import java.util.ArrayList;
import java.util.List;

import com.fb.platform.promotion.rule.config.type.CategoryDiscountPair;

/**
 * @author keith
 *
 */
public class CategoryDiscount {

	List<CategoryDiscountPair> categoryDiscountMap = new ArrayList<CategoryDiscountPair>();

	public List<CategoryDiscountPair> getCategoryDiscountMap() {
		return categoryDiscountMap;
	}

	public void setCategoryDiscountMap(
			List<CategoryDiscountPair> categoryDiscountMap) {
		this.categoryDiscountMap = categoryDiscountMap;
	}
	
	public List<Integer> getAllCategoryList() {
		List<Integer> allCategoryList = new ArrayList<Integer>();
		for(CategoryDiscountPair pair: categoryDiscountMap) {
			allCategoryList.addAll(pair.getCategoryList());
		}
		return allCategoryList;
	}
	
	public boolean isCategoryPresent(int categoryId) {
		for(CategoryDiscountPair pair: categoryDiscountMap) {
			if(pair.getCategoryList().contains(categoryId)) {
				return true;
			}
		}
		return false;
	}

	public void addCategoryDiscountPair(CategoryDiscountPair pair) {
		this.categoryDiscountMap.add(pair);
	}
	
}
