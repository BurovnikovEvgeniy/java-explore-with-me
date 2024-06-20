package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.event.dto.FullEventDto;
import ru.practicum.ewm.event.dto.UpdateEventDto;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.utils.EventState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static ru.practicum.ewm.utils.Constants.DATE_FORMAT;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class EventAdminController {
    private final EventService eventService;

    @GetMapping
    public List<FullEventDto> getEventsByAdmin(@RequestParam(required = false, defaultValue = "") List<Long> usersIds,
                                               @RequestParam(required = false, defaultValue = "") List<EventState> states,
                                               @RequestParam(required = false, defaultValue = "") List<Long> eventsIds,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime rangeStart,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime rangeEnd,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(defaultValue = "10") @Positive int size) {
        if (usersIds == null) {
            usersIds = Collections.emptyList();
        }
        if (states == null) {
            states = Collections.emptyList();
        }
        if (eventsIds == null) {
            eventsIds = Collections.emptyList();
        }
        return eventService.getEventsByAdmin(usersIds, states, eventsIds, rangeStart, rangeEnd, PageRequest.of(from / size, size));
    }

    @PatchMapping("/{eventId}")
    public FullEventDto updateEventByAdmin(@RequestBody @Valid UpdateEventDto updateEventDTO,
                                           @PathVariable Long eventId) {
        return eventService.updateEventByAdmin(updateEventDTO, eventId);
    }
}
