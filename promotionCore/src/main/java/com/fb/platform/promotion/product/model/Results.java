/**
 * 
 */
package com.fb.platform.promotion.product.model;

import java.util.ArrayList;
import java.util.List;

import com.fb.platform.promotion.product.model.result.ProductResult;
import com.fb.platform.promotion.product.model.result.ValueChangeResult;

/**
 * @author vinayak
 *
 */
public class Results {

	private List<Result> results = new ArrayList<Result>();
	private List<ModuleJoin> joins = new ArrayList<ModuleJoin>();
	private boolean isValueResult = false;
	private boolean isProductResult = false;

	public List<Result> getResults() {
		return results;
	}
	public void addResult(Result result) {
		results.add(result);
		if (result instanceof ValueChangeResult) {
			isValueResult = true;
		} else if (result instanceof ProductResult) {
			isProductResult = true;
		}
	}

	public List<ModuleJoin> getJoins() {
		return joins;
	}
	public void setJoins(List<ModuleJoin> joins) {
		this.joins = joins;
	}
	public boolean isValueResult() {
		return isValueResult;
	}

	public boolean isProductResult() {
		return isProductResult;
	}
}
