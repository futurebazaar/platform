/**
 * 
 */
package com.fb.commons.mom.bigBazaar.to;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

/**
 * @author nehaga
 *
 */
public class DeliveryItemTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8248828851879676485L;
	
	private String orderNumber;
	private int itemNumber;
	private String articleNumber;
	private String articleEntered;
	private String salesDesc;
	private int materialGroup;
	private int plant;
	private int storageLocation;
	private BigDecimal actualQuantity;
	private String salesUnit;
	private BigDecimal actualQuantityStockUnit;
	private String unitOfMeasurement;
	private BigDecimal netWeight;
	private BigDecimal grossWeight;
	private String weightUnit;
	private BigDecimal volume;
	private int loadingGroup;
	private int transportationGroup;
	private int distributionChannel;
	private int division;
	private int deliveryGroup;
	private String internationalArticleNumber;
	private int externalItemNumber;
	private DateTime expirationDate;
	private int segment;
	
	public int getItemNumber() {
		return itemNumber;
	}
	public void setItemNumber(int itemNumber) {
		this.itemNumber = itemNumber;
	}
	public String getArticleNumber() {
		return articleNumber;
	}
	public void setArticleNumber(String articleNumber) {
		this.articleNumber = articleNumber;
	}
	public String getArticleEntered() {
		return articleEntered;
	}
	public void setArticleEntered(String articleEntered) {
		this.articleEntered = articleEntered;
	}
	public String getSalesDesc() {
		return salesDesc;
	}
	public void setSalesDesc(String salesDesc) {
		this.salesDesc = salesDesc;
	}
	public int getMaterialGroup() {
		return materialGroup;
	}
	public void setMaterialGroup(int materialGroup) {
		this.materialGroup = materialGroup;
	}
	public int getPlant() {
		return plant;
	}
	public void setPlant(int plant) {
		this.plant = plant;
	}
	public int getStorageLocation() {
		return storageLocation;
	}
	public void setStorageLocation(int storageLocation) {
		this.storageLocation = storageLocation;
	}
	public BigDecimal getActualQuantity() {
		return actualQuantity;
	}
	public void setActualQuantity(BigDecimal actualQuantity) {
		this.actualQuantity = actualQuantity;
	}
	public String getSalesUnit() {
		return salesUnit;
	}
	public void setSalesUnit(String salesUnit) {
		this.salesUnit = salesUnit;
	}
	public BigDecimal getActualQuantityStockUnit() {
		return actualQuantityStockUnit;
	}
	public void setActualQuantityStockUnit(BigDecimal actualQuantityStockUnit) {
		this.actualQuantityStockUnit = actualQuantityStockUnit;
	}
	public String getUnitOfMeasurement() {
		return unitOfMeasurement;
	}
	public void setUnitOfMeasurement(String unitOfMeasurement) {
		this.unitOfMeasurement = unitOfMeasurement;
	}
	public BigDecimal getNetWeight() {
		return netWeight;
	}
	public void setNetWeight(BigDecimal netWeight) {
		this.netWeight = netWeight;
	}
	public BigDecimal getGrossWeight() {
		return grossWeight;
	}
	public void setGrossWeight(BigDecimal grossWeight) {
		this.grossWeight = grossWeight;
	}
	public String getWeightUnit() {
		return weightUnit;
	}
	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}
	public BigDecimal getVolume() {
		return volume;
	}
	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}
	public int getLoadingGroup() {
		return loadingGroup;
	}
	public void setLoadingGroup(int loadingGroup) {
		this.loadingGroup = loadingGroup;
	}
	public int getTransportationGroup() {
		return transportationGroup;
	}
	public void setTransportationGroup(int transportationGroup) {
		this.transportationGroup = transportationGroup;
	}
	public int getDistributionChannel() {
		return distributionChannel;
	}
	public void setDistributionChannel(int distributionChannel) {
		this.distributionChannel = distributionChannel;
	}
	public int getDivision() {
		return division;
	}
	public void setDivision(int division) {
		this.division = division;
	}
	public int getDeliveryGroup() {
		return deliveryGroup;
	}
	public void setDeliveryGroup(int deliveryGroup) {
		this.deliveryGroup = deliveryGroup;
	}
	public String getInternationalArticleNumber() {
		return internationalArticleNumber;
	}
	public void setInternationalArticleNumber(String internationalArticleNumber) {
		this.internationalArticleNumber = internationalArticleNumber;
	}
	public int getExternalItemNumber() {
		return externalItemNumber;
	}
	public void setExternalItemNumber(int externalItemNumber) {
		this.externalItemNumber = externalItemNumber;
	}
	public DateTime getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(DateTime expirationDate) {
		this.expirationDate = expirationDate;
	}
	public int getSegment() {
		return segment;
	}
	public void setSegment(int segment) {
		this.segment = segment;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("orderNumber", this.orderNumber)
			.append("itemNumber", this.itemNumber)
			.append("articleNumber", this.articleNumber)
			.append("articleEntered", this.articleEntered)
			.append("salesDesc", this.salesDesc)
			.append("materialGroup", this.materialGroup)
			.append("plant", this.plant)
			.append("storageLocation", this.storageLocation)
			.append("actualQuantity", this.actualQuantity)
			.append("salesUnit", this.salesUnit)
			.append("actualQuantityStockUnit", this.actualQuantityStockUnit)
			.append("unitOfMeasurement", this.unitOfMeasurement)
			.append("netWeight", this.netWeight)
			.append("grossWeight", this.grossWeight)
			.append("weightUnit", this.weightUnit)
			.append("volume", this.volume)
			.append("loadingGroup", this.loadingGroup)
			.append("transportationGroup", this.transportationGroup)
			.append("distributionChannel", this.distributionChannel)
			.append("division", this.division)
			.append("deliveryGroup", this.deliveryGroup)
			.append("internationalArticleNumber", this.internationalArticleNumber)
			.append("externalItemNumber", this.externalItemNumber)
			.append("expirationDate", this.expirationDate)
			.append("segment", this.segment)
			.toString();
	}
}
