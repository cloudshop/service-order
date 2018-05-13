package com.eyun.order.service.mapper;

import com.eyun.order.domain.*;
import com.eyun.order.service.dto.LeaguerOrderDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity LeaguerOrder and its DTO LeaguerOrderDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LeaguerOrderMapper extends EntityMapper<LeaguerOrderDTO, LeaguerOrder> {



    default LeaguerOrder fromId(Long id) {
        if (id == null) {
            return null;
        }
        LeaguerOrder leaguerOrder = new LeaguerOrder();
        leaguerOrder.setId(id);
        return leaguerOrder;
    }
}
