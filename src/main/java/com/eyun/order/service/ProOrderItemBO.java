package com.eyun.order.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

import com.eyun.order.service.dto.ProOrderItemDTO;

public class ProOrderItemBO {

	    private Long productSkuId;

	    private Integer count;

	    private BigDecimal price;
	    
	    private String url;
	    
	    private String skuName;

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
	    
	    
}
