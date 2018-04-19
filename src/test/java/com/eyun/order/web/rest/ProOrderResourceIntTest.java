package com.eyun.order.web.rest;

import com.eyun.order.OrderApp;

import com.eyun.order.config.SecurityBeanOverrideConfiguration;

import com.eyun.order.domain.ProOrder;
import com.eyun.order.domain.ProOrderItem;
import com.eyun.order.repository.ProOrderRepository;
import com.eyun.order.service.ProOrderService;
import com.eyun.order.service.dto.ProOrderDTO;
import com.eyun.order.service.mapper.ProOrderMapper;
import com.eyun.order.web.rest.errors.ExceptionTranslator;
import com.eyun.order.service.dto.ProOrderCriteria;
import com.eyun.order.service.ProOrderQueryService;

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
 * Test class for the ProOrderResource REST controller.
 *
 * @see ProOrderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {OrderApp.class, SecurityBeanOverrideConfiguration.class})
public class ProOrderResourceIntTest {

    private static final Long DEFAULT_C_USERID = 1L;
    private static final Long UPDATED_C_USERID = 2L;

    private static final String DEFAULT_ORDER_NO = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_NO = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final BigDecimal DEFAULT_PAYMENT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAYMENT = new BigDecimal(2);

    private static final Integer DEFAULT_PAYMENT_TYPE = 1;
    private static final Integer UPDATED_PAYMENT_TYPE = 2;

    private static final Instant DEFAULT_PAYMENT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_POST_FEE = new BigDecimal(1);
    private static final BigDecimal UPDATED_POST_FEE = new BigDecimal(2);

    private static final Instant DEFAULT_CONSIGN_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CONSIGN_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CLOSE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CLOSE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_SHIPPING_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHIPPING_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHIPING_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SHIPING_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BUYER_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_BUYER_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_BUYER_NICK = "AAAAAAAAAA";
    private static final String UPDATED_BUYER_NICK = "BBBBBBBBBB";

    private static final Boolean DEFAULT_BUYER_RATE = false;
    private static final Boolean UPDATED_BUYER_RATE = true;

    private static final Instant DEFAULT_CREATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_DELETED_B = false;
    private static final Boolean UPDATED_DELETED_B = true;

    private static final Boolean DEFAULT_DELETED_C = false;
    private static final Boolean UPDATED_DELETED_C = true;

    private static final Long DEFAULT_SHOP_ID = 1L;
    private static final Long UPDATED_SHOP_ID = 2L;

    private static final String DEFAULT_PAY_NO = "AAAAAAAAAA";
    private static final String UPDATED_PAY_NO = "BBBBBBBBBB";

    @Autowired
    private ProOrderRepository proOrderRepository;

    @Autowired
    private ProOrderMapper proOrderMapper;

    @Autowired
    private ProOrderService proOrderService;

    @Autowired
    private ProOrderQueryService proOrderQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProOrderMockMvc;

    private ProOrder proOrder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProOrderResource proOrderResource = new ProOrderResource(proOrderService, proOrderQueryService);
        this.restProOrderMockMvc = MockMvcBuilders.standaloneSetup(proOrderResource)
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
    public static ProOrder createEntity(EntityManager em) {
        ProOrder proOrder = new ProOrder()
            .cUserid(DEFAULT_C_USERID)
            .orderNo(DEFAULT_ORDER_NO)
            .status(DEFAULT_STATUS)
            .payment(DEFAULT_PAYMENT)
            .paymentType(DEFAULT_PAYMENT_TYPE)
            .paymentTime(DEFAULT_PAYMENT_TIME)
            .postFee(DEFAULT_POST_FEE)
            .consignTime(DEFAULT_CONSIGN_TIME)
            .endTime(DEFAULT_END_TIME)
            .closeTime(DEFAULT_CLOSE_TIME)
            .shippingName(DEFAULT_SHIPPING_NAME)
            .shipingCode(DEFAULT_SHIPING_CODE)
            .buyerMessage(DEFAULT_BUYER_MESSAGE)
            .buyerNick(DEFAULT_BUYER_NICK)
            .buyerRate(DEFAULT_BUYER_RATE)
            .createdTime(DEFAULT_CREATED_TIME)
            .updateTime(DEFAULT_UPDATE_TIME)
            .deletedB(DEFAULT_DELETED_B)
            .deletedC(DEFAULT_DELETED_C)
            .shopId(DEFAULT_SHOP_ID)
            .payNo(DEFAULT_PAY_NO);
        return proOrder;
    }

    @Before
    public void initTest() {
        proOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createProOrder() throws Exception {
        int databaseSizeBeforeCreate = proOrderRepository.findAll().size();

        // Create the ProOrder
        ProOrderDTO proOrderDTO = proOrderMapper.toDto(proOrder);
        restProOrderMockMvc.perform(post("/api/pro-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proOrderDTO)))
            .andExpect(status().isCreated());

        // Validate the ProOrder in the database
        List<ProOrder> proOrderList = proOrderRepository.findAll();
        assertThat(proOrderList).hasSize(databaseSizeBeforeCreate + 1);
        ProOrder testProOrder = proOrderList.get(proOrderList.size() - 1);
        assertThat(testProOrder.getcUserid()).isEqualTo(DEFAULT_C_USERID);
        assertThat(testProOrder.getOrderNo()).isEqualTo(DEFAULT_ORDER_NO);
        assertThat(testProOrder.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testProOrder.getPayment()).isEqualTo(DEFAULT_PAYMENT);
        assertThat(testProOrder.getPaymentType()).isEqualTo(DEFAULT_PAYMENT_TYPE);
        assertThat(testProOrder.getPaymentTime()).isEqualTo(DEFAULT_PAYMENT_TIME);
        assertThat(testProOrder.getPostFee()).isEqualTo(DEFAULT_POST_FEE);
        assertThat(testProOrder.getConsignTime()).isEqualTo(DEFAULT_CONSIGN_TIME);
        assertThat(testProOrder.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testProOrder.getCloseTime()).isEqualTo(DEFAULT_CLOSE_TIME);
        assertThat(testProOrder.getShippingName()).isEqualTo(DEFAULT_SHIPPING_NAME);
        assertThat(testProOrder.getShipingCode()).isEqualTo(DEFAULT_SHIPING_CODE);
        assertThat(testProOrder.getBuyerMessage()).isEqualTo(DEFAULT_BUYER_MESSAGE);
        assertThat(testProOrder.getBuyerNick()).isEqualTo(DEFAULT_BUYER_NICK);
        assertThat(testProOrder.isBuyerRate()).isEqualTo(DEFAULT_BUYER_RATE);
        assertThat(testProOrder.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testProOrder.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testProOrder.isDeletedB()).isEqualTo(DEFAULT_DELETED_B);
        assertThat(testProOrder.isDeletedC()).isEqualTo(DEFAULT_DELETED_C);
        assertThat(testProOrder.getShopId()).isEqualTo(DEFAULT_SHOP_ID);
        assertThat(testProOrder.getPayNo()).isEqualTo(DEFAULT_PAY_NO);
    }

    @Test
    @Transactional
    public void createProOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = proOrderRepository.findAll().size();

        // Create the ProOrder with an existing ID
        proOrder.setId(1L);
        ProOrderDTO proOrderDTO = proOrderMapper.toDto(proOrder);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProOrderMockMvc.perform(post("/api/pro-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proOrderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProOrder in the database
        List<ProOrder> proOrderList = proOrderRepository.findAll();
        assertThat(proOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProOrders() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList
        restProOrderMockMvc.perform(get("/api/pro-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].cUserid").value(hasItem(DEFAULT_C_USERID.intValue())))
            .andExpect(jsonPath("$.[*].orderNo").value(hasItem(DEFAULT_ORDER_NO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].payment").value(hasItem(DEFAULT_PAYMENT.intValue())))
            .andExpect(jsonPath("$.[*].paymentType").value(hasItem(DEFAULT_PAYMENT_TYPE)))
            .andExpect(jsonPath("$.[*].paymentTime").value(hasItem(DEFAULT_PAYMENT_TIME.toString())))
            .andExpect(jsonPath("$.[*].postFee").value(hasItem(DEFAULT_POST_FEE.intValue())))
            .andExpect(jsonPath("$.[*].consignTime").value(hasItem(DEFAULT_CONSIGN_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].closeTime").value(hasItem(DEFAULT_CLOSE_TIME.toString())))
            .andExpect(jsonPath("$.[*].shippingName").value(hasItem(DEFAULT_SHIPPING_NAME.toString())))
            .andExpect(jsonPath("$.[*].shipingCode").value(hasItem(DEFAULT_SHIPING_CODE.toString())))
            .andExpect(jsonPath("$.[*].buyerMessage").value(hasItem(DEFAULT_BUYER_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].buyerNick").value(hasItem(DEFAULT_BUYER_NICK.toString())))
            .andExpect(jsonPath("$.[*].buyerRate").value(hasItem(DEFAULT_BUYER_RATE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].deletedB").value(hasItem(DEFAULT_DELETED_B.booleanValue())))
            .andExpect(jsonPath("$.[*].deletedC").value(hasItem(DEFAULT_DELETED_C.booleanValue())))
            .andExpect(jsonPath("$.[*].shopId").value(hasItem(DEFAULT_SHOP_ID.intValue())))
            .andExpect(jsonPath("$.[*].payNo").value(hasItem(DEFAULT_PAY_NO.toString())));
    }

    @Test
    @Transactional
    public void getProOrder() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get the proOrder
        restProOrderMockMvc.perform(get("/api/pro-orders/{id}", proOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(proOrder.getId().intValue()))
            .andExpect(jsonPath("$.cUserid").value(DEFAULT_C_USERID.intValue()))
            .andExpect(jsonPath("$.orderNo").value(DEFAULT_ORDER_NO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.payment").value(DEFAULT_PAYMENT.intValue()))
            .andExpect(jsonPath("$.paymentType").value(DEFAULT_PAYMENT_TYPE))
            .andExpect(jsonPath("$.paymentTime").value(DEFAULT_PAYMENT_TIME.toString()))
            .andExpect(jsonPath("$.postFee").value(DEFAULT_POST_FEE.intValue()))
            .andExpect(jsonPath("$.consignTime").value(DEFAULT_CONSIGN_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.closeTime").value(DEFAULT_CLOSE_TIME.toString()))
            .andExpect(jsonPath("$.shippingName").value(DEFAULT_SHIPPING_NAME.toString()))
            .andExpect(jsonPath("$.shipingCode").value(DEFAULT_SHIPING_CODE.toString()))
            .andExpect(jsonPath("$.buyerMessage").value(DEFAULT_BUYER_MESSAGE.toString()))
            .andExpect(jsonPath("$.buyerNick").value(DEFAULT_BUYER_NICK.toString()))
            .andExpect(jsonPath("$.buyerRate").value(DEFAULT_BUYER_RATE.booleanValue()))
            .andExpect(jsonPath("$.createdTime").value(DEFAULT_CREATED_TIME.toString()))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()))
            .andExpect(jsonPath("$.deletedB").value(DEFAULT_DELETED_B.booleanValue()))
            .andExpect(jsonPath("$.deletedC").value(DEFAULT_DELETED_C.booleanValue()))
            .andExpect(jsonPath("$.shopId").value(DEFAULT_SHOP_ID.intValue()))
            .andExpect(jsonPath("$.payNo").value(DEFAULT_PAY_NO.toString()));
    }

    @Test
    @Transactional
    public void getAllProOrdersBycUseridIsEqualToSomething() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where cUserid equals to DEFAULT_C_USERID
        defaultProOrderShouldBeFound("cUserid.equals=" + DEFAULT_C_USERID);

        // Get all the proOrderList where cUserid equals to UPDATED_C_USERID
        defaultProOrderShouldNotBeFound("cUserid.equals=" + UPDATED_C_USERID);
    }

    @Test
    @Transactional
    public void getAllProOrdersBycUseridIsInShouldWork() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where cUserid in DEFAULT_C_USERID or UPDATED_C_USERID
        defaultProOrderShouldBeFound("cUserid.in=" + DEFAULT_C_USERID + "," + UPDATED_C_USERID);

        // Get all the proOrderList where cUserid equals to UPDATED_C_USERID
        defaultProOrderShouldNotBeFound("cUserid.in=" + UPDATED_C_USERID);
    }

    @Test
    @Transactional
    public void getAllProOrdersBycUseridIsNullOrNotNull() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where cUserid is not null
        defaultProOrderShouldBeFound("cUserid.specified=true");

        // Get all the proOrderList where cUserid is null
        defaultProOrderShouldNotBeFound("cUserid.specified=false");
    }

    @Test
    @Transactional
    public void getAllProOrdersBycUseridIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where cUserid greater than or equals to DEFAULT_C_USERID
        defaultProOrderShouldBeFound("cUserid.greaterOrEqualThan=" + DEFAULT_C_USERID);

        // Get all the proOrderList where cUserid greater than or equals to UPDATED_C_USERID
        defaultProOrderShouldNotBeFound("cUserid.greaterOrEqualThan=" + UPDATED_C_USERID);
    }

    @Test
    @Transactional
    public void getAllProOrdersBycUseridIsLessThanSomething() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where cUserid less than or equals to DEFAULT_C_USERID
        defaultProOrderShouldNotBeFound("cUserid.lessThan=" + DEFAULT_C_USERID);

        // Get all the proOrderList where cUserid less than or equals to UPDATED_C_USERID
        defaultProOrderShouldBeFound("cUserid.lessThan=" + UPDATED_C_USERID);
    }


    @Test
    @Transactional
    public void getAllProOrdersByOrderNoIsEqualToSomething() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where orderNo equals to DEFAULT_ORDER_NO
        defaultProOrderShouldBeFound("orderNo.equals=" + DEFAULT_ORDER_NO);

        // Get all the proOrderList where orderNo equals to UPDATED_ORDER_NO
        defaultProOrderShouldNotBeFound("orderNo.equals=" + UPDATED_ORDER_NO);
    }

    @Test
    @Transactional
    public void getAllProOrdersByOrderNoIsInShouldWork() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where orderNo in DEFAULT_ORDER_NO or UPDATED_ORDER_NO
        defaultProOrderShouldBeFound("orderNo.in=" + DEFAULT_ORDER_NO + "," + UPDATED_ORDER_NO);

        // Get all the proOrderList where orderNo equals to UPDATED_ORDER_NO
        defaultProOrderShouldNotBeFound("orderNo.in=" + UPDATED_ORDER_NO);
    }

    @Test
    @Transactional
    public void getAllProOrdersByOrderNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where orderNo is not null
        defaultProOrderShouldBeFound("orderNo.specified=true");

        // Get all the proOrderList where orderNo is null
        defaultProOrderShouldNotBeFound("orderNo.specified=false");
    }

    @Test
    @Transactional
    public void getAllProOrdersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where status equals to DEFAULT_STATUS
        defaultProOrderShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the proOrderList where status equals to UPDATED_STATUS
        defaultProOrderShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllProOrdersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultProOrderShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the proOrderList where status equals to UPDATED_STATUS
        defaultProOrderShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllProOrdersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where status is not null
        defaultProOrderShouldBeFound("status.specified=true");

        // Get all the proOrderList where status is null
        defaultProOrderShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllProOrdersByStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where status greater than or equals to DEFAULT_STATUS
        defaultProOrderShouldBeFound("status.greaterOrEqualThan=" + DEFAULT_STATUS);

        // Get all the proOrderList where status greater than or equals to UPDATED_STATUS
        defaultProOrderShouldNotBeFound("status.greaterOrEqualThan=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllProOrdersByStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where status less than or equals to DEFAULT_STATUS
        defaultProOrderShouldNotBeFound("status.lessThan=" + DEFAULT_STATUS);

        // Get all the proOrderList where status less than or equals to UPDATED_STATUS
        defaultProOrderShouldBeFound("status.lessThan=" + UPDATED_STATUS);
    }


    @Test
    @Transactional
    public void getAllProOrdersByPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where payment equals to DEFAULT_PAYMENT
        defaultProOrderShouldBeFound("payment.equals=" + DEFAULT_PAYMENT);

        // Get all the proOrderList where payment equals to UPDATED_PAYMENT
        defaultProOrderShouldNotBeFound("payment.equals=" + UPDATED_PAYMENT);
    }

    @Test
    @Transactional
    public void getAllProOrdersByPaymentIsInShouldWork() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where payment in DEFAULT_PAYMENT or UPDATED_PAYMENT
        defaultProOrderShouldBeFound("payment.in=" + DEFAULT_PAYMENT + "," + UPDATED_PAYMENT);

        // Get all the proOrderList where payment equals to UPDATED_PAYMENT
        defaultProOrderShouldNotBeFound("payment.in=" + UPDATED_PAYMENT);
    }

    @Test
    @Transactional
    public void getAllProOrdersByPaymentIsNullOrNotNull() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where payment is not null
        defaultProOrderShouldBeFound("payment.specified=true");

        // Get all the proOrderList where payment is null
        defaultProOrderShouldNotBeFound("payment.specified=false");
    }

    @Test
    @Transactional
    public void getAllProOrdersByPaymentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where paymentType equals to DEFAULT_PAYMENT_TYPE
        defaultProOrderShouldBeFound("paymentType.equals=" + DEFAULT_PAYMENT_TYPE);

        // Get all the proOrderList where paymentType equals to UPDATED_PAYMENT_TYPE
        defaultProOrderShouldNotBeFound("paymentType.equals=" + UPDATED_PAYMENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllProOrdersByPaymentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where paymentType in DEFAULT_PAYMENT_TYPE or UPDATED_PAYMENT_TYPE
        defaultProOrderShouldBeFound("paymentType.in=" + DEFAULT_PAYMENT_TYPE + "," + UPDATED_PAYMENT_TYPE);

        // Get all the proOrderList where paymentType equals to UPDATED_PAYMENT_TYPE
        defaultProOrderShouldNotBeFound("paymentType.in=" + UPDATED_PAYMENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllProOrdersByPaymentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where paymentType is not null
        defaultProOrderShouldBeFound("paymentType.specified=true");

        // Get all the proOrderList where paymentType is null
        defaultProOrderShouldNotBeFound("paymentType.specified=false");
    }

    @Test
    @Transactional
    public void getAllProOrdersByPaymentTypeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where paymentType greater than or equals to DEFAULT_PAYMENT_TYPE
        defaultProOrderShouldBeFound("paymentType.greaterOrEqualThan=" + DEFAULT_PAYMENT_TYPE);

        // Get all the proOrderList where paymentType greater than or equals to UPDATED_PAYMENT_TYPE
        defaultProOrderShouldNotBeFound("paymentType.greaterOrEqualThan=" + UPDATED_PAYMENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllProOrdersByPaymentTypeIsLessThanSomething() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where paymentType less than or equals to DEFAULT_PAYMENT_TYPE
        defaultProOrderShouldNotBeFound("paymentType.lessThan=" + DEFAULT_PAYMENT_TYPE);

        // Get all the proOrderList where paymentType less than or equals to UPDATED_PAYMENT_TYPE
        defaultProOrderShouldBeFound("paymentType.lessThan=" + UPDATED_PAYMENT_TYPE);
    }


    @Test
    @Transactional
    public void getAllProOrdersByPaymentTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where paymentTime equals to DEFAULT_PAYMENT_TIME
        defaultProOrderShouldBeFound("paymentTime.equals=" + DEFAULT_PAYMENT_TIME);

        // Get all the proOrderList where paymentTime equals to UPDATED_PAYMENT_TIME
        defaultProOrderShouldNotBeFound("paymentTime.equals=" + UPDATED_PAYMENT_TIME);
    }

    @Test
    @Transactional
    public void getAllProOrdersByPaymentTimeIsInShouldWork() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where paymentTime in DEFAULT_PAYMENT_TIME or UPDATED_PAYMENT_TIME
        defaultProOrderShouldBeFound("paymentTime.in=" + DEFAULT_PAYMENT_TIME + "," + UPDATED_PAYMENT_TIME);

        // Get all the proOrderList where paymentTime equals to UPDATED_PAYMENT_TIME
        defaultProOrderShouldNotBeFound("paymentTime.in=" + UPDATED_PAYMENT_TIME);
    }

    @Test
    @Transactional
    public void getAllProOrdersByPaymentTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where paymentTime is not null
        defaultProOrderShouldBeFound("paymentTime.specified=true");

        // Get all the proOrderList where paymentTime is null
        defaultProOrderShouldNotBeFound("paymentTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllProOrdersByPostFeeIsEqualToSomething() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where postFee equals to DEFAULT_POST_FEE
        defaultProOrderShouldBeFound("postFee.equals=" + DEFAULT_POST_FEE);

        // Get all the proOrderList where postFee equals to UPDATED_POST_FEE
        defaultProOrderShouldNotBeFound("postFee.equals=" + UPDATED_POST_FEE);
    }

    @Test
    @Transactional
    public void getAllProOrdersByPostFeeIsInShouldWork() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where postFee in DEFAULT_POST_FEE or UPDATED_POST_FEE
        defaultProOrderShouldBeFound("postFee.in=" + DEFAULT_POST_FEE + "," + UPDATED_POST_FEE);

        // Get all the proOrderList where postFee equals to UPDATED_POST_FEE
        defaultProOrderShouldNotBeFound("postFee.in=" + UPDATED_POST_FEE);
    }

    @Test
    @Transactional
    public void getAllProOrdersByPostFeeIsNullOrNotNull() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where postFee is not null
        defaultProOrderShouldBeFound("postFee.specified=true");

        // Get all the proOrderList where postFee is null
        defaultProOrderShouldNotBeFound("postFee.specified=false");
    }

    @Test
    @Transactional
    public void getAllProOrdersByConsignTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where consignTime equals to DEFAULT_CONSIGN_TIME
        defaultProOrderShouldBeFound("consignTime.equals=" + DEFAULT_CONSIGN_TIME);

        // Get all the proOrderList where consignTime equals to UPDATED_CONSIGN_TIME
        defaultProOrderShouldNotBeFound("consignTime.equals=" + UPDATED_CONSIGN_TIME);
    }

    @Test
    @Transactional
    public void getAllProOrdersByConsignTimeIsInShouldWork() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where consignTime in DEFAULT_CONSIGN_TIME or UPDATED_CONSIGN_TIME
        defaultProOrderShouldBeFound("consignTime.in=" + DEFAULT_CONSIGN_TIME + "," + UPDATED_CONSIGN_TIME);

        // Get all the proOrderList where consignTime equals to UPDATED_CONSIGN_TIME
        defaultProOrderShouldNotBeFound("consignTime.in=" + UPDATED_CONSIGN_TIME);
    }

    @Test
    @Transactional
    public void getAllProOrdersByConsignTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where consignTime is not null
        defaultProOrderShouldBeFound("consignTime.specified=true");

        // Get all the proOrderList where consignTime is null
        defaultProOrderShouldNotBeFound("consignTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllProOrdersByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where endTime equals to DEFAULT_END_TIME
        defaultProOrderShouldBeFound("endTime.equals=" + DEFAULT_END_TIME);

        // Get all the proOrderList where endTime equals to UPDATED_END_TIME
        defaultProOrderShouldNotBeFound("endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllProOrdersByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where endTime in DEFAULT_END_TIME or UPDATED_END_TIME
        defaultProOrderShouldBeFound("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME);

        // Get all the proOrderList where endTime equals to UPDATED_END_TIME
        defaultProOrderShouldNotBeFound("endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllProOrdersByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where endTime is not null
        defaultProOrderShouldBeFound("endTime.specified=true");

        // Get all the proOrderList where endTime is null
        defaultProOrderShouldNotBeFound("endTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllProOrdersByCloseTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where closeTime equals to DEFAULT_CLOSE_TIME
        defaultProOrderShouldBeFound("closeTime.equals=" + DEFAULT_CLOSE_TIME);

        // Get all the proOrderList where closeTime equals to UPDATED_CLOSE_TIME
        defaultProOrderShouldNotBeFound("closeTime.equals=" + UPDATED_CLOSE_TIME);
    }

    @Test
    @Transactional
    public void getAllProOrdersByCloseTimeIsInShouldWork() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where closeTime in DEFAULT_CLOSE_TIME or UPDATED_CLOSE_TIME
        defaultProOrderShouldBeFound("closeTime.in=" + DEFAULT_CLOSE_TIME + "," + UPDATED_CLOSE_TIME);

        // Get all the proOrderList where closeTime equals to UPDATED_CLOSE_TIME
        defaultProOrderShouldNotBeFound("closeTime.in=" + UPDATED_CLOSE_TIME);
    }

    @Test
    @Transactional
    public void getAllProOrdersByCloseTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where closeTime is not null
        defaultProOrderShouldBeFound("closeTime.specified=true");

        // Get all the proOrderList where closeTime is null
        defaultProOrderShouldNotBeFound("closeTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllProOrdersByShippingNameIsEqualToSomething() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where shippingName equals to DEFAULT_SHIPPING_NAME
        defaultProOrderShouldBeFound("shippingName.equals=" + DEFAULT_SHIPPING_NAME);

        // Get all the proOrderList where shippingName equals to UPDATED_SHIPPING_NAME
        defaultProOrderShouldNotBeFound("shippingName.equals=" + UPDATED_SHIPPING_NAME);
    }

    @Test
    @Transactional
    public void getAllProOrdersByShippingNameIsInShouldWork() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where shippingName in DEFAULT_SHIPPING_NAME or UPDATED_SHIPPING_NAME
        defaultProOrderShouldBeFound("shippingName.in=" + DEFAULT_SHIPPING_NAME + "," + UPDATED_SHIPPING_NAME);

        // Get all the proOrderList where shippingName equals to UPDATED_SHIPPING_NAME
        defaultProOrderShouldNotBeFound("shippingName.in=" + UPDATED_SHIPPING_NAME);
    }

    @Test
    @Transactional
    public void getAllProOrdersByShippingNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where shippingName is not null
        defaultProOrderShouldBeFound("shippingName.specified=true");

        // Get all the proOrderList where shippingName is null
        defaultProOrderShouldNotBeFound("shippingName.specified=false");
    }

    @Test
    @Transactional
    public void getAllProOrdersByShipingCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where shipingCode equals to DEFAULT_SHIPING_CODE
        defaultProOrderShouldBeFound("shipingCode.equals=" + DEFAULT_SHIPING_CODE);

        // Get all the proOrderList where shipingCode equals to UPDATED_SHIPING_CODE
        defaultProOrderShouldNotBeFound("shipingCode.equals=" + UPDATED_SHIPING_CODE);
    }

    @Test
    @Transactional
    public void getAllProOrdersByShipingCodeIsInShouldWork() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where shipingCode in DEFAULT_SHIPING_CODE or UPDATED_SHIPING_CODE
        defaultProOrderShouldBeFound("shipingCode.in=" + DEFAULT_SHIPING_CODE + "," + UPDATED_SHIPING_CODE);

        // Get all the proOrderList where shipingCode equals to UPDATED_SHIPING_CODE
        defaultProOrderShouldNotBeFound("shipingCode.in=" + UPDATED_SHIPING_CODE);
    }

    @Test
    @Transactional
    public void getAllProOrdersByShipingCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where shipingCode is not null
        defaultProOrderShouldBeFound("shipingCode.specified=true");

        // Get all the proOrderList where shipingCode is null
        defaultProOrderShouldNotBeFound("shipingCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllProOrdersByBuyerMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where buyerMessage equals to DEFAULT_BUYER_MESSAGE
        defaultProOrderShouldBeFound("buyerMessage.equals=" + DEFAULT_BUYER_MESSAGE);

        // Get all the proOrderList where buyerMessage equals to UPDATED_BUYER_MESSAGE
        defaultProOrderShouldNotBeFound("buyerMessage.equals=" + UPDATED_BUYER_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllProOrdersByBuyerMessageIsInShouldWork() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where buyerMessage in DEFAULT_BUYER_MESSAGE or UPDATED_BUYER_MESSAGE
        defaultProOrderShouldBeFound("buyerMessage.in=" + DEFAULT_BUYER_MESSAGE + "," + UPDATED_BUYER_MESSAGE);

        // Get all the proOrderList where buyerMessage equals to UPDATED_BUYER_MESSAGE
        defaultProOrderShouldNotBeFound("buyerMessage.in=" + UPDATED_BUYER_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllProOrdersByBuyerMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where buyerMessage is not null
        defaultProOrderShouldBeFound("buyerMessage.specified=true");

        // Get all the proOrderList where buyerMessage is null
        defaultProOrderShouldNotBeFound("buyerMessage.specified=false");
    }

    @Test
    @Transactional
    public void getAllProOrdersByBuyerNickIsEqualToSomething() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where buyerNick equals to DEFAULT_BUYER_NICK
        defaultProOrderShouldBeFound("buyerNick.equals=" + DEFAULT_BUYER_NICK);

        // Get all the proOrderList where buyerNick equals to UPDATED_BUYER_NICK
        defaultProOrderShouldNotBeFound("buyerNick.equals=" + UPDATED_BUYER_NICK);
    }

    @Test
    @Transactional
    public void getAllProOrdersByBuyerNickIsInShouldWork() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where buyerNick in DEFAULT_BUYER_NICK or UPDATED_BUYER_NICK
        defaultProOrderShouldBeFound("buyerNick.in=" + DEFAULT_BUYER_NICK + "," + UPDATED_BUYER_NICK);

        // Get all the proOrderList where buyerNick equals to UPDATED_BUYER_NICK
        defaultProOrderShouldNotBeFound("buyerNick.in=" + UPDATED_BUYER_NICK);
    }

    @Test
    @Transactional
    public void getAllProOrdersByBuyerNickIsNullOrNotNull() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where buyerNick is not null
        defaultProOrderShouldBeFound("buyerNick.specified=true");

        // Get all the proOrderList where buyerNick is null
        defaultProOrderShouldNotBeFound("buyerNick.specified=false");
    }

    @Test
    @Transactional
    public void getAllProOrdersByBuyerRateIsEqualToSomething() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where buyerRate equals to DEFAULT_BUYER_RATE
        defaultProOrderShouldBeFound("buyerRate.equals=" + DEFAULT_BUYER_RATE);

        // Get all the proOrderList where buyerRate equals to UPDATED_BUYER_RATE
        defaultProOrderShouldNotBeFound("buyerRate.equals=" + UPDATED_BUYER_RATE);
    }

    @Test
    @Transactional
    public void getAllProOrdersByBuyerRateIsInShouldWork() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where buyerRate in DEFAULT_BUYER_RATE or UPDATED_BUYER_RATE
        defaultProOrderShouldBeFound("buyerRate.in=" + DEFAULT_BUYER_RATE + "," + UPDATED_BUYER_RATE);

        // Get all the proOrderList where buyerRate equals to UPDATED_BUYER_RATE
        defaultProOrderShouldNotBeFound("buyerRate.in=" + UPDATED_BUYER_RATE);
    }

    @Test
    @Transactional
    public void getAllProOrdersByBuyerRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where buyerRate is not null
        defaultProOrderShouldBeFound("buyerRate.specified=true");

        // Get all the proOrderList where buyerRate is null
        defaultProOrderShouldNotBeFound("buyerRate.specified=false");
    }

    @Test
    @Transactional
    public void getAllProOrdersByCreatedTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where createdTime equals to DEFAULT_CREATED_TIME
        defaultProOrderShouldBeFound("createdTime.equals=" + DEFAULT_CREATED_TIME);

        // Get all the proOrderList where createdTime equals to UPDATED_CREATED_TIME
        defaultProOrderShouldNotBeFound("createdTime.equals=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllProOrdersByCreatedTimeIsInShouldWork() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where createdTime in DEFAULT_CREATED_TIME or UPDATED_CREATED_TIME
        defaultProOrderShouldBeFound("createdTime.in=" + DEFAULT_CREATED_TIME + "," + UPDATED_CREATED_TIME);

        // Get all the proOrderList where createdTime equals to UPDATED_CREATED_TIME
        defaultProOrderShouldNotBeFound("createdTime.in=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllProOrdersByCreatedTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where createdTime is not null
        defaultProOrderShouldBeFound("createdTime.specified=true");

        // Get all the proOrderList where createdTime is null
        defaultProOrderShouldNotBeFound("createdTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllProOrdersByUpdateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where updateTime equals to DEFAULT_UPDATE_TIME
        defaultProOrderShouldBeFound("updateTime.equals=" + DEFAULT_UPDATE_TIME);

        // Get all the proOrderList where updateTime equals to UPDATED_UPDATE_TIME
        defaultProOrderShouldNotBeFound("updateTime.equals=" + UPDATED_UPDATE_TIME);
    }

    @Test
    @Transactional
    public void getAllProOrdersByUpdateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where updateTime in DEFAULT_UPDATE_TIME or UPDATED_UPDATE_TIME
        defaultProOrderShouldBeFound("updateTime.in=" + DEFAULT_UPDATE_TIME + "," + UPDATED_UPDATE_TIME);

        // Get all the proOrderList where updateTime equals to UPDATED_UPDATE_TIME
        defaultProOrderShouldNotBeFound("updateTime.in=" + UPDATED_UPDATE_TIME);
    }

    @Test
    @Transactional
    public void getAllProOrdersByUpdateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where updateTime is not null
        defaultProOrderShouldBeFound("updateTime.specified=true");

        // Get all the proOrderList where updateTime is null
        defaultProOrderShouldNotBeFound("updateTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllProOrdersByDeletedBIsEqualToSomething() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where deletedB equals to DEFAULT_DELETED_B
        defaultProOrderShouldBeFound("deletedB.equals=" + DEFAULT_DELETED_B);

        // Get all the proOrderList where deletedB equals to UPDATED_DELETED_B
        defaultProOrderShouldNotBeFound("deletedB.equals=" + UPDATED_DELETED_B);
    }

    @Test
    @Transactional
    public void getAllProOrdersByDeletedBIsInShouldWork() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where deletedB in DEFAULT_DELETED_B or UPDATED_DELETED_B
        defaultProOrderShouldBeFound("deletedB.in=" + DEFAULT_DELETED_B + "," + UPDATED_DELETED_B);

        // Get all the proOrderList where deletedB equals to UPDATED_DELETED_B
        defaultProOrderShouldNotBeFound("deletedB.in=" + UPDATED_DELETED_B);
    }

    @Test
    @Transactional
    public void getAllProOrdersByDeletedBIsNullOrNotNull() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where deletedB is not null
        defaultProOrderShouldBeFound("deletedB.specified=true");

        // Get all the proOrderList where deletedB is null
        defaultProOrderShouldNotBeFound("deletedB.specified=false");
    }

    @Test
    @Transactional
    public void getAllProOrdersByDeletedCIsEqualToSomething() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where deletedC equals to DEFAULT_DELETED_C
        defaultProOrderShouldBeFound("deletedC.equals=" + DEFAULT_DELETED_C);

        // Get all the proOrderList where deletedC equals to UPDATED_DELETED_C
        defaultProOrderShouldNotBeFound("deletedC.equals=" + UPDATED_DELETED_C);
    }

    @Test
    @Transactional
    public void getAllProOrdersByDeletedCIsInShouldWork() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where deletedC in DEFAULT_DELETED_C or UPDATED_DELETED_C
        defaultProOrderShouldBeFound("deletedC.in=" + DEFAULT_DELETED_C + "," + UPDATED_DELETED_C);

        // Get all the proOrderList where deletedC equals to UPDATED_DELETED_C
        defaultProOrderShouldNotBeFound("deletedC.in=" + UPDATED_DELETED_C);
    }

    @Test
    @Transactional
    public void getAllProOrdersByDeletedCIsNullOrNotNull() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where deletedC is not null
        defaultProOrderShouldBeFound("deletedC.specified=true");

        // Get all the proOrderList where deletedC is null
        defaultProOrderShouldNotBeFound("deletedC.specified=false");
    }

    @Test
    @Transactional
    public void getAllProOrdersByShopIdIsEqualToSomething() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where shopId equals to DEFAULT_SHOP_ID
        defaultProOrderShouldBeFound("shopId.equals=" + DEFAULT_SHOP_ID);

        // Get all the proOrderList where shopId equals to UPDATED_SHOP_ID
        defaultProOrderShouldNotBeFound("shopId.equals=" + UPDATED_SHOP_ID);
    }

    @Test
    @Transactional
    public void getAllProOrdersByShopIdIsInShouldWork() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where shopId in DEFAULT_SHOP_ID or UPDATED_SHOP_ID
        defaultProOrderShouldBeFound("shopId.in=" + DEFAULT_SHOP_ID + "," + UPDATED_SHOP_ID);

        // Get all the proOrderList where shopId equals to UPDATED_SHOP_ID
        defaultProOrderShouldNotBeFound("shopId.in=" + UPDATED_SHOP_ID);
    }

    @Test
    @Transactional
    public void getAllProOrdersByShopIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where shopId is not null
        defaultProOrderShouldBeFound("shopId.specified=true");

        // Get all the proOrderList where shopId is null
        defaultProOrderShouldNotBeFound("shopId.specified=false");
    }

    @Test
    @Transactional
    public void getAllProOrdersByShopIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where shopId greater than or equals to DEFAULT_SHOP_ID
        defaultProOrderShouldBeFound("shopId.greaterOrEqualThan=" + DEFAULT_SHOP_ID);

        // Get all the proOrderList where shopId greater than or equals to UPDATED_SHOP_ID
        defaultProOrderShouldNotBeFound("shopId.greaterOrEqualThan=" + UPDATED_SHOP_ID);
    }

    @Test
    @Transactional
    public void getAllProOrdersByShopIdIsLessThanSomething() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where shopId less than or equals to DEFAULT_SHOP_ID
        defaultProOrderShouldNotBeFound("shopId.lessThan=" + DEFAULT_SHOP_ID);

        // Get all the proOrderList where shopId less than or equals to UPDATED_SHOP_ID
        defaultProOrderShouldBeFound("shopId.lessThan=" + UPDATED_SHOP_ID);
    }


    @Test
    @Transactional
    public void getAllProOrdersByPayNoIsEqualToSomething() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where payNo equals to DEFAULT_PAY_NO
        defaultProOrderShouldBeFound("payNo.equals=" + DEFAULT_PAY_NO);

        // Get all the proOrderList where payNo equals to UPDATED_PAY_NO
        defaultProOrderShouldNotBeFound("payNo.equals=" + UPDATED_PAY_NO);
    }

    @Test
    @Transactional
    public void getAllProOrdersByPayNoIsInShouldWork() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where payNo in DEFAULT_PAY_NO or UPDATED_PAY_NO
        defaultProOrderShouldBeFound("payNo.in=" + DEFAULT_PAY_NO + "," + UPDATED_PAY_NO);

        // Get all the proOrderList where payNo equals to UPDATED_PAY_NO
        defaultProOrderShouldNotBeFound("payNo.in=" + UPDATED_PAY_NO);
    }

    @Test
    @Transactional
    public void getAllProOrdersByPayNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);

        // Get all the proOrderList where payNo is not null
        defaultProOrderShouldBeFound("payNo.specified=true");

        // Get all the proOrderList where payNo is null
        defaultProOrderShouldNotBeFound("payNo.specified=false");
    }

    @Test
    @Transactional
    public void getAllProOrdersByProOrderItemIsEqualToSomething() throws Exception {
        // Initialize the database
        ProOrderItem proOrderItem = ProOrderItemResourceIntTest.createEntity(em);
        em.persist(proOrderItem);
        em.flush();
        proOrder.addProOrderItem(proOrderItem);
        proOrderRepository.saveAndFlush(proOrder);
        Long proOrderItemId = proOrderItem.getId();

        // Get all the proOrderList where proOrderItem equals to proOrderItemId
        defaultProOrderShouldBeFound("proOrderItemId.equals=" + proOrderItemId);

        // Get all the proOrderList where proOrderItem equals to proOrderItemId + 1
        defaultProOrderShouldNotBeFound("proOrderItemId.equals=" + (proOrderItemId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProOrderShouldBeFound(String filter) throws Exception {
        restProOrderMockMvc.perform(get("/api/pro-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].cUserid").value(hasItem(DEFAULT_C_USERID.intValue())))
            .andExpect(jsonPath("$.[*].orderNo").value(hasItem(DEFAULT_ORDER_NO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].payment").value(hasItem(DEFAULT_PAYMENT.intValue())))
            .andExpect(jsonPath("$.[*].paymentType").value(hasItem(DEFAULT_PAYMENT_TYPE)))
            .andExpect(jsonPath("$.[*].paymentTime").value(hasItem(DEFAULT_PAYMENT_TIME.toString())))
            .andExpect(jsonPath("$.[*].postFee").value(hasItem(DEFAULT_POST_FEE.intValue())))
            .andExpect(jsonPath("$.[*].consignTime").value(hasItem(DEFAULT_CONSIGN_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].closeTime").value(hasItem(DEFAULT_CLOSE_TIME.toString())))
            .andExpect(jsonPath("$.[*].shippingName").value(hasItem(DEFAULT_SHIPPING_NAME.toString())))
            .andExpect(jsonPath("$.[*].shipingCode").value(hasItem(DEFAULT_SHIPING_CODE.toString())))
            .andExpect(jsonPath("$.[*].buyerMessage").value(hasItem(DEFAULT_BUYER_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].buyerNick").value(hasItem(DEFAULT_BUYER_NICK.toString())))
            .andExpect(jsonPath("$.[*].buyerRate").value(hasItem(DEFAULT_BUYER_RATE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].deletedB").value(hasItem(DEFAULT_DELETED_B.booleanValue())))
            .andExpect(jsonPath("$.[*].deletedC").value(hasItem(DEFAULT_DELETED_C.booleanValue())))
            .andExpect(jsonPath("$.[*].shopId").value(hasItem(DEFAULT_SHOP_ID.intValue())))
            .andExpect(jsonPath("$.[*].payNo").value(hasItem(DEFAULT_PAY_NO.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProOrderShouldNotBeFound(String filter) throws Exception {
        restProOrderMockMvc.perform(get("/api/pro-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingProOrder() throws Exception {
        // Get the proOrder
        restProOrderMockMvc.perform(get("/api/pro-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProOrder() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);
        int databaseSizeBeforeUpdate = proOrderRepository.findAll().size();

        // Update the proOrder
        ProOrder updatedProOrder = proOrderRepository.findOne(proOrder.getId());
        // Disconnect from session so that the updates on updatedProOrder are not directly saved in db
        em.detach(updatedProOrder);
        updatedProOrder
            .cUserid(UPDATED_C_USERID)
            .orderNo(UPDATED_ORDER_NO)
            .status(UPDATED_STATUS)
            .payment(UPDATED_PAYMENT)
            .paymentType(UPDATED_PAYMENT_TYPE)
            .paymentTime(UPDATED_PAYMENT_TIME)
            .postFee(UPDATED_POST_FEE)
            .consignTime(UPDATED_CONSIGN_TIME)
            .endTime(UPDATED_END_TIME)
            .closeTime(UPDATED_CLOSE_TIME)
            .shippingName(UPDATED_SHIPPING_NAME)
            .shipingCode(UPDATED_SHIPING_CODE)
            .buyerMessage(UPDATED_BUYER_MESSAGE)
            .buyerNick(UPDATED_BUYER_NICK)
            .buyerRate(UPDATED_BUYER_RATE)
            .createdTime(UPDATED_CREATED_TIME)
            .updateTime(UPDATED_UPDATE_TIME)
            .deletedB(UPDATED_DELETED_B)
            .deletedC(UPDATED_DELETED_C)
            .shopId(UPDATED_SHOP_ID)
            .payNo(UPDATED_PAY_NO);
        ProOrderDTO proOrderDTO = proOrderMapper.toDto(updatedProOrder);

        restProOrderMockMvc.perform(put("/api/pro-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proOrderDTO)))
            .andExpect(status().isOk());

        // Validate the ProOrder in the database
        List<ProOrder> proOrderList = proOrderRepository.findAll();
        assertThat(proOrderList).hasSize(databaseSizeBeforeUpdate);
        ProOrder testProOrder = proOrderList.get(proOrderList.size() - 1);
        assertThat(testProOrder.getcUserid()).isEqualTo(UPDATED_C_USERID);
        assertThat(testProOrder.getOrderNo()).isEqualTo(UPDATED_ORDER_NO);
        assertThat(testProOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProOrder.getPayment()).isEqualTo(UPDATED_PAYMENT);
        assertThat(testProOrder.getPaymentType()).isEqualTo(UPDATED_PAYMENT_TYPE);
        assertThat(testProOrder.getPaymentTime()).isEqualTo(UPDATED_PAYMENT_TIME);
        assertThat(testProOrder.getPostFee()).isEqualTo(UPDATED_POST_FEE);
        assertThat(testProOrder.getConsignTime()).isEqualTo(UPDATED_CONSIGN_TIME);
        assertThat(testProOrder.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testProOrder.getCloseTime()).isEqualTo(UPDATED_CLOSE_TIME);
        assertThat(testProOrder.getShippingName()).isEqualTo(UPDATED_SHIPPING_NAME);
        assertThat(testProOrder.getShipingCode()).isEqualTo(UPDATED_SHIPING_CODE);
        assertThat(testProOrder.getBuyerMessage()).isEqualTo(UPDATED_BUYER_MESSAGE);
        assertThat(testProOrder.getBuyerNick()).isEqualTo(UPDATED_BUYER_NICK);
        assertThat(testProOrder.isBuyerRate()).isEqualTo(UPDATED_BUYER_RATE);
        assertThat(testProOrder.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testProOrder.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testProOrder.isDeletedB()).isEqualTo(UPDATED_DELETED_B);
        assertThat(testProOrder.isDeletedC()).isEqualTo(UPDATED_DELETED_C);
        assertThat(testProOrder.getShopId()).isEqualTo(UPDATED_SHOP_ID);
        assertThat(testProOrder.getPayNo()).isEqualTo(UPDATED_PAY_NO);
    }

    @Test
    @Transactional
    public void updateNonExistingProOrder() throws Exception {
        int databaseSizeBeforeUpdate = proOrderRepository.findAll().size();

        // Create the ProOrder
        ProOrderDTO proOrderDTO = proOrderMapper.toDto(proOrder);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProOrderMockMvc.perform(put("/api/pro-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proOrderDTO)))
            .andExpect(status().isCreated());

        // Validate the ProOrder in the database
        List<ProOrder> proOrderList = proOrderRepository.findAll();
        assertThat(proOrderList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProOrder() throws Exception {
        // Initialize the database
        proOrderRepository.saveAndFlush(proOrder);
        int databaseSizeBeforeDelete = proOrderRepository.findAll().size();

        // Get the proOrder
        restProOrderMockMvc.perform(delete("/api/pro-orders/{id}", proOrder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProOrder> proOrderList = proOrderRepository.findAll();
        assertThat(proOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProOrder.class);
        ProOrder proOrder1 = new ProOrder();
        proOrder1.setId(1L);
        ProOrder proOrder2 = new ProOrder();
        proOrder2.setId(proOrder1.getId());
        assertThat(proOrder1).isEqualTo(proOrder2);
        proOrder2.setId(2L);
        assertThat(proOrder1).isNotEqualTo(proOrder2);
        proOrder1.setId(null);
        assertThat(proOrder1).isNotEqualTo(proOrder2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProOrderDTO.class);
        ProOrderDTO proOrderDTO1 = new ProOrderDTO();
        proOrderDTO1.setId(1L);
        ProOrderDTO proOrderDTO2 = new ProOrderDTO();
        assertThat(proOrderDTO1).isNotEqualTo(proOrderDTO2);
        proOrderDTO2.setId(proOrderDTO1.getId());
        assertThat(proOrderDTO1).isEqualTo(proOrderDTO2);
        proOrderDTO2.setId(2L);
        assertThat(proOrderDTO1).isNotEqualTo(proOrderDTO2);
        proOrderDTO1.setId(null);
        assertThat(proOrderDTO1).isNotEqualTo(proOrderDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(proOrderMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(proOrderMapper.fromId(null)).isNull();
    }
}
