package com.eyun.order.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyun.order.service.ProOrderService;
import com.eyun.order.service.UaaService;
import com.eyun.order.service.UserService;
import com.eyun.order.service.WalletService;
import com.eyun.order.web.rest.errors.BadRequestAlertException;
import com.eyun.order.web.rest.util.HeaderUtil;
import com.eyun.order.web.rest.util.PaginationUtil;
import com.eyun.order.service.dto.ProOrderDTO;
import com.eyun.order.service.dto.ProOrderItemDTO;
import com.eyun.order.service.dto.ShipDTO;
import com.eyun.order.service.dto.UserDTO;
import com.eyun.order.service.impl.ProOrderServiceImpl;
import com.eyun.order.service.dto.OrderDateDTO;
import com.eyun.order.service.dto.PageOrder;
import com.eyun.order.service.dto.PayNotifyDTO;
import com.eyun.order.service.dto.PayOrderDTO;
import com.eyun.order.service.dto.ProOrderCriteria;
import com.eyun.order.domain.ProOrder;
import com.eyun.order.domain.ProOrderItem;
import com.eyun.order.service.CommisionService;
import com.eyun.order.service.ProOrderBO;
import com.eyun.order.service.ProOrderQueryService;

import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommisionService commissionService;

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
     * @throws Exception
     */

    @ApiOperation(value = "商品直接购买(待付款购买)type:0，购物车购买type:1,")
    @PostMapping("/depproorders/{type}")
    public ResponseEntity<String> depproorders(@RequestBody ProOrderDTO proOrderDTO,@PathVariable Integer type) throws Exception{
    	UserDTO userDTO;
    	  try {
    		 userDTO=uaaService.getAccount();
			  } catch (Exception e) {
				  throw new BadRequestAlertException("获取当前用户失败", "", "");
			 }

    	   proOrderDTO.setcUserid(userDTO.getId());

    	   return new ResponseEntity<>(proOrderService.createOrder(proOrderDTO,type),HttpStatus.OK);
     }

    @ApiOperation(value = "查看代付款1，已完成4，已取消订单5")
    @GetMapping("/findAllItemsByStatus/{status}/{page}/{size}")
    public ResponseEntity<List<ProOrderBO>> findOrderByStatuAndUserid(@PathVariable int status,@PathVariable int page,@PathVariable int size) throws Exception{
    	UserDTO userDTO;
  	  try {
  		 userDTO=uaaService.getAccount();
			  } catch (Exception e) {
				  throw new BadRequestAlertException("获取当前用户失败", "", "");
			 }


    	List<ProOrderBO>  pros= proOrderService.findOrderByStatuAndUserid(userDTO.getId(),status,page,size);
    	return new ResponseEntity<>(pros,HttpStatus.OK);
    }

    @ApiOperation(value = "查看待收货订单(status:2,3)")
    @GetMapping("/findDispatchItems/{page}/{size}")
    public ResponseEntity<List<ProOrderBO>> findDispatchItems(@PathVariable int page,@PathVariable int size) throws Exception{

    	UserDTO userDTO;
  	  try {
  		 userDTO=uaaService.getAccount();
			  } catch (Exception e) {
				  throw new BadRequestAlertException("获取当前用户失败", "", "");
			 }

    	List<ProOrderBO> pros = proOrderService.findDispatchItems(userDTO.getId(),page,size);
    	return new ResponseEntity<>(pros,HttpStatus.OK);
    }

    @ApiOperation(value = "查看全部订单")
    @GetMapping("/findAllOrder/{page}/{size}")
    public ResponseEntity<List<ProOrderBO>> findAllOrder(@PathVariable int page,@PathVariable int size) throws Exception{
    	UserDTO userDTO;
  	    try {
  		 userDTO=uaaService.getAccount();
			  } catch (Exception e) {
				  throw new BadRequestAlertException("获取当前用户失败", "", "");
			 }
    	List<ProOrderBO> pros = proOrderService.findAllOrder(userDTO.getId(),page,size);
    	return new ResponseEntity<>(pros,HttpStatus.OK);
    }


    /**
     * 支付通知回调修改订单状态
     * @author 逍遥子
     * @email 756898059@qq.com
     * @date 2018年4月19日
     * @version 1.0
     * @param payNotifyDTO
     */
    @PutMapping("/pro-order/pay/notify")
    public ResponseEntity<ProOrderDTO> proOrderNotify(@RequestBody PayNotifyDTO payNotifyDTO) {
    	ProOrderDTO proOrderDTO = proOrderService.proOrderNotify(payNotifyDTO);
    	return new ResponseEntity<ProOrderDTO>(proOrderDTO, HttpStatus.OK);
    }

    /**
     * 根据OrderNo查询订单
     * @author 逍遥子
     * @email 756898059@qq.com
     * @date 2018年4月19日
     * @version 1.0
     * @param orderNo
     * @return
     * @return
     */
    @GetMapping("/findOrderByOrderNo/{orderNo}")
    public ResponseEntity<ProOrderDTO> findOrderByOrderNo(@PathVariable("orderNo")String orderNo) {
    	ProOrderDTO proOrderDTO = proOrderService.findOrderByOrderNo(orderNo);
    	return ResponseUtil.wrapOrNotFound(Optional.ofNullable(proOrderDTO));
    }

    /**
     * 已发货 根据orderNo来存物流名称 物流单号
     */
    @ApiOperation(value = "填物流单号，物流名称,更改订单为已发货状态")
    @PostMapping("/updateOrderByShip")
    public ResponseEntity<Void>  updateOrderByShip(@RequestBody ShipDTO shipDTO ){

		ProOrderDTO orderDTO =  proOrderService.findOrderByOrderNo(shipDTO.getOrderNo());
		if(orderDTO == null){
			throw new BadRequestAlertException("该订单号不存在", "", "");
		}
    	orderDTO.setShipingCode(shipDTO.getShipingCode());
    	orderDTO.setShippingName(shipDTO.getShippingName());
    	orderDTO.setStatus(3);
    	ProOrderDTO save = proOrderService.save(orderDTO);
    	return new ResponseEntity<>(null,HttpStatus.OK);
    }

    /**
     * 更改订单状态接口
     * @param orderNo,status
     * @throws Exception
     */
    @ApiOperation("更改订单状态")
    @PostMapping("/updateOrderStatus")
    public ResponseEntity<Boolean> updateOrderStatus(@RequestBody Map map) throws Exception{
		ResponseEntity<Boolean> resp = new ResponseEntity<>(proOrderService.updateOrderStatus((String)map.get("orderNo"),(Integer)map.get("status")),HttpStatus.OK);
		commissionService.orderSettlement((String)map.get("orderNo"));
		//确认收货
        //	commissionService.handleFacilitatorWallet(proOrder.getShopId(), proOrder.getPayment(), proOrder.getOrderNo());
        return resp;
    }

    @ApiOperation("分页查询订单")
    @PostMapping("/manage/findOrderByStatus")
    public ResponseEntity<OrderDateDTO> findOrderByStatus(@RequestBody PageOrder pageOrder){
		Pageable pageable = new PageRequest(pageOrder.getPage(),pageOrder.getSize());
		UserDTO user;
		Page<ProOrderDTO> findByCriteria;
		try {
			user = uaaService.getAccount();
			if(user == null){
				throw new BadRequestAlertException("用户不存在", "userId doesn't exits", "");
			}
		} catch (Exception e) {
            throw new BadRequestAlertException("用户服务异常", "userService", "userService down!");
		}
		OrderDateDTO list = new OrderDateDTO();
		//userId条件
		LongFilter longFilter = new LongFilter();
		longFilter.setEquals(user.getId());
		//status
		IntegerFilter integerFilter = new IntegerFilter();
		integerFilter.setEquals(pageOrder.getStatus());
		//deleted
		BooleanFilter booleanFilterB = new BooleanFilter();
		booleanFilterB.setEquals(false);
		BooleanFilter booleanFilterC = new BooleanFilter();
		booleanFilterC.setEquals(false);
		ProOrderCriteria criteria = new ProOrderCriteria();
		criteria.setcUserid(longFilter);
		criteria.setDeletedB(booleanFilterB);
		criteria.setDeletedC(booleanFilterC);
		if(pageOrder.getStatus() == 0){
			findByCriteria = proOrderQueryService.findByCriteria(criteria, pageable);
		}else{
			criteria.setStatus(integerFilter);
			findByCriteria = proOrderQueryService.findByCriteria(criteria, pageable);
		}

		list.setProOrderAmount(findByCriteria.getTotalElements());
    	list.setProOrder(findByCriteria.getContent());
		return new ResponseEntity<>(list,HttpStatus.OK);
    }



    @ApiOperation("后台管理分页查询商家订单")
    @PostMapping("/manage/findMercuOrderByStatus")
    public ResponseEntity<OrderDateDTO> findMercuOrderByStatus(@RequestBody PageOrder pageOrder){
		Pageable pageable = new PageRequest(pageOrder.getPage()-1,pageOrder.getSize());
		Page<ProOrderDTO> findByCriteria;
		Map findUserMercuryId = userService.findUserMercuryId();
		OrderDateDTO list = new OrderDateDTO();
		//shopId条件
		LongFilter longFilter = new LongFilter();
		longFilter.setEquals(Long.valueOf(String.valueOf(findUserMercuryId.get("id"))));
		//status
		IntegerFilter integerFilter = new IntegerFilter();
		integerFilter.setEquals(pageOrder.getStatus());
		//deleted
		BooleanFilter booleanFilterB = new BooleanFilter();
		booleanFilterB.setEquals(false);
		BooleanFilter booleanFilterC = new BooleanFilter();
		booleanFilterC.setEquals(false);
		ProOrderCriteria criteria = new ProOrderCriteria();
		criteria.setShopId(longFilter);
		criteria.setDeletedB(booleanFilterB);
		criteria.setDeletedC(booleanFilterC);
		if(pageOrder.getStatus() == 0){
			findByCriteria = proOrderQueryService.findByCriteria(criteria, pageable);
		}else{
			criteria.setStatus(integerFilter);
			findByCriteria = proOrderQueryService.findByCriteria(criteria, pageable);
		}
		list.setProOrderAmount(findByCriteria.getTotalElements());
    	list.setProOrder(findByCriteria.getContent());
		return new ResponseEntity<>(list,HttpStatus.OK);
    }


    @ApiOperation("根据orderid查询订单详情")
    @GetMapping("/manage/findOrderById/{orderId}")
    public ResponseEntity<List<ProOrderItem>>  findOrderById(@PathVariable("orderId") Long orderId){

    	if(orderId == null){
			throw new BadRequestAlertException("该订单号有误", orderId.toString(), "");

    	}
    	return  new ResponseEntity<>(proOrderService.findOrderById(orderId),HttpStatus.OK);
    }

    @ApiOperation("根据skuid，获得订单商品详情")
    @GetMapping("/findOrderItemByskuid/{skuId}")
    public ResponseEntity<List<BigInteger>> findOrderItemBySkuId(@PathVariable("skuId") Long skuId){
    	return new ResponseEntity<>(proOrderService.findOrderItemBySkuId(skuId),HttpStatus.OK);
    }

    @ApiOperation("后台管理，根据orderNo逻辑删除订单")
    @GetMapping("/manage/deleteOrder/{orderId}")
    public ResponseEntity<Boolean> deleteOrder(@PathVariable("orderId") Long orderId){
		return new ResponseEntity<>( proOrderService.proOrderDelete(orderId),HttpStatus.OK);
    }
    
    @ApiOperation("确认收货")
    @GetMapping("/ConfirmPro/{orderNo}")
    public ResponseEntity<Boolean> ConfiremPro(@PathVariable("orderNo") String orderNo){
  
        //确认收货，更改订单状态
        proOrderService.updateOrderStatus(orderNo,4);
        //订单，商家分佣
        ProOrderDTO findOrderByOrderNo = proOrderService.findOrderByOrderNo(orderNo);
       
        //给用户加钱
    	//commissionService.orderSettlement(orderNo);
    	//给服务商加钱
        //commissionService.handleFacilitatorWallet(findOrderByOrderNo.getShopId(), findOrderByOrderNo.getPayment(), findOrderByOrderNo.getOrderNo());
	
	    return new ResponseEntity<>(true,HttpStatus.OK);
    }
}
