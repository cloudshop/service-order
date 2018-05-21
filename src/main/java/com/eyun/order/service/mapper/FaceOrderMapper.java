package com.eyun.order.service.mapper;

import com.eyun.order.domain.*;
import com.eyun.order.service.dto.FaceOrderDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity FaceOrder and its DTO FaceOrderDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FaceOrderMapper extends EntityMapper<FaceOrderDTO, FaceOrder> {



    default FaceOrder fromId(Long id) {
        if (id == null) {
            return null;
        }
        FaceOrder faceOrder = new FaceOrder();
        faceOrder.setId(id);
        return faceOrder;
    }
}
