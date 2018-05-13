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

import com.eyun.order.domain.ProOrder;
import com.eyun.order.domain.*; // for static metamodels
import com.eyun.order.repository.ProOrderRepository;
import com.eyun.order.service.dto.ProOrderCriteria;

import com.eyun.order.service.dto.ProOrderDTO;
import com.eyun.order.service.mapper.ProOrderMapper;

/**
 * Service for executing complex queries for ProOrder entities in the database.
 * The main input is a {@link ProOrderCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProOrderDTO} or a {@link Page} of {@link ProOrderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProOrderQueryService extends QueryService<ProOrder> {

    private final Logger log = LoggerFactory.getLogger(ProOrderQueryService.class);


    private final ProOrderRepository proOrderRepository;

    private final ProOrderMapper proOrderMapper;

    public ProOrderQueryService(ProOrderRepository proOrderRepository, ProOrderMapper proOrderMapper) {
        this.proOrderRepository = proOrderRepository;
        this.proOrderMapper = proOrderMapper;
    }

    /**
     * Return a {@link List} of {@link ProOrderDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProOrderDTO> findByCriteria(ProOrderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<ProOrder> specification = createSpecification(criteria);
        return proOrderMapper.toDto(proOrderRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProOrderDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProOrderDTO> findByCriteria(ProOrderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<ProOrder> specification = createSpecification(criteria);
        final Page<ProOrder> result = proOrderRepository.findAll(specification, page);
        return result.map(proOrderMapper::toDto);
    }

    /**
     * Function to convert ProOrderCriteria to a {@link Specifications}
     */
    private Specifications<ProOrder> createSpecification(ProOrderCriteria criteria) {
        Specifications<ProOrder> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ProOrder_.id));
            }
            if (criteria.getcUserid() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getcUserid(), ProOrder_.cUserid));
            }
            if (criteria.getOrderNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrderNo(), ProOrder_.orderNo));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), ProOrder_.price));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatus(), ProOrder_.status));
            }
            if (criteria.getPayment() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPayment(), ProOrder_.payment));
            }
            if (criteria.getPaymentType() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentType(), ProOrder_.paymentType));
            }
            if (criteria.getPaymentTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentTime(), ProOrder_.paymentTime));
            }
            if (criteria.getPostFee() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPostFee(), ProOrder_.postFee));
            }
            if (criteria.getConsignTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getConsignTime(), ProOrder_.consignTime));
            }
            if (criteria.getEndTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndTime(), ProOrder_.endTime));
            }
            if (criteria.getCloseTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCloseTime(), ProOrder_.closeTime));
            }
            if (criteria.getShippingName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getShippingName(), ProOrder_.shippingName));
            }
            if (criteria.getShipingCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getShipingCode(), ProOrder_.shipingCode));
            }
            if (criteria.getBuyerMessage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBuyerMessage(), ProOrder_.buyerMessage));
            }
            if (criteria.getBuyerNick() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBuyerNick(), ProOrder_.buyerNick));
            }
            if (criteria.getBuyerRate() != null) {
                specification = specification.and(buildSpecification(criteria.getBuyerRate(), ProOrder_.buyerRate));
            }
            if (criteria.getCreatedTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedTime(), ProOrder_.createdTime));
            }
            if (criteria.getUpdateTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdateTime(), ProOrder_.updateTime));
            }
            if (criteria.getDeletedB() != null) {
                specification = specification.and(buildSpecification(criteria.getDeletedB(), ProOrder_.deletedB));
            }
            if (criteria.getDeletedC() != null) {
                specification = specification.and(buildSpecification(criteria.getDeletedC(), ProOrder_.deletedC));
            }
            if (criteria.getShopId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShopId(), ProOrder_.shopId));
            }
            if (criteria.getPayNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPayNo(), ProOrder_.payNo));
            }
            if (criteria.getOrderString() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrderString(), ProOrder_.orderString));
            }
            if (criteria.getTransferAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransferAmount(), ProOrder_.transferAmount));
            }
            if (criteria.getProOrderItemId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProOrderItemId(), ProOrder_.proOrderItems, ProOrderItem_.id));
            }
        }
        return specification;
    }

}
