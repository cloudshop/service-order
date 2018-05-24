package com.eyun.order.service.impl;

import com.eyun.order.service.LeaguerOrderService;
import com.eyun.order.service.PayService;
import com.eyun.order.service.UaaService;
import com.eyun.order.service.WalletService;
import com.eyun.order.domain.LeaguerOrder;
import com.eyun.order.domain.ProOrder;
import com.eyun.order.domain.Wallet;
import com.eyun.order.domain.vo.AlipayDTO;
import com.eyun.order.repository.LeaguerOrderRepository;
import com.eyun.order.service.dto.LeaguerOrderDTO;
import com.eyun.order.service.dto.PayNotifyDTO;
import com.eyun.order.service.dto.UserDTO;
import com.eyun.order.service.mapper.LeaguerOrderMapper;
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
 * Service Implementation for managing LeaguerOrder.
 */
@Service
@Transactional
public class LeaguerOrderServiceImpl implements LeaguerOrderService {

    private final Logger log = LoggerFactory.getLogger(LeaguerOrderServiceImpl.class);

    private final LeaguerOrderRepository leaguerOrderRepository;

    private final LeaguerOrderMapper leaguerOrderMapper;

    @Autowired
    private WalletService walletService;

    @Autowired
    private PayService payService;


    public LeaguerOrderServiceImpl(LeaguerOrderRepository leaguerOrderRepository, LeaguerOrderMapper leaguerOrderMapper) {
        this.leaguerOrderRepository = leaguerOrderRepository;
        this.leaguerOrderMapper = leaguerOrderMapper;
    }

    /**
     * Save a leaguerOrder.
     *
     * @param leaguerOrderDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public LeaguerOrderDTO save(LeaguerOrderDTO leaguerOrderDTO) {
        log.debug("Request to save LeaguerOrder : {}", leaguerOrderDTO);
        LeaguerOrder leaguerOrder = leaguerOrderMapper.toEntity(leaguerOrderDTO);
        leaguerOrder = leaguerOrderRepository.save(leaguerOrder);
        return leaguerOrderMapper.toDto(leaguerOrder);
    }

    /**
     * Get all the leaguerOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LeaguerOrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaguerOrders");
        return leaguerOrderRepository.findAll(pageable)
            .map(leaguerOrderMapper::toDto);
    }

    /**
     * Get one leaguerOrder by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public LeaguerOrderDTO findOne(Long id) {
        log.debug("Request to get LeaguerOrder : {}", id);
        LeaguerOrder leaguerOrder = leaguerOrderRepository.findOne(id);
        return leaguerOrderMapper.toDto(leaguerOrder);
    }

    /**
     * Delete the leaguerOrder by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeaguerOrder : {}", id);
        leaguerOrderRepository.delete(id);
    }

	@Override
	public LeaguerOrderDTO leaguerOrderNotify(PayNotifyDTO payNotifyDTO) {
		LeaguerOrder leaguerOrder = leaguerOrderRepository.findByOrderNo(payNotifyDTO.getOrderNo());
		System.out.println(leaguerOrder.toString()+"=============================");
		if (leaguerOrder.getStatus() != 1) {
			log.info("-----------------------");
//			throw new BadRequestAlertException("订单不是未支付状态", "order", "orderStatusError");
		}
		leaguerOrder.setStatus(2);
		leaguerOrder.setPayNo(payNotifyDTO.getPayNo());
		LeaguerOrder save = leaguerOrderRepository.save(leaguerOrder);
		return leaguerOrderMapper.toDto(save);
	}

	@Override
	public String createOrder(UserDTO userDto ,LeaguerOrderDTO leaguerOrderDTO) {

        String orderString = "";
        //设置订单编号
        leaguerOrderDTO.setOrderNo(OrderNoUtil.leaGuerNoPre());// 设置订单编号
        leaguerOrderDTO.setCreatedTime(Instant.now());
        leaguerOrderDTO.setDeleted(false);
        leaguerOrderDTO.setUpdatedTime(Instant.now());
        leaguerOrderDTO.setUserid(userDto.getId());
        leaguerOrderDTO.setStatus(1);
        leaguerOrderDTO.setPayment(new BigDecimal("0.01"));
        //支付
        switch (leaguerOrderDTO.getPayType()) {
		case 1:// 余额支付
			Wallet userWallet = walletService.getUserWallet();
			BigDecimal balance = userWallet.getBalance();
			BigDecimal subtract = balance.subtract(leaguerOrderDTO.getPayment());
			if (subtract.doubleValue() < 0.00) {
				leaguerOrderDTO.setDeleted(true);
				throw new BadRequestAlertException("账户余额不足", balance.toString(),subtract.toString());
			} else {
				orderString = leaguerOrderDTO.getOrderNo();
			}
			break;
		case 2:// 支付宝支付
			AlipayDTO apiPayDTO = new AlipayDTO("贡融积分商城", leaguerOrderDTO.getOrderNo(), "leaguer", "支付", "30m",
					leaguerOrderDTO.getPayment().toString());
			orderString = payService.createAlipayAppOrder(apiPayDTO);
			break;
		case 3:
			BigDecimal payment = leaguerOrderDTO.getPayment();
			BigDecimal multiply = payment.multiply(new BigDecimal("100"));
			orderString = payService.prePay(leaguerOrderDTO.getOrderNo(), multiply.intValue()+"", "leaguer");
		default:
			break;
		}
        leaguerOrderRepository.save(leaguerOrderMapper.toEntity(leaguerOrderDTO));
        return orderString;
	}
}
