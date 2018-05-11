package com.eyun.order.service;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.eyun.order.client.AuthorizedFeignClient;

@AuthorizedFeignClient(name="commission")
public interface CommisionService {
	
	
	@GetMapping("/api/serviceProvider/{userId}")
	public void joinMoney(@PathVariable("userId") Long userId);
	
	@GetMapping("/order/facilitator/wallet")
	public ResponseEntity handleFacilitatorWallet(@RequestParam("shopId") Long shopId, @RequestParam("payment")BigDecimal payment,@RequestParam("orderNo")String orderNo);
}
