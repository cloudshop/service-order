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
import com.eyun.order.repository.ProOrderItemRepository;
import com.eyun.order.repository.ProOrderRepository;
import com.eyun.order.service.dto.PayNotifyDTO;
import com.eyun.order.service.dto.ProOrderDTO;
import com.eyun.order.service.dto.ProOrderItemDTO;
import com.eyun.order.service.dto.ProductSkuDTO;
import com.eyun.order.service.dto.UserDTO;
import com.eyun.order.service.mapper.ProOrderMapper;
import com.eyun.order.web.rest.errors.BadRequestAlertException;
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
    
    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ProOrderItemRepository proOrderItemRepository;
    
    @Autowired
    private WalletService walletService;
    
    @Autowired
    private OrderUtils orderUtils;
    public ProOrderServiceImpl(ProOrderRepository proOrderRepository, ProOrderMapper proOrderMapper) {
        this.proOrderRepository = proOrderRepository;
        this.proOrderMapper = proOrderMapper;
    }

	/**
	 * Save a proOrder. 创建总的订单 详情订单
	 * 1.创建总订单（设置：订单号，用户id，状态，创建时间，更改时间，评价默认值，删除默认值） 2.创建订单详情（设置：创建时间，更改时间,订单id）
	 * 3.定时器 4.库存 5.购物车 6.支付 订单状态：[1.未支付，2.已付款(未发货)，3.已发货，4.交易成功，5.交易关闭]
	 */
	@Override
	public String createOrder(ProOrderDTO proOrderDTO, Integer type) {
		
		String orderString = "";
		Long orderId = null;
		List<ProOrderItemDTO> itemList = new ArrayList<>();
		/*try {	*/
			BigDecimal totalPrice = new BigDecimal(0);
			String sbody = "";
			List skuAll = new ArrayList<Long>();// 统计所有订单的子项
			proOrderDTO.setOrderNo(OrderNoUtil.getOrderNoUtil());// 设置订单编号
//			proOrderDTO.setStatus(1);// 设置订单状态
			proOrderDTO.setCreatedTime(Instant.now());// 设置创建时间
			proOrderDTO.setUpdateTime(Instant.now());// 设置更新时间
			proOrderDTO.setDeletedB(false);// 初始化删除状态
			Set<ProOrderItemDTO> proOrderItems = proOrderDTO.getProOrderItems();// 获取订单的所有子项
			ProOrder proOrder = proOrderMapper.toEntity(proOrderDTO);// proOrderDTO转换实体，但是proOrderDTO中的Item并没有转
			ProOrder save = proOrderRepository.save(proOrder);
			orderId = save.getId();
			for (ProOrderItemDTO proOrderItemDTO : proOrderItems) {
				ProOrderItem pro = new ProOrderItem();
				pro.setCount(proOrderItemDTO.getCount());
				pro.setCreatedTime(Instant.now());
				pro.setUpdatedTime(Instant.now());
				pro.setPrice(proOrderItemDTO.getPrice());
				pro.setProOrder(proOrder);
				pro.setProductSkuId(proOrderItemDTO.getProductSkuId());
				// 计算总价
				totalPrice =  totalPrice.add(proOrderItemDTO.getPrice().multiply(new BigDecimal(proOrderItemDTO.getCount())));
				// 更改库存
				
				Integer i = new Integer(0);
				Map updateProductSkuCount = proService.updateProductSkuCount(i, proOrderItemDTO.getProductSkuId(),
						proOrderItemDTO.getCount());
				itemList.add(proOrderItemDTO);
				
				System.out.println("updateProductSkuCount " + updateProductSkuCount);
				String message = (String) updateProductSkuCount.get("message");
				if (message.equals("failed")) {
					return orderString = "库存不足";
				}
				ProductSkuDTO pros = proService.getProductSku(proOrderItemDTO.getProductSkuId());
				skuAll.add(proOrderItemDTO.getProductSkuId());
				proOrder.getProOrderItems().add(pro);
				proOrderItemRepository.saveAndFlush(pro);
			}
			//计算总价totalPrice
			totalPrice = totalPrice.add(proOrder.getPostFee());
			proOrder.setPayment(totalPrice);
			if (type == 0) {
				shoppingCartService.del(skuAll);
			}	
			ProOrder proOrder2 = proOrderRepository.saveAndFlush(proOrder);
			System.out.println("订单id" + orderId);
			switch (proOrderDTO.getPaymentType()) {
			case 1:// 余额支付
				Wallet userWallet = walletService.getUserWallet();
				BigDecimal balance = userWallet.getBalance();
				BigDecimal subtract = balance.subtract(totalPrice);
				if (subtract.doubleValue() < 0.00) {
					orderString = "账户余额不足";
					//待付款状态
					proOrder2.setStatus(1);
				} else {
					orderString = proOrder.getOrderNo();
					proOrder2.setOrderString(orderString);
					//待付款状态
					proOrder2.setStatus(1);
				}
				break;
			case 2:// 支付宝支付
				AlipayDTO apiPayDTO = new AlipayDTO("贡融积分商城", proOrder.getOrderNo(), "product", "支付", "30m",
						totalPrice.toString());
				orderString = payService.createAlipayAppOrder(apiPayDTO);
				proOrder2.setOrderString(orderString);
				proOrder2.setStatus(1);
				break;
			default:
				break;
			}
		/*} catch (Exception e) {
			//出现任何异常 库存返回，并返回页面“订单失败“,1.需要更改订单状态（取消订单）2.商品回库存
			ProOrder order = proOrderRepository.getOne(orderId);
			order.setDeletedB(true);
			proOrderRepository.save(order);
			
			orderString = "订单失败" + e;	
		}*/
		proOrderRepository.saveAndFlush(proOrder2);
		
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
			// 交易关闭
			order.setStatus(5);
			order.setDeletedB(true);
			order.setUpdateTime(Instant.now());
			proOrderRepository.save(order);
			System.out.println("定时调度++++" + order.toString());
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
		int key = (page-1)*size;
		List<ProOrder> allProOrderByUser = proOrderRepository.getOrderByUserId(1l,key,size);
		List<ProOrderBO> showOrder = orderUtils.showOrder(allProOrderByUser);	
		return showOrder;
	}
	
	@Override
	public List<ProOrderBO> findOrderByStatuAndUserid(Long userId, Integer status,Integer page,Integer size) {
		List<ProOrder> orders = proOrderRepository.findOrderByStatuAndUserid(userId,status,(page-1)*size,size);
		List<ProOrderBO> showOrder = orderUtils.showOrder(orders);
		return showOrder;
	}

	@Override
	public List<ProOrderBO> findDispatchItems(long l, int page, int size) {
		List<ProOrder> orders = proOrderRepository.findDispatchItems(l,(page-1)*size,size);
		List<ProOrderBO> showOrder	= orderUtils.showOrder(orders);
		return showOrder;
	}

	@Override
	public ProOrderDTO findOrderByOrderNo(String orderNo) {
		ProOrder proOrder = proOrderRepository.findOrderByOrderNo(orderNo);
		return proOrderMapper.toDto(proOrder);
	}

	@Override
	public ProOrderDTO proOrderNotify(PayNotifyDTO payNotifyDTO) {
		ProOrder proOrder = proOrderRepository.findOrderByOrderNo(payNotifyDTO.getOrderNo());
		if (proOrder.getStatus() != 1) {
			throw new BadRequestAlertException("订单不是未支付状态", "order", "orderStatusError");
		}
		proOrder.setStatus(2);
		proOrder.setPayNo(payNotifyDTO.getPayNo());
		ProOrder save = proOrderRepository.save(proOrder);
		return proOrderMapper.toDto(save);
	}

	@Override
	public List<ProOrderBO> findAllOrder(long l, int page, int size) {
		List<ProOrder> orderByUserId = proOrderRepository.getOrderByUserId(l,(page-1)*size,size);
		List<ProOrderBO> showOrder = orderUtils.showOrder(orderByUserId);
		return showOrder;
	}

	@Override
	public Boolean updateOrderStatus(String string, Integer integer) {
		ProOrder proOrder = proOrderRepository.getOrderByOrderNo(string);
		proOrder.setStatus(integer);
		ProOrder save = proOrderRepository.save(proOrder);
		if(save == null){
			return  false;
		}
		return true;
	}


}
