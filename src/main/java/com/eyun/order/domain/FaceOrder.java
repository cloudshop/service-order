package com.eyun.order.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A FaceOrder.
 */
@Entity
@Table(name = "face_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FaceOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no")
    private String orderNo;

    @Column(name = "jhi_type")
    private Integer type;

    @Column(name = "buser_id")
    private Long buserId;

    @Column(name = "cuser_id")
    private Long cuserId;

    @Column(name = "payment", precision=10, scale=2)
    private BigDecimal payment;

    @Column(name = "amount", precision=10, scale=2)
    private BigDecimal amount;

    @Column(name = "balance", precision=10, scale=2)
    private BigDecimal balance;

    @Column(name = "ticket", precision=10, scale=2)
    private BigDecimal ticket;

    @Column(name = "status")
    private Integer status;

    @Column(name = "created_time")
    private Instant createdTime;

    @Column(name = "updated_time")
    private Instant updatedTime;

    @Column(name = "transfer_amount", precision=10, scale=2)
    private BigDecimal transferAmount;

    @Column(name = "transfer", precision=10, scale=2)
    private BigDecimal transfer;

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

    public FaceOrder orderNo(String orderNo) {
        this.orderNo = orderNo;
        return this;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getType() {
        return type;
    }

    public FaceOrder type(Integer type) {
        this.type = type;
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getBuserId() {
        return buserId;
    }

    public FaceOrder buserId(Long buserId) {
        this.buserId = buserId;
        return this;
    }

    public void setBuserId(Long buserId) {
        this.buserId = buserId;
    }

    public Long getCuserId() {
        return cuserId;
    }

    public FaceOrder cuserId(Long cuserId) {
        this.cuserId = cuserId;
        return this;
    }

    public void setCuserId(Long cuserId) {
        this.cuserId = cuserId;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public FaceOrder payment(BigDecimal payment) {
        this.payment = payment;
        return this;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public FaceOrder amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public FaceOrder balance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getTicket() {
        return ticket;
    }

    public FaceOrder ticket(BigDecimal ticket) {
        this.ticket = ticket;
        return this;
    }

    public void setTicket(BigDecimal ticket) {
        this.ticket = ticket;
    }

    public Integer getStatus() {
        return status;
    }

    public FaceOrder status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public FaceOrder createdTime(Instant createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public Instant getUpdatedTime() {
        return updatedTime;
    }

    public FaceOrder updatedTime(Instant updatedTime) {
        this.updatedTime = updatedTime;
        return this;
    }

    public void setUpdatedTime(Instant updatedTime) {
        this.updatedTime = updatedTime;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public FaceOrder transferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
        return this;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public BigDecimal getTransfer() {
        return transfer;
    }

    public FaceOrder transfer(BigDecimal transfer) {
        this.transfer = transfer;
        return this;
    }

    public void setTransfer(BigDecimal transfer) {
        this.transfer = transfer;
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
        FaceOrder faceOrder = (FaceOrder) o;
        if (faceOrder.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), faceOrder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FaceOrder{" +
            "id=" + getId() +
            ", orderNo='" + getOrderNo() + "'" +
            ", type=" + getType() +
            ", buserId=" + getBuserId() +
            ", cuserId=" + getCuserId() +
            ", payment=" + getPayment() +
            ", amount=" + getAmount() +
            ", balance=" + getBalance() +
            ", ticket=" + getTicket() +
            ", status=" + getStatus() +
            ", createdTime='" + getCreatedTime() + "'" +
            ", updatedTime='" + getUpdatedTime() + "'" +
            ", transferAmount=" + getTransferAmount() +
            ", transfer=" + getTransfer() +
            "}";
    }
}
