package com.eyun.order.service;

import com.eyun.order.service.dto.FaceOrderDTO;
import com.eyun.order.service.dto.PayNotifyDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing FaceOrder.
 */
public interface FaceOrderService {

    /**
     * Save a faceOrder.
     *
     * @param faceOrderDTO the entity to save
     * @return the persisted entity
     */
    FaceOrderDTO save(FaceOrderDTO faceOrderDTO);

    /**
     * Get all the faceOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FaceOrderDTO> findAll(Pageable pageable);

    /**
     * Get the "id" faceOrder.
     *
     * @param id the id of the entity
     * @return the entity
     */
    FaceOrderDTO findOne(Long id);

    /**
     * Delete the "id" faceOrder.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

	String createOrder(FaceOrderDTO faceOrderDTO);

	FaceOrderDTO faceOrderNotify(PayNotifyDTO payNotifyDTO);

	FaceOrderDTO findFaceOrderByOrderNo(String orderNo);
}
