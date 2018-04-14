package com.eyun.order.service.dto;


import java.time.Instant;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the ProOrderItem entity.
 */
public class ProOrderItemDTO implements Serializable {

    private Long id;

    private Long productSkuId;

    private Integer count;

    private BigDecimal price;

    private Instant createdTime;

    private Instant updatedTime;

    private Long proOrderId;
    
    private String url;
    
    private String skuName;
    
    
    public ProOrderItemDTO() {
		super();
	}
    
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSkuName() {
		return skuName;
	}
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductSkuId() {
        return productSkuId;
    }

    public void setProductSkuId(Long productSkuId) {
        this.productSkuId = productSkuId;
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

    public Long getProOrderId() {
        return proOrderId;
    }

    public void setProOrderId(Long proOrderId) {
        this.proOrderId = proOrderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProOrderItemDTO proOrderItemDTO = (ProOrderItemDTO) o;
        if(proOrderItemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), proOrderItemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProOrderItemDTO{" +
            "id=" + getId() +
            ", productSkuId=" + getProductSkuId() +
            ", count=" + getCount() +
            ", price=" + getPrice() +
            ", createdTime='" + getCreatedTime() + "'" +
            ", updatedTime='" + getUpdatedTime() + "'" +
            "}";
    }
}
