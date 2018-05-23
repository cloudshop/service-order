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
 * Criteria class for the ProOrderItem entity. This class is used in ProOrderItemResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /pro-order-items?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProOrderItemCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private LongFilter productSkuId;

    private IntegerFilter count;

    private BigDecimalFilter price;

    private InstantFilter createdTime;

    private InstantFilter updatedTime;

    private BigDecimalFilter transfer;

    private LongFilter proOrderId;

    public ProOrderItemCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getProductSkuId() {
        return productSkuId;
    }

    public void setProductSkuId(LongFilter productSkuId) {
        this.productSkuId = productSkuId;
    }

    public IntegerFilter getCount() {
        return count;
    }

    public void setCount(IntegerFilter count) {
        this.count = count;
    }

    public BigDecimalFilter getPrice() {
        return price;
    }

    public void setPrice(BigDecimalFilter price) {
        this.price = price;
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

    public BigDecimalFilter getTransfer() {
        return transfer;
    }

    public void setTransfer(BigDecimalFilter transfer) {
        this.transfer = transfer;
    }

    public LongFilter getProOrderId() {
        return proOrderId;
    }

    public void setProOrderId(LongFilter proOrderId) {
        this.proOrderId = proOrderId;
    }

    @Override
    public String toString() {
        return "ProOrderItemCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (productSkuId != null ? "productSkuId=" + productSkuId + ", " : "") +
                (count != null ? "count=" + count + ", " : "") +
                (price != null ? "price=" + price + ", " : "") +
                (createdTime != null ? "createdTime=" + createdTime + ", " : "") +
                (updatedTime != null ? "updatedTime=" + updatedTime + ", " : "") +
                (transfer != null ? "transfer=" + transfer + ", " : "") +
                (proOrderId != null ? "proOrderId=" + proOrderId + ", " : "") +
            "}";
    }

}
