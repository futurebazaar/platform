/**
 * 
 */
package com.fb.commons.mom.to;

import java.io.Serializable;
import java.math.BigDecimal;

import org.joda.time.DateTime;

/**
 * @author nehaga
 *
 */
public class ItemTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6790272798325591466L;

	public ItemTO() {
		super();
	}
	
	public void setItemTO(ItemTO itemTO) {
		this.setSapIdoc(itemTO.getSapIdoc());
		this.setHeader(itemTO.getHeader());
		this.setSapDocumentId(itemTO.getSapDocumentId());
		this.setOrderHeaderDelBlock(itemTO.getOrderHeaderDelBlock());
		this.setDeliveryDate(itemTO.getDeliveryDate());
		this.setUnitOfMeasurement(itemTO.getUnitOfMeasurement());
		this.setShipmentComments(itemTO.getShipmentComments());
		this.setOrderType(itemTO.getOrderType());
		this.setDeliveryNumber(itemTO.getDeliveryNumber());
		this.setBlockMsg(itemTO.getBlockMsg());
		this.setItemCategory(itemTO.getItemCategory());
		this.setOrderId(itemTO.getOrderId());
		this.setDeliveryType(itemTO.getDeliveryType());
		this.setLspName(itemTO.getLspName());
		this.setAwbNumber(itemTO.getAwbNumber());
		this.setCreatedBy(itemTO.getCreatedBy());
		this.setCreatedDate(itemTO.getCreatedDate());
		this.setSkuID(itemTO.getSkuID());
		this.setLspUpdateDesc(itemTO.getLspUpdateDesc());
		this.setPlantId(itemTO.getPlantId());
		this.setItemState(itemTO.getItemState());
		this.setOrderDate(itemTO.getOrderDate());
		this.setQuantity(itemTO.getQuantity());
		this.setOrderState(itemTO.getOrderState());
		this.setAtgDocumentId(itemTO.getAtgDocumentId());
	}
	
	private SapMomTO sapIdoc;
	private String header;
	private int sapDocumentId;
	private String orderHeaderDelBlock;
	private DateTime deliveryDate;
	private String unitOfMeasurement;
	private String shipmentComments;
	private String orderType;
	private String deliveryNumber;
	private String blockMsg;
	private String itemCategory;
	private String orderId;
	private String deliveryType;
	private String lspName;
	private String awbNumber;
	private String createdBy;
	private DateTime createdDate;
	private String skuID;
	private String lspUpdateDesc;
	private String plantId;
	private String itemState;
	private DateTime orderDate;
	private BigDecimal quantity;
	private String orderState;
	private int atgDocumentId;
	private String lspCode;

	public int getAtgDocumentId() {
		return atgDocumentId;
	}

	public void setAtgDocumentId(int atgDocumentId) {
		this.atgDocumentId = atgDocumentId;
	}
	
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
	public DateTime getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(DateTime deliveryDate) {
		this.deliveryDate = deliveryDate;
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
	public BigDecimal getQuantity() {
		return quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	public String getBlockMsg() {
		return blockMsg;
	}
	public void setBlockMsg(String blockMsg) {
		this.blockMsg = blockMsg;
	}
	public String getItemCategory() {
		return itemCategory;
	}
	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getDeliveryType() {
		return deliveryType;
	}
	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getOrderState() {
		return orderState;
	}
	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}
	@Override
	public String toString() {
		String order = "sap Document Id: " + sapDocumentId
				+ "\nATG Document Id : " + atgDocumentId
				+ "\norder state : " + orderState
				+ "\nheader : " + header
				+ "\norder header del block : " + orderHeaderDelBlock
				+ "\ndelivery date : " + deliveryDate
				+ "\nunit of measurement : " + unitOfMeasurement
				+ "\nshipment comments : " + shipmentComments
				+ "\norder type : " + orderType
				+ "\ndelivery number : " + deliveryNumber
				+ "\nblock msg : " + blockMsg
				+ "\nitem category : " + itemCategory
				+ "\norder id : " + orderId
				+ "\ndelivery type : " + deliveryType
				+ "\nlsp code : " + lspCode
				+ "\nlsp name : " + lspName
				+ "\nawb number : " + awbNumber
				+ "\ncreated by : " + createdBy
				+ "\ncreated date : " + createdDate
				+ "\nsku id : " + skuID
				+ "\nlsp update description : " + lspUpdateDesc
				+ "\nplant id : " + plantId
				+ "\nitem state : " + itemState
				+ "\norder date : " + orderDate
				+ "\nquantity : " + quantity;
		if(sapIdoc != null) {
			order += "\n" + sapIdoc.toString();
		}
		return order;
		
	}
	public String getLspCode() {
		return lspCode;
	}
	public void setLspCode(String lspCode) {
		this.lspCode = lspCode;
	}
		   
		   
}
