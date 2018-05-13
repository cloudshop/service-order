package com.eyun.order.service;

import com.eyun.order.domain.DepOrder;
import com.eyun.order.service.dto.DepOrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing DepOrder.
 */
public interface DepOrderService {

    /**
     * Save a depOrder.
     *
     * @param depOrderDTO the entity to save
     * @return the persisted entity
     */
    DepOrderDTO save(DepOrderDTO depOrderDTO);

    /**
     * Get all the depOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DepOrderDTO> findAll(Pageable pageable);

    /**
     * Get the "id" depOrder.
     *
     * @param id the id of the entity
     * @return the entity
     */
    DepOrderDTO findOne(Long id);

    /**
     * Delete the "id" depOrder.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

	DepOrder depositNotify(String orderNo) throws Exception;
	
}
