package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.EndpointHit;
import ru.practicum.ewm.ViewStats;
import ru.practicum.ewm.error.exceptions.ValidationException;
import ru.practicum.ewm.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.util.Constants.DATE_FORMAT;
import static ru.practicum.ewm.util.Constants.END_BEFORE_START;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHit addStat(@RequestBody EndpointHit endpointHit) {
        log.info("Response from POST request on /hit");
        return service.addStat(endpointHit);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewStats> getStats(
            @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime end,
            @RequestParam(defaultValue = "") String[] uris,
            @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Response from GET request on /stats");
        if (start.isAfter(end)) throw new ValidationException(END_BEFORE_START);
        return service.getStats(start, end, List.of(uris), unique);
    }
}