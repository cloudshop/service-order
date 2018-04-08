package com.eyun.order.service.impl;

import com.eyun.order.service.DepOrderService;
import com.eyun.order.domain.DepOrder;
import com.eyun.order.repository.DepOrderRepository;
import com.eyun.order.service.dto.DepOrderDTO;
import com.eyun.order.service.mapper.DepOrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing DepOrder.
 */
@Service
@Transactional
public class DepOrderServiceImpl implements DepOrderService {

    private final Logger log = LoggerFactory.getLogger(DepOrderServiceImpl.class);

    private final DepOrderRepository depOrderRepository;

    private final DepOrderMapper depOrderMapper;

    public DepOrderServiceImpl(DepOrderRepository depOrderRepository, DepOrderMapper depOrderMapper) {
        this.depOrderRepository = depOrderRepository;
        this.depOrderMapper = depOrderMapper;
    }

    /**
     * Save a depOrder.
     *
     * @param depOrderDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DepOrderDTO save(DepOrderDTO depOrderDTO) {
        log.debug("Request to save DepOrder : {}", depOrderDTO);
        DepOrder depOrder = depOrderMapper.toEntity(depOrderDTO);
        depOrder = depOrderRepository.save(depOrder);
        return depOrderMapper.toDto(depOrder);
    }

    /**
     * Get all the depOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DepOrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DepOrders");
        return depOrderRepository.findAll(pageable)
            .map(depOrderMapper::toDto);
    }

    /**
     * Get one depOrder by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public DepOrderDTO findOne(Long id) {
        log.debug("Request to get DepOrder : {}", id);
        DepOrder depOrder = depOrderRepository.findOne(id);
        return depOrderMapper.toDto(depOrder);
    }

    /**
     * Delete the depOrder by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DepOrder : {}", id);
        depOrderRepository.delete(id);
    }
}
