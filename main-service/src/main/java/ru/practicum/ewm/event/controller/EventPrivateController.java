package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.event.dto.FullEventDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.ShortEventDto;
import ru.practicum.ewm.event.dto.UpdateEventDto;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.request.dto.RequestResultDto;
import ru.practicum.ewm.request.dto.RequestUpdateDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.ewm.utils.Constants.EVENTS_PRIVATE_URI;
import static ru.practicum.ewm.utils.Constants.EVENT_ID_REQUESTS_URI;
import static ru.practicum.ewm.utils.Constants.EVENT_ID_URI;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(EVENTS_PRIVATE_URI)
public class EventPrivateController {
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FullEventDto addEvent(@RequestBody @Valid NewEventDto newEventDTO,
                                 @PathVariable @Positive Long userId) {
        log.info("Response from POST request on {}", EVENTS_PRIVATE_URI);
        if (newEventDTO.getPaid() == null) {
            newEventDTO.setPaid(false);
        }
        if (newEventDTO.getParticipantLimit() == null) {
            newEventDTO.setParticipantLimit(0L);
        }
        if (newEventDTO.getRequestModeration() == null) {
            newEventDTO.setRequestModeration(true);
        }
        return eventService.addEvent(newEventDTO, userId);
    }

    @GetMapping(EVENT_ID_URI)
    public FullEventDto getEvent(@PathVariable @Positive Long userId,
                                 @PathVariable @Positive Long eventId) {
        log.info("Response from GET request on {}{}", EVENTS_PRIVATE_URI, EVENT_ID_URI);
        return eventService.getEvent(userId, eventId);
    }

    @GetMapping
    public List<ShortEventDto> getAllEvents(@PathVariable @Positive Long userId,
                                            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                            @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Response from GET request on {}", EVENTS_PRIVATE_URI);
        return eventService.getAllEvents(userId, PageRequest.of(from / size, size));
    }

    @PatchMapping(EVENT_ID_URI)
    public FullEventDto updateEvent(@RequestBody @Valid UpdateEventDto updateEventDTO,
                                    @PathVariable @Positive Long userId,
                                    @PathVariable @Positive Long eventId) {
        log.info("Response from PATCH request on {}{}", EVENTS_PRIVATE_URI, EVENT_ID_URI);
        return eventService.updateEvent(updateEventDTO, userId, eventId);
    }

    @GetMapping(EVENT_ID_REQUESTS_URI)
    public List<RequestDto> getRequestsByEventId(@PathVariable @Positive Long userId,
                                                 @PathVariable @Positive Long eventId) {
        log.info("Response from GET request on {}{}", EVENTS_PRIVATE_URI, EVENT_ID_REQUESTS_URI);
        return eventService.getRequestsByEventId(userId, eventId);
    }

    @PatchMapping(EVENT_ID_REQUESTS_URI)
    public RequestResultDto updateRequestsStatus(@RequestBody @Valid RequestUpdateDto requestUpdateDTO,
                                                 @PathVariable @Positive Long userId,
                                                 @PathVariable @Positive Long eventId) {
        log.info("Response from PATCH request on {}{}", EVENTS_PRIVATE_URI, EVENT_ID_REQUESTS_URI);
        return eventService.updateRequestsStatus(requestUpdateDTO, userId, eventId);
    }
}