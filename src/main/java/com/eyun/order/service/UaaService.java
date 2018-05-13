package com.eyun.order.service;

import org.springframework.web.bind.annotation.GetMapping;

import com.eyun.order.client.AuthorizedFeignClient;
import com.eyun.order.service.dto.UserDTO;

@AuthorizedFeignClient(name="uaa")
public interface UaaService {

	@GetMapping("/api/account")
	public UserDTO getAccount();
	
}
