package com.eyun.order.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyun.order.service.LeaguerOrderService;
import com.eyun.order.service.PayService;
import com.eyun.order.service.UaaService;
import com.eyun.order.service.WalletService;
import com.eyun.order.web.rest.errors.BadRequestAlertException;
import com.eyun.order.web.rest.util.HeaderUtil;
import com.eyun.order.web.rest.util.OrderNoUtil;
import com.eyun.order.web.rest.util.PaginationUtil;
import com.eyun.order.service.dto.LeaguerOrderDTO;
import com.eyun.order.service.dto.PayNotifyDTO;
import com.eyun.order.service.dto.UserDTO;
import com.eyun.order.service.dto.LeaguerOrderCriteria;
import com.eyun.order.domain.LeaguerOrder;
import com.eyun.order.domain.Wallet;
import com.eyun.order.domain.vo.AlipayDTO;
import com.eyun.order.service.LeaguerOrderQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing LeaguerOrder.
 */
@RestController
@RequestMapping("/api")
public class LeaguerOrderResource {

    private final Logger log = LoggerFactory.getLogger(LeaguerOrderResource.class);

    private static final String ENTITY_NAME = "leaguerOrder";

    private final LeaguerOrderService leaguerOrderService;

    private final LeaguerOrderQueryService leaguerOrderQueryService;
    
    @Autowired
    private UaaService uaaService;

    @Autowired
    private WalletService walletService;
    
    @Autowired
    private PayService payService;
    
    public LeaguerOrderResource(LeaguerOrderService leaguerOrderService, LeaguerOrderQueryService leaguerOrderQueryService) {
        this.leaguerOrderService = leaguerOrderService;
        this.leaguerOrderQueryService = leaguerOrderQueryService;
    }

    /**
     * POST  /leaguer-orders : Create a new leaguerOrder.
     *
     * @param leaguerOrderDTO the leaguerOrderDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new leaguerOrderDTO, or with status 400 (Bad Request) if the leaguerOrder has already an ID
     * @throws Exception 
     */
    @ApiOperation("创建增值业务的订单")
    @PostMapping("/leaguer-orders")
    @Timed
    public ResponseEntity<String> createLeaguerOrder(@RequestBody LeaguerOrderDTO leaguerOrderDTO) throws Exception {
        log.debug("REST request to save LeaguerOrder : {}", leaguerOrderDTO);
        if (leaguerOrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaguerOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        
    	UserDTO userDTO=uaaService.getAccount();
    	if (userDTO==null){
    	    throw new Exception("获取当前登陆用户失败");
    	}
    	
        
        
        String orderString = "";
        //设置订单编号
        leaguerOrderDTO.setOrderNo(OrderNoUtil.leaGuerNoPre());// 设置订单编号
        leaguerOrderDTO.setCreatedTime(Instant.now());
        leaguerOrderDTO.setDeleted(false);
        leaguerOrderDTO.setUpdatedTime(Instant.now());  
        leaguerOrderDTO.setUserid(userDTO.getId());
        leaguerOrderDTO.setStatus(1);
        leaguerOrderDTO.setPayment(new BigDecimal(998));
        
        
        LeaguerOrderDTO result = leaguerOrderService.save(leaguerOrderDTO);   
        //支付
        switch (result.getPayType()) {
		case 1:// 余额支付
			Wallet userWallet = walletService.getUserWallet();
			BigDecimal balance = userWallet.getBalance();
			BigDecimal subtract = balance.subtract(result.getPayment());
			if (subtract.doubleValue() < 0.00) {
				result.setDeleted(true);
				throw new BadRequestAlertException("账户余额不足", balance.toString(),subtract.toString());
			} else {
				orderString = result.getOrderNo();
			}
			break;
		case 2:// 支付宝支付
			AlipayDTO apiPayDTO = new AlipayDTO("贡融积分商城", result.getOrderNo(), "leaguer", "支付", "30m",
					result.getPayment().toString());
			orderString = payService.createAlipayAppOrder(apiPayDTO);
			break;
		default:
			break;
		} 
        
        leaguerOrderService.save(result);   
        return new ResponseEntity<>(orderString,HttpStatus.OK);
    }

    /**
     * PUT  /leaguer-orders : Updates an existing leaguerOrder.
     *
     * @param leaguerOrderDTO the leaguerOrderDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated leaguerOrderDTO,
     * or with status 400 (Bad Request) if the leaguerOrderDTO is not valid,
     * or with status 500 (Internal Server Error) if the leaguerOrderDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/leaguer-orders")
    @Timed
    public ResponseEntity<LeaguerOrderDTO> updateLeaguerOrder(@RequestBody LeaguerOrderDTO leaguerOrderDTO) throws URISyntaxException {
        log.debug("REST request to update LeaguerOrder : {}", leaguerOrderDTO);
        if (leaguerOrderDTO.getId() == null) {
//            return createLeaguerOrder(leaguerOrderDTO);
        	return null;
        }
        LeaguerOrderDTO result = leaguerOrderService.save(leaguerOrderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, leaguerOrderDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /leaguer-orders : get all the leaguerOrders.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of leaguerOrders in body
     */
    @GetMapping("/leaguer-orders")
    @Timed
    public ResponseEntity<List<LeaguerOrderDTO>> getAllLeaguerOrders(LeaguerOrderCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LeaguerOrders by criteria: {}", criteria);
        Page<LeaguerOrderDTO> page = leaguerOrderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/leaguer-orders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /leaguer-orders/:id : get the "id" leaguerOrder.
     *
     * @param id the id of the leaguerOrderDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the leaguerOrderDTO, or with status 404 (Not Found)
     */
    @GetMapping("/leaguer-orders/{id}")
    @Timed
    public ResponseEntity<LeaguerOrderDTO> getLeaguerOrder(@PathVariable Long id) {
        log.debug("REST request to get LeaguerOrder : {}", id);
        LeaguerOrderDTO leaguerOrderDTO = leaguerOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(leaguerOrderDTO));
    }

    /**
     * DELETE  /leaguer-orders/:id : delete the "id" leaguerOrder.
     *
     * @param id the id of the leaguerOrderDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/leaguer-orders/{id}")
    @Timed
    public ResponseEntity<Void> deleteLeaguerOrder(@PathVariable Long id) {
        log.debug("REST request to delete LeaguerOrder : {}", id);
        leaguerOrderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    /**
     * 支付回调
     * @author 逍遥子
     * @email 756898059@qq.com
     * @date 2018年4月27日
     * @version 1.0
     * @param payNotifyDTO
     * @return
     */
    @PutMapping("/api/leaguer-order/pay/notify")
	public ResponseEntity<LeaguerOrderDTO> leaguerOrderNotify(@RequestBody PayNotifyDTO payNotifyDTO) {
    	LeaguerOrderDTO leaguerOrderDTO = leaguerOrderService.leaguerOrderNotify(payNotifyDTO);
    	return new ResponseEntity<LeaguerOrderDTO>(leaguerOrderDTO, HttpStatus.OK);
    }
    
}
