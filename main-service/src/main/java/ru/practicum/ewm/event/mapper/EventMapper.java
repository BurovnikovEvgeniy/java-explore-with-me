package ru.practicum.ewm.event.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.dto.FullEventDTO;
import ru.practicum.ewm.event.dto.NewEventDTO;
import ru.practicum.ewm.event.dto.ShortEventDTO;
import ru.practicum.ewm.event.model.Event;

import java.util.List;
import java.util.Set;

@DecoratedWith(EventMapperDecorator.class)
@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "category", target = "category")
    Event toEvent(NewEventDTO newEventDTO, Category category);

    @Named(value = "shortDto")
    ShortEventDTO toShortEventDTO(Event event);

    List<ShortEventDTO> toShortEventDTO(List<Event> events);

    Set<ShortEventDTO> toShortEventDTO(Set<Event> events);

    FullEventDTO toFullEventDTO(Event event);
}