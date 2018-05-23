package com.eyun.order.service;

import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.eyun.order.service.dto.UserDTO;

@Component
public class AsyncTask {
	
	@Autowired
	private UserService userService;

	@Async
	public Future<String> notifyUserMicroservice(Long userid) {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(userid);
		userService.UpdaeUserStatus(userDTO);
		System.out.println(userid);
		return new AsyncResult<String>("ok");
	}

	
}
