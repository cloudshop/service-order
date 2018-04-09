package com.eyun.order.service.impl;

import com.eyun.order.service.DepOrderService;
import com.eyun.order.service.PayService;
import com.eyun.order.service.WalletService;
import com.eyun.order.domain.DepOrder;
import com.eyun.order.domain.Wallet;
import com.eyun.order.repository.DepOrderRepository;
import com.eyun.order.service.dto.BalanceDTO;
import com.eyun.order.service.dto.DepOrderDTO;
import com.eyun.order.service.mapper.DepOrderMapper;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing DepOrder.
 */
@Service
@Transactional
public class DepOrderServiceImpl implements DepOrderService {

    private final Logger log = LoggerFactory.getLogger(DepOrderServiceImpl.class);

    private final DepOrderRepository depOrderRepository;

    private final DepOrderMapper depOrderMapper;
    
    private final PayService payService;
    
    private final WalletService walletService;

    public DepOrderServiceImpl(DepOrderRepository depOrderRepository, DepOrderMapper depOrderMapper, PayService payService, WalletService walletService) {
    	this.walletService = walletService;
    	this.payService = payService;
        this.depOrderRepository = depOrderRepository;
        this.depOrderMapper = depOrderMapper;
    }

    /**
     * Save a depOrder.
     *
     * @param depOrderDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DepOrderDTO save(DepOrderDTO depOrderDTO) {
        log.debug("Request to save DepOrder : {}", depOrderDTO);
        DepOrder depOrder = depOrderMapper.toEntity(depOrderDTO);
        depOrder = depOrderRepository.save(depOrder);
        return depOrderMapper.toDto(depOrder);
    }

    /**
     * Get all the depOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DepOrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DepOrders");
        return depOrderRepository.findAll(pageable)
            .map(depOrderMapper::toDto);
    }

    /**
     * Get one depOrder by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public DepOrderDTO findOne(Long id) {
        log.debug("Request to get DepOrder : {}", id);
        DepOrder depOrder = depOrderRepository.findOne(id);
        return depOrderMapper.toDto(depOrder);
    }

    /**
     * Delete the depOrder by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DepOrder : {}", id);
        depOrderRepository.delete(id);
    }

	@Override
	public void depositNotify(String orderNo) throws Exception {
		DepOrder depOrder = depOrderRepository.findByOrderNoAndStatus(orderNo,1);
		if (depOrder == null) {
		}
		String queryOrder = payService.queryOrder(orderNo);
		JSONObject jsonObject = new JSONObject(queryOrder);
		JSONObject resp = jsonObject.getJSONObject("alipay_trade_query_response");
		
		if (orderNo.equals(resp.getString("out_trade_no")) 
				&& depOrder.getPayment().doubleValue() == resp.getDouble("total_amount")
				&& "TRADE_SUCCESS".equals(resp.getString("trade_status"))) {//判断订单号是否相同 金额是否相等 支付状态
			depOrder.updatedTime(Instant.now());
			depOrder.setPayNo(resp.getString("trade_no"));
			String date = resp.getString("send_pay_date");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date1 = sdf.parse(date);
			depOrder.setPayTime(Instant.now());
			depOrder.setStatus(1);
			//调用钱包服务 添加余额
			BalanceDTO balanceDTO = new BalanceDTO();
			balanceDTO.setMoney(depOrder.getPayment());
			balanceDTO.setOrderNo(depOrder.getOrderNo());
			balanceDTO.setType(1);//1 充值
			balanceDTO.setUserid(depOrder.getUserid());
			Wallet wallet = walletService.updateBalance(balanceDTO);
		}
		
	}
	
}
