package ru.practicum.ewm.event.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.dto.FullEventDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.ShortEventDto;
import ru.practicum.ewm.event.model.Event;

import java.util.List;
import java.util.Set;

@DecoratedWith(EventMapperDecorator.class)
@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "category", target = "category")
    Event toEvent(NewEventDto newEventDto, Category category);

    @Named(value = "shortDto")
    ShortEventDto toShortEventDto(Event event);

    List<ShortEventDto> toShortEventDto(List<Event> events);

    Set<ShortEventDto> toShortEventDto(Set<Event> events);

    FullEventDto toFullEventDto(Event event);
}