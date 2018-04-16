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
 * Criteria class for the ProOrder entity. This class is used in ProOrderResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /pro-orders?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProOrderCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private LongFilter bUserid;

    private StringFilter orderNo;

    private IntegerFilter status;

    private BigDecimalFilter payment;

    private IntegerFilter paymentType;

    private InstantFilter paymentTime;

    private BigDecimalFilter postFee;

    private InstantFilter consignTime;

    private InstantFilter endTime;

    private InstantFilter closeTime;

    private StringFilter shippingName;

    private StringFilter shipingCode;

    private StringFilter buyerMessage;

    private StringFilter buyerNick;

    private BooleanFilter buyerRate;

    private InstantFilter createdTime;

    private InstantFilter updateTime;

    private BooleanFilter deletedB;

    private BooleanFilter deletedC;

    private LongFilter shopId;

    private StringFilter payNo;

    private LongFilter proOrderItemId;

    public ProOrderCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getbUserid() {
        return bUserid;
    }

    public void setbUserid(LongFilter bUserid) {
        this.bUserid = bUserid;
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

    public BigDecimalFilter getPayment() {
        return payment;
    }

    public void setPayment(BigDecimalFilter payment) {
        this.payment = payment;
    }

    public IntegerFilter getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(IntegerFilter paymentType) {
        this.paymentType = paymentType;
    }

    public InstantFilter getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(InstantFilter paymentTime) {
        this.paymentTime = paymentTime;
    }

    public BigDecimalFilter getPostFee() {
        return postFee;
    }

    public void setPostFee(BigDecimalFilter postFee) {
        this.postFee = postFee;
    }

    public InstantFilter getConsignTime() {
        return consignTime;
    }

    public void setConsignTime(InstantFilter consignTime) {
        this.consignTime = consignTime;
    }

    public InstantFilter getEndTime() {
        return endTime;
    }

    public void setEndTime(InstantFilter endTime) {
        this.endTime = endTime;
    }

    public InstantFilter getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(InstantFilter closeTime) {
        this.closeTime = closeTime;
    }

    public StringFilter getShippingName() {
        return shippingName;
    }

    public void setShippingName(StringFilter shippingName) {
        this.shippingName = shippingName;
    }

    public StringFilter getShipingCode() {
        return shipingCode;
    }

    public void setShipingCode(StringFilter shipingCode) {
        this.shipingCode = shipingCode;
    }

    public StringFilter getBuyerMessage() {
        return buyerMessage;
    }

    public void setBuyerMessage(StringFilter buyerMessage) {
        this.buyerMessage = buyerMessage;
    }

    public StringFilter getBuyerNick() {
        return buyerNick;
    }

    public void setBuyerNick(StringFilter buyerNick) {
        this.buyerNick = buyerNick;
    }

    public BooleanFilter getBuyerRate() {
        return buyerRate;
    }

    public void setBuyerRate(BooleanFilter buyerRate) {
        this.buyerRate = buyerRate;
    }

    public InstantFilter getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(InstantFilter createdTime) {
        this.createdTime = createdTime;
    }

    public InstantFilter getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(InstantFilter updateTime) {
        this.updateTime = updateTime;
    }

    public BooleanFilter getDeletedB() {
        return deletedB;
    }

    public void setDeletedB(BooleanFilter deletedB) {
        this.deletedB = deletedB;
    }

    public BooleanFilter getDeletedC() {
        return deletedC;
    }

    public void setDeletedC(BooleanFilter deletedC) {
        this.deletedC = deletedC;
    }

    public LongFilter getShopId() {
        return shopId;
    }

    public void setShopId(LongFilter shopId) {
        this.shopId = shopId;
    }

    public StringFilter getPayNo() {
        return payNo;
    }

    public void setPayNo(StringFilter payNo) {
        this.payNo = payNo;
    }

    public LongFilter getProOrderItemId() {
        return proOrderItemId;
    }

    public void setProOrderItemId(LongFilter proOrderItemId) {
        this.proOrderItemId = proOrderItemId;
    }

    @Override
    public String toString() {
        return "ProOrderCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (bUserid != null ? "bUserid=" + bUserid + ", " : "") +
                (orderNo != null ? "orderNo=" + orderNo + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (payment != null ? "payment=" + payment + ", " : "") +
                (paymentType != null ? "paymentType=" + paymentType + ", " : "") +
                (paymentTime != null ? "paymentTime=" + paymentTime + ", " : "") +
                (postFee != null ? "postFee=" + postFee + ", " : "") +
                (consignTime != null ? "consignTime=" + consignTime + ", " : "") +
                (endTime != null ? "endTime=" + endTime + ", " : "") +
                (closeTime != null ? "closeTime=" + closeTime + ", " : "") +
                (shippingName != null ? "shippingName=" + shippingName + ", " : "") +
                (shipingCode != null ? "shipingCode=" + shipingCode + ", " : "") +
                (buyerMessage != null ? "buyerMessage=" + buyerMessage + ", " : "") +
                (buyerNick != null ? "buyerNick=" + buyerNick + ", " : "") +
                (buyerRate != null ? "buyerRate=" + buyerRate + ", " : "") +
                (createdTime != null ? "createdTime=" + createdTime + ", " : "") +
                (updateTime != null ? "updateTime=" + updateTime + ", " : "") +
                (deletedB != null ? "deletedB=" + deletedB + ", " : "") +
                (deletedC != null ? "deletedC=" + deletedC + ", " : "") +
                (shopId != null ? "shopId=" + shopId + ", " : "") +
                (payNo != null ? "payNo=" + payNo + ", " : "") +
                (proOrderItemId != null ? "proOrderItemId=" + proOrderItemId + ", " : "") +
            "}";
    }

}
