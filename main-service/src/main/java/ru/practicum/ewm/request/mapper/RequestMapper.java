package ru.practicum.ewm.request.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.user.model.User;

import java.util.List;

@DecoratedWith(RequestMapperDecorator.class)
@Mapper(componentModel = "spring")
public interface RequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "event", source = "event")
    @Mapping(target = "requester", source = "requester")
    Request toRequest(RequestDto requestDTO, Event event, User requester);

    @Mapping(target = "event", expression = "java(request.getEvent().getId())")
    @Mapping(target = "requester", expression = "java(request.getRequester().getId())")
    RequestDto toRequestDto(Request request);

    List<RequestDto> toRequestDto(List<Request> requests);
}