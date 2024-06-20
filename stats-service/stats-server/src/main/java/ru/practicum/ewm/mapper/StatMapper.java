package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.ewm.EndpointHit;
import ru.practicum.ewm.model.Stat;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StatMapper {
    Stat toStat(EndpointHit endpointHit);

    EndpointHit toEndpointHit(Stat stat);
}