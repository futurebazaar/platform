/**
 * 
 */
package com.fb.platform.catalog;

/**
 * @author vinayak
 *
 */
public class Brand {

	private int id;
	private String name;
	private String tabline;
	private String description;
	private String slug;
	private boolean moderate;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTabline() {
		return tabline;
	}
	public void setTabline(String tabline) {
		this.tabline = tabline;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	public boolean isModerate() {
		return moderate;
	}
	public void setModerate(boolean moderate) {
		this.moderate = moderate;
	}
}
