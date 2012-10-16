package com.fb.platform.payback.to;

import java.io.Serializable;

public class RollbackResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int deletedHeaderRows;
	private int deletedItemRows;
	private long headerId;
	private PointsResponseCodeEnum responseEnum;
	
	public int getDeletedItemRows() {
		return deletedItemRows;
	}
	public void setDeletedItemRows(int deletedItemRows) {
		this.deletedItemRows = deletedItemRows;
	}
	public int getDeletedHeaderRows() {
		return deletedHeaderRows;
	}
	public void setDeletedHeaderRows(int deletedHeaderRows) {
		this.deletedHeaderRows = deletedHeaderRows;
	}
	public long getHeaderId() {
		return headerId;
	}
	public void setHeaderId(long headerId) {
		this.headerId = headerId;
	}
	public PointsResponseCodeEnum getResponseEnum() {
		return responseEnum;
	}
	public void setResponseEnum(PointsResponseCodeEnum responseEnum) {
		this.responseEnum = responseEnum;
	}

}
