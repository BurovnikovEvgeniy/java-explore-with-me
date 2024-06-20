package ru.practicum.ewm.event.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.event.dto.FullEventDTO;
import ru.practicum.ewm.event.dto.NewEventDTO;
import ru.practicum.ewm.event.dto.ShortEventDTO;
import ru.practicum.ewm.event.dto.UpdateEventDTO;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.dto.RequestDTO;
import ru.practicum.ewm.request.dto.RequestResultDTO;
import ru.practicum.ewm.request.dto.RequestUpdateDTO;
import ru.practicum.ewm.utils.EventSort;
import ru.practicum.ewm.utils.EventState;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    FullEventDTO addEvent(NewEventDTO newEventDTO, Long userId);

    FullEventDTO getEvent(Long userId, Long eventId);

    List<ShortEventDTO> getAllEvents(Long userId, PageRequest pageRequest);

    FullEventDTO updateEvent(UpdateEventDTO updateEventDTO, Long userId, Long eventId);

    List<RequestDTO> getRequestsByEventId(Long userId, Long eventId);

    RequestResultDTO updateRequestsStatus(RequestUpdateDTO requestUpdateDTO, Long userId, Long eventId);

    List<FullEventDTO> getEventsByAdmin(List<Long> usersIds, List<EventState> states, List<Long> categoriesIds,
                                        LocalDateTime start, LocalDateTime end, PageRequest pageRequest);

    FullEventDTO updateEventByAdmin(UpdateEventDTO userUpdateEventDTO, Long eventId);

    List<ShortEventDTO> getPublishedEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd, Boolean onlyAvailable, EventSort sort,
                                           PageRequest pageRequest, HttpServletRequest request);

    FullEventDTO getPublishedEventById(Long id, HttpServletRequest request);

    List<Event> fillWithEventViews(List<Event> events);

    List<Event> fillWithConfirmedRequests(List<Event> events);

}