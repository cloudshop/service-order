package com.eyun.order.repository;

import com.eyun.order.domain.ProOrderItem;
import com.eyun.order.service.dto.ProductSkuDTO;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ProOrderItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProOrderItemRepository extends JpaRepository<ProOrderItem, Long>, JpaSpecificationExecutor<ProOrderItem> {

}
