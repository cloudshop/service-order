package com.eyun.order.service.impl;

import com.eyun.order.service.FaceOrderService;
import com.eyun.order.service.PayService;
import com.eyun.order.service.WalletService;
import com.eyun.order.domain.FaceOrder;
import com.eyun.order.domain.ProOrder;
import com.eyun.order.domain.Wallet;
import com.eyun.order.domain.vo.AlipayDTO;
import com.eyun.order.repository.FaceOrderRepository;
import com.eyun.order.service.dto.FaceOrderDTO;
import com.eyun.order.service.dto.PayNotifyDTO;
import com.eyun.order.service.dto.ProOrderDTO;
import com.eyun.order.service.mapper.FaceOrderMapper;
import com.eyun.order.web.rest.errors.BadRequestAlertException;
import com.eyun.order.web.rest.util.OrderNoUtil;

import java.math.BigDecimal;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing FaceOrder.
 */
@Service
@Transactional
public class FaceOrderServiceImpl implements FaceOrderService {

    private final Logger log = LoggerFactory.getLogger(FaceOrderServiceImpl.class);

    private final FaceOrderRepository faceOrderRepository;

    private final FaceOrderMapper faceOrderMapper;
    
    @Autowired
    private WalletService walletService;
    @Autowired
    private PayService payService;

    public FaceOrderServiceImpl(FaceOrderRepository faceOrderRepository, FaceOrderMapper faceOrderMapper) {
        this.faceOrderRepository = faceOrderRepository;
        this.faceOrderMapper = faceOrderMapper;
    }

    /**
     * Save a faceOrder.
     *
     * @param faceOrderDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FaceOrderDTO save(FaceOrderDTO faceOrderDTO) {
        log.debug("Request to save FaceOrder : {}", faceOrderDTO);
        FaceOrder faceOrder = faceOrderMapper.toEntity(faceOrderDTO);
        faceOrder = faceOrderRepository.save(faceOrder);
        return faceOrderMapper.toDto(faceOrder);
    }

    /**
     * Get all the faceOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FaceOrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FaceOrders");
        return faceOrderRepository.findAll(pageable)
            .map(faceOrderMapper::toDto);
    }

    /**
     * Get one faceOrder by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public FaceOrderDTO findOne(Long id) {
        log.debug("Request to get FaceOrder : {}", id);
        FaceOrder faceOrder = faceOrderRepository.findOne(id);
        return faceOrderMapper.toDto(faceOrder);
    }

    /**
     * Delete the faceOrder by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FaceOrder : {}", id);
        faceOrderRepository.delete(id);
    }

	@Override
	public String createOrder(FaceOrderDTO faceOrderDTO) {
		
		String orderString = "";
		faceOrderDTO.setCreatedTime(Instant.now());
		faceOrderDTO.setUpdatedTime(Instant.now());
		faceOrderDTO.setOrderNo(OrderNoUtil.faceNoPre());
		faceOrderDTO.setStatus(1);
		switch(faceOrderDTO.getType()){
		case 1://余额支付
			Wallet userWallet = walletService.getUserWallet();
			BigDecimal balance = userWallet.getBalance();
			BigDecimal subtract = balance.subtract(faceOrderDTO.getPayment());
			if (subtract.doubleValue() < 0.00) {
				throw new BadRequestAlertException("账户余额不足", balance.toString(),subtract.toString());
			} else {
				orderString = faceOrderDTO.getOrderNo();
			}
			break;
		case 2://余额 + 券
			/**
			 * 1.校验（余额值+券） 与 总额 是否一致
			 * 2.校验余额与券是否足够
			 */
			Wallet userWallet1 = walletService.getUserWallet();
			BigDecimal balance1 = userWallet1.getBalance();
			if(faceOrderDTO.getTicket().add(faceOrderDTO.getPayment()).equals(faceOrderDTO.getAmount())==false){
				throw new BadRequestAlertException("总金额不足", "", "");
			}else if (balance1.subtract(faceOrderDTO.getPayment()).doubleValue() < 0.00) {
				throw new BadRequestAlertException("账户余额不足", balance1.toString(),balance1.subtract(faceOrderDTO.getPayment()).toString());
			}else if(userWallet1.getTicket().subtract(faceOrderDTO.getTicket()).doubleValue() < 0.00){
				throw new BadRequestAlertException("账户券不足", userWallet1.getTicket().toString(), userWallet1.getTicket().subtract(faceOrderDTO.getTicket()).toString());
			}else{
				orderString = faceOrderDTO.getOrderNo();
			}
			break;
		case 3://券
			Wallet userWallet2 = walletService.getUserWallet();
			BigDecimal balance2 = userWallet2.getBalance();
			if(userWallet2.getTicket().subtract(faceOrderDTO.getTicket()).doubleValue() < 0.00){
				throw new BadRequestAlertException("账户券不足", userWallet2.getTicket().toString(), userWallet2.getTicket().subtract(faceOrderDTO.getTicket()).toString());
			}else{
				orderString = faceOrderDTO.getOrderNo();
			}
			break;
		case 4://支付宝
			AlipayDTO apiPayDTO = new AlipayDTO("贡融积分商城", faceOrderDTO.getOrderNo(), "faceTrans", "支付", "30m",
					faceOrderDTO.getAmount().toString());
			orderString = payService.createAlipayAppOrder(apiPayDTO);
			break;
		case 5://微信
			//TODO 调用微信支付接口
		default:
			break;
		}
		faceOrderRepository.save(faceOrderMapper.toEntity(faceOrderDTO));
        return orderString;
		}

	@Override
	public FaceOrderDTO faceOrderNotify(PayNotifyDTO payNotifyDTO) {
		FaceOrder faceOrder = faceOrderRepository.findOrderByOrderNo(payNotifyDTO.getOrderNo());
		System.out.println("faceOrder++++++++++++++++++++++++++++++++++++" + faceOrder.toString());
		System.out.println("payNotifyDTO==============" + payNotifyDTO.toString());
		if(faceOrder.getStatus()!= 1){
			throw new BadRequestAlertException("订单不是未支付状态", "order", "orderStatusError");
		}
		faceOrder.setStatus(2);
		FaceOrder save = faceOrderRepository.save(faceOrder);
		return faceOrderMapper.toDto(save);
	}

	@Override
	public FaceOrderDTO findFaceOrderByOrderNo(String orderNo) {
		FaceOrder findOrderByOrderNo = faceOrderRepository.findOrderByOrderNo(orderNo);
		return faceOrderMapper.toDto(findOrderByOrderNo);
	}
}