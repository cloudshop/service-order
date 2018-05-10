package com.eyun.order.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.eyun.order.client.AuthorizedFeignClient;

@AuthorizedFeignClient(name="commission")
public interface CommisionService {
	
	
	@GetMapping("/api/serviceProvider/{userId}")
	public void joinMoney(@PathVariable("userId") Long userId);
}
