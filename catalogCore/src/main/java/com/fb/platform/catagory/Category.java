/**
 * 
 */
package com.fb.platform.catagory;

/**
 * @author vinayak
 *
 */
public class Category {

	private int id;
	private int name;
	//referes to parent category id
	private int parentId;
	private String tagline;
	private String description;
	private int extId;
	private String slug;
	//refers to store object
	private int storeId;
	private boolean moderate;
	private String imageLocation;
	private String googleConversionLabel;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getName() {
		return name;
	}
	public void setName(int name) {
		this.name = name;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getTagline() {
		return tagline;
	}
	public void setTagline(String tagline) {
		this.tagline = tagline;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getExtId() {
		return extId;
	}
	public void setExtId(int extId) {
		this.extId = extId;
	}
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	public int getStoreId() {
		return storeId;
	}
	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}
	public boolean isModerate() {
		return moderate;
	}
	public void setModerate(boolean moderate) {
		this.moderate = moderate;
	}
	public String getImageLocation() {
		return imageLocation;
	}
	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}
	public String getGoogleConversionLabel() {
		return googleConversionLabel;
	}
	public void setGoogleConversionLabel(String googleConversionLabel) {
		this.googleConversionLabel = googleConversionLabel;
	}
	
}
