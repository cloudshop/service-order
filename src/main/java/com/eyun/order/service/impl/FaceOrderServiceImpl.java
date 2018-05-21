package com.eyun.order.service.impl;

import com.eyun.order.service.FaceOrderService;
import com.eyun.order.domain.FaceOrder;
import com.eyun.order.repository.FaceOrderRepository;
import com.eyun.order.service.dto.FaceOrderDTO;
import com.eyun.order.service.mapper.FaceOrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing FaceOrder.
 */
@Service
@Transactional
public class FaceOrderServiceImpl implements FaceOrderService {

    private final Logger log = LoggerFactory.getLogger(FaceOrderServiceImpl.class);

    private final FaceOrderRepository faceOrderRepository;

    private final FaceOrderMapper faceOrderMapper;

    public FaceOrderServiceImpl(FaceOrderRepository faceOrderRepository, FaceOrderMapper faceOrderMapper) {
        this.faceOrderRepository = faceOrderRepository;
        this.faceOrderMapper = faceOrderMapper;
    }

    /**
     * Save a faceOrder.
     *
     * @param faceOrderDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FaceOrderDTO save(FaceOrderDTO faceOrderDTO) {
        log.debug("Request to save FaceOrder : {}", faceOrderDTO);
        FaceOrder faceOrder = faceOrderMapper.toEntity(faceOrderDTO);
        faceOrder = faceOrderRepository.save(faceOrder);
        return faceOrderMapper.toDto(faceOrder);
    }

    /**
     * Get all the faceOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FaceOrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FaceOrders");
        return faceOrderRepository.findAll(pageable)
            .map(faceOrderMapper::toDto);
    }

    /**
     * Get one faceOrder by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public FaceOrderDTO findOne(Long id) {
        log.debug("Request to get FaceOrder : {}", id);
        FaceOrder faceOrder = faceOrderRepository.findOne(id);
        return faceOrderMapper.toDto(faceOrder);
    }

    /**
     * Delete the faceOrder by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FaceOrder : {}", id);
        faceOrderRepository.delete(id);
    }
}
