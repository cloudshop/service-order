package com.eyun.order.service;

import com.eyun.order.service.dto.ProOrderDTO;
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
}
