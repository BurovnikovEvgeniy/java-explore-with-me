package ru.practicum.ewm.request.repository.service;

import ru.practicum.ewm.request.dto.RequestDto;

import java.util.List;

public interface RequestService {
    RequestDto addRequest(Long userId, Long eventId);

    List<RequestDto> getAllRequests(Long userId);

    RequestDto cancelRequest(Long userId, Long requestId);
}