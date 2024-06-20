package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.event.dto.FullEventDTO;
import ru.practicum.ewm.event.dto.UpdateEventDTO;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.utils.EventState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.utils.Constants.DATE_FORMAT;
import static ru.practicum.ewm.utils.Constants.EVENTS_ADMIN_URI;
import static ru.practicum.ewm.utils.Constants.EVENT_ID_URI;
import static ru.practicum.ewm.utils.Utilities.checkEventStart;
import static ru.practicum.ewm.utils.Utilities.fromSizePage;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(EVENTS_ADMIN_URI)
public class EventAdminController {
    private final EventService eventService;

    @GetMapping
    public List<FullEventDTO> getEventsByAdmin(@RequestParam(defaultValue = "") List<Long> usersIds,
                                               @RequestParam(defaultValue = "") List<EventState> states,
                                               @RequestParam(defaultValue = "") List<Long> eventsIds,
                                               @RequestParam(required = false)
                                               @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime rangeStart,
                                               @RequestParam(required = false)
                                               @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime rangeEnd,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Response from GET request on {}", EVENTS_ADMIN_URI);
        return eventService.getEventsByAdmin(usersIds, states, eventsIds, rangeStart, rangeEnd,
                fromSizePage(from, size));
    }

    @PatchMapping(EVENT_ID_URI)
    public FullEventDTO updateEventByAdmin(@RequestBody @Valid UpdateEventDTO updateEventDTO,
                                           @PathVariable Long eventId) {
        log.info("Response from PATCH request on {}", EVENTS_ADMIN_URI + EVENT_ID_URI);
        if (updateEventDTO.getEventDate() != null) checkEventStart(updateEventDTO.getEventDate());
        return eventService.updateEventByAdmin(updateEventDTO, eventId);
    }
}
