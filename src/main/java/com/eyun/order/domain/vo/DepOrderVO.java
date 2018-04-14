package com.eyun.order.domain.vo;

import java.math.BigDecimal;

public class DepOrderVO {

	private BigDecimal payment;
	
	private Integer payType;
	
	public BigDecimal getPayment() {
		return payment;
	}

	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

}
