package com.eyun.order.repository;

import com.eyun.order.domain.FaceOrder;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the FaceOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FaceOrderRepository extends JpaRepository<FaceOrder, Long>, JpaSpecificationExecutor<FaceOrder> {

	public FaceOrder findOrderByOrderNo(String orderNo);

}
