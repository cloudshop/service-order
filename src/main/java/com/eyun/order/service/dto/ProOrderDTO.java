package com.eyun.order.service.dto;


import java.time.Instant;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.eyun.order.domain.ProOrderItem;

import java.util.Objects;

/**
 * A DTO for the ProOrder entity.
 */
public class ProOrderDTO implements Serializable {

    private Long id;

    private Long bUserid;

    private String orderNo;

    private Integer status;

    private BigDecimal payment;

    private Integer paymentType;

    private Instant paymentTime;

    private BigDecimal postFee;

    private Instant consignTime;

    private Instant endTime;

    private Instant closeTime;

    private String shippingName;

    private String shipingCode;

    private String buyerMessage;

    private String buyerNick;

    private Boolean buyerRate;

    private Instant createdTime;

    private Instant updateTime;

    private Boolean deletedB;

    private Boolean deletedC;

    private Long shopId;
    
    private Set<ProOrderItemDTO> proOrderItems = new HashSet<>();
    
    public Set<ProOrderItemDTO> getProOrderItems() {
		return proOrderItems;
	}

	public void setProOrderItems(Set<ProOrderItemDTO> proOrderItems) {
		this.proOrderItems = proOrderItems;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getbUserid() {
        return bUserid;
    }

    public void setbUserid(Long bUserid) {
        this.bUserid = bUserid;
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

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Instant getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Instant paymentTime) {
        this.paymentTime = paymentTime;
    }

    public BigDecimal getPostFee() {
        return postFee;
    }

    public void setPostFee(BigDecimal postFee) {
        this.postFee = postFee;
    }

    public Instant getConsignTime() {
        return consignTime;
    }

    public void setConsignTime(Instant consignTime) {
        this.consignTime = consignTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Instant getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Instant closeTime) {
        this.closeTime = closeTime;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public String getShipingCode() {
        return shipingCode;
    }

    public void setShipingCode(String shipingCode) {
        this.shipingCode = shipingCode;
    }

    public String getBuyerMessage() {
        return buyerMessage;
    }

    public void setBuyerMessage(String buyerMessage) {
        this.buyerMessage = buyerMessage;
    }

    public String getBuyerNick() {
        return buyerNick;
    }

    public void setBuyerNick(String buyerNick) {
        this.buyerNick = buyerNick;
    }

    public Boolean isBuyerRate() {
        return buyerRate;
    }

    public void setBuyerRate(Boolean buyerRate) {
        this.buyerRate = buyerRate;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean isDeletedB() {
        return deletedB;
    }

    public void setDeletedB(Boolean deletedB) {
        this.deletedB = deletedB;
    }

    public Boolean isDeletedC() {
        return deletedC;
    }

    public void setDeletedC(Boolean deletedC) {
        this.deletedC = deletedC;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProOrderDTO proOrderDTO = (ProOrderDTO) o;
        if(proOrderDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), proOrderDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProOrderDTO{" +
            "id=" + getId() +
            ", bUserid=" + getbUserid() +
            ", orderNo='" + getOrderNo() + "'" +
            ", status=" + getStatus() +
            ", payment=" + getPayment() +
            ", paymentType=" + getPaymentType() +
            ", paymentTime='" + getPaymentTime() + "'" +
            ", postFee=" + getPostFee() +
            ", consignTime='" + getConsignTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", closeTime='" + getCloseTime() + "'" +
            ", shippingName='" + getShippingName() + "'" +
            ", shipingCode='" + getShipingCode() + "'" +
            ", buyerMessage='" + getBuyerMessage() + "'" +
            ", buyerNick='" + getBuyerNick() + "'" +
            ", buyerRate='" + isBuyerRate() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", deletedB='" + isDeletedB() + "'" +
            ", deletedC='" + isDeletedC() + "'" +
            ", shopId=" + getShopId() +
            "}";
    }
}
