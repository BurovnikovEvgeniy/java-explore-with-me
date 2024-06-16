package ru.practicum.ewm.request.repository.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exceptions.AccessRejectException;
import ru.practicum.ewm.exceptions.EntityNotFoundException;
import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.utils.EventState;
import ru.practicum.ewm.utils.RequestStatus;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.utils.RequestStatus.CANCELED;
import static ru.practicum.ewm.utils.RequestStatus.CONFIRMED;
import static ru.practicum.ewm.utils.RequestStatus.PENDING;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    private final RequestMapper requestMapper;

    @Override
    @Transactional
    public RequestDto addRequest(Long userId, Long eventId) {
        User user = getUserIfExist(userId);
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new EntityNotFoundException("Событие не найдено"));
        if (event.getInitiator().getId().equals(userId)) {
            throw new AccessRejectException("Менеджер мероприятий не может сделать запрос на это мероприятие");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new AccessRejectException("Событие с id=" + eventId + " не опубликовано");
        }
        if (requestRepository.existsByEventIdAndRequesterId(eventId, userId)) {
            throw new AccessRejectException("Пользователь с id=" + userId + " уже отправил запрос на участие в событии с id=" + eventId);
        }
        List<Request> list = requestRepository.findAll();
        Long confirmedRequests = requestRepository.countByEventIdAndStatus(eventId, CONFIRMED);
        if (event.getParticipantLimit() > 0 && event.getParticipantLimit() <= confirmedRequests) {
            throw new AccessRejectException("Лимит участников достигнут");
        }
        RequestStatus status;
        status = event.getRequestModeration() ? PENDING : CONFIRMED;
        if (event.getParticipantLimit() == 0) status = CONFIRMED;
        Request request = Request.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(user)
                .status(status)
                .build();
        return requestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getAllRequests(Long userId) {
        getUserIfExist(userId);
        return requestMapper.toRequestDto(requestRepository.findAllByRequesterId(userId));
    }

    @Override
    public RequestDto cancelRequest(Long userId, Long requestId) {
        getUserIfExist(userId);
        Request request = requestRepository.findById(requestId).orElseThrow(() ->
                new EntityNotFoundException("Запрос с id=" + requestId + " не найден"));
        request.setStatus(CANCELED);
        return requestMapper.toRequestDto(requestRepository.save(request));
    }

    private User getUserIfExist(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("Пользователь с id=" + userId + " не найден"));
    }
}