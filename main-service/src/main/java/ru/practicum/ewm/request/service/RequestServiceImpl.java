package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.error.ConflictException;
import ru.practicum.ewm.error.NotFoundException;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.request.dto.RequestDTO;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.utils.EventState;
import ru.practicum.ewm.utils.RequestStatus;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.utils.Constants.EVENT_NOT_FOUND;
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
    public RequestDTO addRequest(Long userId, Long eventId) {
        log.debug("Adding request from user ID{} to event ID{}", userId, eventId);
        User user = getUserIfExist(userId);
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format(EVENT_NOT_FOUND, eventId)));
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("The event manager is unable to make a request for his event");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException(String.format("Event ID%d still not published", eventId));
        }
        if (requestRepository.existsByEventIdAndRequesterId(eventId, userId)) {
            throw new ConflictException(String.format("User ID%d already send request on event ID%d", userId, eventId));
        }
        List<Request> list = requestRepository.findAll();
        Long confirmedRequests = requestRepository.countByEventIdAndStatus(eventId, CONFIRMED);
        if (event.getParticipantLimit() > 0 && event.getParticipantLimit() <= confirmedRequests) {
            throw new ConflictException("Limit of the participants is already reached");
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
        return requestMapper.toRequestDTO(requestRepository.save(request));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDTO> getAllRequests(Long userId) {
        log.debug(String.format("Getting requests of user ID%d", userId));
        getUserIfExist(userId);
        return requestMapper.toRequestDTO(requestRepository.findAllByRequesterId(userId));
    }

    @Override
    public RequestDTO cancelRequest(Long userId, Long requestId) {
        log.debug(String.format("Canceling request ID%d", requestId));
        getUserIfExist(userId);
        Request request = requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException(String.format("Request ID%d doesn't exist", requestId)));
        request.setStatus(CANCELED);
        return requestMapper.toRequestDTO(requestRepository.save(request));
    }

    private User getUserIfExist(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Юзер не найден"));
    }
}