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


@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class EventPrivateController {
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FullEventDto addEvent(@RequestBody @Valid NewEventDto newEventDto, @PathVariable @Positive Long userId) {
        if (newEventDto.getPaid() == null) {
            newEventDto.setPaid(false);
        }
        if (newEventDto.getParticipantLimit() == null) {
            newEventDto.setParticipantLimit(0L);
        }
        if (newEventDto.getRequestModeration() == null) {
            newEventDto.setRequestModeration(true);
        }
        return eventService.addEvent(newEventDto, userId);
    }

    @GetMapping("/{eventId}")
    public FullEventDto getEvent(@PathVariable @Positive Long userId,
                                 @PathVariable @Positive Long eventId) {
        return eventService.getEvent(userId, eventId);
    }

    @GetMapping
    public List<ShortEventDto> getAllEvents(@PathVariable @Positive Long userId,
                                            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                            @RequestParam(defaultValue = "10") @Positive int size) {
        return eventService.getAllEvents(userId, PageRequest.of(from / size, size));
    }

    @PatchMapping("/{eventId}")
    public FullEventDto updateEvent(@RequestBody @Valid UpdateEventDto updateEventDTO,
                                    @PathVariable @Positive Long userId,
                                    @PathVariable @Positive Long eventId) {
        return eventService.updateEvent(updateEventDTO, userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getRequestsByEventId(@PathVariable @Positive Long userId,
                                                 @PathVariable @Positive Long eventId) {
        return eventService.getRequestsByEventId(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public RequestResultDto updateRequestsStatus(@RequestBody @Valid RequestUpdateDto requestUpdateDTO,
                                                 @PathVariable @Positive Long userId,
                                                 @PathVariable @Positive Long eventId) {
        return eventService.updateRequestsStatus(requestUpdateDTO, userId, eventId);
    }
}