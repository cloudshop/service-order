package com.eyun.order.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the ProductSku entity.
 */
public class ProductSkuDTO implements Serializable {

    private Long id;

    @NotNull
    private Long productId;

    @NotNull
    @Min(value = 0)
    private Integer count;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal price;

    private Integer status;

    private String skuName;

    private String skuCode;

    private String attrString;

    private Instant createdTime;

    private Instant updatedTime;

    private Boolean deleted;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    private BigDecimal transfer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getAttrString() {
        return attrString;
    }

    public void setAttrString(String attrString) {
        this.attrString = attrString;
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

    public BigDecimal getTransfer() {
        return transfer;
    }

    public void setTransfer(BigDecimal transfer) {
        this.transfer = transfer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductSkuDTO productSkuDTO = (ProductSkuDTO) o;
        if(productSkuDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productSkuDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductSkuDTO{" +
            "id=" + getId() +
            ", productId=" + getProductId() +
            ", count=" + getCount() +
            ", price=" + getPrice() +
            ", status=" + getStatus() +
            ", skuName='" + getSkuName() + "'" +
            ", skuCode='" + getSkuCode() + "'" +
            ", attrString='" + getAttrString() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            ", updatedTime='" + getUpdatedTime() + "'" +
            ", deleted='" + isDeleted() + "'" +
            ", transfer=" + getTransfer() +
            "}";
    }
}
