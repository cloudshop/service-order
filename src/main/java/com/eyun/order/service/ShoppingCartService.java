package com.eyun.order.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.eyun.order.client.AuthorizedFeignClient;

@AuthorizedFeignClient(name = "shoppingcart")
public interface ShoppingCartService {
	@RequestMapping(value = "/api/shoppingcar/del",method = RequestMethod.GET)
	public String del(@RequestParam("userId") Long userId,@RequestParam("skuids") List<Long> skuids);
	
}
