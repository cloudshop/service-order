package com.eyun.order.service;

import com.eyun.order.domain.LeaguerOrder;
import com.eyun.order.service.dto.LeaguerOrderDTO;
import com.eyun.order.service.dto.PayNotifyDTO;
import com.eyun.order.service.dto.UserDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing LeaguerOrder.
 */
public interface LeaguerOrderService {

    /**
     * Save a leaguerOrder.
     *
     * @param leaguerOrderDTO the entity to save
     * @return the persisted entity
     */
    LeaguerOrderDTO save(LeaguerOrderDTO leaguerOrderDTO);

    /**
     * Get all the leaguerOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<LeaguerOrderDTO> findAll(Pageable pageable);

    /**
     * Get the "id" leaguerOrder.
     *
     * @param id the id of the entity
     * @return the entity
     */
    LeaguerOrderDTO findOne(Long id);

    /**
     * Delete the "id" leaguerOrder.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

	LeaguerOrderDTO leaguerOrderNotify(PayNotifyDTO payNotifyDTO);
	
	public String createOrder(UserDTO userDto ,LeaguerOrderDTO leaguerOrderDTO);
}
