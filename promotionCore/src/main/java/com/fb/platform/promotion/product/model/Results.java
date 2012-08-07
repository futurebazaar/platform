/**
 * 
 */
package com.fb.platform.promotion.product.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vinayak
 *
 */
public class Results {

	private List<Result> results = new ArrayList<Result>();
	private List<ModuleJoin> joins = new ArrayList<ModuleJoin>();
	public List<Result> getResults() {
		return results;
	}
	public void setResults(List<Result> results) {
		this.results = results;
	}
	public List<ModuleJoin> getJoins() {
		return joins;
	}
	public void setJoins(List<ModuleJoin> joins) {
		this.joins = joins;
	}
	
}
