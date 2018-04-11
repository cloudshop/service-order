package com.eyun.order.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyun.order.service.DepOrderService;
import com.eyun.order.service.PayService;
import com.eyun.order.web.rest.errors.BadRequestAlertException;
import com.eyun.order.web.rest.util.HeaderUtil;
import com.eyun.order.web.rest.util.OrderNoUtil;
import com.eyun.order.web.rest.util.PaginationUtil;
import com.eyun.order.service.dto.DepOrderDTO;
import com.eyun.order.service.dto.DepOrderCriteria;
import com.eyun.order.domain.vo.AlipayDTO;
import com.eyun.order.domain.vo.DepOrderVO;
import com.eyun.order.service.DepOrderQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing DepOrder.
 */
@RestController
@RequestMapping("/api")
public class DepOrderResource {

    private final Logger log = LoggerFactory.getLogger(DepOrderResource.class);

    private static final String ENTITY_NAME = "depOrder";

    private final DepOrderService depOrderService;

    private final DepOrderQueryService depOrderQueryService;
    
    private final PayService payService;

    public DepOrderResource(DepOrderService depOrderService, DepOrderQueryService depOrderQueryService, PayService payService) {
    	this.payService = payService;
        this.depOrderService = depOrderService;
        this.depOrderQueryService = depOrderQueryService;
    }

    /**
     * POST  /dep-orders : Create a new depOrder.
     *
     * @param depOrderDTO the depOrderDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new depOrderDTO, or with status 400 (Bad Request) if the depOrder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/dep-orders")
    @Timed
    public ResponseEntity<DepOrderDTO> createDepOrder(@RequestBody DepOrderDTO depOrderDTO) throws URISyntaxException {
        log.debug("REST request to save DepOrder : {}", depOrderDTO);
        if (depOrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new depOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DepOrderDTO result = depOrderService.save(depOrderDTO);
        return ResponseEntity.created(new URI("/api/dep-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /dep-orders : Updates an existing depOrder.
     *
     * @param depOrderDTO the depOrderDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated depOrderDTO,
     * or with status 400 (Bad Request) if the depOrderDTO is not valid,
     * or with status 500 (Internal Server Error) if the depOrderDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/dep-orders")
    @Timed
    public ResponseEntity<DepOrderDTO> updateDepOrder(@RequestBody DepOrderDTO depOrderDTO) throws URISyntaxException {
        log.debug("REST request to update DepOrder : {}", depOrderDTO);
        if (depOrderDTO.getId() == null) {
            return createDepOrder(depOrderDTO);
        }
        DepOrderDTO result = depOrderService.save(depOrderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, depOrderDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /dep-orders : get all the depOrders.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of depOrders in body
     */
    @GetMapping("/dep-orders")
    @Timed
    public ResponseEntity<List<DepOrderDTO>> getAllDepOrders(DepOrderCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DepOrders by criteria: {}", criteria);
        Page<DepOrderDTO> page = depOrderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/dep-orders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /dep-orders/:id : get the "id" depOrder.
     *
     * @param id the id of the depOrderDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the depOrderDTO, or with status 404 (Not Found)
     */
    @GetMapping("/dep-orders/{id}")
    @Timed
    public ResponseEntity<DepOrderDTO> getDepOrder(@PathVariable Long id) {
        log.debug("REST request to get DepOrder : {}", id);
        DepOrderDTO depOrderDTO = depOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(depOrderDTO));
    }

    /**
     * DELETE  /dep-orders/:id : delete the "id" depOrder.
     *
     * @param id the id of the depOrderDTO to delete
     * @return the ResponseEntity with status 200 (OK)
    @DeleteMapping("/dep-orders/{id}")
    @Timed
    public ResponseEntity<Void> deleteDepOrder(@PathVariable Long id) {
        log.debug("REST request to delete DepOrder : {}", id);
        depOrderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
     */
    
    /**
     * 创建充值订单
     * @author 逍遥子
     * @email 756898059@qq.com
     * @date 2018年4月9日
     * @version 1.0
     * @param depOrderVO
     * @return
     */
    @ApiOperation(value="创建充值订单")
    @PostMapping("/dep-orders/deposit")
    public String createdDepostOrder(@RequestBody DepOrderVO depOrderVO) {
    	//String orderNo = UUID.randomUUID().toString().replaceAll("-", "");
    	String orderString = "";
    	String orderNo = OrderNoUtil.getOrderNoUtil();
    	switch (depOrderVO.getPayType()) {
		case 1: //阿里支付
			AlipayDTO alipay = new AlipayDTO();
			alipay.setBody("贡融积分商城充值");
			alipay.setOutTradeNo(orderNo);
			alipay.setSubject("充值余额");
			alipay.setPassbackParams("deposit");//deposit 充值
			alipay.setTotalAmount(depOrderVO.getPayment().toString());
			alipay.setTimeoutExpress("15m");
			orderString = payService.createAlipayAppOrder(alipay);
			break;
		case 2: //微信支付
			break;
		default:
			break;
		}
		
    	DepOrderDTO depOrderDTO = new DepOrderDTO();
    	depOrderDTO.setOrderNo(orderNo);
    	depOrderDTO.setCreatedTime(Instant.now());
    	depOrderDTO.setDeleted(false);
    	depOrderDTO.setPayment(depOrderVO.getPayment());
    	depOrderDTO.setPayType(depOrderVO.getPayType());
    	depOrderDTO.setStatus(1);
    	depOrderDTO.setUserid(depOrderVO.getUserid());
    	depOrderDTO.setWalletId(depOrderVO.getWalletId());
		depOrderService.save(depOrderDTO);
		
		return orderString;
    }
    
    /**
     * 支付回调通知地址
     * @author 逍遥子
     * @email 756898059@qq.com
     * @date 2018年4月9日
     * @version 1.0
     * @param orderNo
     * @return
     * @throws Exception
     */
    @PutMapping("/dep-orders/deposit/{orderNo}") 
    public ResponseEntity<String> depositNotify (@RequestBody String orderNo) throws Exception {
    	depOrderService.depositNotify(orderNo);
    	return ResponseEntity.ok()
    			.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, orderNo))
    			.body("success");
    }
    
}
