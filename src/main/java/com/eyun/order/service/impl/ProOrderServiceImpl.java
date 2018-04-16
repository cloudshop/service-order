package com.eyun.order.service.impl;

import com.eyun.order.service.PayService;
import com.eyun.order.service.ProOrderBO;
import com.eyun.order.service.ProOrderService;
import com.eyun.order.service.ProService;
import com.eyun.order.service.ShoppingCartService;
import com.eyun.order.service.WalletService;
import com.eyun.order.domain.ProOrder;
import com.eyun.order.domain.ProOrderItem;
import com.eyun.order.domain.Wallet;
import com.eyun.order.domain.vo.AlipayDTO;
import com.eyun.order.repository.ProOrderRepository;
import com.eyun.order.service.dto.ProOrderDTO;
import com.eyun.order.service.dto.ProOrderItemDTO;
import com.eyun.order.service.dto.ProductSkuDTO;
import com.eyun.order.service.mapper.ProOrderMapper;
import com.eyun.order.web.rest.util.OrderNoUtil;
import com.eyun.order.web.rest.util.OrderUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @Autowired
    private ProService proService;
    @Autowired
    private PayService payService;
    
    private BigDecimal totalPrice;
    @Autowired
    private ShoppingCartService shoppingCartService;
    
    @Autowired
    private WalletService walletService;
    
    @Autowired
    private OrderUtils orderUtils;
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
     * 订单状态：1.未支付，2.已付款，3.未发货，4，已发货，5.交易成功，6.交易关闭
     */
    @Override
    public String createOrder(ProOrderDTO proOrderDTO) {
        log.debug("Request to save ProOrder : {}", proOrderDTO);
        String sbody = "";
        String orderString ="";
        List skuAll = new ArrayList<Long>();
        log.debug("proOrderDTO的支付类型" + proOrderDTO.getPaymentType());
        switch (proOrderDTO.getPaymentType()){
        case 1://余额支付
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
    			BigDecimal bPrice = proOrderItem.getPrice();
    			Integer count = proOrderItem.getCount();
    			BigDecimal bCount = new BigDecimal(count); 
    			totalPrice = bPrice.multiply(bCount);
    	    	Integer i = new Integer(0);
    	    	//更改库存
    	        Map updateProductSkuCount = proService.updateProductSkuCount(i, proOrderItem.getProductSkuId(),proOrderItem.getCount());
    	        String message = (String)updateProductSkuCount.get("message");
    	        System.out.println(message);
    	       /* if(message.equals("failed")){
    	        	return "库存不足";
    	        }*/
    	        ProductSkuDTO pro = proService.getProductSku(proOrderItem.getProductSkuId());
    	        sbody += pro.getSkuName();
    	        skuAll.add(proOrderItem.getProductSkuId());
    		}
            
         // 更改 购物车（userId）
            shoppingCartService.del(skuAll);
            totalPrice = totalPrice.add(proOrder.getPostFee());
            proOrder.setPayment(totalPrice);

         //判断余额
            Wallet userWallet = walletService.getUserWallet();
            BigDecimal balance = userWallet.getBalance();
            BigDecimal subtract = balance.subtract(totalPrice);
            if (subtract.doubleValue() < 0.00) {
            //if(totalPrice.compareTo(balance) == -1 ){
            	orderString = "账户余额不足";
            }else{
                ProOrder save = proOrderRepository.save(proOrder);
                orderString = save.getOrderNo();
            }
            break;
        case 2://支付宝支付
        	//余额支付
        	proOrderDTO.setOrderNo(OrderNoUtil.getOrderNoUtil());
            proOrderDTO.setStatus(1);
            proOrderDTO.setCreatedTime(Instant.now());
            proOrderDTO.setUpdateTime(Instant.now());
            proOrderDTO.setDeletedB(false);
            Set<ProOrderItemDTO> proOrderItems1 = proOrderDTO.getProOrderItems();
            ProOrder proOrder1 = proOrderMapper.toEntity(proOrderDTO);
            ProOrder proOrders1 = proOrderRepository.save(proOrder1);
            log.debug("添加商品订单详细属性");
            for (ProOrderItemDTO proOrderItem : proOrderItems1) {
            	proOrderItem.setCreatedTime(Instant.now());
    			proOrderItem.setUpdatedTime(Instant.now());
    			proOrderItem.setProOrderId(proOrder1.getId());
    			BigDecimal bPrice = proOrderItem.getPrice();
    			Integer count = proOrderItem.getCount();
    			BigDecimal bCount = new BigDecimal(count); 
    			totalPrice = bPrice.multiply(bCount);
    	    	Integer i = new Integer(0);
    	    	//更改库存
    	        Map updateProductSkuCount = proService.updateProductSkuCount(i, proOrderItem.getProductSkuId(),proOrderItem.getCount());
    	        String message = (String)updateProductSkuCount.get("message");
/*    	        if(message.equals("failed")){
    	        	return "库存不足";
    	        }*/
    	        ProductSkuDTO pro = proService.getProductSku(proOrderItem.getProductSkuId());
    	        sbody += pro.getSkuName();
    	        skuAll.add(proOrderItem.getProductSkuId());
    		}
            
            shoppingCartService.del(skuAll);
            totalPrice = totalPrice.add(proOrder1.getPostFee());
            proOrder1.setPayment(totalPrice);
            ProOrder save1 = proOrderRepository.save(proOrder1);
            AlipayDTO apiPayDTO = new AlipayDTO(sbody, save1.getOrderNo(), "product", "", "", "30m");
            orderString = payService.createAlipayAppOrder(apiPayDTO);
            
            break;  
        case 3:
        	return null;
        default:
 		   break;
         }
           return orderString;  
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
	public void updateOrderById() {
		List<BigInteger> ordersId = proOrderRepository.findOrders();
		for (BigInteger btId : ordersId) {
			ProOrder order = proOrderRepository.findOne(btId.longValue());
			//交易关闭
			order.setStatus(6);
			order.setDeletedB(true);
			order.setUpdateTime(Instant.now());
			proOrderRepository.save(order);
			System.out.println("定时调度++++"+order.toString());
		}
	}

	@Override
	public ProOrderDTO save(ProOrderDTO proOrderDTO) {
		  ProOrder proOrder = proOrderMapper.toEntity(proOrderDTO);
	      proOrderRepository.save(proOrder);
		return proOrderDTO;
	}

	@Override
	public List<ProOrderBO> getProOrderItemsByUser(int i, int page, int size) {
		// TODO Auto-generated method stub
		int key = (page-1)*size;
		List<ProOrder> allProOrderByUser = proOrderRepository.getOrderByUserId(1l,key,size);
		
		List<ProOrderBO> showOrder = orderUtils.showOrder(allProOrderByUser);
		
		return showOrder;
	}
	
	@Override
	public List<ProOrderBO> findOrderByStatuAndUserid(Long userId, Integer status,Integer page,Integer size) {
		List<ProOrder> orders = proOrderRepository.findOrderByStatuAndUserid(1l,status,(page-1)*size,size);
		List<ProOrderBO> showOrder = orderUtils.showOrder(orders);
		return showOrder;
	}

	@Override
	public List<ProOrderBO> findDispatchItems(long l, int page, int size) {
		List<ProOrder> orders = proOrderRepository.findDispatchItems(1l,(page-1)*size,size);
		List<ProOrderBO> showOrder	= orderUtils.showOrder(orders);

		return showOrder;
	}

	@Override
	public String orderItems(ProOrderDTO proOrderDTO){
	    log.debug("Request to save ProOrder : {}", proOrderDTO);
        String sbody = "";
        String orderString ="";
        List skuAll = new ArrayList<Long>();
        proOrderDTO.setOrderNo(OrderNoUtil.getOrderNoUtil());
        proOrderDTO.setStatus(1);
        proOrderDTO.setCreatedTime(Instant.now());
        proOrderDTO.setUpdateTime(Instant.now());
        proOrderDTO.setDeletedB(false);
        log.debug("配置商铺订单属性" + proOrderDTO);
        Set<ProOrderItemDTO> proOrderItems = proOrderDTO.getProOrderItems();
        ProOrder proOrder = proOrderMapper.toEntity(proOrderDTO);
        ProOrder proOrders = proOrderRepository.save(proOrder);
        log.debug("添加商品订单详细属性");
        for (ProOrderItemDTO proOrderItem : proOrderItems) {
        	proOrderItem.setCreatedTime(Instant.now());
			proOrderItem.setUpdatedTime(Instant.now());
			proOrderItem.setProOrderId(proOrder.getId());
			BigDecimal bPrice = proOrderItem.getPrice();
			Integer count = proOrderItem.getCount();
			BigDecimal bCount = new BigDecimal(count); 
			totalPrice = bPrice.multiply(bCount);
	    	ProOrderItemDTO save = proOrderItemServiceImpl.save(proOrderItem);	
	    	Integer i = new Integer(0);
	    	//更改库存
	        proService.updateProductSkuCount(i, proOrderItem.getProductSkuId(),proOrderItem.getCount());
	        ProductSkuDTO pro = proService.getProductSku(proOrderItem.getProductSkuId());
	        sbody += pro.getSkuName();
	        skuAll.add(proOrderItem.getProductSkuId());
		}
        // 更改 购物车（userId）
        totalPrice = totalPrice.add(proOrder.getPostFee());
        proOrder.setPayment(totalPrice);
        ProOrder save = proOrderRepository.save(proOrder);
        
        log.debug("调用apiPayDTO接口");
        AlipayDTO apiPayDTO = new AlipayDTO(sbody, save.getOrderNo(), "product", "", "", "30m");
        orderString = payService.createAlipayAppOrder(apiPayDTO);
        return orderString;
    }

}
