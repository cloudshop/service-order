package com.eyun.order.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.eyun.order.client.AuthorizedFeignClient;
import com.eyun.order.domain.vo.AlipayDTO;

@AuthorizedFeignClient(name="pay")
public interface PayService {

	@PostMapping("/api/alipay/app/orderString")
	public String createAlipayAppOrder (@RequestBody AlipayDTO alipayVM);
	
	@GetMapping("/api/alipay/order/{orderNo}")
	public String queryOrder(@PathVariable("orderNo") String orderNo);
	
}
