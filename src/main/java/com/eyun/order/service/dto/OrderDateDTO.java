package com.eyun.order.service.dto;

import java.util.List;

import com.eyun.order.domain.ProOrder;

public class OrderDateDTO {
	
	private List<ProOrderDTO> proOrder;
	
	private Long proOrderAmount;

	public List<ProOrderDTO> getProOrder() {
		return proOrder;
	}

	public void setProOrder(List<ProOrderDTO> proOrder) {
		this.proOrder = proOrder;
	}

	public Long getProOrderAmount() {
		return proOrderAmount;
	}

	public void setProOrderAmount(Long proOrderAmount) {
		this.proOrderAmount = proOrderAmount;
	}

    
	
}
