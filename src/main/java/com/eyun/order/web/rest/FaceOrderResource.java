package com.eyun.order.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyun.order.service.FaceOrderService;
import com.eyun.order.web.rest.errors.BadRequestAlertException;
import com.eyun.order.web.rest.util.HeaderUtil;
import com.eyun.order.web.rest.util.PaginationUtil;
import com.eyun.order.service.dto.FaceOrderDTO;
import com.eyun.order.service.dto.FaceOrderCriteria;
import com.eyun.order.service.FaceOrderQueryService;
import io.github.jhipster.web.util.ResponseUtil;
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

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing FaceOrder.
 */
@RestController
@RequestMapping("/api")
public class FaceOrderResource {

    private final Logger log = LoggerFactory.getLogger(FaceOrderResource.class);

    private static final String ENTITY_NAME = "faceOrder";

    private final FaceOrderService faceOrderService;

    private final FaceOrderQueryService faceOrderQueryService;

    public FaceOrderResource(FaceOrderService faceOrderService, FaceOrderQueryService faceOrderQueryService) {
        this.faceOrderService = faceOrderService;
        this.faceOrderQueryService = faceOrderQueryService;
    }

    /**
     * POST  /face-orders : Create a new faceOrder.
     *
     * @param faceOrderDTO the faceOrderDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new faceOrderDTO, or with status 400 (Bad Request) if the faceOrder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/face-orders")
    @Timed
    public ResponseEntity<FaceOrderDTO> createFaceOrder(@RequestBody FaceOrderDTO faceOrderDTO) throws URISyntaxException {
        log.debug("REST request to save FaceOrder : {}", faceOrderDTO);
        if (faceOrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new faceOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FaceOrderDTO result = faceOrderService.save(faceOrderDTO);
        return ResponseEntity.created(new URI("/api/face-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /face-orders : Updates an existing faceOrder.
     *
     * @param faceOrderDTO the faceOrderDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated faceOrderDTO,
     * or with status 400 (Bad Request) if the faceOrderDTO is not valid,
     * or with status 500 (Internal Server Error) if the faceOrderDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/face-orders")
    @Timed
    public ResponseEntity<FaceOrderDTO> updateFaceOrder(@RequestBody FaceOrderDTO faceOrderDTO) throws URISyntaxException {
        log.debug("REST request to update FaceOrder : {}", faceOrderDTO);
        if (faceOrderDTO.getId() == null) {
            return createFaceOrder(faceOrderDTO);
        }
        FaceOrderDTO result = faceOrderService.save(faceOrderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, faceOrderDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /face-orders : get all the faceOrders.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of faceOrders in body
     */
    @GetMapping("/face-orders")
    @Timed
    public ResponseEntity<List<FaceOrderDTO>> getAllFaceOrders(FaceOrderCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FaceOrders by criteria: {}", criteria);
        Page<FaceOrderDTO> page = faceOrderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/face-orders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /face-orders/:id : get the "id" faceOrder.
     *
     * @param id the id of the faceOrderDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the faceOrderDTO, or with status 404 (Not Found)
     */
    @GetMapping("/face-orders/{id}")
    @Timed
    public ResponseEntity<FaceOrderDTO> getFaceOrder(@PathVariable Long id) {
        log.debug("REST request to get FaceOrder : {}", id);
        FaceOrderDTO faceOrderDTO = faceOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(faceOrderDTO));
    }

    /**
     * DELETE  /face-orders/:id : delete the "id" faceOrder.
     *
     * @param id the id of the faceOrderDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/face-orders/{id}")
    @Timed
    public ResponseEntity<Void> deleteFaceOrder(@PathVariable Long id) {
        log.debug("REST request to delete FaceOrder : {}", id);
        faceOrderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
