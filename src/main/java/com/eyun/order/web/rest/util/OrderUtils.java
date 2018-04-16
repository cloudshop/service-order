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
import com.eyun.order.service.dto.ProductSkuDTO;
@Component
public class OrderUtils {
	@Autowired
    private ProService proService;
	
	public   List<ProOrderBO> showOrder(List<ProOrder> allProOrderByUser){
		List<ProOrderBO> proVo = new ArrayList<ProOrderBO>();
	    for (ProOrder proOrder : allProOrderByUser) {
	    	ProOrderBO pbo = new ProOrderBO();
	    	pbo.setOrderid(proOrder.getId());
	    	String shopName = "";
	    	pbo.setShopName(shopName);
	    	pbo.setStatus(proOrder.getStatus());
	    	
	    	
	    	Set<ProOrderItem> proOrderItems = proOrder.getProOrderItems();
	    	for (ProOrderItem proOrderItem : proOrderItems) {
	    		ProOrderItemBO pItem = new ProOrderItemBO();
	    		pItem.setProductSkuId(proOrderItem.getProductSkuId());
	    		pItem.setPrice(proOrderItem.getPrice());
	    		pItem.setPrice(proOrderItem.getPrice());
	    		System.out.println("proOrderItem" + proOrderItem.getId());
	    		System.out.println("proOrderItem 的值" + proOrderItem.toString() );
	    		ProductSkuDTO sku = proService.getProductSku(proOrderItem.getProductSkuId());
	    		pItem.setSkuName(sku.getSkuName());
	    		pbo.getProOrderItems().add(pItem);
			
			}
	    	proVo.add(pbo);
	    }
			
		return proVo;
	}
	
	
	
	

}
