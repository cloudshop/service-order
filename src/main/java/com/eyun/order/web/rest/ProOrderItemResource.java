package com.eyun.order.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyun.order.service.ProOrderItemService;
import com.eyun.order.web.rest.errors.BadRequestAlertException;
import com.eyun.order.web.rest.util.HeaderUtil;
import com.eyun.order.web.rest.util.PaginationUtil;
import com.eyun.order.service.dto.ProOrderItemDTO;
import com.eyun.order.service.dto.ProOrderItemCriteria;
import com.eyun.order.service.ProOrderItemQueryService;
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
 * REST controller for managing ProOrderItem.
 */
@RestController
@RequestMapping("/api")
public class ProOrderItemResource {

    private final Logger log = LoggerFactory.getLogger(ProOrderItemResource.class);

    private static final String ENTITY_NAME = "proOrderItem";

    private final ProOrderItemService proOrderItemService;

    private final ProOrderItemQueryService proOrderItemQueryService;

    public ProOrderItemResource(ProOrderItemService proOrderItemService, ProOrderItemQueryService proOrderItemQueryService) {
        this.proOrderItemService = proOrderItemService;
        this.proOrderItemQueryService = proOrderItemQueryService;
    }

    /**
     * POST  /pro-order-items : Create a new proOrderItem.
     *
     * @param proOrderItemDTO the proOrderItemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new proOrderItemDTO, or with status 400 (Bad Request) if the proOrderItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pro-order-items")
    @Timed
    public ResponseEntity<ProOrderItemDTO> createProOrderItem(@RequestBody ProOrderItemDTO proOrderItemDTO) throws URISyntaxException {
        log.debug("REST request to save ProOrderItem : {}", proOrderItemDTO);
        if (proOrderItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new proOrderItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProOrderItemDTO result = proOrderItemService.save(proOrderItemDTO);
        return ResponseEntity.created(new URI("/api/pro-order-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pro-order-items : Updates an existing proOrderItem.
     *
     * @param proOrderItemDTO the proOrderItemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated proOrderItemDTO,
     * or with status 400 (Bad Request) if the proOrderItemDTO is not valid,
     * or with status 500 (Internal Server Error) if the proOrderItemDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pro-order-items")
    @Timed
    public ResponseEntity<ProOrderItemDTO> updateProOrderItem(@RequestBody ProOrderItemDTO proOrderItemDTO) throws URISyntaxException {
        log.debug("REST request to update ProOrderItem : {}", proOrderItemDTO);
        if (proOrderItemDTO.getId() == null) {
            return createProOrderItem(proOrderItemDTO);
        }
        ProOrderItemDTO result = proOrderItemService.save(proOrderItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, proOrderItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pro-order-items : get all the proOrderItems.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of proOrderItems in body
     */
    @GetMapping("/pro-order-items")
    @Timed
    public ResponseEntity<List<ProOrderItemDTO>> getAllProOrderItems(ProOrderItemCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ProOrderItems by criteria: {}", criteria);
        Page<ProOrderItemDTO> page = proOrderItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pro-order-items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pro-order-items/:id : get the "id" proOrderItem.
     *
     * @param id the id of the proOrderItemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the proOrderItemDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pro-order-items/{id}")
    @Timed
    public ResponseEntity<ProOrderItemDTO> getProOrderItem(@PathVariable Long id) {
        log.debug("REST request to get ProOrderItem : {}", id);
        ProOrderItemDTO proOrderItemDTO = proOrderItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(proOrderItemDTO));
    }

    /**
     * DELETE  /pro-order-items/:id : delete the "id" proOrderItem.
     *
     * @param id the id of the proOrderItemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pro-order-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteProOrderItem(@PathVariable Long id) {
        log.debug("REST request to delete ProOrderItem : {}", id);
        proOrderItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    
}
