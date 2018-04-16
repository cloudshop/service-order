package com.eyun.order.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class ProOrderBO {
    private Long orderid;
    
    private String shopName;
    
    private BigDecimal payment;
    
    private Integer status;
    
    public Long getOrderid() {
		return orderid;
	}

	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public BigDecimal getPayment() {
		return payment;
	}

	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	private String orderNo;
    
    
    private Set<ProOrderItemBO> proOrderItems = new HashSet<>();
    
    public Set<ProOrderItemBO> getProOrderItems() {
		return proOrderItems;
	}

	public void setProOrderItems(Set<ProOrderItemBO> proOrderItems) {
		this.proOrderItems = proOrderItems;
	}

}
