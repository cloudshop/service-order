package com.eyun.order.service.dto;


import java.time.Instant;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the DepOrder entity.
 */
public class DepOrderDTO implements Serializable {

    private Long id;

    private String orderNo;

    private Integer status;

    private Long userid;

    private BigDecimal payment;

    private Integer payType;

    private String payNo;

    private Instant payTime;

    private Long walletId;

    private Instant createdTime;

    private Instant updatedTime;

    private Boolean deleted;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
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

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DepOrderDTO depOrderDTO = (DepOrderDTO) o;
        if(depOrderDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), depOrderDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DepOrderDTO{" +
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
