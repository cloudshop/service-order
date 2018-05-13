package com.eyun.order.service.mapper;

import com.eyun.order.domain.*;
import com.eyun.order.service.dto.DepOrderDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity DepOrder and its DTO DepOrderDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DepOrderMapper extends EntityMapper<DepOrderDTO, DepOrder> {



    default DepOrder fromId(Long id) {
        if (id == null) {
            return null;
        }
        DepOrder depOrder = new DepOrder();
        depOrder.setId(id);
        return depOrder;
    }
}
