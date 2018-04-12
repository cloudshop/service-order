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
	@Query(value = "SELECT * FROM pro_order_item left JOIN pro_order p on pro_order_id = p.`id` WHERE b_userid = ?1 LIMIT ?2,?3  ",nativeQuery = true)
	public List<ProOrderItem> getOrderItemByUserId(Long id,Integer page,Integer size);
	
	@Query(value = "select * from pro_order_item left join pro_order  p on pro_order_id = p.`id` where b_userid = ?1 and status = ?2 Limit ?3,?4",nativeQuery = true)
	public List<ProOrderItem> findOrderByStatuAndUserid(long l, int status,Integer page,Integer size);
	
	@Query(value = "SELECT * FROM pro_order_item LEFT JOIN pro_order p ON pro_order_id = p.`id` "
			+ "WHERE b_userid = ?1 AND (STATUS = 3 OR STATUS = 2 OR STATUS =4) limit ?2,?3",nativeQuery = true)
	public List<ProOrderItem> findDispatchItems(long l, int page, int size);
}
