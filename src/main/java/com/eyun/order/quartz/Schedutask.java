package com.eyun.order.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import com.eyun.order.service.ProOrderService;

@Configuration  
@Component 
@EnableScheduling 
public class Schedutask {
	   private final Logger log = LoggerFactory.getLogger(Schedutask.class);

		
		@Autowired
		private ProOrderService proOrderService;
		
	     public void execute(){
	 
	    	 proOrderService.updateOrderById();
	    	 
	    	 proOrderService.updateStatus();
	    	 
	     }
}
