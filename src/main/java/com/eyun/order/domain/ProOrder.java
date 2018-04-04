package com.eyun.order.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ProOrder.
 */
@Entity
@Table(name = "pro_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "b_userid")
    private Long bUserid;

    @Column(name = "order_no")
    private String orderNo;

    @Column(name = "status")
    private Integer status;

    @Column(name = "payment", precision=10, scale=2)
    private BigDecimal payment;

    @Column(name = "payment_type")
    private Integer paymentType;

    @Column(name = "payment_time")
    private Instant paymentTime;

    @Column(name = "post_fee", precision=10, scale=2)
    private BigDecimal postFee;

    @Column(name = "consign_time")
    private Instant consignTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Column(name = "close_time")
    private Instant closeTime;

    @Column(name = "shipping_name")
    private String shippingName;

    @Column(name = "shiping_code")
    private String shipingCode;

    @Column(name = "buyer_message")
    private String buyerMessage;

    @Column(name = "buyer_nick")
    private String buyerNick;

    @Column(name = "buyer_rate")
    private Boolean buyerRate;

    @Column(name = "created_time")
    private Instant createdTime;

    @Column(name = "update_time")
    private Instant updateTime;

    @Column(name = "deleted_b")
    private Boolean deletedB;

    @Column(name = "deleted_c")
    private Boolean deletedC;

    @Column(name = "shop_id")
    private Long shopId;

    @OneToMany(mappedBy = "proOrder")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ProOrderItem> proOrderItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getbUserid() {
        return bUserid;
    }

    public ProOrder bUserid(Long bUserid) {
        this.bUserid = bUserid;
        return this;
    }

    public void setbUserid(Long bUserid) {
        this.bUserid = bUserid;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public ProOrder orderNo(String orderNo) {
        this.orderNo = orderNo;
        return this;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getStatus() {
        return status;
    }

    public ProOrder status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public ProOrder payment(BigDecimal payment) {
        this.payment = payment;
        return this;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public ProOrder paymentType(Integer paymentType) {
        this.paymentType = paymentType;
        return this;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Instant getPaymentTime() {
        return paymentTime;
    }

    public ProOrder paymentTime(Instant paymentTime) {
        this.paymentTime = paymentTime;
        return this;
    }

    public void setPaymentTime(Instant paymentTime) {
        this.paymentTime = paymentTime;
    }

    public BigDecimal getPostFee() {
        return postFee;
    }

    public ProOrder postFee(BigDecimal postFee) {
        this.postFee = postFee;
        return this;
    }

    public void setPostFee(BigDecimal postFee) {
        this.postFee = postFee;
    }

    public Instant getConsignTime() {
        return consignTime;
    }

    public ProOrder consignTime(Instant consignTime) {
        this.consignTime = consignTime;
        return this;
    }

    public void setConsignTime(Instant consignTime) {
        this.consignTime = consignTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public ProOrder endTime(Instant endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Instant getCloseTime() {
        return closeTime;
    }

    public ProOrder closeTime(Instant closeTime) {
        this.closeTime = closeTime;
        return this;
    }

    public void setCloseTime(Instant closeTime) {
        this.closeTime = closeTime;
    }

    public String getShippingName() {
        return shippingName;
    }

    public ProOrder shippingName(String shippingName) {
        this.shippingName = shippingName;
        return this;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public String getShipingCode() {
        return shipingCode;
    }

    public ProOrder shipingCode(String shipingCode) {
        this.shipingCode = shipingCode;
        return this;
    }

    public void setShipingCode(String shipingCode) {
        this.shipingCode = shipingCode;
    }

    public String getBuyerMessage() {
        return buyerMessage;
    }

    public ProOrder buyerMessage(String buyerMessage) {
        this.buyerMessage = buyerMessage;
        return this;
    }

    public void setBuyerMessage(String buyerMessage) {
        this.buyerMessage = buyerMessage;
    }

    public String getBuyerNick() {
        return buyerNick;
    }

    public ProOrder buyerNick(String buyerNick) {
        this.buyerNick = buyerNick;
        return this;
    }

    public void setBuyerNick(String buyerNick) {
        this.buyerNick = buyerNick;
    }

    public Boolean isBuyerRate() {
        return buyerRate;
    }

    public ProOrder buyerRate(Boolean buyerRate) {
        this.buyerRate = buyerRate;
        return this;
    }

    public void setBuyerRate(Boolean buyerRate) {
        this.buyerRate = buyerRate;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public ProOrder createdTime(Instant createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public ProOrder updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean isDeletedB() {
        return deletedB;
    }

    public ProOrder deletedB(Boolean deletedB) {
        this.deletedB = deletedB;
        return this;
    }

    public void setDeletedB(Boolean deletedB) {
        this.deletedB = deletedB;
    }

    public Boolean isDeletedC() {
        return deletedC;
    }

    public ProOrder deletedC(Boolean deletedC) {
        this.deletedC = deletedC;
        return this;
    }

    public void setDeletedC(Boolean deletedC) {
        this.deletedC = deletedC;
    }

    public Long getShopId() {
        return shopId;
    }

    public ProOrder shopId(Long shopId) {
        this.shopId = shopId;
        return this;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Set<ProOrderItem> getProOrderItems() {
        return proOrderItems;
    }

    public ProOrder proOrderItems(Set<ProOrderItem> proOrderItems) {
        this.proOrderItems = proOrderItems;
        return this;
    }

    public ProOrder addProOrderItem(ProOrderItem proOrderItem) {
        this.proOrderItems.add(proOrderItem);
        proOrderItem.setProOrder(this);
        return this;
    }

    public ProOrder removeProOrderItem(ProOrderItem proOrderItem) {
        this.proOrderItems.remove(proOrderItem);
        proOrderItem.setProOrder(null);
        return this;
    }

    public void setProOrderItems(Set<ProOrderItem> proOrderItems) {
        this.proOrderItems = proOrderItems;
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
        ProOrder proOrder = (ProOrder) o;
        if (proOrder.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), proOrder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProOrder{" +
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
