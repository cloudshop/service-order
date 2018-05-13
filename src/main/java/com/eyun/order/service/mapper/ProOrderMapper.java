package com.eyun.order.service.mapper;

import com.eyun.order.domain.*;
import com.eyun.order.service.dto.ProOrderDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ProOrder and its DTO ProOrderDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProOrderMapper extends EntityMapper<ProOrderDTO, ProOrder> {


    @Mapping(target = "proOrderItems", ignore = true)
    ProOrder toEntity(ProOrderDTO proOrderDTO);

    default ProOrder fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProOrder proOrder = new ProOrder();
        proOrder.setId(id);
        return proOrder;
    }
}
