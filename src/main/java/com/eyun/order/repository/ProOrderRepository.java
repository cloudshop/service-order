package com.eyun.order.repository;

import com.eyun.order.domain.ProOrder;
import com.eyun.order.domain.ProOrderItem;
import com.eyun.order.service.dto.ProOrderDTO;

import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ProOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProOrderRepository extends JpaRepository<ProOrder, Long>, JpaSpecificationExecutor<ProOrder> {
	@Query(value = "SELECT o.id FROM pro_order o WHERE o.status = 1 and  TIMESTAMPDIFF(SECOND,NOW(),UPDATE_TIME) > 1800",nativeQuery = true)
	public List<BigInteger> findOrders();
	
	@Query(value = "select * from pro_order o where status in (2,3) and c_userid = ?1 limit ?2,?3 ",nativeQuery = true)
	public List<ProOrder> findDispatchItems(long l, int i, int size);
	
	@Query(value = "SELECT * FROM pro_order p  WHERE c_userid = ?1 LIMIT ?2,?3  ",nativeQuery = true)
	public List<ProOrder> getOrderByUserId(Long id,Integer page,Integer size);
	
	@Query(value = "select * from  pro_order  p  where c_userid = ?1 and status = ?2 Limit ?3,?4",nativeQuery = true)
	public List<ProOrder> findOrderByStatuAndUserid(long l, int status,Integer page,Integer size);

	public ProOrder findOrderByOrderNo(String orderNo);
	
}
