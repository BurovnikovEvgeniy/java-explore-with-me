package ru.practicum.ewm.request.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.request.service.RequestService;

import javax.validation.constraints.Positive;
import java.util.List;

import static ru.practicum.ewm.utils.Constants.REQUESTS_PRIVATE_URI;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping(REQUESTS_PRIVATE_URI)
public class RequestController {
    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto addRequest(@PathVariable @Positive Long userId,
                                 @RequestParam @Positive Long eventId) {
        log.info("Response from POST request on {}", REQUESTS_PRIVATE_URI);
        return requestService.addRequest(userId, eventId);
    }

    @GetMapping
    public List<RequestDto> getAllRequests(@PathVariable @Positive Long userId) {
        log.info("Response from GET request on {}", REQUESTS_PRIVATE_URI);
        return requestService.getAllRequests(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable @Positive Long userId,
                                    @PathVariable @Positive Long requestId) {
        log.info("Response from GET request on {}", REQUESTS_PRIVATE_URI);
        return requestService.cancelRequest(userId, requestId);
    }
}