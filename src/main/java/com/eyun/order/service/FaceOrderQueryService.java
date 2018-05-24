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

import com.eyun.order.domain.FaceOrder;
import com.eyun.order.domain.*; // for static metamodels
import com.eyun.order.repository.FaceOrderRepository;
import com.eyun.order.service.dto.FaceOrderCriteria;

import com.eyun.order.service.dto.FaceOrderDTO;
import com.eyun.order.service.mapper.FaceOrderMapper;

/**
 * Service for executing complex queries for FaceOrder entities in the database.
 * The main input is a {@link FaceOrderCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FaceOrderDTO} or a {@link Page} of {@link FaceOrderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FaceOrderQueryService extends QueryService<FaceOrder> {

    private final Logger log = LoggerFactory.getLogger(FaceOrderQueryService.class);


    private final FaceOrderRepository faceOrderRepository;

    private final FaceOrderMapper faceOrderMapper;

    public FaceOrderQueryService(FaceOrderRepository faceOrderRepository, FaceOrderMapper faceOrderMapper) {
        this.faceOrderRepository = faceOrderRepository;
        this.faceOrderMapper = faceOrderMapper;
    }

    /**
     * Return a {@link List} of {@link FaceOrderDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FaceOrderDTO> findByCriteria(FaceOrderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<FaceOrder> specification = createSpecification(criteria);
        return faceOrderMapper.toDto(faceOrderRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FaceOrderDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FaceOrderDTO> findByCriteria(FaceOrderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<FaceOrder> specification = createSpecification(criteria);
        final Page<FaceOrder> result = faceOrderRepository.findAll(specification, page);
        return result.map(faceOrderMapper::toDto);
    }

    /**
     * Function to convert FaceOrderCriteria to a {@link Specifications}
     */
    private Specifications<FaceOrder> createSpecification(FaceOrderCriteria criteria) {
        Specifications<FaceOrder> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), FaceOrder_.id));
            }
            if (criteria.getOrderNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrderNo(), FaceOrder_.orderNo));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getType(), FaceOrder_.type));
            }
            if (criteria.getBuserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBuserId(), FaceOrder_.buserId));
            }
            if (criteria.getCuserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCuserId(), FaceOrder_.cuserId));
            }
            if (criteria.getPayment() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPayment(), FaceOrder_.payment));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), FaceOrder_.amount));
            }
            if (criteria.getBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBalance(), FaceOrder_.balance));
            }
            if (criteria.getTicket() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTicket(), FaceOrder_.ticket));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatus(), FaceOrder_.status));
            }
            if (criteria.getCreatedTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedTime(), FaceOrder_.createdTime));
            }
            if (criteria.getUpdatedTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedTime(), FaceOrder_.updatedTime));
            }
            if (criteria.getTransferAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransferAmount(), FaceOrder_.transferAmount));
            }
            if (criteria.getTransfer() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransfer(), FaceOrder_.transfer));
            }
        }
        return specification;
    }

}
