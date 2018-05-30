package com.eyun.order.service.dto;

import java.util.List;

import com.eyun.order.domain.ProOrder;
import com.eyun.order.service.ProOrderBO;

public class OrderDateDTO {
	
	private List<ProOrderBO> proOrder;
	
	private Long proOrderAmount;


	public List<ProOrderBO> getProOrder() {
		return proOrder;
	}

	public void setProOrder(List<ProOrderBO> proOrder) {
		this.proOrder = proOrder;
	}

	public Long getProOrderAmount() {
		return proOrderAmount;
	}

	public void setProOrderAmount(Long proOrderAmount) {
		this.proOrderAmount = proOrderAmount;
	}

}

