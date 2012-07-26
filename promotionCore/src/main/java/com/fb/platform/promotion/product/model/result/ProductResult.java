/**
 * 
 */
package com.fb.platform.promotion.product.model.result;

import java.util.ArrayList;
import java.util.List;

import com.fb.platform.promotion.product.model.Result;
import com.fb.platform.promotion.product.model.ResultType;

/**
 * @author vinayak
 *
 */
public class ProductResult implements Result {

	private List<Integer> productIds = new ArrayList<Integer>();
	private ResultType resultType = null;
	private String resultValue = null;

	public List<Integer> getProductIds() {
		return productIds;
	}
	public void setProductIds(List<Integer> productIds) {
		this.productIds = productIds;
	}
	public ResultType getResultType() {
		return resultType;
	}
	public void setResultType(ResultType resultType) {
		this.resultType = resultType;
	}
	public String getResultValue() {
		return resultValue;
	}
	public void setResultValue(String resultValue) {
		this.resultValue = resultValue;
	}
}
