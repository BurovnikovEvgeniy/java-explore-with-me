package ru.practicum.ewm.location.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.ewm.location.dto.LocationDto;
import ru.practicum.ewm.location.model.Location;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LocationMapper {

    @Mapping(target = "id", ignore = true)
    Location toLocation(LocationDto locationDto);
}