package com.eyun.order.service.impl;

import com.eyun.order.service.ProOrderItemService;
import com.eyun.order.domain.ProOrderItem;
import com.eyun.order.repository.ProOrderItemRepository;
import com.eyun.order.service.dto.ProOrderItemDTO;
import com.eyun.order.service.mapper.ProOrderItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing ProOrderItem.
 */
@Service
@Transactional
public class ProOrderItemServiceImpl implements ProOrderItemService {

    private final Logger log = LoggerFactory.getLogger(ProOrderItemServiceImpl.class);

    private final ProOrderItemRepository proOrderItemRepository;

    private final ProOrderItemMapper proOrderItemMapper;

    public ProOrderItemServiceImpl(ProOrderItemRepository proOrderItemRepository, ProOrderItemMapper proOrderItemMapper) {
        this.proOrderItemRepository = proOrderItemRepository;
        this.proOrderItemMapper = proOrderItemMapper;
    }

    /**
     * Save a proOrderItem.
     *
     * @param proOrderItemDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ProOrderItemDTO save(ProOrderItemDTO proOrderItemDTO) {
        log.debug("Request to save ProOrderItem : {}", proOrderItemDTO);
        ProOrderItem proOrderItem = proOrderItemMapper.toEntity(proOrderItemDTO);
        proOrderItem = proOrderItemRepository.save(proOrderItem);
        return proOrderItemMapper.toDto(proOrderItem);
    }

    /**
     * Get all the proOrderItems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProOrderItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProOrderItems");
        return proOrderItemRepository.findAll(pageable)
            .map(proOrderItemMapper::toDto);
    }

    /**
     * Get one proOrderItem by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ProOrderItemDTO findOne(Long id) {
        log.debug("Request to get ProOrderItem : {}", id);
        ProOrderItem proOrderItem = proOrderItemRepository.findOne(id);
        return proOrderItemMapper.toDto(proOrderItem);
    }

    /**
     * Delete the proOrderItem by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProOrderItem : {}", id);
        proOrderItemRepository.delete(id);
    }
}
