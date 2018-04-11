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
	@Query(value = "SELECT * FROM pro_order_item INNER JOIN pro_order WHERE b_userid = ?1 LIMIT ?2,?3  ",nativeQuery = true)
	public List<ProOrderItem> getOrderItemByUserId(Long id,Integer page,Integer size);
}
