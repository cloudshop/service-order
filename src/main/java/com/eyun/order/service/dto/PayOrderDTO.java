package com.eyun.order.service.dto;


import java.time.Instant;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the PayOrder entity.
 */
public class PayOrderDTO implements Serializable {

    private Long id;

    private Long userid;

    private String payNo;

    private String orderNo;

    private BigDecimal balance;

    private BigDecimal ticket;

    private BigDecimal integral;

    private Instant payTime;

    private Long walletId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public BigDecimal getIntegral() {
        return integral;
    }

    public void setIntegral(BigDecimal integral) {
        this.integral = integral;
    }

    public Instant getPayTime() {
        return payTime;
    }

    public void setPayTime(Instant payTime) {
        this.payTime = payTime;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PayOrderDTO payOrderDTO = (PayOrderDTO) o;
        if(payOrderDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), payOrderDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PayOrderDTO{" +
            "id=" + getId() +
            ", userid=" + getUserid() +
            ", payNo='" + getPayNo() + "'" +
            ", orderNo='" + getOrderNo() + "'" +
            ", balance=" + getBalance() +
            ", ticket=" + getTicket() +
            ", integral=" + getIntegral() +
            ", payTime='" + getPayTime() + "'" +
            "}";
    }
}
