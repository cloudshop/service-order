package com.eyun.order.web.rest;

import com.eyun.order.OrderApp;

import com.eyun.order.config.SecurityBeanOverrideConfiguration;

import com.eyun.order.domain.DepOrder;
import com.eyun.order.repository.DepOrderRepository;
import com.eyun.order.service.DepOrderService;
import com.eyun.order.service.dto.DepOrderDTO;
import com.eyun.order.service.mapper.DepOrderMapper;
import com.eyun.order.web.rest.errors.ExceptionTranslator;
import com.eyun.order.service.dto.DepOrderCriteria;
import com.eyun.order.service.DepOrderQueryService;

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
 * Test class for the DepOrderResource REST controller.
 *
 * @see DepOrderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {OrderApp.class, SecurityBeanOverrideConfiguration.class})
public class DepOrderResourceIntTest {

    private static final String DEFAULT_ORDER_NO = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_NO = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final Long DEFAULT_USERID = 1L;
    private static final Long UPDATED_USERID = 2L;

    private static final BigDecimal DEFAULT_PAYMENT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAYMENT = new BigDecimal(2);

    private static final Integer DEFAULT_PAY_TYPE = 1;
    private static final Integer UPDATED_PAY_TYPE = 2;

    private static final String DEFAULT_PAY_NO = "AAAAAAAAAA";
    private static final String UPDATED_PAY_NO = "BBBBBBBBBB";

    private static final Instant DEFAULT_PAY_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAY_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_WALLET_ID = 1L;
    private static final Long UPDATED_WALLET_ID = 2L;

    private static final Instant DEFAULT_CREATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    @Autowired
    private DepOrderRepository depOrderRepository;

    @Autowired
    private DepOrderMapper depOrderMapper;

    @Autowired
    private DepOrderService depOrderService;

    @Autowired
    private DepOrderQueryService depOrderQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDepOrderMockMvc;

    private DepOrder depOrder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DepOrderResource depOrderResource = new DepOrderResource(depOrderService, depOrderQueryService);
        this.restDepOrderMockMvc = MockMvcBuilders.standaloneSetup(depOrderResource)
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
    public static DepOrder createEntity(EntityManager em) {
        DepOrder depOrder = new DepOrder()
            .orderNo(DEFAULT_ORDER_NO)
            .status(DEFAULT_STATUS)
            .userid(DEFAULT_USERID)
            .payment(DEFAULT_PAYMENT)
            .payType(DEFAULT_PAY_TYPE)
            .payNo(DEFAULT_PAY_NO)
            .payTime(DEFAULT_PAY_TIME)
            .walletId(DEFAULT_WALLET_ID)
            .createdTime(DEFAULT_CREATED_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME)
            .deleted(DEFAULT_DELETED);
        return depOrder;
    }

    @Before
    public void initTest() {
        depOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createDepOrder() throws Exception {
        int databaseSizeBeforeCreate = depOrderRepository.findAll().size();

        // Create the DepOrder
        DepOrderDTO depOrderDTO = depOrderMapper.toDto(depOrder);
        restDepOrderMockMvc.perform(post("/api/dep-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depOrderDTO)))
            .andExpect(status().isCreated());

        // Validate the DepOrder in the database
        List<DepOrder> depOrderList = depOrderRepository.findAll();
        assertThat(depOrderList).hasSize(databaseSizeBeforeCreate + 1);
        DepOrder testDepOrder = depOrderList.get(depOrderList.size() - 1);
        assertThat(testDepOrder.getOrderNo()).isEqualTo(DEFAULT_ORDER_NO);
        assertThat(testDepOrder.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDepOrder.getUserid()).isEqualTo(DEFAULT_USERID);
        assertThat(testDepOrder.getPayment()).isEqualTo(DEFAULT_PAYMENT);
        assertThat(testDepOrder.getPayType()).isEqualTo(DEFAULT_PAY_TYPE);
        assertThat(testDepOrder.getPayNo()).isEqualTo(DEFAULT_PAY_NO);
        assertThat(testDepOrder.getPayTime()).isEqualTo(DEFAULT_PAY_TIME);
        assertThat(testDepOrder.getWalletId()).isEqualTo(DEFAULT_WALLET_ID);
        assertThat(testDepOrder.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testDepOrder.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testDepOrder.isDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void createDepOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = depOrderRepository.findAll().size();

        // Create the DepOrder with an existing ID
        depOrder.setId(1L);
        DepOrderDTO depOrderDTO = depOrderMapper.toDto(depOrder);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepOrderMockMvc.perform(post("/api/dep-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depOrderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DepOrder in the database
        List<DepOrder> depOrderList = depOrderRepository.findAll();
        assertThat(depOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDepOrders() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList
        restDepOrderMockMvc.perform(get("/api/dep-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderNo").value(hasItem(DEFAULT_ORDER_NO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.intValue())))
            .andExpect(jsonPath("$.[*].payment").value(hasItem(DEFAULT_PAYMENT.intValue())))
            .andExpect(jsonPath("$.[*].payType").value(hasItem(DEFAULT_PAY_TYPE)))
            .andExpect(jsonPath("$.[*].payNo").value(hasItem(DEFAULT_PAY_NO.toString())))
            .andExpect(jsonPath("$.[*].payTime").value(hasItem(DEFAULT_PAY_TIME.toString())))
            .andExpect(jsonPath("$.[*].walletId").value(hasItem(DEFAULT_WALLET_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(DEFAULT_UPDATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    public void getDepOrder() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get the depOrder
        restDepOrderMockMvc.perform(get("/api/dep-orders/{id}", depOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(depOrder.getId().intValue()))
            .andExpect(jsonPath("$.orderNo").value(DEFAULT_ORDER_NO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.userid").value(DEFAULT_USERID.intValue()))
            .andExpect(jsonPath("$.payment").value(DEFAULT_PAYMENT.intValue()))
            .andExpect(jsonPath("$.payType").value(DEFAULT_PAY_TYPE))
            .andExpect(jsonPath("$.payNo").value(DEFAULT_PAY_NO.toString()))
            .andExpect(jsonPath("$.payTime").value(DEFAULT_PAY_TIME.toString()))
            .andExpect(jsonPath("$.walletId").value(DEFAULT_WALLET_ID.intValue()))
            .andExpect(jsonPath("$.createdTime").value(DEFAULT_CREATED_TIME.toString()))
            .andExpect(jsonPath("$.updatedTime").value(DEFAULT_UPDATED_TIME.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllDepOrdersByOrderNoIsEqualToSomething() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where orderNo equals to DEFAULT_ORDER_NO
        defaultDepOrderShouldBeFound("orderNo.equals=" + DEFAULT_ORDER_NO);

        // Get all the depOrderList where orderNo equals to UPDATED_ORDER_NO
        defaultDepOrderShouldNotBeFound("orderNo.equals=" + UPDATED_ORDER_NO);
    }

    @Test
    @Transactional
    public void getAllDepOrdersByOrderNoIsInShouldWork() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where orderNo in DEFAULT_ORDER_NO or UPDATED_ORDER_NO
        defaultDepOrderShouldBeFound("orderNo.in=" + DEFAULT_ORDER_NO + "," + UPDATED_ORDER_NO);

        // Get all the depOrderList where orderNo equals to UPDATED_ORDER_NO
        defaultDepOrderShouldNotBeFound("orderNo.in=" + UPDATED_ORDER_NO);
    }

    @Test
    @Transactional
    public void getAllDepOrdersByOrderNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where orderNo is not null
        defaultDepOrderShouldBeFound("orderNo.specified=true");

        // Get all the depOrderList where orderNo is null
        defaultDepOrderShouldNotBeFound("orderNo.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepOrdersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where status equals to DEFAULT_STATUS
        defaultDepOrderShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the depOrderList where status equals to UPDATED_STATUS
        defaultDepOrderShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllDepOrdersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultDepOrderShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the depOrderList where status equals to UPDATED_STATUS
        defaultDepOrderShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllDepOrdersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where status is not null
        defaultDepOrderShouldBeFound("status.specified=true");

        // Get all the depOrderList where status is null
        defaultDepOrderShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepOrdersByStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where status greater than or equals to DEFAULT_STATUS
        defaultDepOrderShouldBeFound("status.greaterOrEqualThan=" + DEFAULT_STATUS);

        // Get all the depOrderList where status greater than or equals to UPDATED_STATUS
        defaultDepOrderShouldNotBeFound("status.greaterOrEqualThan=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllDepOrdersByStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where status less than or equals to DEFAULT_STATUS
        defaultDepOrderShouldNotBeFound("status.lessThan=" + DEFAULT_STATUS);

        // Get all the depOrderList where status less than or equals to UPDATED_STATUS
        defaultDepOrderShouldBeFound("status.lessThan=" + UPDATED_STATUS);
    }


    @Test
    @Transactional
    public void getAllDepOrdersByUseridIsEqualToSomething() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where userid equals to DEFAULT_USERID
        defaultDepOrderShouldBeFound("userid.equals=" + DEFAULT_USERID);

        // Get all the depOrderList where userid equals to UPDATED_USERID
        defaultDepOrderShouldNotBeFound("userid.equals=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllDepOrdersByUseridIsInShouldWork() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where userid in DEFAULT_USERID or UPDATED_USERID
        defaultDepOrderShouldBeFound("userid.in=" + DEFAULT_USERID + "," + UPDATED_USERID);

        // Get all the depOrderList where userid equals to UPDATED_USERID
        defaultDepOrderShouldNotBeFound("userid.in=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllDepOrdersByUseridIsNullOrNotNull() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where userid is not null
        defaultDepOrderShouldBeFound("userid.specified=true");

        // Get all the depOrderList where userid is null
        defaultDepOrderShouldNotBeFound("userid.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepOrdersByUseridIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where userid greater than or equals to DEFAULT_USERID
        defaultDepOrderShouldBeFound("userid.greaterOrEqualThan=" + DEFAULT_USERID);

        // Get all the depOrderList where userid greater than or equals to UPDATED_USERID
        defaultDepOrderShouldNotBeFound("userid.greaterOrEqualThan=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllDepOrdersByUseridIsLessThanSomething() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where userid less than or equals to DEFAULT_USERID
        defaultDepOrderShouldNotBeFound("userid.lessThan=" + DEFAULT_USERID);

        // Get all the depOrderList where userid less than or equals to UPDATED_USERID
        defaultDepOrderShouldBeFound("userid.lessThan=" + UPDATED_USERID);
    }


    @Test
    @Transactional
    public void getAllDepOrdersByPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where payment equals to DEFAULT_PAYMENT
        defaultDepOrderShouldBeFound("payment.equals=" + DEFAULT_PAYMENT);

        // Get all the depOrderList where payment equals to UPDATED_PAYMENT
        defaultDepOrderShouldNotBeFound("payment.equals=" + UPDATED_PAYMENT);
    }

    @Test
    @Transactional
    public void getAllDepOrdersByPaymentIsInShouldWork() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where payment in DEFAULT_PAYMENT or UPDATED_PAYMENT
        defaultDepOrderShouldBeFound("payment.in=" + DEFAULT_PAYMENT + "," + UPDATED_PAYMENT);

        // Get all the depOrderList where payment equals to UPDATED_PAYMENT
        defaultDepOrderShouldNotBeFound("payment.in=" + UPDATED_PAYMENT);
    }

    @Test
    @Transactional
    public void getAllDepOrdersByPaymentIsNullOrNotNull() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where payment is not null
        defaultDepOrderShouldBeFound("payment.specified=true");

        // Get all the depOrderList where payment is null
        defaultDepOrderShouldNotBeFound("payment.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepOrdersByPayTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where payType equals to DEFAULT_PAY_TYPE
        defaultDepOrderShouldBeFound("payType.equals=" + DEFAULT_PAY_TYPE);

        // Get all the depOrderList where payType equals to UPDATED_PAY_TYPE
        defaultDepOrderShouldNotBeFound("payType.equals=" + UPDATED_PAY_TYPE);
    }

    @Test
    @Transactional
    public void getAllDepOrdersByPayTypeIsInShouldWork() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where payType in DEFAULT_PAY_TYPE or UPDATED_PAY_TYPE
        defaultDepOrderShouldBeFound("payType.in=" + DEFAULT_PAY_TYPE + "," + UPDATED_PAY_TYPE);

        // Get all the depOrderList where payType equals to UPDATED_PAY_TYPE
        defaultDepOrderShouldNotBeFound("payType.in=" + UPDATED_PAY_TYPE);
    }

    @Test
    @Transactional
    public void getAllDepOrdersByPayTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where payType is not null
        defaultDepOrderShouldBeFound("payType.specified=true");

        // Get all the depOrderList where payType is null
        defaultDepOrderShouldNotBeFound("payType.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepOrdersByPayTypeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where payType greater than or equals to DEFAULT_PAY_TYPE
        defaultDepOrderShouldBeFound("payType.greaterOrEqualThan=" + DEFAULT_PAY_TYPE);

        // Get all the depOrderList where payType greater than or equals to UPDATED_PAY_TYPE
        defaultDepOrderShouldNotBeFound("payType.greaterOrEqualThan=" + UPDATED_PAY_TYPE);
    }

    @Test
    @Transactional
    public void getAllDepOrdersByPayTypeIsLessThanSomething() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where payType less than or equals to DEFAULT_PAY_TYPE
        defaultDepOrderShouldNotBeFound("payType.lessThan=" + DEFAULT_PAY_TYPE);

        // Get all the depOrderList where payType less than or equals to UPDATED_PAY_TYPE
        defaultDepOrderShouldBeFound("payType.lessThan=" + UPDATED_PAY_TYPE);
    }


    @Test
    @Transactional
    public void getAllDepOrdersByPayNoIsEqualToSomething() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where payNo equals to DEFAULT_PAY_NO
        defaultDepOrderShouldBeFound("payNo.equals=" + DEFAULT_PAY_NO);

        // Get all the depOrderList where payNo equals to UPDATED_PAY_NO
        defaultDepOrderShouldNotBeFound("payNo.equals=" + UPDATED_PAY_NO);
    }

    @Test
    @Transactional
    public void getAllDepOrdersByPayNoIsInShouldWork() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where payNo in DEFAULT_PAY_NO or UPDATED_PAY_NO
        defaultDepOrderShouldBeFound("payNo.in=" + DEFAULT_PAY_NO + "," + UPDATED_PAY_NO);

        // Get all the depOrderList where payNo equals to UPDATED_PAY_NO
        defaultDepOrderShouldNotBeFound("payNo.in=" + UPDATED_PAY_NO);
    }

    @Test
    @Transactional
    public void getAllDepOrdersByPayNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where payNo is not null
        defaultDepOrderShouldBeFound("payNo.specified=true");

        // Get all the depOrderList where payNo is null
        defaultDepOrderShouldNotBeFound("payNo.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepOrdersByPayTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where payTime equals to DEFAULT_PAY_TIME
        defaultDepOrderShouldBeFound("payTime.equals=" + DEFAULT_PAY_TIME);

        // Get all the depOrderList where payTime equals to UPDATED_PAY_TIME
        defaultDepOrderShouldNotBeFound("payTime.equals=" + UPDATED_PAY_TIME);
    }

    @Test
    @Transactional
    public void getAllDepOrdersByPayTimeIsInShouldWork() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where payTime in DEFAULT_PAY_TIME or UPDATED_PAY_TIME
        defaultDepOrderShouldBeFound("payTime.in=" + DEFAULT_PAY_TIME + "," + UPDATED_PAY_TIME);

        // Get all the depOrderList where payTime equals to UPDATED_PAY_TIME
        defaultDepOrderShouldNotBeFound("payTime.in=" + UPDATED_PAY_TIME);
    }

    @Test
    @Transactional
    public void getAllDepOrdersByPayTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where payTime is not null
        defaultDepOrderShouldBeFound("payTime.specified=true");

        // Get all the depOrderList where payTime is null
        defaultDepOrderShouldNotBeFound("payTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepOrdersByWalletIdIsEqualToSomething() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where walletId equals to DEFAULT_WALLET_ID
        defaultDepOrderShouldBeFound("walletId.equals=" + DEFAULT_WALLET_ID);

        // Get all the depOrderList where walletId equals to UPDATED_WALLET_ID
        defaultDepOrderShouldNotBeFound("walletId.equals=" + UPDATED_WALLET_ID);
    }

    @Test
    @Transactional
    public void getAllDepOrdersByWalletIdIsInShouldWork() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where walletId in DEFAULT_WALLET_ID or UPDATED_WALLET_ID
        defaultDepOrderShouldBeFound("walletId.in=" + DEFAULT_WALLET_ID + "," + UPDATED_WALLET_ID);

        // Get all the depOrderList where walletId equals to UPDATED_WALLET_ID
        defaultDepOrderShouldNotBeFound("walletId.in=" + UPDATED_WALLET_ID);
    }

    @Test
    @Transactional
    public void getAllDepOrdersByWalletIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where walletId is not null
        defaultDepOrderShouldBeFound("walletId.specified=true");

        // Get all the depOrderList where walletId is null
        defaultDepOrderShouldNotBeFound("walletId.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepOrdersByWalletIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where walletId greater than or equals to DEFAULT_WALLET_ID
        defaultDepOrderShouldBeFound("walletId.greaterOrEqualThan=" + DEFAULT_WALLET_ID);

        // Get all the depOrderList where walletId greater than or equals to UPDATED_WALLET_ID
        defaultDepOrderShouldNotBeFound("walletId.greaterOrEqualThan=" + UPDATED_WALLET_ID);
    }

    @Test
    @Transactional
    public void getAllDepOrdersByWalletIdIsLessThanSomething() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where walletId less than or equals to DEFAULT_WALLET_ID
        defaultDepOrderShouldNotBeFound("walletId.lessThan=" + DEFAULT_WALLET_ID);

        // Get all the depOrderList where walletId less than or equals to UPDATED_WALLET_ID
        defaultDepOrderShouldBeFound("walletId.lessThan=" + UPDATED_WALLET_ID);
    }


    @Test
    @Transactional
    public void getAllDepOrdersByCreatedTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where createdTime equals to DEFAULT_CREATED_TIME
        defaultDepOrderShouldBeFound("createdTime.equals=" + DEFAULT_CREATED_TIME);

        // Get all the depOrderList where createdTime equals to UPDATED_CREATED_TIME
        defaultDepOrderShouldNotBeFound("createdTime.equals=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllDepOrdersByCreatedTimeIsInShouldWork() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where createdTime in DEFAULT_CREATED_TIME or UPDATED_CREATED_TIME
        defaultDepOrderShouldBeFound("createdTime.in=" + DEFAULT_CREATED_TIME + "," + UPDATED_CREATED_TIME);

        // Get all the depOrderList where createdTime equals to UPDATED_CREATED_TIME
        defaultDepOrderShouldNotBeFound("createdTime.in=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllDepOrdersByCreatedTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where createdTime is not null
        defaultDepOrderShouldBeFound("createdTime.specified=true");

        // Get all the depOrderList where createdTime is null
        defaultDepOrderShouldNotBeFound("createdTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepOrdersByUpdatedTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where updatedTime equals to DEFAULT_UPDATED_TIME
        defaultDepOrderShouldBeFound("updatedTime.equals=" + DEFAULT_UPDATED_TIME);

        // Get all the depOrderList where updatedTime equals to UPDATED_UPDATED_TIME
        defaultDepOrderShouldNotBeFound("updatedTime.equals=" + UPDATED_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllDepOrdersByUpdatedTimeIsInShouldWork() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where updatedTime in DEFAULT_UPDATED_TIME or UPDATED_UPDATED_TIME
        defaultDepOrderShouldBeFound("updatedTime.in=" + DEFAULT_UPDATED_TIME + "," + UPDATED_UPDATED_TIME);

        // Get all the depOrderList where updatedTime equals to UPDATED_UPDATED_TIME
        defaultDepOrderShouldNotBeFound("updatedTime.in=" + UPDATED_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllDepOrdersByUpdatedTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where updatedTime is not null
        defaultDepOrderShouldBeFound("updatedTime.specified=true");

        // Get all the depOrderList where updatedTime is null
        defaultDepOrderShouldNotBeFound("updatedTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepOrdersByDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where deleted equals to DEFAULT_DELETED
        defaultDepOrderShouldBeFound("deleted.equals=" + DEFAULT_DELETED);

        // Get all the depOrderList where deleted equals to UPDATED_DELETED
        defaultDepOrderShouldNotBeFound("deleted.equals=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void getAllDepOrdersByDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where deleted in DEFAULT_DELETED or UPDATED_DELETED
        defaultDepOrderShouldBeFound("deleted.in=" + DEFAULT_DELETED + "," + UPDATED_DELETED);

        // Get all the depOrderList where deleted equals to UPDATED_DELETED
        defaultDepOrderShouldNotBeFound("deleted.in=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void getAllDepOrdersByDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);

        // Get all the depOrderList where deleted is not null
        defaultDepOrderShouldBeFound("deleted.specified=true");

        // Get all the depOrderList where deleted is null
        defaultDepOrderShouldNotBeFound("deleted.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultDepOrderShouldBeFound(String filter) throws Exception {
        restDepOrderMockMvc.perform(get("/api/dep-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderNo").value(hasItem(DEFAULT_ORDER_NO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.intValue())))
            .andExpect(jsonPath("$.[*].payment").value(hasItem(DEFAULT_PAYMENT.intValue())))
            .andExpect(jsonPath("$.[*].payType").value(hasItem(DEFAULT_PAY_TYPE)))
            .andExpect(jsonPath("$.[*].payNo").value(hasItem(DEFAULT_PAY_NO.toString())))
            .andExpect(jsonPath("$.[*].payTime").value(hasItem(DEFAULT_PAY_TIME.toString())))
            .andExpect(jsonPath("$.[*].walletId").value(hasItem(DEFAULT_WALLET_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(DEFAULT_UPDATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultDepOrderShouldNotBeFound(String filter) throws Exception {
        restDepOrderMockMvc.perform(get("/api/dep-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingDepOrder() throws Exception {
        // Get the depOrder
        restDepOrderMockMvc.perform(get("/api/dep-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDepOrder() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);
        int databaseSizeBeforeUpdate = depOrderRepository.findAll().size();

        // Update the depOrder
        DepOrder updatedDepOrder = depOrderRepository.findOne(depOrder.getId());
        // Disconnect from session so that the updates on updatedDepOrder are not directly saved in db
        em.detach(updatedDepOrder);
        updatedDepOrder
            .orderNo(UPDATED_ORDER_NO)
            .status(UPDATED_STATUS)
            .userid(UPDATED_USERID)
            .payment(UPDATED_PAYMENT)
            .payType(UPDATED_PAY_TYPE)
            .payNo(UPDATED_PAY_NO)
            .payTime(UPDATED_PAY_TIME)
            .walletId(UPDATED_WALLET_ID)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .deleted(UPDATED_DELETED);
        DepOrderDTO depOrderDTO = depOrderMapper.toDto(updatedDepOrder);

        restDepOrderMockMvc.perform(put("/api/dep-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depOrderDTO)))
            .andExpect(status().isOk());

        // Validate the DepOrder in the database
        List<DepOrder> depOrderList = depOrderRepository.findAll();
        assertThat(depOrderList).hasSize(databaseSizeBeforeUpdate);
        DepOrder testDepOrder = depOrderList.get(depOrderList.size() - 1);
        assertThat(testDepOrder.getOrderNo()).isEqualTo(UPDATED_ORDER_NO);
        assertThat(testDepOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDepOrder.getUserid()).isEqualTo(UPDATED_USERID);
        assertThat(testDepOrder.getPayment()).isEqualTo(UPDATED_PAYMENT);
        assertThat(testDepOrder.getPayType()).isEqualTo(UPDATED_PAY_TYPE);
        assertThat(testDepOrder.getPayNo()).isEqualTo(UPDATED_PAY_NO);
        assertThat(testDepOrder.getPayTime()).isEqualTo(UPDATED_PAY_TIME);
        assertThat(testDepOrder.getWalletId()).isEqualTo(UPDATED_WALLET_ID);
        assertThat(testDepOrder.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testDepOrder.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testDepOrder.isDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingDepOrder() throws Exception {
        int databaseSizeBeforeUpdate = depOrderRepository.findAll().size();

        // Create the DepOrder
        DepOrderDTO depOrderDTO = depOrderMapper.toDto(depOrder);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDepOrderMockMvc.perform(put("/api/dep-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depOrderDTO)))
            .andExpect(status().isCreated());

        // Validate the DepOrder in the database
        List<DepOrder> depOrderList = depOrderRepository.findAll();
        assertThat(depOrderList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDepOrder() throws Exception {
        // Initialize the database
        depOrderRepository.saveAndFlush(depOrder);
        int databaseSizeBeforeDelete = depOrderRepository.findAll().size();

        // Get the depOrder
        restDepOrderMockMvc.perform(delete("/api/dep-orders/{id}", depOrder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DepOrder> depOrderList = depOrderRepository.findAll();
        assertThat(depOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepOrder.class);
        DepOrder depOrder1 = new DepOrder();
        depOrder1.setId(1L);
        DepOrder depOrder2 = new DepOrder();
        depOrder2.setId(depOrder1.getId());
        assertThat(depOrder1).isEqualTo(depOrder2);
        depOrder2.setId(2L);
        assertThat(depOrder1).isNotEqualTo(depOrder2);
        depOrder1.setId(null);
        assertThat(depOrder1).isNotEqualTo(depOrder2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepOrderDTO.class);
        DepOrderDTO depOrderDTO1 = new DepOrderDTO();
        depOrderDTO1.setId(1L);
        DepOrderDTO depOrderDTO2 = new DepOrderDTO();
        assertThat(depOrderDTO1).isNotEqualTo(depOrderDTO2);
        depOrderDTO2.setId(depOrderDTO1.getId());
        assertThat(depOrderDTO1).isEqualTo(depOrderDTO2);
        depOrderDTO2.setId(2L);
        assertThat(depOrderDTO1).isNotEqualTo(depOrderDTO2);
        depOrderDTO1.setId(null);
        assertThat(depOrderDTO1).isNotEqualTo(depOrderDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(depOrderMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(depOrderMapper.fromId(null)).isNull();
    }
}
