package ru.practicum.ewm.event.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.dto.NewEventDTO;
import ru.practicum.ewm.event.model.Event;

public abstract class EventMapperDecorator implements EventMapper {
    @Autowired
    private EventMapper eventMapper;

    @Override
    public Event toEvent(NewEventDTO newEventDTO, Category category) {
        Event event = eventMapper.toEvent(newEventDTO, category);
        event.setCategory(category);
        return event;
    }
}