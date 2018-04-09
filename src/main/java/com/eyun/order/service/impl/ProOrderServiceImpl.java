package com.eyun.order.service.impl;

import com.eyun.order.service.ProOrderService;
import com.eyun.order.domain.ProOrder;
import com.eyun.order.domain.ProOrderItem;
import com.eyun.order.repository.ProOrderRepository;
import com.eyun.order.service.dto.ProOrderDTO;
import com.eyun.order.service.dto.ProOrderItemDTO;
import com.eyun.order.service.mapper.ProOrderMapper;
import com.eyun.order.web.rest.util.OrderNoUtil;

import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing ProOrder.
 */
@Service
@Transactional
public class ProOrderServiceImpl implements ProOrderService {
	

    private final Logger log = LoggerFactory.getLogger(ProOrderServiceImpl.class);

    private final ProOrderRepository proOrderRepository;
    
    @Autowired
    private ProOrderItemServiceImpl proOrderItemServiceImpl;
    
    private final ProOrderMapper proOrderMapper;
    
   
    


    public ProOrderServiceImpl(ProOrderRepository proOrderRepository, ProOrderMapper proOrderMapper) {
        this.proOrderRepository = proOrderRepository;
        this.proOrderMapper = proOrderMapper;
    }

    /**
     * Save a proOrder.
     * 创建总的订单 详情订单
     * 1.创建总订单（设置：订单号，用户id，状态，创建时间，更改时间，评价默认值，删除默认值）
     * 2.创建订单详情（设置：创建时间，更改时间,订单id）
     * 3.定时器
     * 4.库存
     * 5.购物车
     * 6.支付
     * @param proOrderDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ProOrderDTO save(ProOrderDTO proOrderDTO) {
        log.debug("Request to save ProOrder : {}", proOrderDTO);
        proOrderDTO.setOrderNo(OrderNoUtil.getOrderNoUtil());
        proOrderDTO.setStatus(1);
        proOrderDTO.setCreatedTime(Instant.now());
        proOrderDTO.setUpdateTime(Instant.now());
        proOrderDTO.setDeletedB(false);
        Set<ProOrderItemDTO> proOrderItems = proOrderDTO.getProOrderItems();
        ProOrder proOrder = proOrderMapper.toEntity(proOrderDTO);
        ProOrder proOrders = proOrderRepository.save(proOrder);

        for (ProOrderItemDTO proOrderItem : proOrderItems) {
        	proOrderItem.setCreatedTime(Instant.now());
			proOrderItem.setUpdatedTime(Instant.now());
			proOrderItem.setProOrderId(proOrder.getId());
	    	ProOrderItemDTO save = proOrderItemServiceImpl.save(proOrderItem);	
			System.out.println("***"+proOrderItem.toString());
		}
        return proOrderMapper.toDto(proOrder);
    }

    /**
     * Get all the proOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProOrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProOrders");
        return proOrderRepository.findAll(pageable)
            .map(proOrderMapper::toDto);
    }

    /**
     * Get one proOrder by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ProOrderDTO findOne(Long id) {
        log.debug("Request to get ProOrder : {}", id);
        ProOrder proOrder = proOrderRepository.findOne(id);
        return proOrderMapper.toDto(proOrder);
    }

    /**
     * Delete the proOrder by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProOrder : {}", id);
        proOrderRepository.delete(id);
    }

	@Override
	public ProOrder save(ProOrder proOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	
/*	public List<Integer> findUnprocessOrders() {
		return proOrderRepository.findOrders();
	}
*/
	@Override
	public void updateOrderById() {
		List<BigInteger> ordersId = proOrderRepository.findOrders();
		for (BigInteger btId : ordersId) {
			ProOrder order = proOrderRepository.findOne(btId.longValue());
			order.setStatus(5);
			order.setDeletedB(true);
			order.setUpdateTime(Instant.now());
			proOrderRepository.save(order);
			System.out.println("定时调度+++++++"+order.toString());
		}
	}

	@Override
	public List<ProOrder> findUnprocessOrders() {
		// TODO Auto-generated method stub
		return null;
	}


}
