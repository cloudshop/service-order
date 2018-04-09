package com.eyun.order.client;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "product")
public interface ProServiceFeignClient {
	@RequestMapping(value = "/api/product-skus/stock/{processtype}",method = RequestMethod.PUT)
	Map updatePro(@PathVariable("processtype") Integer processtype,@RequestBody Map m);
}
