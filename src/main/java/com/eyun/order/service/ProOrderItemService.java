package com.eyun.order.service;

import com.eyun.order.domain.ProOrder;
import com.eyun.order.domain.ProOrderItem;
import com.eyun.order.service.dto.ProOrderItemDTO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing ProOrderItem.
 */
public interface ProOrderItemService {

    /**
     * Save a proOrderItem.
     *
     * @param proOrderItemDTO the entity to save
     * @return the persisted entity
     */
    ProOrderItemDTO save(ProOrderItemDTO proOrderItemDTO);

    /**
     * Get all the proOrderItems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ProOrderItemDTO> findAll(Pageable pageable);

    /**
     * Get the "id" proOrderItem.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ProOrderItemDTO findOne(Long id);

    /**
     * Delete the "id" proOrderItem.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

	ProOrderItem getProOrderItem(ProOrderItemDTO proOrderItem, ProOrder proOrder);

}
