package com.eyun.order.service.impl;

import org.springframework.stereotype.Service;

import com.eyun.order.domain.vo.AlipayDTO;
import com.eyun.order.service.PayService;

@Service
public class PayServiceImpl implements PayService{

	@Override
	public String createAlipayAppOrder(AlipayDTO alipayVM) {
		System.out.println("fallback pay");
		return "errer";
	}

	@Override
	public String queryOrder(String orderNo) {
		// TODO Auto-generated method stub
		return null;
	}

}
