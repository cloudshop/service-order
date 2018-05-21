package com.eyun.order.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.InstantFilter;




/**
 * Criteria class for the FaceOrder entity. This class is used in FaceOrderResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /face-orders?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FaceOrderCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter orderNo;

    private IntegerFilter type;

    private LongFilter buserId;

    private LongFilter cuserId;

    private BigDecimalFilter payment;

    private BigDecimalFilter amount;

    private BigDecimalFilter balance;

    private BigDecimalFilter ticket;

    private IntegerFilter status;

    private InstantFilter createdTime;

    private InstantFilter updatedTime;

    public FaceOrderCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(StringFilter orderNo) {
        this.orderNo = orderNo;
    }

    public IntegerFilter getType() {
        return type;
    }

    public void setType(IntegerFilter type) {
        this.type = type;
    }

    public LongFilter getBuserId() {
        return buserId;
    }

    public void setBuserId(LongFilter buserId) {
        this.buserId = buserId;
    }

    public LongFilter getCuserId() {
        return cuserId;
    }

    public void setCuserId(LongFilter cuserId) {
        this.cuserId = cuserId;
    }

    public BigDecimalFilter getPayment() {
        return payment;
    }

    public void setPayment(BigDecimalFilter payment) {
        this.payment = payment;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public BigDecimalFilter getBalance() {
        return balance;
    }

    public void setBalance(BigDecimalFilter balance) {
        this.balance = balance;
    }

    public BigDecimalFilter getTicket() {
        return ticket;
    }

    public void setTicket(BigDecimalFilter ticket) {
        this.ticket = ticket;
    }

    public IntegerFilter getStatus() {
        return status;
    }

    public void setStatus(IntegerFilter status) {
        this.status = status;
    }

    public InstantFilter getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(InstantFilter createdTime) {
        this.createdTime = createdTime;
    }

    public InstantFilter getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(InstantFilter updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public String toString() {
        return "FaceOrderCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (orderNo != null ? "orderNo=" + orderNo + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (buserId != null ? "buserId=" + buserId + ", " : "") +
                (cuserId != null ? "cuserId=" + cuserId + ", " : "") +
                (payment != null ? "payment=" + payment + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (balance != null ? "balance=" + balance + ", " : "") +
                (ticket != null ? "ticket=" + ticket + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (createdTime != null ? "createdTime=" + createdTime + ", " : "") +
                (updatedTime != null ? "updatedTime=" + updatedTime + ", " : "") +
            "}";
    }

}
