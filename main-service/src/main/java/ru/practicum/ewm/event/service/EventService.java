package ru.practicum.ewm.event.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.event.dto.FullEventDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.ShortEventDto;
import ru.practicum.ewm.event.dto.UpdateEventDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.request.dto.RequestResultDto;
import ru.practicum.ewm.request.dto.RequestUpdateDto;
import ru.practicum.ewm.utils.EventSort;
import ru.practicum.ewm.utils.EventState;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    FullEventDto addEvent(NewEventDto newEventDto, Long userId);

    FullEventDto getEvent(Long userId, Long eventId);

    List<ShortEventDto> getAllEvents(Long userId, PageRequest pageRequest);

    FullEventDto updateEvent(UpdateEventDto updateEventDto, Long userId, Long eventId);

    List<RequestDto> getRequestsByEventId(Long userId, Long eventId);

    RequestResultDto updateRequestsStatus(RequestUpdateDto requestUpdateDto, Long userId, Long eventId);

    List<FullEventDto> getEventsByAdmin(List<Long> usersIds, List<EventState> states, List<Long> categoriesIds,
                                        LocalDateTime start, LocalDateTime end, PageRequest pageRequest);

    FullEventDto updateEventByAdmin(UpdateEventDto userUpdateEventDto, Long eventId);

    List<ShortEventDto> getPublishedEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd, Boolean onlyAvailable, EventSort sort,
                                           PageRequest pageRequest, HttpServletRequest request);

    FullEventDto getPublishedEventById(Long id, HttpServletRequest request);

    public List<Event> fillWithEventViews(List<Event> events);

    List<Event> fillWithConfirmedRequests(List<Event> events);

}