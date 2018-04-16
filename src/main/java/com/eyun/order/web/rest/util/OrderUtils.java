package com.eyun.order.web.rest.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.eyun.order.domain.ProOrder;
import com.eyun.order.domain.ProOrderItem;
import com.eyun.order.service.ProOrderService;
import com.eyun.order.service.ProService;
import com.eyun.order.service.dto.ProOrderDTO;
import com.eyun.order.service.dto.ProOrderItemDTO;

public class OrderUtils {
	@Autowired
    private static ProService proService;
	public static List<ProOrderDTO> showOrder(List<ProOrder> orders){
		List<ProOrderDTO> proDTO = new ArrayList<ProOrderDTO>();
		//遍历order订单表
		System.out.println("orders" + orders.size() + orders.toString());
		for (ProOrder proOrder : orders) {
			//取订单表中的每个订单，然后创建dto，给dto传
			 ProOrderDTO pDTO = new ProOrderDTO();
			 pDTO.setOrderNo(proOrder.getOrderNo());
			 pDTO.setShopName("");
			 pDTO.setPayment(proOrder.getPayment());
			 pDTO.setStatus(proOrder.getStatus());
			 
			 Set<ProOrderItemDTO> items = pDTO.getProOrderItems();
			 
			 Set<ProOrderItem> proOrderItems = proOrder.getProOrderItems();
			 System.out.println("proOrderItem的值" + proOrderItems.toString());
			 //set转为list 便于之后便利
			 List<ProOrderItem> proOrderIte = new ArrayList<ProOrderItem>(proOrderItems);
			 //取出 我要的proOrderItem集合中我要的属性值（id，counts）
			 List<Long> ids=proOrderItems.stream().map(ProOrderItem::getProductSkuId).collect(Collectors.toList());
			 System.out.println("ProOrderItem的值"+ ids.toString() + "proOrderIte" + proOrderItems.toString());
			 List<Integer> counts=proOrderItems.stream().map(ProOrderItem::getCount).collect(Collectors.toList());

			 for (int i = 0; i < proOrderIte.size(); i++) {
				 List id = new ArrayList<>();
				 
				 ProOrderItemDTO pd = new ProOrderItemDTO();
				 id.add(ids.get(i));
				 
				 System.out.println("id String" + id.toString());
				 
				 List<Map> follow = proService.follow(id);
				 
				 System.out.println("follow:"+follow);
				 pd.setUrl(follow.get(i).get("url").toString());
				 pd.setSkuName(follow.get(i).get("skuname").toString());
				 pd.setPrice((BigDecimal)follow.get(i).get("price"));
				 pd.setId((Long)follow.get(i).get("skuid"));
				 pd.setCount(counts.get(i));
				 items.add(pd);
			}
			 proDTO.add(pDTO);
			 
		}
			
		return proDTO;
	}
	
	
	
	

}
