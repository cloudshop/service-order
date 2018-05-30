package com.eyun.order.service;

import com.eyun.order.domain.ProOrder;
import com.eyun.order.domain.ProOrderItem;
import com.eyun.order.service.dto.OrderDateDTO;
import com.eyun.order.service.dto.PageOrder;
import com.eyun.order.service.dto.PayNotifyDTO;
import com.eyun.order.service.dto.ProOrderDTO;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing ProOrder.
 */
public interface ProOrderService {

    /**
     * Save a proOrder.
     *
     * @param proOrderDTO the entity to save
     * @return the persisted entity
     */
    ProOrderDTO save(ProOrderDTO proOrderDTO);

    
    /**
     * Get all the proOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ProOrderDTO> findAll(Pageable pageable);

    /**
     * Get the "id" proOrder.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ProOrderDTO findOne(Long id);

    /**
     * Delete the "id" proOrder.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
    	
	void updateOrderById();

	List<ProOrderBO> getProOrderItemsByUser(int i, int page, int size);

	List<ProOrderBO> findDispatchItems(long l, int page, int size);

	List<ProOrderBO> findOrderByStatuAndUserid(Long userId, Integer status, Integer page, Integer size);
	
	String createOrder(ProOrderDTO proOrderDTO, Integer type);

	ProOrderDTO findOrderByOrderNo(String orderNo);

	ProOrderDTO proOrderNotify(PayNotifyDTO payNotifyDTO);

	List<ProOrderBO> findAllOrder(long l, int page, int size);

	Boolean updateOrderStatus(String string, Integer integer);

	void updateStatus();


	List<ProOrder> findOrderByStatus(Integer status);

	List<ProOrder> findAll();

	List<ProOrderItem> findOrderById(Long orderId);


	List<BigInteger> findOrderItemBySkuId(Long skuId);


	Boolean proOrderDelete(Long orderId);


	List<ProOrder> toProEntityList(List<ProOrderDTO> content);
	}