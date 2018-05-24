package com.eyun.order.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.codahale.metrics.annotation.Timed;
import com.eyun.order.client.AuthorizedFeignClient;
import com.eyun.order.service.dto.MercuryDTO;
import com.eyun.order.service.dto.UserDTO;

@AuthorizedFeignClient(name="user")
public interface UserService {

	@GetMapping("/api/mercuries/{id}")
	public MercuryDTO getMercuries(@PathVariable("id") Long id);
	
	@PostMapping("/api/user-annexes-UpdaeUserStatus")
    public ResponseEntity UpdaeUserStatus(@RequestBody UserDTO userDTO);
	@GetMapping("/api/user-annexes-changeService/{id}")
	public ResponseEntity userAnnexesChangeService(@PathVariable("id") Long id);
    @GetMapping("/api/mercuries/getUserIdMercuryId")
    public Map findUserMercuryId();
   
}

