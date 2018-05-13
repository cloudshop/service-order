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
 * Criteria class for the LeaguerOrder entity. This class is used in LeaguerOrderResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /leaguer-orders?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LeaguerOrderCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter orderNo;

    private IntegerFilter status;

    private LongFilter userid;

    private BigDecimalFilter payment;

    private BigDecimalFilter ticket;

    private IntegerFilter payType;

    private StringFilter payNo;

    private InstantFilter payTime;

    private InstantFilter createdTime;

    private InstantFilter updatedTime;

    private BooleanFilter deleted;

    public LeaguerOrderCriteria() {
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

    public IntegerFilter getStatus() {
        return status;
    }

    public void setStatus(IntegerFilter status) {
        this.status = status;
    }

    public LongFilter getUserid() {
        return userid;
    }

    public void setUserid(LongFilter userid) {
        this.userid = userid;
    }

    public BigDecimalFilter getPayment() {
        return payment;
    }

    public void setPayment(BigDecimalFilter payment) {
        this.payment = payment;
    }

    public BigDecimalFilter getTicket() {
        return ticket;
    }

    public void setTicket(BigDecimalFilter ticket) {
        this.ticket = ticket;
    }

    public IntegerFilter getPayType() {
        return payType;
    }

    public void setPayType(IntegerFilter payType) {
        this.payType = payType;
    }

    public StringFilter getPayNo() {
        return payNo;
    }

    public void setPayNo(StringFilter payNo) {
        this.payNo = payNo;
    }

    public InstantFilter getPayTime() {
        return payTime;
    }

    public void setPayTime(InstantFilter payTime) {
        this.payTime = payTime;
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

    public BooleanFilter getDeleted() {
        return deleted;
    }

    public void setDeleted(BooleanFilter deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "LeaguerOrderCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (orderNo != null ? "orderNo=" + orderNo + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (userid != null ? "userid=" + userid + ", " : "") +
                (payment != null ? "payment=" + payment + ", " : "") +
                (ticket != null ? "ticket=" + ticket + ", " : "") +
                (payType != null ? "payType=" + payType + ", " : "") +
                (payNo != null ? "payNo=" + payNo + ", " : "") +
                (payTime != null ? "payTime=" + payTime + ", " : "") +
                (createdTime != null ? "createdTime=" + createdTime + ", " : "") +
                (updatedTime != null ? "updatedTime=" + updatedTime + ", " : "") +
                (deleted != null ? "deleted=" + deleted + ", " : "") +
            "}";
    }

}
