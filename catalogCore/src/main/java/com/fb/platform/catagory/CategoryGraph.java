/**
 * 
 */
package com.fb.platform.catagory;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author vinayak
 *
 */
public class CategoryGraph {

	private int id;
	private int categoryId;
	private int parentId;
	private String position;
	private int sortOrder;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public int getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("id", this.getId())
		.append("categoryId", this.getCategoryId())
		.append("parentId", this.getParentId())
		.append("sortOrder", this.getSortOrder())
		.toString();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof CategoryGraph)) {
			return false;
		}

		return this.hashCode() == other.hashCode();
	}

	@Override
	public int hashCode() {
		int result =  0;
		result = categoryId;
		result = 29 * result + parentId;
		result = 29 * result + sortOrder;

		return result;
	}
}
