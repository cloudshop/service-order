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

import com.eyun.order.domain.DepOrder;
import com.eyun.order.domain.*; // for static metamodels
import com.eyun.order.repository.DepOrderRepository;
import com.eyun.order.service.dto.DepOrderCriteria;

import com.eyun.order.service.dto.DepOrderDTO;
import com.eyun.order.service.mapper.DepOrderMapper;

/**
 * Service for executing complex queries for DepOrder entities in the database.
 * The main input is a {@link DepOrderCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DepOrderDTO} or a {@link Page} of {@link DepOrderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DepOrderQueryService extends QueryService<DepOrder> {

    private final Logger log = LoggerFactory.getLogger(DepOrderQueryService.class);


    private final DepOrderRepository depOrderRepository;

    private final DepOrderMapper depOrderMapper;

    public DepOrderQueryService(DepOrderRepository depOrderRepository, DepOrderMapper depOrderMapper) {
        this.depOrderRepository = depOrderRepository;
        this.depOrderMapper = depOrderMapper;
    }

    /**
     * Return a {@link List} of {@link DepOrderDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DepOrderDTO> findByCriteria(DepOrderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<DepOrder> specification = createSpecification(criteria);
        return depOrderMapper.toDto(depOrderRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DepOrderDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DepOrderDTO> findByCriteria(DepOrderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<DepOrder> specification = createSpecification(criteria);
        final Page<DepOrder> result = depOrderRepository.findAll(specification, page);
        return result.map(depOrderMapper::toDto);
    }

    /**
     * Function to convert DepOrderCriteria to a {@link Specifications}
     */
    private Specifications<DepOrder> createSpecification(DepOrderCriteria criteria) {
        Specifications<DepOrder> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), DepOrder_.id));
            }
            if (criteria.getOrderNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrderNo(), DepOrder_.orderNo));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatus(), DepOrder_.status));
            }
            if (criteria.getUserid() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserid(), DepOrder_.userid));
            }
            if (criteria.getPayment() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPayment(), DepOrder_.payment));
            }
            if (criteria.getPayType() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPayType(), DepOrder_.payType));
            }
            if (criteria.getPayNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPayNo(), DepOrder_.payNo));
            }
            if (criteria.getPayTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPayTime(), DepOrder_.payTime));
            }
            if (criteria.getWalletId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWalletId(), DepOrder_.walletId));
            }
            if (criteria.getCreatedTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedTime(), DepOrder_.createdTime));
            }
            if (criteria.getUpdatedTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedTime(), DepOrder_.updatedTime));
            }
            if (criteria.getDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getDeleted(), DepOrder_.deleted));
            }
        }
        return specification;
    }

}
