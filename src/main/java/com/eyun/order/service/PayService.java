package com.eyun.order.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.eyun.order.client.AuthorizedFeignClient;
import com.eyun.order.domain.vo.AlipayDTO;

@AuthorizedFeignClient(name="pay")
public interface PayService {

	@PostMapping("/api/alipay/app/orderString")
	public String createAlipayAppOrder (@RequestBody AlipayDTO alipayVM);
	
	
	@GetMapping("/api/alipay/order/{orderNo}")
	public String queryOrder(@PathVariable("orderNo") String orderNo);
	
	@PostMapping("/api/wxpay/preorder")
	public String prePay(
			@RequestParam(required = true,value = "out_trade_no")String out_trade_no,
			@RequestParam(required = true,value = "total_fee")String total_fee,
			@RequestParam(required = true,value = "attach")String attach);
	
}
