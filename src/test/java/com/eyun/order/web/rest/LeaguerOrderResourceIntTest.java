package com.eyun.order.web.rest;

import com.eyun.order.OrderApp;

import com.eyun.order.config.SecurityBeanOverrideConfiguration;

import com.eyun.order.domain.LeaguerOrder;
import com.eyun.order.repository.LeaguerOrderRepository;
import com.eyun.order.service.LeaguerOrderService;
import com.eyun.order.service.dto.LeaguerOrderDTO;
import com.eyun.order.service.mapper.LeaguerOrderMapper;
import com.eyun.order.web.rest.errors.ExceptionTranslator;
import com.eyun.order.service.dto.LeaguerOrderCriteria;
import com.eyun.order.service.LeaguerOrderQueryService;

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
 * Test class for the LeaguerOrderResource REST controller.
 *
 * @see LeaguerOrderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {OrderApp.class, SecurityBeanOverrideConfiguration.class})
public class LeaguerOrderResourceIntTest {

    private static final String DEFAULT_ORDER_NO = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_NO = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final Long DEFAULT_USERID = 1L;
    private static final Long UPDATED_USERID = 2L;

    private static final BigDecimal DEFAULT_PAYMENT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAYMENT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TICKET = new BigDecimal(1);
    private static final BigDecimal UPDATED_TICKET = new BigDecimal(2);

    private static final Integer DEFAULT_PAY_TYPE = 1;
    private static final Integer UPDATED_PAY_TYPE = 2;

    private static final String DEFAULT_PAY_NO = "AAAAAAAAAA";
    private static final String UPDATED_PAY_NO = "BBBBBBBBBB";

    private static final Instant DEFAULT_PAY_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAY_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    @Autowired
    private LeaguerOrderRepository leaguerOrderRepository;

    @Autowired
    private LeaguerOrderMapper leaguerOrderMapper;

    @Autowired
    private LeaguerOrderService leaguerOrderService;

    @Autowired
    private LeaguerOrderQueryService leaguerOrderQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLeaguerOrderMockMvc;

    private LeaguerOrder leaguerOrder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LeaguerOrderResource leaguerOrderResource = new LeaguerOrderResource(leaguerOrderService, leaguerOrderQueryService);
        this.restLeaguerOrderMockMvc = MockMvcBuilders.standaloneSetup(leaguerOrderResource)
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
    public static LeaguerOrder createEntity(EntityManager em) {
        LeaguerOrder leaguerOrder = new LeaguerOrder()
            .orderNo(DEFAULT_ORDER_NO)
            .status(DEFAULT_STATUS)
            .userid(DEFAULT_USERID)
            .payment(DEFAULT_PAYMENT)
            .ticket(DEFAULT_TICKET)
            .payType(DEFAULT_PAY_TYPE)
            .payNo(DEFAULT_PAY_NO)
            .payTime(DEFAULT_PAY_TIME)
            .createdTime(DEFAULT_CREATED_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME)
            .deleted(DEFAULT_DELETED);
        return leaguerOrder;
    }

    @Before
    public void initTest() {
        leaguerOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createLeaguerOrder() throws Exception {
        int databaseSizeBeforeCreate = leaguerOrderRepository.findAll().size();

        // Create the LeaguerOrder
        LeaguerOrderDTO leaguerOrderDTO = leaguerOrderMapper.toDto(leaguerOrder);
        restLeaguerOrderMockMvc.perform(post("/api/leaguer-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leaguerOrderDTO)))
            .andExpect(status().isCreated());

        // Validate the LeaguerOrder in the database
        List<LeaguerOrder> leaguerOrderList = leaguerOrderRepository.findAll();
        assertThat(leaguerOrderList).hasSize(databaseSizeBeforeCreate + 1);
        LeaguerOrder testLeaguerOrder = leaguerOrderList.get(leaguerOrderList.size() - 1);
        assertThat(testLeaguerOrder.getOrderNo()).isEqualTo(DEFAULT_ORDER_NO);
        assertThat(testLeaguerOrder.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testLeaguerOrder.getUserid()).isEqualTo(DEFAULT_USERID);
        assertThat(testLeaguerOrder.getPayment()).isEqualTo(DEFAULT_PAYMENT);
        assertThat(testLeaguerOrder.getTicket()).isEqualTo(DEFAULT_TICKET);
        assertThat(testLeaguerOrder.getPayType()).isEqualTo(DEFAULT_PAY_TYPE);
        assertThat(testLeaguerOrder.getPayNo()).isEqualTo(DEFAULT_PAY_NO);
        assertThat(testLeaguerOrder.getPayTime()).isEqualTo(DEFAULT_PAY_TIME);
        assertThat(testLeaguerOrder.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testLeaguerOrder.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testLeaguerOrder.isDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void createLeaguerOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = leaguerOrderRepository.findAll().size();

        // Create the LeaguerOrder with an existing ID
        leaguerOrder.setId(1L);
        LeaguerOrderDTO leaguerOrderDTO = leaguerOrderMapper.toDto(leaguerOrder);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaguerOrderMockMvc.perform(post("/api/leaguer-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leaguerOrderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LeaguerOrder in the database
        List<LeaguerOrder> leaguerOrderList = leaguerOrderRepository.findAll();
        assertThat(leaguerOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLeaguerOrders() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList
        restLeaguerOrderMockMvc.perform(get("/api/leaguer-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaguerOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderNo").value(hasItem(DEFAULT_ORDER_NO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.intValue())))
            .andExpect(jsonPath("$.[*].payment").value(hasItem(DEFAULT_PAYMENT.intValue())))
            .andExpect(jsonPath("$.[*].ticket").value(hasItem(DEFAULT_TICKET.intValue())))
            .andExpect(jsonPath("$.[*].payType").value(hasItem(DEFAULT_PAY_TYPE)))
            .andExpect(jsonPath("$.[*].payNo").value(hasItem(DEFAULT_PAY_NO.toString())))
            .andExpect(jsonPath("$.[*].payTime").value(hasItem(DEFAULT_PAY_TIME.toString())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(DEFAULT_UPDATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    public void getLeaguerOrder() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get the leaguerOrder
        restLeaguerOrderMockMvc.perform(get("/api/leaguer-orders/{id}", leaguerOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(leaguerOrder.getId().intValue()))
            .andExpect(jsonPath("$.orderNo").value(DEFAULT_ORDER_NO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.userid").value(DEFAULT_USERID.intValue()))
            .andExpect(jsonPath("$.payment").value(DEFAULT_PAYMENT.intValue()))
            .andExpect(jsonPath("$.ticket").value(DEFAULT_TICKET.intValue()))
            .andExpect(jsonPath("$.payType").value(DEFAULT_PAY_TYPE))
            .andExpect(jsonPath("$.payNo").value(DEFAULT_PAY_NO.toString()))
            .andExpect(jsonPath("$.payTime").value(DEFAULT_PAY_TIME.toString()))
            .andExpect(jsonPath("$.createdTime").value(DEFAULT_CREATED_TIME.toString()))
            .andExpect(jsonPath("$.updatedTime").value(DEFAULT_UPDATED_TIME.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByOrderNoIsEqualToSomething() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where orderNo equals to DEFAULT_ORDER_NO
        defaultLeaguerOrderShouldBeFound("orderNo.equals=" + DEFAULT_ORDER_NO);

        // Get all the leaguerOrderList where orderNo equals to UPDATED_ORDER_NO
        defaultLeaguerOrderShouldNotBeFound("orderNo.equals=" + UPDATED_ORDER_NO);
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByOrderNoIsInShouldWork() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where orderNo in DEFAULT_ORDER_NO or UPDATED_ORDER_NO
        defaultLeaguerOrderShouldBeFound("orderNo.in=" + DEFAULT_ORDER_NO + "," + UPDATED_ORDER_NO);

        // Get all the leaguerOrderList where orderNo equals to UPDATED_ORDER_NO
        defaultLeaguerOrderShouldNotBeFound("orderNo.in=" + UPDATED_ORDER_NO);
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByOrderNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where orderNo is not null
        defaultLeaguerOrderShouldBeFound("orderNo.specified=true");

        // Get all the leaguerOrderList where orderNo is null
        defaultLeaguerOrderShouldNotBeFound("orderNo.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where status equals to DEFAULT_STATUS
        defaultLeaguerOrderShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the leaguerOrderList where status equals to UPDATED_STATUS
        defaultLeaguerOrderShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultLeaguerOrderShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the leaguerOrderList where status equals to UPDATED_STATUS
        defaultLeaguerOrderShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where status is not null
        defaultLeaguerOrderShouldBeFound("status.specified=true");

        // Get all the leaguerOrderList where status is null
        defaultLeaguerOrderShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where status greater than or equals to DEFAULT_STATUS
        defaultLeaguerOrderShouldBeFound("status.greaterOrEqualThan=" + DEFAULT_STATUS);

        // Get all the leaguerOrderList where status greater than or equals to UPDATED_STATUS
        defaultLeaguerOrderShouldNotBeFound("status.greaterOrEqualThan=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where status less than or equals to DEFAULT_STATUS
        defaultLeaguerOrderShouldNotBeFound("status.lessThan=" + DEFAULT_STATUS);

        // Get all the leaguerOrderList where status less than or equals to UPDATED_STATUS
        defaultLeaguerOrderShouldBeFound("status.lessThan=" + UPDATED_STATUS);
    }


    @Test
    @Transactional
    public void getAllLeaguerOrdersByUseridIsEqualToSomething() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where userid equals to DEFAULT_USERID
        defaultLeaguerOrderShouldBeFound("userid.equals=" + DEFAULT_USERID);

        // Get all the leaguerOrderList where userid equals to UPDATED_USERID
        defaultLeaguerOrderShouldNotBeFound("userid.equals=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByUseridIsInShouldWork() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where userid in DEFAULT_USERID or UPDATED_USERID
        defaultLeaguerOrderShouldBeFound("userid.in=" + DEFAULT_USERID + "," + UPDATED_USERID);

        // Get all the leaguerOrderList where userid equals to UPDATED_USERID
        defaultLeaguerOrderShouldNotBeFound("userid.in=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByUseridIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where userid is not null
        defaultLeaguerOrderShouldBeFound("userid.specified=true");

        // Get all the leaguerOrderList where userid is null
        defaultLeaguerOrderShouldNotBeFound("userid.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByUseridIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where userid greater than or equals to DEFAULT_USERID
        defaultLeaguerOrderShouldBeFound("userid.greaterOrEqualThan=" + DEFAULT_USERID);

        // Get all the leaguerOrderList where userid greater than or equals to UPDATED_USERID
        defaultLeaguerOrderShouldNotBeFound("userid.greaterOrEqualThan=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByUseridIsLessThanSomething() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where userid less than or equals to DEFAULT_USERID
        defaultLeaguerOrderShouldNotBeFound("userid.lessThan=" + DEFAULT_USERID);

        // Get all the leaguerOrderList where userid less than or equals to UPDATED_USERID
        defaultLeaguerOrderShouldBeFound("userid.lessThan=" + UPDATED_USERID);
    }


    @Test
    @Transactional
    public void getAllLeaguerOrdersByPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where payment equals to DEFAULT_PAYMENT
        defaultLeaguerOrderShouldBeFound("payment.equals=" + DEFAULT_PAYMENT);

        // Get all the leaguerOrderList where payment equals to UPDATED_PAYMENT
        defaultLeaguerOrderShouldNotBeFound("payment.equals=" + UPDATED_PAYMENT);
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByPaymentIsInShouldWork() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where payment in DEFAULT_PAYMENT or UPDATED_PAYMENT
        defaultLeaguerOrderShouldBeFound("payment.in=" + DEFAULT_PAYMENT + "," + UPDATED_PAYMENT);

        // Get all the leaguerOrderList where payment equals to UPDATED_PAYMENT
        defaultLeaguerOrderShouldNotBeFound("payment.in=" + UPDATED_PAYMENT);
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByPaymentIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where payment is not null
        defaultLeaguerOrderShouldBeFound("payment.specified=true");

        // Get all the leaguerOrderList where payment is null
        defaultLeaguerOrderShouldNotBeFound("payment.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByTicketIsEqualToSomething() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where ticket equals to DEFAULT_TICKET
        defaultLeaguerOrderShouldBeFound("ticket.equals=" + DEFAULT_TICKET);

        // Get all the leaguerOrderList where ticket equals to UPDATED_TICKET
        defaultLeaguerOrderShouldNotBeFound("ticket.equals=" + UPDATED_TICKET);
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByTicketIsInShouldWork() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where ticket in DEFAULT_TICKET or UPDATED_TICKET
        defaultLeaguerOrderShouldBeFound("ticket.in=" + DEFAULT_TICKET + "," + UPDATED_TICKET);

        // Get all the leaguerOrderList where ticket equals to UPDATED_TICKET
        defaultLeaguerOrderShouldNotBeFound("ticket.in=" + UPDATED_TICKET);
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByTicketIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where ticket is not null
        defaultLeaguerOrderShouldBeFound("ticket.specified=true");

        // Get all the leaguerOrderList where ticket is null
        defaultLeaguerOrderShouldNotBeFound("ticket.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByPayTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where payType equals to DEFAULT_PAY_TYPE
        defaultLeaguerOrderShouldBeFound("payType.equals=" + DEFAULT_PAY_TYPE);

        // Get all the leaguerOrderList where payType equals to UPDATED_PAY_TYPE
        defaultLeaguerOrderShouldNotBeFound("payType.equals=" + UPDATED_PAY_TYPE);
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByPayTypeIsInShouldWork() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where payType in DEFAULT_PAY_TYPE or UPDATED_PAY_TYPE
        defaultLeaguerOrderShouldBeFound("payType.in=" + DEFAULT_PAY_TYPE + "," + UPDATED_PAY_TYPE);

        // Get all the leaguerOrderList where payType equals to UPDATED_PAY_TYPE
        defaultLeaguerOrderShouldNotBeFound("payType.in=" + UPDATED_PAY_TYPE);
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByPayTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where payType is not null
        defaultLeaguerOrderShouldBeFound("payType.specified=true");

        // Get all the leaguerOrderList where payType is null
        defaultLeaguerOrderShouldNotBeFound("payType.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByPayTypeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where payType greater than or equals to DEFAULT_PAY_TYPE
        defaultLeaguerOrderShouldBeFound("payType.greaterOrEqualThan=" + DEFAULT_PAY_TYPE);

        // Get all the leaguerOrderList where payType greater than or equals to UPDATED_PAY_TYPE
        defaultLeaguerOrderShouldNotBeFound("payType.greaterOrEqualThan=" + UPDATED_PAY_TYPE);
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByPayTypeIsLessThanSomething() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where payType less than or equals to DEFAULT_PAY_TYPE
        defaultLeaguerOrderShouldNotBeFound("payType.lessThan=" + DEFAULT_PAY_TYPE);

        // Get all the leaguerOrderList where payType less than or equals to UPDATED_PAY_TYPE
        defaultLeaguerOrderShouldBeFound("payType.lessThan=" + UPDATED_PAY_TYPE);
    }


    @Test
    @Transactional
    public void getAllLeaguerOrdersByPayNoIsEqualToSomething() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where payNo equals to DEFAULT_PAY_NO
        defaultLeaguerOrderShouldBeFound("payNo.equals=" + DEFAULT_PAY_NO);

        // Get all the leaguerOrderList where payNo equals to UPDATED_PAY_NO
        defaultLeaguerOrderShouldNotBeFound("payNo.equals=" + UPDATED_PAY_NO);
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByPayNoIsInShouldWork() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where payNo in DEFAULT_PAY_NO or UPDATED_PAY_NO
        defaultLeaguerOrderShouldBeFound("payNo.in=" + DEFAULT_PAY_NO + "," + UPDATED_PAY_NO);

        // Get all the leaguerOrderList where payNo equals to UPDATED_PAY_NO
        defaultLeaguerOrderShouldNotBeFound("payNo.in=" + UPDATED_PAY_NO);
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByPayNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where payNo is not null
        defaultLeaguerOrderShouldBeFound("payNo.specified=true");

        // Get all the leaguerOrderList where payNo is null
        defaultLeaguerOrderShouldNotBeFound("payNo.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByPayTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where payTime equals to DEFAULT_PAY_TIME
        defaultLeaguerOrderShouldBeFound("payTime.equals=" + DEFAULT_PAY_TIME);

        // Get all the leaguerOrderList where payTime equals to UPDATED_PAY_TIME
        defaultLeaguerOrderShouldNotBeFound("payTime.equals=" + UPDATED_PAY_TIME);
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByPayTimeIsInShouldWork() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where payTime in DEFAULT_PAY_TIME or UPDATED_PAY_TIME
        defaultLeaguerOrderShouldBeFound("payTime.in=" + DEFAULT_PAY_TIME + "," + UPDATED_PAY_TIME);

        // Get all the leaguerOrderList where payTime equals to UPDATED_PAY_TIME
        defaultLeaguerOrderShouldNotBeFound("payTime.in=" + UPDATED_PAY_TIME);
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByPayTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where payTime is not null
        defaultLeaguerOrderShouldBeFound("payTime.specified=true");

        // Get all the leaguerOrderList where payTime is null
        defaultLeaguerOrderShouldNotBeFound("payTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByCreatedTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where createdTime equals to DEFAULT_CREATED_TIME
        defaultLeaguerOrderShouldBeFound("createdTime.equals=" + DEFAULT_CREATED_TIME);

        // Get all the leaguerOrderList where createdTime equals to UPDATED_CREATED_TIME
        defaultLeaguerOrderShouldNotBeFound("createdTime.equals=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByCreatedTimeIsInShouldWork() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where createdTime in DEFAULT_CREATED_TIME or UPDATED_CREATED_TIME
        defaultLeaguerOrderShouldBeFound("createdTime.in=" + DEFAULT_CREATED_TIME + "," + UPDATED_CREATED_TIME);

        // Get all the leaguerOrderList where createdTime equals to UPDATED_CREATED_TIME
        defaultLeaguerOrderShouldNotBeFound("createdTime.in=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByCreatedTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where createdTime is not null
        defaultLeaguerOrderShouldBeFound("createdTime.specified=true");

        // Get all the leaguerOrderList where createdTime is null
        defaultLeaguerOrderShouldNotBeFound("createdTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByUpdatedTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where updatedTime equals to DEFAULT_UPDATED_TIME
        defaultLeaguerOrderShouldBeFound("updatedTime.equals=" + DEFAULT_UPDATED_TIME);

        // Get all the leaguerOrderList where updatedTime equals to UPDATED_UPDATED_TIME
        defaultLeaguerOrderShouldNotBeFound("updatedTime.equals=" + UPDATED_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByUpdatedTimeIsInShouldWork() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where updatedTime in DEFAULT_UPDATED_TIME or UPDATED_UPDATED_TIME
        defaultLeaguerOrderShouldBeFound("updatedTime.in=" + DEFAULT_UPDATED_TIME + "," + UPDATED_UPDATED_TIME);

        // Get all the leaguerOrderList where updatedTime equals to UPDATED_UPDATED_TIME
        defaultLeaguerOrderShouldNotBeFound("updatedTime.in=" + UPDATED_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByUpdatedTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where updatedTime is not null
        defaultLeaguerOrderShouldBeFound("updatedTime.specified=true");

        // Get all the leaguerOrderList where updatedTime is null
        defaultLeaguerOrderShouldNotBeFound("updatedTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where deleted equals to DEFAULT_DELETED
        defaultLeaguerOrderShouldBeFound("deleted.equals=" + DEFAULT_DELETED);

        // Get all the leaguerOrderList where deleted equals to UPDATED_DELETED
        defaultLeaguerOrderShouldNotBeFound("deleted.equals=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where deleted in DEFAULT_DELETED or UPDATED_DELETED
        defaultLeaguerOrderShouldBeFound("deleted.in=" + DEFAULT_DELETED + "," + UPDATED_DELETED);

        // Get all the leaguerOrderList where deleted equals to UPDATED_DELETED
        defaultLeaguerOrderShouldNotBeFound("deleted.in=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void getAllLeaguerOrdersByDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);

        // Get all the leaguerOrderList where deleted is not null
        defaultLeaguerOrderShouldBeFound("deleted.specified=true");

        // Get all the leaguerOrderList where deleted is null
        defaultLeaguerOrderShouldNotBeFound("deleted.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultLeaguerOrderShouldBeFound(String filter) throws Exception {
        restLeaguerOrderMockMvc.perform(get("/api/leaguer-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaguerOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderNo").value(hasItem(DEFAULT_ORDER_NO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.intValue())))
            .andExpect(jsonPath("$.[*].payment").value(hasItem(DEFAULT_PAYMENT.intValue())))
            .andExpect(jsonPath("$.[*].ticket").value(hasItem(DEFAULT_TICKET.intValue())))
            .andExpect(jsonPath("$.[*].payType").value(hasItem(DEFAULT_PAY_TYPE)))
            .andExpect(jsonPath("$.[*].payNo").value(hasItem(DEFAULT_PAY_NO.toString())))
            .andExpect(jsonPath("$.[*].payTime").value(hasItem(DEFAULT_PAY_TIME.toString())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(DEFAULT_UPDATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultLeaguerOrderShouldNotBeFound(String filter) throws Exception {
        restLeaguerOrderMockMvc.perform(get("/api/leaguer-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingLeaguerOrder() throws Exception {
        // Get the leaguerOrder
        restLeaguerOrderMockMvc.perform(get("/api/leaguer-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLeaguerOrder() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);
        int databaseSizeBeforeUpdate = leaguerOrderRepository.findAll().size();

        // Update the leaguerOrder
        LeaguerOrder updatedLeaguerOrder = leaguerOrderRepository.findOne(leaguerOrder.getId());
        // Disconnect from session so that the updates on updatedLeaguerOrder are not directly saved in db
        em.detach(updatedLeaguerOrder);
        updatedLeaguerOrder
            .orderNo(UPDATED_ORDER_NO)
            .status(UPDATED_STATUS)
            .userid(UPDATED_USERID)
            .payment(UPDATED_PAYMENT)
            .ticket(UPDATED_TICKET)
            .payType(UPDATED_PAY_TYPE)
            .payNo(UPDATED_PAY_NO)
            .payTime(UPDATED_PAY_TIME)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .deleted(UPDATED_DELETED);
        LeaguerOrderDTO leaguerOrderDTO = leaguerOrderMapper.toDto(updatedLeaguerOrder);

        restLeaguerOrderMockMvc.perform(put("/api/leaguer-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leaguerOrderDTO)))
            .andExpect(status().isOk());

        // Validate the LeaguerOrder in the database
        List<LeaguerOrder> leaguerOrderList = leaguerOrderRepository.findAll();
        assertThat(leaguerOrderList).hasSize(databaseSizeBeforeUpdate);
        LeaguerOrder testLeaguerOrder = leaguerOrderList.get(leaguerOrderList.size() - 1);
        assertThat(testLeaguerOrder.getOrderNo()).isEqualTo(UPDATED_ORDER_NO);
        assertThat(testLeaguerOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testLeaguerOrder.getUserid()).isEqualTo(UPDATED_USERID);
        assertThat(testLeaguerOrder.getPayment()).isEqualTo(UPDATED_PAYMENT);
        assertThat(testLeaguerOrder.getTicket()).isEqualTo(UPDATED_TICKET);
        assertThat(testLeaguerOrder.getPayType()).isEqualTo(UPDATED_PAY_TYPE);
        assertThat(testLeaguerOrder.getPayNo()).isEqualTo(UPDATED_PAY_NO);
        assertThat(testLeaguerOrder.getPayTime()).isEqualTo(UPDATED_PAY_TIME);
        assertThat(testLeaguerOrder.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testLeaguerOrder.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testLeaguerOrder.isDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingLeaguerOrder() throws Exception {
        int databaseSizeBeforeUpdate = leaguerOrderRepository.findAll().size();

        // Create the LeaguerOrder
        LeaguerOrderDTO leaguerOrderDTO = leaguerOrderMapper.toDto(leaguerOrder);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLeaguerOrderMockMvc.perform(put("/api/leaguer-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leaguerOrderDTO)))
            .andExpect(status().isCreated());

        // Validate the LeaguerOrder in the database
        List<LeaguerOrder> leaguerOrderList = leaguerOrderRepository.findAll();
        assertThat(leaguerOrderList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLeaguerOrder() throws Exception {
        // Initialize the database
        leaguerOrderRepository.saveAndFlush(leaguerOrder);
        int databaseSizeBeforeDelete = leaguerOrderRepository.findAll().size();

        // Get the leaguerOrder
        restLeaguerOrderMockMvc.perform(delete("/api/leaguer-orders/{id}", leaguerOrder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LeaguerOrder> leaguerOrderList = leaguerOrderRepository.findAll();
        assertThat(leaguerOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaguerOrder.class);
        LeaguerOrder leaguerOrder1 = new LeaguerOrder();
        leaguerOrder1.setId(1L);
        LeaguerOrder leaguerOrder2 = new LeaguerOrder();
        leaguerOrder2.setId(leaguerOrder1.getId());
        assertThat(leaguerOrder1).isEqualTo(leaguerOrder2);
        leaguerOrder2.setId(2L);
        assertThat(leaguerOrder1).isNotEqualTo(leaguerOrder2);
        leaguerOrder1.setId(null);
        assertThat(leaguerOrder1).isNotEqualTo(leaguerOrder2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaguerOrderDTO.class);
        LeaguerOrderDTO leaguerOrderDTO1 = new LeaguerOrderDTO();
        leaguerOrderDTO1.setId(1L);
        LeaguerOrderDTO leaguerOrderDTO2 = new LeaguerOrderDTO();
        assertThat(leaguerOrderDTO1).isNotEqualTo(leaguerOrderDTO2);
        leaguerOrderDTO2.setId(leaguerOrderDTO1.getId());
        assertThat(leaguerOrderDTO1).isEqualTo(leaguerOrderDTO2);
        leaguerOrderDTO2.setId(2L);
        assertThat(leaguerOrderDTO1).isNotEqualTo(leaguerOrderDTO2);
        leaguerOrderDTO1.setId(null);
        assertThat(leaguerOrderDTO1).isNotEqualTo(leaguerOrderDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(leaguerOrderMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(leaguerOrderMapper.fromId(null)).isNull();
    }
}
