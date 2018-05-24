package com.eyun.order.service.impl;

import com.eyun.order.service.CommisionService;
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
import com.eyun.order.service.dto.OrderDateDTO;
import com.eyun.order.service.dto.PageOrder;
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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    private CommisionService commissionService;

    @Autowired
    private OrderUtils orderUtils;
    public ProOrderServiceImpl(ProOrderRepository proOrderRepository, ProOrderMapper proOrderMapper) {
        this.proOrderRepository = proOrderRepository;
        this.proOrderMapper = proOrderMapper;
    }

	/**
	 * Save a proOrder. 创建总的订单 详情订单
	 * 1.创建总订单（设置：订单号，用户id，状态，创建时间，更改时间，评价默认值，删除默认值） 2.创建订单详情（设置：创建时间，更改时间,订单id）
	 * 3.定时器 4.库存 5.购物车 6.支付 订单状态：[1.未支付，2.已付款(未发货)，3.已发货，4.交易成功，5.交易关闭] 7. 计算支付金额，让利额，让利之前的金额
	 *
	 */
	@Override
	public String createOrder(ProOrderDTO proOrderDTO, Integer type) {

		String orderString = "";
		Long orderId = null;
		List<ProOrderItemDTO> itemList = new ArrayList<>();
			BigDecimal payment = new BigDecimal(0);
			BigDecimal price = new BigDecimal(0);
			BigDecimal transfor_amount = new BigDecimal(0);
			String sbody = "";
			List skuAll = new ArrayList<Long>();// 统计所有订单的子项
			proOrderDTO.setOrderNo(OrderNoUtil.getOrderNoUtil());// 设置订单编号
			proOrderDTO.setCreatedTime(Instant.now());// 设置创建时间
			proOrderDTO.setUpdateTime(Instant.now());// 设置更新时间
			proOrderDTO.setDeletedB(false);// 初始化删除状态
			proOrderDTO.setDeletedC(false);
			Set<ProOrderItemDTO> proOrderItems = proOrderDTO.getProOrderItems();// 获取订单的所有子项
			ProOrder proOrder = proOrderMapper.toEntity(proOrderDTO);// proOrderDTO转换实体，但是proOrderDTO中的Item并没有转
			ProOrder save = proOrderRepository.save(proOrder);
			orderId = save.getId();
			for (ProOrderItemDTO proOrderItemDTO : proOrderItems) {
				BigDecimal transfor = new BigDecimal(0);
				ProOrderItem pro = new ProOrderItem();
				Map updateProductSkuCount = null;
				pro.setCount(proOrderItemDTO.getCount());
				pro.setCreatedTime(Instant.now());
				pro.setUpdatedTime(Instant.now());
				pro.setPrice(proOrderItemDTO.getPrice());
				pro.setProOrder(proOrder);
				pro.setProductSkuId(proOrderItemDTO.getProductSkuId());
				// 更改库存,添加让利
				
				ProductSkuDTO productSku = proService.getProductSku(proOrderItemDTO.getProductSkuId());
				if(productSku == null){
					throw new BadRequestAlertException("SkuId有误,无法获取商品","","");
				}
				BigDecimal transfer = productSku.getTransfer();
				pro.setTransfer(transfer);
				price = price.add(proOrderItemDTO.getPrice().multiply(new BigDecimal(proOrderItemDTO.getCount())));//让利之前的金额
				transfor_amount = transfor_amount.add((proOrderItemDTO.getPrice().multiply(new BigDecimal(proOrderItemDTO.getCount())).multiply(transfer)));//计算让利额
				Integer i = new Integer(0);
				try {
					        updateProductSkuCount = proService.updateProductSkuCount(i, proOrderItemDTO.getProductSkuId(),
							proOrderItemDTO.getCount());
					        System.out.println(updateProductSkuCount.toString());
				} catch (Exception e) {
							throw new BadRequestAlertException("更改库存失败", "productSkuCount failed", "");
				}
				
				itemList.add(proOrderItemDTO);
				String message = (String) updateProductSkuCount.get("message");

				if (message.equals("failed")) {
					throw new BadRequestAlertException("库存不足","","");
				}

				skuAll.add(proOrderItemDTO.getProductSkuId());
				proOrder.getProOrderItems().add(pro);
				proOrderItemRepository.save(pro);
			}

			//计算支付金额
			proOrder.setTransferAmount(transfor_amount);
			proOrder.setPrice(price);
//			payment = payment.add(price).subtract(transfor_amount);//计算让利之后的金额
//			payment = payment.add(proOrder.getPostFee());//支付金额 = 总金额 — 让利额  + 运费
			payment = payment.add(proOrder.getPostFee()).add(price);
			proOrder.setPayment(payment);
			proOrder.setDeletedB(false);
			proOrder.setStatus(1);
			if (type == 0) {
				shoppingCartService.del(skuAll);
			}

			ProOrder proOrder2 = proOrderRepository.save(proOrder);
			switch (proOrderDTO.getPaymentType()) {
			case 1:// 余额支付
				Wallet userWallet = walletService.getUserWallet();
				BigDecimal balance = userWallet.getBalance();
				BigDecimal subtract = balance.subtract(payment);
				if (subtract.doubleValue() < 0.00) {
					proOrder.setDeletedB(true);
					throw new BadRequestAlertException("账户余额不足", balance.toString(),subtract.toString());
					//待付款状态
				} else {
					orderString = proOrder.getOrderNo();
					proOrder2.setOrderString(orderString);
					//待付款状态
					proOrder2.setStatus(1);
				}
				break;
			case 2:// 支付宝支付
				AlipayDTO apiPayDTO = new AlipayDTO("贡融积分商城", proOrder.getOrderNo(), "product", "支付", "30m",
						payment.toString());
				orderString = payService.createAlipayAppOrder(apiPayDTO);
				proOrder2.setOrderString(orderString);
				break;
			case 3:
				BigDecimal payment1 = proOrder2.getPayment();
				BigDecimal multiply = payment1.multiply(new BigDecimal("100"));
				orderString = payService.prePay(proOrder2.getOrderNo(),multiply.intValue()+"", "product");
				proOrder2.setOrderString(orderString);
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
			// 交易关闭
			order.setStatus(5);
			order.setDeletedB(true);
			order.setUpdateTime(Instant.now());
			 //给用户加钱
	    	commissionService.orderSettlement(order.getOrderNo());
	    	//给服务商加钱
	        commissionService.handleFacilitatorWallet(order.getShopId(), order.getPayment(), order.getOrderNo(),order.getTransferAmount());
			proOrderRepository.save(order);
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
	//	commissionService.handleFacilitatorWallet(proOrder.getShopId(), proOrder.getPayment(), proOrder.getOrderNo());
		return proOrderMapper.toDto(save);
	}

	@Override
	public List<ProOrderBO> findAllOrder(long l, int page, int size) {
		List<ProOrder> orderByUserId = proOrderRepository.getOrderByUserId(l,(page-1)*size,size);
		List<ProOrderBO> showOrder = orderUtils.showOrder(orderByUserId);
		return showOrder;
	}

	@Override
	public Boolean updateOrderStatus(String string, Integer status) {
		ProOrder proOrder = proOrderRepository.findOrderByOrderNo(string);
		if(proOrder == null){
			throw new BadRequestAlertException("该订单号不存在", "", "");
		}
		//确认收货
		proOrder.setStatus(status);
		ProOrder save = proOrderRepository.save(proOrder);	
/*		 //给用户加钱
    	commissionService.orderSettlement(save.getOrderNo());
    	//给服务商加钱
        commissionService.handleFacilitatorWallet(save.getShopId(), save.getPayment(), save.getOrderNo());*/ 
		commissionService.orderSettlement(save.getOrderNo());
        commissionService.handleFacilitatorWallet(save.getShopId(), save.getPayment(), save.getOrderNo(),save.getTransferAmount());
		return true;
	}

	@Override
	public void updateStatus() {
	    List<BigInteger> dispatureStatus = proOrderRepository.updateDispatureStatus();
		for (BigInteger btId : dispatureStatus) {
			ProOrder order = proOrderRepository.findOne(btId.longValue());
			//已收货
			order.setStatus(4);
			order.setDeletedB(true);
			order.setUpdateTime(Instant.now());
			//邀请人加积分
	    	commissionService.orderSettlement(order.getOrderNo());
	    	//给服务商加钱
	        commissionService.handleFacilitatorWallet(order.getShopId(), order.getPayment(), order.getOrderNo(), order.getTransferAmount());
			proOrderRepository.save(order);
		}
	}

	@Override
	public List<ProOrder> findOrderByStatus(Integer status) {
		return proOrderRepository.findOrderByStatus(status);
	}

	@Override
	public List<ProOrder> findAll() {
		return proOrderRepository.findAll();
	}

	@Override
	public List<ProOrderItem> findOrderById(Long orderId) {
		proOrderItemRepository.findAllByOrderId(orderId);
		return proOrderItemRepository.findAllByOrderId(orderId);
	}

	@Override
	public List<BigInteger> findOrderItemBySkuId(Long skuId) {
		return proOrderItemRepository.findOrerItemBySkuId(skuId);

	}

	@Override
	public Boolean proOrderDelete(Long orderId) {
		try {
			ProOrder order = proOrderRepository.findOne(orderId);
			order.setDeletedB(true);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BadRequestAlertException("删除失败","order","delete failed");
		}
	}
}
