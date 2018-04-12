package com.eyun.order.repository;

import com.eyun.order.domain.ProOrder;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ProOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProOrderRepository extends JpaRepository<ProOrder, Long>, JpaSpecificationExecutor<ProOrder> {
	@Query(value = "SELECT o.id FROM pro_order o WHERE o.status = 1 and  TIMESTAMPDIFF(SECOND,NOW(),UPDATE_TIME) > 1800",nativeQuery = true)
	public List<BigInteger> findOrders();
	}
