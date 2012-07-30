/**
 * 
 */
package com.fb.commons.mom.to;

import java.io.Serializable;

import org.joda.time.DateTime;

/**
 * @author nehaga
 *
})
 */
public class ItemTO implements Serializable {
	private SapMomTO sapIdoc;
	private int sapDocumentId;
	private String orderHeaderDelBlock;
	private String header;
	private DateTime deliveryDate;
	private String atgDocumentId;
	private String unitOfMeasurement;
	private String shipmentComments;
	private String orderType;
	private String deliveryNumber;
	private String lspName;
	private String awbNumber;
	private String createdBy;
	private DateTime createdDate;
	private String skuID;
	private String lspUpdateDesc;
	private DateTime pgiCreationDate;
	private String plantId;
	private String itemState;
	private DateTime orderDate;
	private int quantity;
	private int invoiceNumber;
	private DateTime invoiceDate;
	
	public SapMomTO getSapIdoc() {
		return sapIdoc;
	}
	public void setSapIdoc(SapMomTO sapIdoc) {
		this.sapIdoc = sapIdoc;
	}
	
	public int getSapDocumentId() {
		return sapDocumentId;
	}
	public void setSapDocumentId(int sapDocumentId) {
		this.sapDocumentId = sapDocumentId;
	}
	public String getOrderHeaderDelBlock() {
		return orderHeaderDelBlock;
	}
	public void setOrderHeaderDelBlock(String orderHeaderDelBlock) {
		this.orderHeaderDelBlock = orderHeaderDelBlock;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public DateTime getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(DateTime deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getAtgDocumentId() {
		return atgDocumentId;
	}
	public void setAtgDocumentId(String atgDocumentId) {
		this.atgDocumentId = atgDocumentId;
	}
	public String getUnitOfMeasurement() {
		return unitOfMeasurement;
	}
	public void setUnitOfMeasurement(String unitOfMeasurement) {
		this.unitOfMeasurement = unitOfMeasurement;
	}
	public String getShipmentComments() {
		return shipmentComments;
	}
	public void setShipmentComments(String shipmentComments) {
		this.shipmentComments = shipmentComments;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getDeliveryNumber() {
		return deliveryNumber;
	}
	public void setDeliveryNumber(String deliveryNumber) {
		this.deliveryNumber = deliveryNumber;
	}
	public String getLspName() {
		return lspName;
	}
	public void setLspName(String lspName) {
		this.lspName = lspName;
	}
	public String getAwbNumber() {
		return awbNumber;
	}
	public void setAwbNumber(String awbNumber) {
		this.awbNumber = awbNumber;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public DateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}
	public String getSkuID() {
		return skuID;
	}
	public void setSkuID(String skuID) {
		this.skuID = skuID;
	}
	public String getLspUpdateDesc() {
		return lspUpdateDesc;
	}
	public void setLspUpdateDesc(String lspUpdateDesc) {
		this.lspUpdateDesc = lspUpdateDesc;
	}
	public DateTime getPgiCreationDate() {
		return pgiCreationDate;
	}
	public void setPgiCreationDate(DateTime pgiCreationDate) {
		this.pgiCreationDate = pgiCreationDate;
	}
	public String getPlantId() {
		return plantId;
	}
	public void setPlantId(String plantId) {
		this.plantId = plantId;
	}
	public String getItemState() {
		return itemState;
	}
	public void setItemState(String itemState) {
		this.itemState = itemState;
	}
	public DateTime getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(DateTime orderDate) {
		this.orderDate = orderDate;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(int invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public DateTime getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(DateTime invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	@Override
	public String toString() {
		String order = "sap Document Id: " + sapDocumentId
				+ "\norder header del block : " + orderHeaderDelBlock
				+ "\nheader : " + header
				+ "\ndelivery date : " + deliveryDate
				+ "\natg document id : " + atgDocumentId
				+ "\nunit of measurement : " + unitOfMeasurement
				+ "\nshipment comments : " + shipmentComments
				+ "\norder type : " + orderType
				+ "\n delivery number : " + deliveryNumber
				+ "\nlsp name : " + lspName
				+ "\nawb number : " + awbNumber
				+ "\ncreated by : " + createdBy
				+ "\ncreated date : " + createdDate
				+ "\nsku id : " + skuID
				+ "\nlsp update description : " + lspUpdateDesc
				+ "\npgi description date : " + pgiCreationDate
				+ "\nplant id : " + plantId
				+ "\nitem state : " + itemState
				+ "\norder date : " + orderDate
				+ "\nquantity : " + quantity
				+ "\ninvoice number : " + invoiceNumber
				+ "\ninvoice date : " + invoiceDate;
		if(sapIdoc != null) {
			order += "\n" + sapIdoc.toString();
		}
		return order;
		
	}
		   
		   
}
