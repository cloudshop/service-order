package com.eyun.order.web.rest.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eyun.order.domain.ProOrder;
import com.eyun.order.domain.ProOrderItem;
import com.eyun.order.service.ProOrderBO;
import com.eyun.order.service.ProOrderItemBO;
import com.eyun.order.service.ProService;
import com.eyun.order.service.UserService;
import com.eyun.order.service.dto.MercuryDTO;
import com.eyun.order.service.dto.ProductSkuDTO;
import com.eyun.order.service.dto.UserDTO;
@Component
public class OrderUtils {
	@Autowired
    private ProService proService;
	
	@Autowired
	private UserService userService;
	
	public   List<ProOrderBO> showOrder(List<ProOrder> allProOrderByUser){
		List<ProOrderBO> proVo = new ArrayList<ProOrderBO>();
	    for (ProOrder proOrder : allProOrderByUser) {
	    	ProOrderBO pbo = new ProOrderBO();
	    	pbo.setOrderid(proOrder.getId());
	    	MercuryDTO mercuries = userService.getMercuries(proOrder.getShopId());
	    	pbo.setShopName(mercuries.getName());
	    	pbo.setStatus(proOrder.getStatus());
	    	pbo.setOrderString(proOrder.getOrderString());
	    	pbo.setOrderid(proOrder.getId());
	    	Set<ProOrderItem> proOrderItems = proOrder.getProOrderItems();
	    	for (ProOrderItem proOrderItem : proOrderItems) {
	    		ProOrderItemBO pItem = new ProOrderItemBO();
	    		pItem.setProductSkuId(proOrderItem.getProductSkuId());
	    		pItem.setPrice(proOrderItem.getPrice());
	    		pItem.setPrice(proOrderItem.getPrice());
	    		ProductSkuDTO sku = proService.getProductSku(proOrderItem.getProductSkuId());
	    		pItem.setSkuName(sku.getSkuName());
	    		pbo.getProOrderItems().add(pItem);
	    		System.out.println("ProOrderBo 的所有值 " + pbo.getProOrderItems().size());
			}
	    	proVo.add(pbo);
	    
	    }			
		return proVo;
	}
	
	
	
	

}
