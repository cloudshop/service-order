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

import com.eyun.order.domain.LeaguerOrder;
import com.eyun.order.domain.*; // for static metamodels
import com.eyun.order.repository.LeaguerOrderRepository;
import com.eyun.order.service.dto.LeaguerOrderCriteria;

import com.eyun.order.service.dto.LeaguerOrderDTO;
import com.eyun.order.service.mapper.LeaguerOrderMapper;

/**
 * Service for executing complex queries for LeaguerOrder entities in the database.
 * The main input is a {@link LeaguerOrderCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeaguerOrderDTO} or a {@link Page} of {@link LeaguerOrderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeaguerOrderQueryService extends QueryService<LeaguerOrder> {

    private final Logger log = LoggerFactory.getLogger(LeaguerOrderQueryService.class);


    private final LeaguerOrderRepository leaguerOrderRepository;

    private final LeaguerOrderMapper leaguerOrderMapper;

    public LeaguerOrderQueryService(LeaguerOrderRepository leaguerOrderRepository, LeaguerOrderMapper leaguerOrderMapper) {
        this.leaguerOrderRepository = leaguerOrderRepository;
        this.leaguerOrderMapper = leaguerOrderMapper;
    }

    /**
     * Return a {@link List} of {@link LeaguerOrderDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeaguerOrderDTO> findByCriteria(LeaguerOrderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<LeaguerOrder> specification = createSpecification(criteria);
        return leaguerOrderMapper.toDto(leaguerOrderRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeaguerOrderDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaguerOrderDTO> findByCriteria(LeaguerOrderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<LeaguerOrder> specification = createSpecification(criteria);
        final Page<LeaguerOrder> result = leaguerOrderRepository.findAll(specification, page);
        return result.map(leaguerOrderMapper::toDto);
    }

    /**
     * Function to convert LeaguerOrderCriteria to a {@link Specifications}
     */
    private Specifications<LeaguerOrder> createSpecification(LeaguerOrderCriteria criteria) {
        Specifications<LeaguerOrder> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), LeaguerOrder_.id));
            }
            if (criteria.getOrderNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrderNo(), LeaguerOrder_.orderNo));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatus(), LeaguerOrder_.status));
            }
            if (criteria.getUserid() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserid(), LeaguerOrder_.userid));
            }
            if (criteria.getPayment() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPayment(), LeaguerOrder_.payment));
            }
            if (criteria.getTicket() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTicket(), LeaguerOrder_.ticket));
            }
            if (criteria.getPayType() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPayType(), LeaguerOrder_.payType));
            }
            if (criteria.getPayNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPayNo(), LeaguerOrder_.payNo));
            }
            if (criteria.getPayTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPayTime(), LeaguerOrder_.payTime));
            }
            if (criteria.getCreatedTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedTime(), LeaguerOrder_.createdTime));
            }
            if (criteria.getUpdatedTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedTime(), LeaguerOrder_.updatedTime));
            }
            if (criteria.getDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getDeleted(), LeaguerOrder_.deleted));
            }
        }
        return specification;
    }

}
