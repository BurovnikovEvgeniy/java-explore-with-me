package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.EndpointHit;
import ru.practicum.ewm.ViewStats;
import ru.practicum.ewm.model.Stat;

@Mapper(componentModel = "spring")
public interface StatMapper {
    Stat toStat(EndpointHit endpointHit);

    EndpointHit toEndpointHit(Stat stat);
}
