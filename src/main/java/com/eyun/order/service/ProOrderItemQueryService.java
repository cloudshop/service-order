package com.eyun.order.service;


import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.eyun.order.domain.ProOrderItem;
import com.eyun.order.domain.*; // for static metamodels
import com.eyun.order.repository.ProOrderItemRepository;
import com.eyun.order.service.dto.ProOrderItemCriteria;

import com.eyun.order.service.dto.ProOrderItemDTO;
import com.eyun.order.service.mapper.ProOrderItemMapper;

/**
 * Service for executing complex queries for ProOrderItem entities in the database.
 * The main input is a {@link ProOrderItemCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProOrderItemDTO} or a {@link Page} of {@link ProOrderItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProOrderItemQueryService extends QueryService<ProOrderItem> {

    private final Logger log = LoggerFactory.getLogger(ProOrderItemQueryService.class);


    private final ProOrderItemRepository proOrderItemRepository;

    private final ProOrderItemMapper proOrderItemMapper;

    public ProOrderItemQueryService(ProOrderItemRepository proOrderItemRepository, ProOrderItemMapper proOrderItemMapper) {
        this.proOrderItemRepository = proOrderItemRepository;
        this.proOrderItemMapper = proOrderItemMapper;
    }

    /**
     * Return a {@link List} of {@link ProOrderItemDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProOrderItemDTO> findByCriteria(ProOrderItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<ProOrderItem> specification = createSpecification(criteria);
        return proOrderItemMapper.toDto(proOrderItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProOrderItemDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProOrderItemDTO> findByCriteria(ProOrderItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<ProOrderItem> specification = createSpecification(criteria);
        final Page<ProOrderItem> result = proOrderItemRepository.findAll(specification, page);
        return result.map(proOrderItemMapper::toDto);
    }

    /**
     * Function to convert ProOrderItemCriteria to a {@link Specifications}
     */
    private Specifications<ProOrderItem> createSpecification(ProOrderItemCriteria criteria) {
        Specifications<ProOrderItem> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ProOrderItem_.id));
            }
            if (criteria.getProductSkuId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProductSkuId(), ProOrderItem_.productSkuId));
            }
            if (criteria.getCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCount(), ProOrderItem_.count));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), ProOrderItem_.price));
            }
            if (criteria.getCreatedTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedTime(), ProOrderItem_.createdTime));
            }
            if (criteria.getUpdatedTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedTime(), ProOrderItem_.updatedTime));
            }
            if (criteria.getProOrderId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProOrderId(), ProOrderItem_.proOrder, ProOrder_.id));
            }
        }
        return specification;
    }

}
