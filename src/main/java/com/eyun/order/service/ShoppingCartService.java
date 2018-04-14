package com.eyun.order.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.eyun.order.client.AuthorizedFeignClient;

@AuthorizedFeignClient(name = "shoppingcart")
public interface ShoppingCartService {
	@PostMapping("/api/shoppingcar/del")
	public String del(@RequestBody List<Long> skuids);

}
