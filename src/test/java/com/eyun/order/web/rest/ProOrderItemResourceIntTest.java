package com.eyun.order.web.rest;

import com.eyun.order.OrderApp;

import com.eyun.order.config.SecurityBeanOverrideConfiguration;

import com.eyun.order.domain.ProOrderItem;
import com.eyun.order.domain.ProOrder;
import com.eyun.order.repository.ProOrderItemRepository;
import com.eyun.order.service.ProOrderItemService;
import com.eyun.order.service.dto.ProOrderItemDTO;
import com.eyun.order.service.mapper.ProOrderItemMapper;
import com.eyun.order.web.rest.errors.ExceptionTranslator;
import com.eyun.order.service.dto.ProOrderItemCriteria;
import com.eyun.order.service.ProOrderItemQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.eyun.order.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProOrderItemResource REST controller.
 *
 * @see ProOrderItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {OrderApp.class, SecurityBeanOverrideConfiguration.class})
public class ProOrderItemResourceIntTest {

    private static final Long DEFAULT_PRODUCT_SKU_ID = 1L;
    private static final Long UPDATED_PRODUCT_SKU_ID = 2L;

    private static final Integer DEFAULT_COUNT = 1;
    private static final Integer UPDATED_COUNT = 2;

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final Instant DEFAULT_CREATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TRANSFER = new BigDecimal(0);
    private static final BigDecimal UPDATED_TRANSFER = new BigDecimal(1);

    @Autowired
    private ProOrderItemRepository proOrderItemRepository;

    @Autowired
    private ProOrderItemMapper proOrderItemMapper;

    @Autowired
    private ProOrderItemService proOrderItemService;

    @Autowired
    private ProOrderItemQueryService proOrderItemQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProOrderItemMockMvc;

    private ProOrderItem proOrderItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProOrderItemResource proOrderItemResource = new ProOrderItemResource(proOrderItemService, proOrderItemQueryService);
        this.restProOrderItemMockMvc = MockMvcBuilders.standaloneSetup(proOrderItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProOrderItem createEntity(EntityManager em) {
        ProOrderItem proOrderItem = new ProOrderItem()
            .productSkuId(DEFAULT_PRODUCT_SKU_ID)
            .count(DEFAULT_COUNT)
            .price(DEFAULT_PRICE)
            .createdTime(DEFAULT_CREATED_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME)
            .transfer(DEFAULT_TRANSFER);
        return proOrderItem;
    }

    @Before
    public void initTest() {
        proOrderItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createProOrderItem() throws Exception {
        int databaseSizeBeforeCreate = proOrderItemRepository.findAll().size();

        // Create the ProOrderItem
        ProOrderItemDTO proOrderItemDTO = proOrderItemMapper.toDto(proOrderItem);
        restProOrderItemMockMvc.perform(post("/api/pro-order-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proOrderItemDTO)))
            .andExpect(status().isCreated());

        // Validate the ProOrderItem in the database
        List<ProOrderItem> proOrderItemList = proOrderItemRepository.findAll();
        assertThat(proOrderItemList).hasSize(databaseSizeBeforeCreate + 1);
        ProOrderItem testProOrderItem = proOrderItemList.get(proOrderItemList.size() - 1);
        assertThat(testProOrderItem.getProductSkuId()).isEqualTo(DEFAULT_PRODUCT_SKU_ID);
        assertThat(testProOrderItem.getCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(testProOrderItem.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testProOrderItem.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testProOrderItem.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testProOrderItem.getTransfer()).isEqualTo(DEFAULT_TRANSFER);
    }

    @Test
    @Transactional
    public void createProOrderItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = proOrderItemRepository.findAll().size();

        // Create the ProOrderItem with an existing ID
        proOrderItem.setId(1L);
        ProOrderItemDTO proOrderItemDTO = proOrderItemMapper.toDto(proOrderItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProOrderItemMockMvc.perform(post("/api/pro-order-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proOrderItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProOrderItem in the database
        List<ProOrderItem> proOrderItemList = proOrderItemRepository.findAll();
        assertThat(proOrderItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProOrderItems() throws Exception {
        // Initialize the database
        proOrderItemRepository.saveAndFlush(proOrderItem);

        // Get all the proOrderItemList
        restProOrderItemMockMvc.perform(get("/api/pro-order-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proOrderItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].productSkuId").value(hasItem(DEFAULT_PRODUCT_SKU_ID.intValue())))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(DEFAULT_UPDATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].transfer").value(hasItem(DEFAULT_TRANSFER.intValue())));
    }

    @Test
    @Transactional
    public void getProOrderItem() throws Exception {
        // Initialize the database
        proOrderItemRepository.saveAndFlush(proOrderItem);

        // Get the proOrderItem
        restProOrderItemMockMvc.perform(get("/api/pro-order-items/{id}", proOrderItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(proOrderItem.getId().intValue()))
            .andExpect(jsonPath("$.productSkuId").value(DEFAULT_PRODUCT_SKU_ID.intValue()))
            .andExpect(jsonPath("$.count").value(DEFAULT_COUNT))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.createdTime").value(DEFAULT_CREATED_TIME.toString()))
            .andExpect(jsonPath("$.updatedTime").value(DEFAULT_UPDATED_TIME.toString()))
            .andExpect(jsonPath("$.transfer").value(DEFAULT_TRANSFER.intValue()));
    }

    @Test
    @Transactional
    public void getAllProOrderItemsByProductSkuIdIsEqualToSomething() throws Exception {
        // Initialize the database
        proOrderItemRepository.saveAndFlush(proOrderItem);

        // Get all the proOrderItemList where productSkuId equals to DEFAULT_PRODUCT_SKU_ID
        defaultProOrderItemShouldBeFound("productSkuId.equals=" + DEFAULT_PRODUCT_SKU_ID);

        // Get all the proOrderItemList where productSkuId equals to UPDATED_PRODUCT_SKU_ID
        defaultProOrderItemShouldNotBeFound("productSkuId.equals=" + UPDATED_PRODUCT_SKU_ID);
    }

    @Test
    @Transactional
    public void getAllProOrderItemsByProductSkuIdIsInShouldWork() throws Exception {
        // Initialize the database
        proOrderItemRepository.saveAndFlush(proOrderItem);

        // Get all the proOrderItemList where productSkuId in DEFAULT_PRODUCT_SKU_ID or UPDATED_PRODUCT_SKU_ID
        defaultProOrderItemShouldBeFound("productSkuId.in=" + DEFAULT_PRODUCT_SKU_ID + "," + UPDATED_PRODUCT_SKU_ID);

        // Get all the proOrderItemList where productSkuId equals to UPDATED_PRODUCT_SKU_ID
        defaultProOrderItemShouldNotBeFound("productSkuId.in=" + UPDATED_PRODUCT_SKU_ID);
    }

    @Test
    @Transactional
    public void getAllProOrderItemsByProductSkuIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        proOrderItemRepository.saveAndFlush(proOrderItem);

        // Get all the proOrderItemList where productSkuId is not null
        defaultProOrderItemShouldBeFound("productSkuId.specified=true");

        // Get all the proOrderItemList where productSkuId is null
        defaultProOrderItemShouldNotBeFound("productSkuId.specified=false");
    }

    @Test
    @Transactional
    public void getAllProOrderItemsByProductSkuIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        proOrderItemRepository.saveAndFlush(proOrderItem);

        // Get all the proOrderItemList where productSkuId greater than or equals to DEFAULT_PRODUCT_SKU_ID
        defaultProOrderItemShouldBeFound("productSkuId.greaterOrEqualThan=" + DEFAULT_PRODUCT_SKU_ID);

        // Get all the proOrderItemList where productSkuId greater than or equals to UPDATED_PRODUCT_SKU_ID
        defaultProOrderItemShouldNotBeFound("productSkuId.greaterOrEqualThan=" + UPDATED_PRODUCT_SKU_ID);
    }

    @Test
    @Transactional
    public void getAllProOrderItemsByProductSkuIdIsLessThanSomething() throws Exception {
        // Initialize the database
        proOrderItemRepository.saveAndFlush(proOrderItem);

        // Get all the proOrderItemList where productSkuId less than or equals to DEFAULT_PRODUCT_SKU_ID
        defaultProOrderItemShouldNotBeFound("productSkuId.lessThan=" + DEFAULT_PRODUCT_SKU_ID);

        // Get all the proOrderItemList where productSkuId less than or equals to UPDATED_PRODUCT_SKU_ID
        defaultProOrderItemShouldBeFound("productSkuId.lessThan=" + UPDATED_PRODUCT_SKU_ID);
    }


    @Test
    @Transactional
    public void getAllProOrderItemsByCountIsEqualToSomething() throws Exception {
        // Initialize the database
        proOrderItemRepository.saveAndFlush(proOrderItem);

        // Get all the proOrderItemList where count equals to DEFAULT_COUNT
        defaultProOrderItemShouldBeFound("count.equals=" + DEFAULT_COUNT);

        // Get all the proOrderItemList where count equals to UPDATED_COUNT
        defaultProOrderItemShouldNotBeFound("count.equals=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    public void getAllProOrderItemsByCountIsInShouldWork() throws Exception {
        // Initialize the database
        proOrderItemRepository.saveAndFlush(proOrderItem);

        // Get all the proOrderItemList where count in DEFAULT_COUNT or UPDATED_COUNT
        defaultProOrderItemShouldBeFound("count.in=" + DEFAULT_COUNT + "," + UPDATED_COUNT);

        // Get all the proOrderItemList where count equals to UPDATED_COUNT
        defaultProOrderItemShouldNotBeFound("count.in=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    public void getAllProOrderItemsByCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        proOrderItemRepository.saveAndFlush(proOrderItem);

        // Get all the proOrderItemList where count is not null
        defaultProOrderItemShouldBeFound("count.specified=true");

        // Get all the proOrderItemList where count is null
        defaultProOrderItemShouldNotBeFound("count.specified=false");
    }

    @Test
    @Transactional
    public void getAllProOrderItemsByCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        proOrderItemRepository.saveAndFlush(proOrderItem);

        // Get all the proOrderItemList where count greater than or equals to DEFAULT_COUNT
        defaultProOrderItemShouldBeFound("count.greaterOrEqualThan=" + DEFAULT_COUNT);

        // Get all the proOrderItemList where count greater than or equals to UPDATED_COUNT
        defaultProOrderItemShouldNotBeFound("count.greaterOrEqualThan=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    public void getAllProOrderItemsByCountIsLessThanSomething() throws Exception {
        // Initialize the database
        proOrderItemRepository.saveAndFlush(proOrderItem);

        // Get all the proOrderItemList where count less than or equals to DEFAULT_COUNT
        defaultProOrderItemShouldNotBeFound("count.lessThan=" + DEFAULT_COUNT);

        // Get all the proOrderItemList where count less than or equals to UPDATED_COUNT
        defaultProOrderItemShouldBeFound("count.lessThan=" + UPDATED_COUNT);
    }


    @Test
    @Transactional
    public void getAllProOrderItemsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        proOrderItemRepository.saveAndFlush(proOrderItem);

        // Get all the proOrderItemList where price equals to DEFAULT_PRICE
        defaultProOrderItemShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the proOrderItemList where price equals to UPDATED_PRICE
        defaultProOrderItemShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllProOrderItemsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        proOrderItemRepository.saveAndFlush(proOrderItem);

        // Get all the proOrderItemList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultProOrderItemShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the proOrderItemList where price equals to UPDATED_PRICE
        defaultProOrderItemShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllProOrderItemsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        proOrderItemRepository.saveAndFlush(proOrderItem);

        // Get all the proOrderItemList where price is not null
        defaultProOrderItemShouldBeFound("price.specified=true");

        // Get all the proOrderItemList where price is null
        defaultProOrderItemShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllProOrderItemsByCreatedTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        proOrderItemRepository.saveAndFlush(proOrderItem);

        // Get all the proOrderItemList where createdTime equals to DEFAULT_CREATED_TIME
        defaultProOrderItemShouldBeFound("createdTime.equals=" + DEFAULT_CREATED_TIME);

        // Get all the proOrderItemList where createdTime equals to UPDATED_CREATED_TIME
        defaultProOrderItemShouldNotBeFound("createdTime.equals=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllProOrderItemsByCreatedTimeIsInShouldWork() throws Exception {
        // Initialize the database
        proOrderItemRepository.saveAndFlush(proOrderItem);

        // Get all the proOrderItemList where createdTime in DEFAULT_CREATED_TIME or UPDATED_CREATED_TIME
        defaultProOrderItemShouldBeFound("createdTime.in=" + DEFAULT_CREATED_TIME + "," + UPDATED_CREATED_TIME);

        // Get all the proOrderItemList where createdTime equals to UPDATED_CREATED_TIME
        defaultProOrderItemShouldNotBeFound("createdTime.in=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllProOrderItemsByCreatedTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        proOrderItemRepository.saveAndFlush(proOrderItem);

        // Get all the proOrderItemList where createdTime is not null
        defaultProOrderItemShouldBeFound("createdTime.specified=true");

        // Get all the proOrderItemList where createdTime is null
        defaultProOrderItemShouldNotBeFound("createdTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllProOrderItemsByUpdatedTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        proOrderItemRepository.saveAndFlush(proOrderItem);

        // Get all the proOrderItemList where updatedTime equals to DEFAULT_UPDATED_TIME
        defaultProOrderItemShouldBeFound("updatedTime.equals=" + DEFAULT_UPDATED_TIME);

        // Get all the proOrderItemList where updatedTime equals to UPDATED_UPDATED_TIME
        defaultProOrderItemShouldNotBeFound("updatedTime.equals=" + UPDATED_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllProOrderItemsByUpdatedTimeIsInShouldWork() throws Exception {
        // Initialize the database
        proOrderItemRepository.saveAndFlush(proOrderItem);

        // Get all the proOrderItemList where updatedTime in DEFAULT_UPDATED_TIME or UPDATED_UPDATED_TIME
        defaultProOrderItemShouldBeFound("updatedTime.in=" + DEFAULT_UPDATED_TIME + "," + UPDATED_UPDATED_TIME);

        // Get all the proOrderItemList where updatedTime equals to UPDATED_UPDATED_TIME
        defaultProOrderItemShouldNotBeFound("updatedTime.in=" + UPDATED_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllProOrderItemsByUpdatedTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        proOrderItemRepository.saveAndFlush(proOrderItem);

        // Get all the proOrderItemList where updatedTime is not null
        defaultProOrderItemShouldBeFound("updatedTime.specified=true");

        // Get all the proOrderItemList where updatedTime is null
        defaultProOrderItemShouldNotBeFound("updatedTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllProOrderItemsByTransferIsEqualToSomething() throws Exception {
        // Initialize the database
        proOrderItemRepository.saveAndFlush(proOrderItem);

        // Get all the proOrderItemList where transfer equals to DEFAULT_TRANSFER
        defaultProOrderItemShouldBeFound("transfer.equals=" + DEFAULT_TRANSFER);

        // Get all the proOrderItemList where transfer equals to UPDATED_TRANSFER
        defaultProOrderItemShouldNotBeFound("transfer.equals=" + UPDATED_TRANSFER);
    }

    @Test
    @Transactional
    public void getAllProOrderItemsByTransferIsInShouldWork() throws Exception {
        // Initialize the database
        proOrderItemRepository.saveAndFlush(proOrderItem);

        // Get all the proOrderItemList where transfer in DEFAULT_TRANSFER or UPDATED_TRANSFER
        defaultProOrderItemShouldBeFound("transfer.in=" + DEFAULT_TRANSFER + "," + UPDATED_TRANSFER);

        // Get all the proOrderItemList where transfer equals to UPDATED_TRANSFER
        defaultProOrderItemShouldNotBeFound("transfer.in=" + UPDATED_TRANSFER);
    }

    @Test
    @Transactional
    public void getAllProOrderItemsByTransferIsNullOrNotNull() throws Exception {
        // Initialize the database
        proOrderItemRepository.saveAndFlush(proOrderItem);

        // Get all the proOrderItemList where transfer is not null
        defaultProOrderItemShouldBeFound("transfer.specified=true");

        // Get all the proOrderItemList where transfer is null
        defaultProOrderItemShouldNotBeFound("transfer.specified=false");
    }

    @Test
    @Transactional
    public void getAllProOrderItemsByProOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        ProOrder proOrder = ProOrderResourceIntTest.createEntity(em);
        em.persist(proOrder);
        em.flush();
        proOrderItem.setProOrder(proOrder);
        proOrderItemRepository.saveAndFlush(proOrderItem);
        Long proOrderId = proOrder.getId();

        // Get all the proOrderItemList where proOrder equals to proOrderId
        defaultProOrderItemShouldBeFound("proOrderId.equals=" + proOrderId);

        // Get all the proOrderItemList where proOrder equals to proOrderId + 1
        defaultProOrderItemShouldNotBeFound("proOrderId.equals=" + (proOrderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProOrderItemShouldBeFound(String filter) throws Exception {
        restProOrderItemMockMvc.perform(get("/api/pro-order-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proOrderItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].productSkuId").value(hasItem(DEFAULT_PRODUCT_SKU_ID.intValue())))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(DEFAULT_UPDATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].transfer").value(hasItem(DEFAULT_TRANSFER.intValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProOrderItemShouldNotBeFound(String filter) throws Exception {
        restProOrderItemMockMvc.perform(get("/api/pro-order-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingProOrderItem() throws Exception {
        // Get the proOrderItem
        restProOrderItemMockMvc.perform(get("/api/pro-order-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProOrderItem() throws Exception {
        // Initialize the database
        proOrderItemRepository.saveAndFlush(proOrderItem);
        int databaseSizeBeforeUpdate = proOrderItemRepository.findAll().size();

        // Update the proOrderItem
        ProOrderItem updatedProOrderItem = proOrderItemRepository.findOne(proOrderItem.getId());
        // Disconnect from session so that the updates on updatedProOrderItem are not directly saved in db
        em.detach(updatedProOrderItem);
        updatedProOrderItem
            .productSkuId(UPDATED_PRODUCT_SKU_ID)
            .count(UPDATED_COUNT)
            .price(UPDATED_PRICE)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .transfer(UPDATED_TRANSFER);
        ProOrderItemDTO proOrderItemDTO = proOrderItemMapper.toDto(updatedProOrderItem);

        restProOrderItemMockMvc.perform(put("/api/pro-order-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proOrderItemDTO)))
            .andExpect(status().isOk());

        // Validate the ProOrderItem in the database
        List<ProOrderItem> proOrderItemList = proOrderItemRepository.findAll();
        assertThat(proOrderItemList).hasSize(databaseSizeBeforeUpdate);
        ProOrderItem testProOrderItem = proOrderItemList.get(proOrderItemList.size() - 1);
        assertThat(testProOrderItem.getProductSkuId()).isEqualTo(UPDATED_PRODUCT_SKU_ID);
        assertThat(testProOrderItem.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testProOrderItem.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testProOrderItem.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testProOrderItem.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testProOrderItem.getTransfer()).isEqualTo(UPDATED_TRANSFER);
    }

    @Test
    @Transactional
    public void updateNonExistingProOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = proOrderItemRepository.findAll().size();

        // Create the ProOrderItem
        ProOrderItemDTO proOrderItemDTO = proOrderItemMapper.toDto(proOrderItem);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProOrderItemMockMvc.perform(put("/api/pro-order-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proOrderItemDTO)))
            .andExpect(status().isCreated());

        // Validate the ProOrderItem in the database
        List<ProOrderItem> proOrderItemList = proOrderItemRepository.findAll();
        assertThat(proOrderItemList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProOrderItem() throws Exception {
        // Initialize the database
        proOrderItemRepository.saveAndFlush(proOrderItem);
        int databaseSizeBeforeDelete = proOrderItemRepository.findAll().size();

        // Get the proOrderItem
        restProOrderItemMockMvc.perform(delete("/api/pro-order-items/{id}", proOrderItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProOrderItem> proOrderItemList = proOrderItemRepository.findAll();
        assertThat(proOrderItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProOrderItem.class);
        ProOrderItem proOrderItem1 = new ProOrderItem();
        proOrderItem1.setId(1L);
        ProOrderItem proOrderItem2 = new ProOrderItem();
        proOrderItem2.setId(proOrderItem1.getId());
        assertThat(proOrderItem1).isEqualTo(proOrderItem2);
        proOrderItem2.setId(2L);
        assertThat(proOrderItem1).isNotEqualTo(proOrderItem2);
        proOrderItem1.setId(null);
        assertThat(proOrderItem1).isNotEqualTo(proOrderItem2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProOrderItemDTO.class);
        ProOrderItemDTO proOrderItemDTO1 = new ProOrderItemDTO();
        proOrderItemDTO1.setId(1L);
        ProOrderItemDTO proOrderItemDTO2 = new ProOrderItemDTO();
        assertThat(proOrderItemDTO1).isNotEqualTo(proOrderItemDTO2);
        proOrderItemDTO2.setId(proOrderItemDTO1.getId());
        assertThat(proOrderItemDTO1).isEqualTo(proOrderItemDTO2);
        proOrderItemDTO2.setId(2L);
        assertThat(proOrderItemDTO1).isNotEqualTo(proOrderItemDTO2);
        proOrderItemDTO1.setId(null);
        assertThat(proOrderItemDTO1).isNotEqualTo(proOrderItemDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(proOrderItemMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(proOrderItemMapper.fromId(null)).isNull();
    }
}
