package com.eyun.order.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyun.order.service.ProOrderService;
import com.eyun.order.service.UaaService;
import com.eyun.order.web.rest.errors.BadRequestAlertException;
import com.eyun.order.web.rest.util.HeaderUtil;
import com.eyun.order.web.rest.util.PaginationUtil;
import com.eyun.order.service.dto.ProOrderDTO;
import com.eyun.order.service.dto.ProOrderItemDTO;
import com.eyun.order.service.dto.UserDTO;
import com.eyun.order.service.impl.ProOrderServiceImpl;
import com.eyun.order.service.dto.ProOrderCriteria;
import com.eyun.order.domain.ProOrder;
import com.eyun.order.domain.ProOrderItem;
import com.eyun.order.service.ProOrderBO;
import com.eyun.order.service.ProOrderQueryService;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * REST controller for managing ProOrder.
 */
@RestController
@RequestMapping("/api")
public class ProOrderResource {

    private final Logger log = LoggerFactory.getLogger(ProOrderResource.class);

    private static final String ENTITY_NAME = "proOrder";

    private final ProOrderService proOrderService;

    private final ProOrderQueryService proOrderQueryService;
    
    @Autowired
    private UaaService uaaService;

    public ProOrderResource(ProOrderService proOrderService, ProOrderQueryService proOrderQueryService) {
        this.proOrderService = proOrderService;
        this.proOrderQueryService = proOrderQueryService;
    }

    /**
     * POST  /pro-orders : Create a new proOrder.
     *
     * @param proOrderDTO the proOrderDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new proOrderDTO, or with status 400 (Bad Request) if the proOrder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pro-orders")
    @Timed
    public ResponseEntity<ProOrderDTO> createProOrder(@RequestBody ProOrderDTO proOrderDTO) throws URISyntaxException {
        log.debug("REST request to save ProOrder : {}", proOrderDTO);
        if (proOrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new proOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProOrderDTO result = proOrderService.save(proOrderDTO);
        return ResponseEntity.created(new URI("/api/pro-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pro-orders : Updates an existing proOrder.
     *
     * @param proOrderDTO the proOrderDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated proOrderDTO,
     * or with status 400 (Bad Request) if the proOrderDTO is not valid,
     * or with status 500 (Internal Server Error) if the proOrderDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pro-orders")
    @Timed
    public ResponseEntity<ProOrderDTO> updateProOrder(@RequestBody ProOrderDTO proOrderDTO) throws URISyntaxException {
        log.debug("REST request to update ProOrder : {}", proOrderDTO);
        if (proOrderDTO.getId() == null) {
            return createProOrder(proOrderDTO);
        }
        ProOrderDTO result = proOrderService.save(proOrderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, proOrderDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pro-orders : get all the proOrders.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of proOrders in body
     */
    @GetMapping("/pro-orders")
    @Timed
    public ResponseEntity<List<ProOrderDTO>> getAllProOrders(ProOrderCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ProOrders by criteria: {}", criteria);
        Page<ProOrderDTO> page = proOrderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pro-orders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pro-orders/:id : get the "id" proOrder.
     *
     * @param id the id of the proOrderDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the proOrderDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pro-orders/{id}")
    @Timed
    public ResponseEntity<ProOrderDTO> getProOrder(@PathVariable Long id) {
        log.debug("REST request to get ProOrder : {}", id);
        ProOrderDTO proOrderDTO = proOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(proOrderDTO));
    }

    /**
     * DELETE  /pro-orders/:id : delete the "id" proOrder.
     *
     * @param id the id of the proOrderDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pro-orders/{id}")
    @Timed
    public ResponseEntity<Void> deleteProOrder(@PathVariable Long id) {
        log.debug("REST request to delete ProOrder : {}", id);
        proOrderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    /**
     * 提交商家proOrder信息
     * @param list
     * @return
     */ 
    
    @ApiOperation(value = "商品购买，购物车购买")
    @PostMapping("/depproorders/{type}")
    public ResponseEntity<String> depproorders(@RequestBody ProOrderDTO proOrderDTO,@PathVariable Integer type){
                return new ResponseEntity<>(proOrderService.createOrder(proOrderDTO,type),HttpStatus.OK);	    		    	
     }

    @ApiOperation(value = "查看当前用户的所有订单")
    @GetMapping("/findAllProOrder/{page}/{size}")
    public ResponseEntity<List<ProOrderBO>> findAllProOrderByUser(@PathVariable int page,@PathVariable int size ) {
//    	UserDTO user = uaaService.getAccount();
//    	Long userId = user.getId();
    	List<ProOrderBO> proOrderItemsByUser = proOrderService.getProOrderItemsByUser(1,page,size);
        return new ResponseEntity<>(proOrderItemsByUser,HttpStatus.OK);
    }
    
    @ApiOperation(value = "查看代付款1，已完成5，已取消订单6")
    @GetMapping("/findAllItemsByStatus/{status}/{page}/{size}")
    public ResponseEntity<List<ProOrderBO>> findOrderByStatuAndUserid(@PathVariable int status,@PathVariable int page,@PathVariable int size){		
    	Integer userId;
    	List<ProOrderBO>  pros= proOrderService.findOrderByStatuAndUserid(1l,status,page,size);  	
    	return new ResponseEntity<>(pros,HttpStatus.OK);	
    }
    
    @ApiOperation(value = "查看待收货订单(status:2,3,4)")
    @GetMapping("/findDispatchItems/{page}/{size}")
    public ResponseEntity<List<ProOrderBO>> findDispatchItems(@PathVariable int page,@PathVariable int size){		
    	Integer userId;
    	List<ProOrderBO> pros = proOrderService.findDispatchItems(1l,page,size); 
    	return new ResponseEntity<>(pros,HttpStatus.OK);	
    }
   
}
