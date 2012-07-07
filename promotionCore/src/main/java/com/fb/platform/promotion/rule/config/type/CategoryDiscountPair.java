/**
 * 
 */
package com.fb.platform.promotion.rule.config.type;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author keith
 *
 */
public class CategoryDiscountPair {

	BigDecimal percent = new BigDecimal(0);
	List<Integer> categoryList = new ArrayList<Integer>();
	String categoryName;
	
	public CategoryDiscountPair(BigDecimal percent, List<Integer> categoryList) {
		this.percent = percent;
		this.categoryList = categoryList;
	}

	public BigDecimal getPercent() {
		return percent;
	}

	public void setPercent(BigDecimal percent) {
		this.percent = percent;
	}

	public List<Integer> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Integer> categoryList) {
		this.categoryList = categoryList;
	}
	
	public void addCategory(int categoryId) {
		this.categoryList.add(categoryId);
	}
	
	public void removeCategory(int categoryId) {
		this.categoryList.remove(Integer.valueOf(categoryId));
	}
	
}
