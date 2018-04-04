package com.eyun.order.service.impl;

import com.eyun.order.service.ProOrderService;
import com.eyun.order.domain.ProOrder;
import com.eyun.order.repository.ProOrderRepository;
import com.eyun.order.service.dto.ProOrderDTO;
import com.eyun.order.service.mapper.ProOrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing ProOrder.
 */
@Service
@Transactional
public class ProOrderServiceImpl implements ProOrderService {

    private final Logger log = LoggerFactory.getLogger(ProOrderServiceImpl.class);

    private final ProOrderRepository proOrderRepository;

    private final ProOrderMapper proOrderMapper;

    public ProOrderServiceImpl(ProOrderRepository proOrderRepository, ProOrderMapper proOrderMapper) {
        this.proOrderRepository = proOrderRepository;
        this.proOrderMapper = proOrderMapper;
    }

    /**
     * Save a proOrder.
     *
     * @param proOrderDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ProOrderDTO save(ProOrderDTO proOrderDTO) {
        log.debug("Request to save ProOrder : {}", proOrderDTO);
        ProOrder proOrder = proOrderMapper.toEntity(proOrderDTO);
        proOrder = proOrderRepository.save(proOrder);
        return proOrderMapper.toDto(proOrder);
    }

    /**
     * Get all the proOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProOrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProOrders");
        return proOrderRepository.findAll(pageable)
            .map(proOrderMapper::toDto);
    }

    /**
     * Get one proOrder by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ProOrderDTO findOne(Long id) {
        log.debug("Request to get ProOrder : {}", id);
        ProOrder proOrder = proOrderRepository.findOne(id);
        return proOrderMapper.toDto(proOrder);
    }

    /**
     * Delete the proOrder by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProOrder : {}", id);
        proOrderRepository.delete(id);
    }
}
