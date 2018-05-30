package com.eyun.order.service;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.eyun.order.client.AuthorizedFeignClient;
import com.eyun.order.domain.vo.AlipayDTO;
import com.eyun.order.service.dto.ProductSkuDTO;

@AuthorizedFeignClient(name="product")
public interface ProService {	
	@GetMapping("/api/product-skus/stock/{processtype}/{id}/{count}")
	public Map updateProductSkuCount(@PathVariable("processtype") Integer processtype,@PathVariable("id") Long id,@PathVariable("count") Integer count);
	
	@GetMapping("/api/product-skus/{id}")
	public ProductSkuDTO getProductSku(@PathVariable("id") Long id);
	
	@PostMapping("/api/product/follow")
	public List<Map> follow(@RequestBody List pros);

	@GetMapping("/api/sku-imgs/")
	public List<Map> getSkuImg(@RequestParam("skuId") Long skuId);
	
	
}


