package com.eyun.order.service.mapper;

import com.eyun.order.domain.*;
import com.eyun.order.service.dto.ProOrderDTO;
import com.eyun.order.service.dto.ProOrderItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ProOrderItem and its DTO ProOrderItemDTO.
 */
@Mapper(componentModel = "spring", uses = {ProOrderMapper.class})
public interface ProOrderItemMapper extends EntityMapper<ProOrderItemDTO, ProOrderItem> {

    @Mapping(source = "proOrder.id", target = "proOrderId")
    ProOrderItemDTO toDto(ProOrderItem proOrderItem);

    @Mapping(source = "proOrderId", target = "proOrder")
    ProOrderItem toEntity(ProOrderItemDTO proOrderItemDTO);

    default ProOrderItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProOrderItem proOrderItem = new ProOrderItem();
        proOrderItem.setId(id);
        return proOrderItem;
    }
}
