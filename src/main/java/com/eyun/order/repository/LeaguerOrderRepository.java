package com.eyun.order.repository;

import com.eyun.order.domain.LeaguerOrder;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the LeaguerOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeaguerOrderRepository extends JpaRepository<LeaguerOrder, Long>, JpaSpecificationExecutor<LeaguerOrder> {

}
