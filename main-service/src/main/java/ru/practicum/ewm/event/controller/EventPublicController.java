package ru.practicum.ewm.event.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.error.ValidationException;
import ru.practicum.ewm.event.dto.FullEventDto;
import ru.practicum.ewm.event.dto.ShortEventDto;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.utils.EventSort;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.utils.Constants.DATE_FORMAT;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/events")
public class EventPublicController {
    public static final String ID_URI = "/{id}";
    private final EventService eventService;

    @GetMapping
    public List<ShortEventDto> getPublishedEvents(@RequestParam(defaultValue = "") String text,
                                                  @RequestParam(defaultValue = "") List<Long> categories,
                                                  @RequestParam(required = false) Boolean paid,
                                                  @RequestParam(required = false)
                                                  @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime rangeStart,
                                                  @RequestParam(required = false)
                                                  @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime rangeEnd,
                                                  @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                  @RequestParam(required = false) EventSort sort,
                                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                  @RequestParam(defaultValue = "10") @Positive int size,
                                                  HttpServletRequest request) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new ValidationException("The date of start cannot be after end");
        }
        return eventService.getPublishedEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, PageRequest.of(from / size, size), request);
    }

    @GetMapping(ID_URI)
    public FullEventDto getPublishedEventById(@PathVariable @Positive Long id, HttpServletRequest request) {
        return eventService.getPublishedEventById(id, request);
    }
}