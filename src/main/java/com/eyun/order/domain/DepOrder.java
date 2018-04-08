package com.eyun.order.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DepOrder.
 */
@Entity
@Table(name = "dep_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DepOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no")
    private String orderNo;

    @Column(name = "status")
    private Integer status;

    @Column(name = "userid")
    private Long userid;

    @Column(name = "payment", precision=10, scale=2)
    private BigDecimal payment;

    @Column(name = "pay_type")
    private Integer payType;

    @Column(name = "pay_no")
    private String payNo;

    @Column(name = "pay_time")
    private Instant payTime;

    @Column(name = "wallet_id")
    private Long walletId;

    @Column(name = "created_time")
    private Instant createdTime;

    @Column(name = "updated_time")
    private Instant updatedTime;

    @Column(name = "deleted")
    private Boolean deleted;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public DepOrder orderNo(String orderNo) {
        this.orderNo = orderNo;
        return this;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getStatus() {
        return status;
    }

    public DepOrder status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getUserid() {
        return userid;
    }

    public DepOrder userid(Long userid) {
        this.userid = userid;
        return this;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public DepOrder payment(BigDecimal payment) {
        this.payment = payment;
        return this;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public Integer getPayType() {
        return payType;
    }

    public DepOrder payType(Integer payType) {
        this.payType = payType;
        return this;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getPayNo() {
        return payNo;
    }

    public DepOrder payNo(String payNo) {
        this.payNo = payNo;
        return this;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public Instant getPayTime() {
        return payTime;
    }

    public DepOrder payTime(Instant payTime) {
        this.payTime = payTime;
        return this;
    }

    public void setPayTime(Instant payTime) {
        this.payTime = payTime;
    }

    public Long getWalletId() {
        return walletId;
    }

    public DepOrder walletId(Long walletId) {
        this.walletId = walletId;
        return this;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public DepOrder createdTime(Instant createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public Instant getUpdatedTime() {
        return updatedTime;
    }

    public DepOrder updatedTime(Instant updatedTime) {
        this.updatedTime = updatedTime;
        return this;
    }

    public void setUpdatedTime(Instant updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public DepOrder deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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
        DepOrder depOrder = (DepOrder) o;
        if (depOrder.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), depOrder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DepOrder{" +
            "id=" + getId() +
            ", orderNo='" + getOrderNo() + "'" +
            ", status=" + getStatus() +
            ", userid=" + getUserid() +
            ", payment=" + getPayment() +
            ", payType=" + getPayType() +
            ", payNo='" + getPayNo() + "'" +
            ", payTime='" + getPayTime() + "'" +
            ", walletId=" + getWalletId() +
            ", createdTime='" + getCreatedTime() + "'" +
            ", updatedTime='" + getUpdatedTime() + "'" +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }
}
