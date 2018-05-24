package com.eyun.order.web.rest;

import com.eyun.order.OrderApp;

import com.eyun.order.config.SecurityBeanOverrideConfiguration;

import com.eyun.order.domain.FaceOrder;
import com.eyun.order.repository.FaceOrderRepository;
import com.eyun.order.service.FaceOrderService;
import com.eyun.order.service.dto.FaceOrderDTO;
import com.eyun.order.service.mapper.FaceOrderMapper;
import com.eyun.order.web.rest.errors.ExceptionTranslator;
import com.eyun.order.service.dto.FaceOrderCriteria;
import com.eyun.order.service.FaceOrderQueryService;

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
 * Test class for the FaceOrderResource REST controller.
 *
 * @see FaceOrderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {OrderApp.class, SecurityBeanOverrideConfiguration.class})
public class FaceOrderResourceIntTest {

    private static final String DEFAULT_ORDER_NO = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_NO = "BBBBBBBBBB";

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;

    private static final Long DEFAULT_BUSER_ID = 1L;
    private static final Long UPDATED_BUSER_ID = 2L;

    private static final Long DEFAULT_CUSER_ID = 1L;
    private static final Long UPDATED_CUSER_ID = 2L;

    private static final BigDecimal DEFAULT_PAYMENT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAYMENT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TICKET = new BigDecimal(1);
    private static final BigDecimal UPDATED_TICKET = new BigDecimal(2);

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final Instant DEFAULT_CREATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TRANSFER_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TRANSFER_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TRANSFER = new BigDecimal(1);
    private static final BigDecimal UPDATED_TRANSFER = new BigDecimal(2);

    @Autowired
    private FaceOrderRepository faceOrderRepository;

    @Autowired
    private FaceOrderMapper faceOrderMapper;

    @Autowired
    private FaceOrderService faceOrderService;

    @Autowired
    private FaceOrderQueryService faceOrderQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFaceOrderMockMvc;

    private FaceOrder faceOrder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FaceOrderResource faceOrderResource = new FaceOrderResource(faceOrderService, faceOrderQueryService);
        this.restFaceOrderMockMvc = MockMvcBuilders.standaloneSetup(faceOrderResource)
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
    public static FaceOrder createEntity(EntityManager em) {
        FaceOrder faceOrder = new FaceOrder()
            .orderNo(DEFAULT_ORDER_NO)
            .type(DEFAULT_TYPE)
            .buserId(DEFAULT_BUSER_ID)
            .cuserId(DEFAULT_CUSER_ID)
            .payment(DEFAULT_PAYMENT)
            .amount(DEFAULT_AMOUNT)
            .balance(DEFAULT_BALANCE)
            .ticket(DEFAULT_TICKET)
            .status(DEFAULT_STATUS)
            .createdTime(DEFAULT_CREATED_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME)
            .transferAmount(DEFAULT_TRANSFER_AMOUNT)
            .transfer(DEFAULT_TRANSFER);
        return faceOrder;
    }

    @Before
    public void initTest() {
        faceOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createFaceOrder() throws Exception {
        int databaseSizeBeforeCreate = faceOrderRepository.findAll().size();

        // Create the FaceOrder
        FaceOrderDTO faceOrderDTO = faceOrderMapper.toDto(faceOrder);
        restFaceOrderMockMvc.perform(post("/api/face-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(faceOrderDTO)))
            .andExpect(status().isCreated());

        // Validate the FaceOrder in the database
        List<FaceOrder> faceOrderList = faceOrderRepository.findAll();
        assertThat(faceOrderList).hasSize(databaseSizeBeforeCreate + 1);
        FaceOrder testFaceOrder = faceOrderList.get(faceOrderList.size() - 1);
        assertThat(testFaceOrder.getOrderNo()).isEqualTo(DEFAULT_ORDER_NO);
        assertThat(testFaceOrder.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testFaceOrder.getBuserId()).isEqualTo(DEFAULT_BUSER_ID);
        assertThat(testFaceOrder.getCuserId()).isEqualTo(DEFAULT_CUSER_ID);
        assertThat(testFaceOrder.getPayment()).isEqualTo(DEFAULT_PAYMENT);
        assertThat(testFaceOrder.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testFaceOrder.getBalance()).isEqualTo(DEFAULT_BALANCE);
        assertThat(testFaceOrder.getTicket()).isEqualTo(DEFAULT_TICKET);
        assertThat(testFaceOrder.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testFaceOrder.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testFaceOrder.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testFaceOrder.getTransferAmount()).isEqualTo(DEFAULT_TRANSFER_AMOUNT);
        assertThat(testFaceOrder.getTransfer()).isEqualTo(DEFAULT_TRANSFER);
    }

    @Test
    @Transactional
    public void createFaceOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = faceOrderRepository.findAll().size();

        // Create the FaceOrder with an existing ID
        faceOrder.setId(1L);
        FaceOrderDTO faceOrderDTO = faceOrderMapper.toDto(faceOrder);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFaceOrderMockMvc.perform(post("/api/face-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(faceOrderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FaceOrder in the database
        List<FaceOrder> faceOrderList = faceOrderRepository.findAll();
        assertThat(faceOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFaceOrders() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList
        restFaceOrderMockMvc.perform(get("/api/face-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(faceOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderNo").value(hasItem(DEFAULT_ORDER_NO.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].buserId").value(hasItem(DEFAULT_BUSER_ID.intValue())))
            .andExpect(jsonPath("$.[*].cuserId").value(hasItem(DEFAULT_CUSER_ID.intValue())))
            .andExpect(jsonPath("$.[*].payment").value(hasItem(DEFAULT_PAYMENT.intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())))
            .andExpect(jsonPath("$.[*].ticket").value(hasItem(DEFAULT_TICKET.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(DEFAULT_UPDATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].transferAmount").value(hasItem(DEFAULT_TRANSFER_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].transfer").value(hasItem(DEFAULT_TRANSFER.intValue())));
    }

    @Test
    @Transactional
    public void getFaceOrder() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get the faceOrder
        restFaceOrderMockMvc.perform(get("/api/face-orders/{id}", faceOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(faceOrder.getId().intValue()))
            .andExpect(jsonPath("$.orderNo").value(DEFAULT_ORDER_NO.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.buserId").value(DEFAULT_BUSER_ID.intValue()))
            .andExpect(jsonPath("$.cuserId").value(DEFAULT_CUSER_ID.intValue()))
            .andExpect(jsonPath("$.payment").value(DEFAULT_PAYMENT.intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.intValue()))
            .andExpect(jsonPath("$.ticket").value(DEFAULT_TICKET.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.createdTime").value(DEFAULT_CREATED_TIME.toString()))
            .andExpect(jsonPath("$.updatedTime").value(DEFAULT_UPDATED_TIME.toString()))
            .andExpect(jsonPath("$.transferAmount").value(DEFAULT_TRANSFER_AMOUNT.intValue()))
            .andExpect(jsonPath("$.transfer").value(DEFAULT_TRANSFER.intValue()));
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByOrderNoIsEqualToSomething() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where orderNo equals to DEFAULT_ORDER_NO
        defaultFaceOrderShouldBeFound("orderNo.equals=" + DEFAULT_ORDER_NO);

        // Get all the faceOrderList where orderNo equals to UPDATED_ORDER_NO
        defaultFaceOrderShouldNotBeFound("orderNo.equals=" + UPDATED_ORDER_NO);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByOrderNoIsInShouldWork() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where orderNo in DEFAULT_ORDER_NO or UPDATED_ORDER_NO
        defaultFaceOrderShouldBeFound("orderNo.in=" + DEFAULT_ORDER_NO + "," + UPDATED_ORDER_NO);

        // Get all the faceOrderList where orderNo equals to UPDATED_ORDER_NO
        defaultFaceOrderShouldNotBeFound("orderNo.in=" + UPDATED_ORDER_NO);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByOrderNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where orderNo is not null
        defaultFaceOrderShouldBeFound("orderNo.specified=true");

        // Get all the faceOrderList where orderNo is null
        defaultFaceOrderShouldNotBeFound("orderNo.specified=false");
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where type equals to DEFAULT_TYPE
        defaultFaceOrderShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the faceOrderList where type equals to UPDATED_TYPE
        defaultFaceOrderShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultFaceOrderShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the faceOrderList where type equals to UPDATED_TYPE
        defaultFaceOrderShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where type is not null
        defaultFaceOrderShouldBeFound("type.specified=true");

        // Get all the faceOrderList where type is null
        defaultFaceOrderShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByTypeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where type greater than or equals to DEFAULT_TYPE
        defaultFaceOrderShouldBeFound("type.greaterOrEqualThan=" + DEFAULT_TYPE);

        // Get all the faceOrderList where type greater than or equals to UPDATED_TYPE
        defaultFaceOrderShouldNotBeFound("type.greaterOrEqualThan=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByTypeIsLessThanSomething() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where type less than or equals to DEFAULT_TYPE
        defaultFaceOrderShouldNotBeFound("type.lessThan=" + DEFAULT_TYPE);

        // Get all the faceOrderList where type less than or equals to UPDATED_TYPE
        defaultFaceOrderShouldBeFound("type.lessThan=" + UPDATED_TYPE);
    }


    @Test
    @Transactional
    public void getAllFaceOrdersByBuserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where buserId equals to DEFAULT_BUSER_ID
        defaultFaceOrderShouldBeFound("buserId.equals=" + DEFAULT_BUSER_ID);

        // Get all the faceOrderList where buserId equals to UPDATED_BUSER_ID
        defaultFaceOrderShouldNotBeFound("buserId.equals=" + UPDATED_BUSER_ID);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByBuserIdIsInShouldWork() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where buserId in DEFAULT_BUSER_ID or UPDATED_BUSER_ID
        defaultFaceOrderShouldBeFound("buserId.in=" + DEFAULT_BUSER_ID + "," + UPDATED_BUSER_ID);

        // Get all the faceOrderList where buserId equals to UPDATED_BUSER_ID
        defaultFaceOrderShouldNotBeFound("buserId.in=" + UPDATED_BUSER_ID);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByBuserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where buserId is not null
        defaultFaceOrderShouldBeFound("buserId.specified=true");

        // Get all the faceOrderList where buserId is null
        defaultFaceOrderShouldNotBeFound("buserId.specified=false");
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByBuserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where buserId greater than or equals to DEFAULT_BUSER_ID
        defaultFaceOrderShouldBeFound("buserId.greaterOrEqualThan=" + DEFAULT_BUSER_ID);

        // Get all the faceOrderList where buserId greater than or equals to UPDATED_BUSER_ID
        defaultFaceOrderShouldNotBeFound("buserId.greaterOrEqualThan=" + UPDATED_BUSER_ID);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByBuserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where buserId less than or equals to DEFAULT_BUSER_ID
        defaultFaceOrderShouldNotBeFound("buserId.lessThan=" + DEFAULT_BUSER_ID);

        // Get all the faceOrderList where buserId less than or equals to UPDATED_BUSER_ID
        defaultFaceOrderShouldBeFound("buserId.lessThan=" + UPDATED_BUSER_ID);
    }


    @Test
    @Transactional
    public void getAllFaceOrdersByCuserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where cuserId equals to DEFAULT_CUSER_ID
        defaultFaceOrderShouldBeFound("cuserId.equals=" + DEFAULT_CUSER_ID);

        // Get all the faceOrderList where cuserId equals to UPDATED_CUSER_ID
        defaultFaceOrderShouldNotBeFound("cuserId.equals=" + UPDATED_CUSER_ID);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByCuserIdIsInShouldWork() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where cuserId in DEFAULT_CUSER_ID or UPDATED_CUSER_ID
        defaultFaceOrderShouldBeFound("cuserId.in=" + DEFAULT_CUSER_ID + "," + UPDATED_CUSER_ID);

        // Get all the faceOrderList where cuserId equals to UPDATED_CUSER_ID
        defaultFaceOrderShouldNotBeFound("cuserId.in=" + UPDATED_CUSER_ID);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByCuserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where cuserId is not null
        defaultFaceOrderShouldBeFound("cuserId.specified=true");

        // Get all the faceOrderList where cuserId is null
        defaultFaceOrderShouldNotBeFound("cuserId.specified=false");
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByCuserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where cuserId greater than or equals to DEFAULT_CUSER_ID
        defaultFaceOrderShouldBeFound("cuserId.greaterOrEqualThan=" + DEFAULT_CUSER_ID);

        // Get all the faceOrderList where cuserId greater than or equals to UPDATED_CUSER_ID
        defaultFaceOrderShouldNotBeFound("cuserId.greaterOrEqualThan=" + UPDATED_CUSER_ID);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByCuserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where cuserId less than or equals to DEFAULT_CUSER_ID
        defaultFaceOrderShouldNotBeFound("cuserId.lessThan=" + DEFAULT_CUSER_ID);

        // Get all the faceOrderList where cuserId less than or equals to UPDATED_CUSER_ID
        defaultFaceOrderShouldBeFound("cuserId.lessThan=" + UPDATED_CUSER_ID);
    }


    @Test
    @Transactional
    public void getAllFaceOrdersByPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where payment equals to DEFAULT_PAYMENT
        defaultFaceOrderShouldBeFound("payment.equals=" + DEFAULT_PAYMENT);

        // Get all the faceOrderList where payment equals to UPDATED_PAYMENT
        defaultFaceOrderShouldNotBeFound("payment.equals=" + UPDATED_PAYMENT);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByPaymentIsInShouldWork() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where payment in DEFAULT_PAYMENT or UPDATED_PAYMENT
        defaultFaceOrderShouldBeFound("payment.in=" + DEFAULT_PAYMENT + "," + UPDATED_PAYMENT);

        // Get all the faceOrderList where payment equals to UPDATED_PAYMENT
        defaultFaceOrderShouldNotBeFound("payment.in=" + UPDATED_PAYMENT);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByPaymentIsNullOrNotNull() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where payment is not null
        defaultFaceOrderShouldBeFound("payment.specified=true");

        // Get all the faceOrderList where payment is null
        defaultFaceOrderShouldNotBeFound("payment.specified=false");
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where amount equals to DEFAULT_AMOUNT
        defaultFaceOrderShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the faceOrderList where amount equals to UPDATED_AMOUNT
        defaultFaceOrderShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultFaceOrderShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the faceOrderList where amount equals to UPDATED_AMOUNT
        defaultFaceOrderShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where amount is not null
        defaultFaceOrderShouldBeFound("amount.specified=true");

        // Get all the faceOrderList where amount is null
        defaultFaceOrderShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where balance equals to DEFAULT_BALANCE
        defaultFaceOrderShouldBeFound("balance.equals=" + DEFAULT_BALANCE);

        // Get all the faceOrderList where balance equals to UPDATED_BALANCE
        defaultFaceOrderShouldNotBeFound("balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where balance in DEFAULT_BALANCE or UPDATED_BALANCE
        defaultFaceOrderShouldBeFound("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE);

        // Get all the faceOrderList where balance equals to UPDATED_BALANCE
        defaultFaceOrderShouldNotBeFound("balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where balance is not null
        defaultFaceOrderShouldBeFound("balance.specified=true");

        // Get all the faceOrderList where balance is null
        defaultFaceOrderShouldNotBeFound("balance.specified=false");
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByTicketIsEqualToSomething() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where ticket equals to DEFAULT_TICKET
        defaultFaceOrderShouldBeFound("ticket.equals=" + DEFAULT_TICKET);

        // Get all the faceOrderList where ticket equals to UPDATED_TICKET
        defaultFaceOrderShouldNotBeFound("ticket.equals=" + UPDATED_TICKET);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByTicketIsInShouldWork() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where ticket in DEFAULT_TICKET or UPDATED_TICKET
        defaultFaceOrderShouldBeFound("ticket.in=" + DEFAULT_TICKET + "," + UPDATED_TICKET);

        // Get all the faceOrderList where ticket equals to UPDATED_TICKET
        defaultFaceOrderShouldNotBeFound("ticket.in=" + UPDATED_TICKET);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByTicketIsNullOrNotNull() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where ticket is not null
        defaultFaceOrderShouldBeFound("ticket.specified=true");

        // Get all the faceOrderList where ticket is null
        defaultFaceOrderShouldNotBeFound("ticket.specified=false");
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where status equals to DEFAULT_STATUS
        defaultFaceOrderShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the faceOrderList where status equals to UPDATED_STATUS
        defaultFaceOrderShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultFaceOrderShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the faceOrderList where status equals to UPDATED_STATUS
        defaultFaceOrderShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where status is not null
        defaultFaceOrderShouldBeFound("status.specified=true");

        // Get all the faceOrderList where status is null
        defaultFaceOrderShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where status greater than or equals to DEFAULT_STATUS
        defaultFaceOrderShouldBeFound("status.greaterOrEqualThan=" + DEFAULT_STATUS);

        // Get all the faceOrderList where status greater than or equals to UPDATED_STATUS
        defaultFaceOrderShouldNotBeFound("status.greaterOrEqualThan=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where status less than or equals to DEFAULT_STATUS
        defaultFaceOrderShouldNotBeFound("status.lessThan=" + DEFAULT_STATUS);

        // Get all the faceOrderList where status less than or equals to UPDATED_STATUS
        defaultFaceOrderShouldBeFound("status.lessThan=" + UPDATED_STATUS);
    }


    @Test
    @Transactional
    public void getAllFaceOrdersByCreatedTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where createdTime equals to DEFAULT_CREATED_TIME
        defaultFaceOrderShouldBeFound("createdTime.equals=" + DEFAULT_CREATED_TIME);

        // Get all the faceOrderList where createdTime equals to UPDATED_CREATED_TIME
        defaultFaceOrderShouldNotBeFound("createdTime.equals=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByCreatedTimeIsInShouldWork() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where createdTime in DEFAULT_CREATED_TIME or UPDATED_CREATED_TIME
        defaultFaceOrderShouldBeFound("createdTime.in=" + DEFAULT_CREATED_TIME + "," + UPDATED_CREATED_TIME);

        // Get all the faceOrderList where createdTime equals to UPDATED_CREATED_TIME
        defaultFaceOrderShouldNotBeFound("createdTime.in=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByCreatedTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where createdTime is not null
        defaultFaceOrderShouldBeFound("createdTime.specified=true");

        // Get all the faceOrderList where createdTime is null
        defaultFaceOrderShouldNotBeFound("createdTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByUpdatedTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where updatedTime equals to DEFAULT_UPDATED_TIME
        defaultFaceOrderShouldBeFound("updatedTime.equals=" + DEFAULT_UPDATED_TIME);

        // Get all the faceOrderList where updatedTime equals to UPDATED_UPDATED_TIME
        defaultFaceOrderShouldNotBeFound("updatedTime.equals=" + UPDATED_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByUpdatedTimeIsInShouldWork() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where updatedTime in DEFAULT_UPDATED_TIME or UPDATED_UPDATED_TIME
        defaultFaceOrderShouldBeFound("updatedTime.in=" + DEFAULT_UPDATED_TIME + "," + UPDATED_UPDATED_TIME);

        // Get all the faceOrderList where updatedTime equals to UPDATED_UPDATED_TIME
        defaultFaceOrderShouldNotBeFound("updatedTime.in=" + UPDATED_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByUpdatedTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where updatedTime is not null
        defaultFaceOrderShouldBeFound("updatedTime.specified=true");

        // Get all the faceOrderList where updatedTime is null
        defaultFaceOrderShouldNotBeFound("updatedTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByTransferAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where transferAmount equals to DEFAULT_TRANSFER_AMOUNT
        defaultFaceOrderShouldBeFound("transferAmount.equals=" + DEFAULT_TRANSFER_AMOUNT);

        // Get all the faceOrderList where transferAmount equals to UPDATED_TRANSFER_AMOUNT
        defaultFaceOrderShouldNotBeFound("transferAmount.equals=" + UPDATED_TRANSFER_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByTransferAmountIsInShouldWork() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where transferAmount in DEFAULT_TRANSFER_AMOUNT or UPDATED_TRANSFER_AMOUNT
        defaultFaceOrderShouldBeFound("transferAmount.in=" + DEFAULT_TRANSFER_AMOUNT + "," + UPDATED_TRANSFER_AMOUNT);

        // Get all the faceOrderList where transferAmount equals to UPDATED_TRANSFER_AMOUNT
        defaultFaceOrderShouldNotBeFound("transferAmount.in=" + UPDATED_TRANSFER_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByTransferAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where transferAmount is not null
        defaultFaceOrderShouldBeFound("transferAmount.specified=true");

        // Get all the faceOrderList where transferAmount is null
        defaultFaceOrderShouldNotBeFound("transferAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByTransferIsEqualToSomething() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where transfer equals to DEFAULT_TRANSFER
        defaultFaceOrderShouldBeFound("transfer.equals=" + DEFAULT_TRANSFER);

        // Get all the faceOrderList where transfer equals to UPDATED_TRANSFER
        defaultFaceOrderShouldNotBeFound("transfer.equals=" + UPDATED_TRANSFER);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByTransferIsInShouldWork() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where transfer in DEFAULT_TRANSFER or UPDATED_TRANSFER
        defaultFaceOrderShouldBeFound("transfer.in=" + DEFAULT_TRANSFER + "," + UPDATED_TRANSFER);

        // Get all the faceOrderList where transfer equals to UPDATED_TRANSFER
        defaultFaceOrderShouldNotBeFound("transfer.in=" + UPDATED_TRANSFER);
    }

    @Test
    @Transactional
    public void getAllFaceOrdersByTransferIsNullOrNotNull() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);

        // Get all the faceOrderList where transfer is not null
        defaultFaceOrderShouldBeFound("transfer.specified=true");

        // Get all the faceOrderList where transfer is null
        defaultFaceOrderShouldNotBeFound("transfer.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultFaceOrderShouldBeFound(String filter) throws Exception {
        restFaceOrderMockMvc.perform(get("/api/face-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(faceOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderNo").value(hasItem(DEFAULT_ORDER_NO.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].buserId").value(hasItem(DEFAULT_BUSER_ID.intValue())))
            .andExpect(jsonPath("$.[*].cuserId").value(hasItem(DEFAULT_CUSER_ID.intValue())))
            .andExpect(jsonPath("$.[*].payment").value(hasItem(DEFAULT_PAYMENT.intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())))
            .andExpect(jsonPath("$.[*].ticket").value(hasItem(DEFAULT_TICKET.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(DEFAULT_UPDATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].transferAmount").value(hasItem(DEFAULT_TRANSFER_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].transfer").value(hasItem(DEFAULT_TRANSFER.intValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultFaceOrderShouldNotBeFound(String filter) throws Exception {
        restFaceOrderMockMvc.perform(get("/api/face-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingFaceOrder() throws Exception {
        // Get the faceOrder
        restFaceOrderMockMvc.perform(get("/api/face-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFaceOrder() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);
        int databaseSizeBeforeUpdate = faceOrderRepository.findAll().size();

        // Update the faceOrder
        FaceOrder updatedFaceOrder = faceOrderRepository.findOne(faceOrder.getId());
        // Disconnect from session so that the updates on updatedFaceOrder are not directly saved in db
        em.detach(updatedFaceOrder);
        updatedFaceOrder
            .orderNo(UPDATED_ORDER_NO)
            .type(UPDATED_TYPE)
            .buserId(UPDATED_BUSER_ID)
            .cuserId(UPDATED_CUSER_ID)
            .payment(UPDATED_PAYMENT)
            .amount(UPDATED_AMOUNT)
            .balance(UPDATED_BALANCE)
            .ticket(UPDATED_TICKET)
            .status(UPDATED_STATUS)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .transferAmount(UPDATED_TRANSFER_AMOUNT)
            .transfer(UPDATED_TRANSFER);
        FaceOrderDTO faceOrderDTO = faceOrderMapper.toDto(updatedFaceOrder);

        restFaceOrderMockMvc.perform(put("/api/face-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(faceOrderDTO)))
            .andExpect(status().isOk());

        // Validate the FaceOrder in the database
        List<FaceOrder> faceOrderList = faceOrderRepository.findAll();
        assertThat(faceOrderList).hasSize(databaseSizeBeforeUpdate);
        FaceOrder testFaceOrder = faceOrderList.get(faceOrderList.size() - 1);
        assertThat(testFaceOrder.getOrderNo()).isEqualTo(UPDATED_ORDER_NO);
        assertThat(testFaceOrder.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testFaceOrder.getBuserId()).isEqualTo(UPDATED_BUSER_ID);
        assertThat(testFaceOrder.getCuserId()).isEqualTo(UPDATED_CUSER_ID);
        assertThat(testFaceOrder.getPayment()).isEqualTo(UPDATED_PAYMENT);
        assertThat(testFaceOrder.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testFaceOrder.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testFaceOrder.getTicket()).isEqualTo(UPDATED_TICKET);
        assertThat(testFaceOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFaceOrder.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testFaceOrder.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testFaceOrder.getTransferAmount()).isEqualTo(UPDATED_TRANSFER_AMOUNT);
        assertThat(testFaceOrder.getTransfer()).isEqualTo(UPDATED_TRANSFER);
    }

    @Test
    @Transactional
    public void updateNonExistingFaceOrder() throws Exception {
        int databaseSizeBeforeUpdate = faceOrderRepository.findAll().size();

        // Create the FaceOrder
        FaceOrderDTO faceOrderDTO = faceOrderMapper.toDto(faceOrder);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFaceOrderMockMvc.perform(put("/api/face-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(faceOrderDTO)))
            .andExpect(status().isCreated());

        // Validate the FaceOrder in the database
        List<FaceOrder> faceOrderList = faceOrderRepository.findAll();
        assertThat(faceOrderList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFaceOrder() throws Exception {
        // Initialize the database
        faceOrderRepository.saveAndFlush(faceOrder);
        int databaseSizeBeforeDelete = faceOrderRepository.findAll().size();

        // Get the faceOrder
        restFaceOrderMockMvc.perform(delete("/api/face-orders/{id}", faceOrder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FaceOrder> faceOrderList = faceOrderRepository.findAll();
        assertThat(faceOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FaceOrder.class);
        FaceOrder faceOrder1 = new FaceOrder();
        faceOrder1.setId(1L);
        FaceOrder faceOrder2 = new FaceOrder();
        faceOrder2.setId(faceOrder1.getId());
        assertThat(faceOrder1).isEqualTo(faceOrder2);
        faceOrder2.setId(2L);
        assertThat(faceOrder1).isNotEqualTo(faceOrder2);
        faceOrder1.setId(null);
        assertThat(faceOrder1).isNotEqualTo(faceOrder2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FaceOrderDTO.class);
        FaceOrderDTO faceOrderDTO1 = new FaceOrderDTO();
        faceOrderDTO1.setId(1L);
        FaceOrderDTO faceOrderDTO2 = new FaceOrderDTO();
        assertThat(faceOrderDTO1).isNotEqualTo(faceOrderDTO2);
        faceOrderDTO2.setId(faceOrderDTO1.getId());
        assertThat(faceOrderDTO1).isEqualTo(faceOrderDTO2);
        faceOrderDTO2.setId(2L);
        assertThat(faceOrderDTO1).isNotEqualTo(faceOrderDTO2);
        faceOrderDTO1.setId(null);
        assertThat(faceOrderDTO1).isNotEqualTo(faceOrderDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(faceOrderMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(faceOrderMapper.fromId(null)).isNull();
    }
}
