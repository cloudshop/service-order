package com.eyun.order.service.dto;

public class ShipDTO {
    private String shippingName;

    private String shipingCode;
    
    private String orderNo;

	public String getShippingName() {
		return shippingName;
	}

	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}

	public String getShipingCode() {
		return shipingCode;
	}

	public void setShipingCode(String shipingCode) {
		this.shipingCode = shipingCode;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

}
