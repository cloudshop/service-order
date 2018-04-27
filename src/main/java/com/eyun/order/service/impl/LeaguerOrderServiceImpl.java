package com.eyun.order.service.impl;

import com.eyun.order.service.LeaguerOrderService;
import com.eyun.order.domain.LeaguerOrder;
import com.eyun.order.domain.ProOrder;
import com.eyun.order.repository.LeaguerOrderRepository;
import com.eyun.order.service.dto.LeaguerOrderDTO;
import com.eyun.order.service.dto.PayNotifyDTO;
import com.eyun.order.service.mapper.LeaguerOrderMapper;
import com.eyun.order.web.rest.errors.BadRequestAlertException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing LeaguerOrder.
 */
@Service
@Transactional
public class LeaguerOrderServiceImpl implements LeaguerOrderService {

    private final Logger log = LoggerFactory.getLogger(LeaguerOrderServiceImpl.class);

    private final LeaguerOrderRepository leaguerOrderRepository;

    private final LeaguerOrderMapper leaguerOrderMapper;

    public LeaguerOrderServiceImpl(LeaguerOrderRepository leaguerOrderRepository, LeaguerOrderMapper leaguerOrderMapper) {
        this.leaguerOrderRepository = leaguerOrderRepository;
        this.leaguerOrderMapper = leaguerOrderMapper;
    }

    /**
     * Save a leaguerOrder.
     *
     * @param leaguerOrderDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public LeaguerOrderDTO save(LeaguerOrderDTO leaguerOrderDTO) {
        log.debug("Request to save LeaguerOrder : {}", leaguerOrderDTO);
        LeaguerOrder leaguerOrder = leaguerOrderMapper.toEntity(leaguerOrderDTO);
        leaguerOrder = leaguerOrderRepository.save(leaguerOrder);
        return leaguerOrderMapper.toDto(leaguerOrder);
    }

    /**
     * Get all the leaguerOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LeaguerOrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaguerOrders");
        return leaguerOrderRepository.findAll(pageable)
            .map(leaguerOrderMapper::toDto);
    }

    /**
     * Get one leaguerOrder by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public LeaguerOrderDTO findOne(Long id) {
        log.debug("Request to get LeaguerOrder : {}", id);
        LeaguerOrder leaguerOrder = leaguerOrderRepository.findOne(id);
        return leaguerOrderMapper.toDto(leaguerOrder);
    }

    /**
     * Delete the leaguerOrder by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeaguerOrder : {}", id);
        leaguerOrderRepository.delete(id);
    }

	@Override
	public LeaguerOrderDTO leaguerOrderNotify(PayNotifyDTO payNotifyDTO) {
		LeaguerOrder leaguerOrder = leaguerOrderRepository.findByOrderNo(payNotifyDTO.getOrderNo());
		if (leaguerOrder.getStatus() != 1) {
			throw new BadRequestAlertException("订单不是未支付状态", "order", "orderStatusError");
		}
		leaguerOrder.setStatus(2);
		leaguerOrder.setPayNo(payNotifyDTO.getPayNo());
		LeaguerOrder save = leaguerOrderRepository.save(leaguerOrder);
		return leaguerOrderMapper.toDto(save);
	}
}
