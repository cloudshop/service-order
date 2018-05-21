package com.eyun.order.service.dto;


import java.time.Instant;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the FaceOrder entity.
 */
public class FaceOrderDTO implements Serializable {

    private Long id;

    private String orderNo;

    private Integer type;

    private Long buserId;

    private Long cuserId;

    private BigDecimal payment;

    private BigDecimal amount;

    private BigDecimal balance;

    private BigDecimal ticket;

    private Integer status;

    private Instant createdTime;

    private Instant updatedTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getBuserId() {
        return buserId;
    }

    public void setBuserId(Long buserId) {
        this.buserId = buserId;
    }

    public Long getCuserId() {
        return cuserId;
    }

    public void setCuserId(Long cuserId) {
        this.cuserId = cuserId;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getTicket() {
        return ticket;
    }

    public void setTicket(BigDecimal ticket) {
        this.ticket = ticket;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public Instant getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Instant updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FaceOrderDTO faceOrderDTO = (FaceOrderDTO) o;
        if(faceOrderDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), faceOrderDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FaceOrderDTO{" +
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
            "}";
    }
}
