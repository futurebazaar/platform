package com.fb.platform.promotion.model;

import com.fb.commons.promotion.to.ValueType;

public class PromoValues {
	private int id;
	private String valueName;
	private String valueDesc;
	private String valueData;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getValueName() {
		return valueName;
	}
	public void setValueName(String valueName) {
		this.valueName = valueName;
	}
	public String getValueDesc() {
		return valueDesc;
	}
	public void setValueDesc(String valueDesc) {
		this.valueDesc = valueDesc;
	}
	public String getValueData() {
		return valueData;
	}
	public void setValueData(String valueData) {
		this.valueData = valueData;
	}
	public int getPromoId() {
		return promoId;
	}
	public void setPromoId(int promoId) {
		this.promoId = promoId;
	}
	public ValueType getValueType() {
		return valueType;
	}
	public void setValueType(ValueType valueType) {
		this.valueType = valueType;
	}
	private int promoId;
	private ValueType valueType; /*direct/bundle*/
}
