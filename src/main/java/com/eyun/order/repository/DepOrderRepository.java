package com.eyun.order.repository;

import com.eyun.order.domain.DepOrder;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the DepOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepOrderRepository extends JpaRepository<DepOrder, Long>, JpaSpecificationExecutor<DepOrder> {

	public DepOrder findByOrderNoAndStatus(String orderNo,Integer status);
	
}
