/**
 * 
 */
package com.fb.platform.promotion.product.model;

import java.util.ArrayList;
import java.util.List;

import com.fb.platform.promotion.product.model.result.ValueChangeResult;

/**
 * @author vinayak
 *
 */
public class Results {

	private List<Result> results = new ArrayList<Result>();
	private List<ModuleJoin> joins = new ArrayList<ModuleJoin>();
	private boolean isValueResult = false;

	public List<Result> getResults() {
		return results;
	}
	public void setResults(List<Result> results) {
		this.results = results;
		for (Result result : results) {
			if (result instanceof ValueChangeResult) {
				isValueResult = true;
			}
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

}
