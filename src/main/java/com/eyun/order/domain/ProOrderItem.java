package com.eyun.order.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A ProOrderItem.
 */
@Entity
@Table(name = "pro_order_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProOrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_sku_id")
    private Long productSkuId;

    @Column(name = "count")
    private Integer count;

    @Column(name = "price", precision=10, scale=2)
    private BigDecimal price;

    @Column(name = "created_time")
    private Instant createdTime;

    @Column(name = "updated_time")
    private Instant updatedTime;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    @Column(name = "transfer", precision=10, scale=2)
    private BigDecimal transfer;

    @ManyToOne
    private ProOrder proOrder;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductSkuId() {
        return productSkuId;
    }

    public ProOrderItem productSkuId(Long productSkuId) {
        this.productSkuId = productSkuId;
        return this;
    }

    public void setProductSkuId(Long productSkuId) {
        this.productSkuId = productSkuId;
    }

    public Integer getCount() {
        return count;
    }

    public ProOrderItem count(Integer count) {
        this.count = count;
        return this;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProOrderItem price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public ProOrderItem createdTime(Instant createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public Instant getUpdatedTime() {
        return updatedTime;
    }

    public ProOrderItem updatedTime(Instant updatedTime) {
        this.updatedTime = updatedTime;
        return this;
    }

    public void setUpdatedTime(Instant updatedTime) {
        this.updatedTime = updatedTime;
    }

    public BigDecimal getTransfer() {
        return transfer;
    }

    public ProOrderItem transfer(BigDecimal transfer) {
        this.transfer = transfer;
        return this;
    }

    public void setTransfer(BigDecimal transfer) {
        this.transfer = transfer;
    }

    public ProOrder getProOrder() {
        return proOrder;
    }

    public ProOrderItem proOrder(ProOrder proOrder) {
        this.proOrder = proOrder;
        return this;
    }

    public void setProOrder(ProOrder proOrder) {
        this.proOrder = proOrder;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProOrderItem proOrderItem = (ProOrderItem) o;
        if (proOrderItem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), proOrderItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProOrderItem{" +
            "id=" + getId() +
            ", productSkuId=" + getProductSkuId() +
            ", count=" + getCount() +
            ", price=" + getPrice() +
            ", createdTime='" + getCreatedTime() + "'" +
            ", updatedTime='" + getUpdatedTime() + "'" +
            ", transfer=" + getTransfer() +
            "}";
    }
}
