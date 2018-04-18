package com.eyun.order.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.eyun.order.client.AuthorizedFeignClient;
import com.eyun.order.service.dto.MercuryDTO;

@AuthorizedFeignClient(name="user")
public interface UserService {

		@GetMapping("/api/mercuries/{id}")
		public MercuryDTO getMercuries(@PathVariable("id") Long id);
		
	}

