package com.eyun.order.repository;

import com.eyun.order.domain.ProOrder;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ProOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProOrderRepository extends JpaRepository<ProOrder, Long>, JpaSpecificationExecutor<ProOrder> {

}
