package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.EndpointHit;
import ru.practicum.ewm.StatsClient;
import ru.practicum.ewm.ViewStats;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.dto.FullEventDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.ShortEventDto;
import ru.practicum.ewm.event.dto.UpdateEventDto;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.exceptions.EntityNotFoundException;
import ru.practicum.ewm.exceptions.NoValidDataParams;
import ru.practicum.ewm.location.mapper.LocationMapper;
import ru.practicum.ewm.location.model.Location;
import ru.practicum.ewm.location.repository.LocationRepository;
import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.request.dto.RequestResultDto;
import ru.practicum.ewm.request.dto.RequestUpdateDto;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.utils.EventSort;
import ru.practicum.ewm.utils.EventState;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.ewm.utils.EventState.CANCELED;
import static ru.practicum.ewm.utils.EventState.PENDING;
import static ru.practicum.ewm.utils.EventState.PUBLISHED;
import static ru.practicum.ewm.utils.RequestStatus.CONFIRMED;
import static ru.practicum.ewm.utils.RequestStatus.REJECTED;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;

    private final EventMapper eventMapper;
    private final LocationMapper locationMapper;
    private final RequestMapper requestMapper;

    private final StatsClient statsClient;

    @Override
    @Transactional
    public FullEventDto addEvent(NewEventDto newEventDto, Long userId) {
        User owner = getUser(userId);
        Category category = getCategory(newEventDto.getCategory());
        Event newEvent = eventMapper.toEvent(newEventDto, category);
        newEvent.setInitiator(owner);
        Location location = locationRepository.save(locationMapper.toLocation(newEventDto.getLocation()));
        newEvent.setLocation(location);
        newEvent.setCreatedOn(LocalDateTime.now());
        newEvent.setState(PENDING);
        Event event = eventRepository.save(newEvent);
        event.setViews(0L);
        event.setConfirmedRequests(0L);
        log.debug("Event is added {}", event);
        return eventMapper.toFullEventDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public FullEventDto getEvent(Long userId, Long eventId) {
        getUser(userId);
        Event event = getEvent(eventId);
        event = fillWithEventViews(List.of(event)).get(0);
        event = fillWithConfirmedRequests(List.of(event)).get(0);
        return eventMapper.toFullEventDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShortEventDto> getAllEvents(Long userId, PageRequest pageRequest) {
        getUser(userId);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageRequest);
        events = fillWithEventViews(events);
        events = fillWithConfirmedRequests(events);
        return eventMapper.toShortEventDto(events);
    }

    @Override
    @Transactional
    public FullEventDto updateEvent(UpdateEventDto updateEventDto, Long userId, Long eventId) {
        getUser(userId);
        Event event = getEvent(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Ошибка доступа: событие может быть изменено только владельцем");
        }
        Event updatedEvent = checkEventForUpdate(event, updateEventDto);
        if (updateEventDto.getStateAction() != null) {
            switch (updateEventDto.getStateAction()) {
                case CANCEL_REVIEW:
                    updatedEvent.setState(CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    updatedEvent.setState(PENDING);
                    break;
                default:
                    throw new NoValidDataParams("Некорректное состояние события");
            }
        }
        event = eventRepository.save(updatedEvent);
        event.setViews(0L);
        event.setConfirmedRequests(0L);
        return eventMapper.toFullEventDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getRequestsByEventId(Long userId, Long eventId) {
        getUser(userId);
        getEvent(eventId);
        return requestMapper.toRequestDto(requestRepository.findAllByEventId(eventId));
    }

    @Override
    @Transactional
    public RequestResultDto updateRequestsStatus(RequestUpdateDto requestUpdateDto, Long userId, Long eventId) {
        getUser(userId);
        Event event = getEvent(eventId);
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            throw new ConflictException("Запрос нельзя обновить");
        }
        Long numOfConfirmedRequests = requestRepository.countByEventIdAndStatus(eventId, CONFIRMED);
        if (event.getParticipantLimit().equals(numOfConfirmedRequests)) {
            throw new ConflictException("Лимит участников достигнут, ограничение - " + numOfConfirmedRequests);
        }

        RequestResultDto requestResultDto = RequestResultDto
                .builder()
                .confirmedRequests(new ArrayList<>())
                .rejectedRequests(new ArrayList<>())
                .build();
        List<Request> requestsToUpdate = requestRepository.findAllByIdIn(requestUpdateDto.getRequestIds());
        for (Request request : requestsToUpdate) {
            if (event.getParticipantLimit().equals(numOfConfirmedRequests)) break;
            RequestDto requestDto;
            switch (requestUpdateDto.getStatus()) {
                case REJECTED:
                    request.setStatus(REJECTED);
                    requestDto = requestMapper.toRequestDto(requestRepository.save(request));
                    requestResultDto.getRejectedRequests().add(requestDto);
                    break;
                case CONFIRMED:
                    request.setStatus(CONFIRMED);
                    requestDto = requestMapper.toRequestDto(requestRepository.save(request));
                    requestResultDto.getConfirmedRequests().add(requestDto);
                    numOfConfirmedRequests++;
                    break;
                default:
                    throw new NoValidDataParams("Передан некорректный запрос");
            }
        }
        return requestResultDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FullEventDto> getEventsByAdmin(List<Long> usersIds, List<EventState> states,
                                               List<Long> categoriesIds, LocalDateTime start, LocalDateTime end,
                                               PageRequest pageRequest) {
        List<Specification<Event>> specifications = new ArrayList<>();
        specifications.add(states.isEmpty() ? null : stateIn(states));
        specifications.add(usersIds.isEmpty() ? null : userIdIn(usersIds));
        specifications.add(states.isEmpty() ? null : stateIn(states));
        specifications.add(categoriesIds.isEmpty() ? null : categoryIdIn(categoriesIds));
        specifications.add(start == null ? null : eventDateAfter(start));
        specifications.add(end == null ? null : eventDateBefore(end));
        specifications = specifications.stream().filter(Objects::nonNull).collect(Collectors.toList());
        List<Event> events = eventRepository.findAll(specifications
                .stream()
                .reduce(Specification::and)
                .orElse(null), pageRequest).toList();
        events = fillWithConfirmedRequests(events);
        events = fillWithEventViews(events);
        return events.stream()
                .map(eventMapper::toFullEventDto)
                .sorted(Comparator.comparing(FullEventDto::getId, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FullEventDto updateEventByAdmin(UpdateEventDto updateEventDto, Long eventId) {
        Event event = checkEventForUpdate(getEvent(eventId), updateEventDto);
        if (event.getState().equals(CANCELED)) {
            throw new ConflictException("Событие отменено");
        }
        if (updateEventDto.getStateAction() != null) {
            switch (updateEventDto.getStateAction()) {
                case REJECT_EVENT:
                    event.setState(CANCELED);
                    break;
                case PUBLISH_EVENT:
                    event.setPublishedOn(LocalDateTime.now());
                    event.setState(PUBLISHED);
                    break;
                default:
                    throw new NoValidDataParams("Передан некорректный запрос");
            }
        }
        Event updatedEvent = eventRepository.save(event);
        event.setViews(0L);
        event.setConfirmedRequests(0L);
        return eventMapper.toFullEventDto(updatedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShortEventDto> getPublishedEvents(String text, List<Long> categories, Boolean paid,
                                                  LocalDateTime start, LocalDateTime end,
                                                  Boolean onlyAvailable, EventSort sort, PageRequest pageRequest,
                                                  HttpServletRequest request) {
        addStats(request);
        List<Specification<Event>> specifications = new ArrayList<>();
        specifications.add(stateIn(List.of(PUBLISHED)));
        specifications.add(text.isBlank() ? null : annotationAndDescriptionContaining(text));
        specifications.add(categories.isEmpty() ? null : categoryIdIn(categories));
        specifications.add(paid == null ? null : paidIs(paid));
        specifications.add(start == null ? null : eventDateAfter(start));
        specifications.add(end == null ? null : eventDateBefore(end));
        specifications = specifications.stream().filter(Objects::nonNull).collect(Collectors.toList());
        Specification<Event> s = specifications
                .stream()
                .reduce(Specification::and).orElse(null);

        List<Event> events = eventRepository.findAll(s, pageRequest).toList();
        events = fillWithConfirmedRequests(events);
        events = onlyAvailable ? events
                .stream()
                .filter(event -> event.getParticipantLimit() > event.getConfirmedRequests())
                .collect(Collectors.toList()) : events;
        events = fillWithEventViews(events);
        if (sort == null) return eventMapper.toShortEventDto(events);
        switch (sort) {
            case VIEWS:
                return events
                        .stream()
                        .sorted(Comparator.comparing(Event::getViews))
                        .map(eventMapper::toShortEventDto)
                        .collect(Collectors.toList());
            case EVENT_DATE:
                return events
                        .stream()
                        .sorted(Comparator.comparing(Event::getEventDate))
                        .map(eventMapper::toShortEventDto)
                        .collect(Collectors.toList());
            default:
                return eventMapper.toShortEventDto(events);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public FullEventDto getPublishedEventById(Long id, HttpServletRequest request) {
        Event event = getEvent(id);
        if (!event.getState().equals(PUBLISHED))
            throw new EntityNotFoundException("Событие с id=" + id + " еще не опубликовано");
        addStats(request);
        event = fillWithEventViews(List.of(event)).get(0);
        event = fillWithConfirmedRequests(List.of(event)).get(0);
        return eventMapper.toFullEventDto(event);
    }

    private void addStats(HttpServletRequest request) {
        statsClient.saveHit(EndpointHit.builder()
                .app("explore-with-me")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @Override
    public List<Event> fillWithEventViews(List<Event> events) {
        String eventsUri = "/events" + "/";
        List<String> uris = events
                .stream()
                .map(event -> String.format(eventsUri + "%d", event.getId()))
                .collect(Collectors.toList());
        List<LocalDateTime> startDates = events
                .stream()
                .map(Event::getCreatedOn)
                .sorted()
                .collect(Collectors.toList());
        if (startDates.stream().findFirst().isEmpty()) return events;
        List<ViewStats> viewStatsList = statsClient.getHit(startDates.stream().findFirst().get(), LocalDateTime.now(),
                uris, true);
        Map<Long, Long> mapEventIdViews = viewStatsList
                .stream()
                .collect(Collectors.toMap(
                        statsDto -> Long.parseLong(statsDto.getUri().substring(eventsUri.length())),
                        ViewStats::getHits));
        return events
                .stream()
                .peek(event -> event.setViews(mapEventIdViews.getOrDefault(event.getId(), 0L)))
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> fillWithConfirmedRequests(List<Event> events) {
        List<Request> confirmedRequests = requestRepository.findAllByEventIdInAndStatus(events
                .stream()
                .map(Event::getId)
                .collect(Collectors.toList()), CONFIRMED);
        Map<Long, List<Request>> mapEventIdConfirmedRequests = confirmedRequests
                .stream()
                .collect(Collectors.groupingBy(request -> request.getEvent().getId()));
        return events
                .stream()
                .peek(event -> event.setConfirmedRequests((long) mapEventIdConfirmedRequests
                        .getOrDefault(event.getId(), new ArrayList<>()).size()))
                .collect(Collectors.toList());
    }

    private Event checkEventForUpdate(Event event, UpdateEventDto updateEventDto) {
        if (event.getState().equals(PUBLISHED)) {
            throw new ConflictException("Событие опубликовано, редактирование невозможно");
        }

        if (updateEventDto.getAnnotation() != null && !updateEventDto.getAnnotation()
                .equals(event.getAnnotation())) {
            event.setAnnotation(updateEventDto.getAnnotation());
        }
        if (updateEventDto.getCategory() != null && !updateEventDto.getCategory()
                .equals(event.getCategory().getId())) {
            event.setCategory(getCategory(updateEventDto.getCategory()));
        }
        if (updateEventDto.getDescription() != null && !updateEventDto.getDescription()
                .equals(event.getDescription())) {
            event.setDescription(updateEventDto.getDescription());
        }
        if (updateEventDto.getEventDate() != null && !updateEventDto.getEventDate()
                .equals(event.getEventDate())) {
            event.setEventDate(updateEventDto.getEventDate());
        }
        if (updateEventDto.getLocation() != null) {
            event.setLocation(locationRepository.save(locationMapper.toLocation(updateEventDto.getLocation())));
        }
        if (updateEventDto.getPaid() != null && !updateEventDto.getPaid().equals(event.getPaid())) {
            event.setPaid(updateEventDto.getPaid());
        }
        if (updateEventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventDto.getParticipantLimit());
        }
        if (updateEventDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventDto.getRequestModeration());
        }
        if (updateEventDto.getTitle() != null && !updateEventDto.getTitle().equals(event.getTitle())) {
            event.setTitle(updateEventDto.getTitle());
        }
        return event;
    }

    private Specification<Event> userIdIn(List<Long> usersIds) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("initiator").get("id")).value(usersIds));
    }

    private Specification<Event> stateIn(List<EventState> states) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("state")).value(states));
    }

    private Specification<Event> categoryIdIn(List<Long> categoriesIds) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("category").get("id"))
                .value(categoriesIds));
    }

    private Specification<Event> eventDateAfter(LocalDateTime rangeStart) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
    }

    private Specification<Event> eventDateBefore(LocalDateTime rangeEnd) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
    }

    private Specification<Event> annotationAndDescriptionContaining(String text) {
        String searchText = "%" + text.toLowerCase() + "%";
        return ((root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), searchText),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchText)));
    }

    private Specification<Event> paidIs(Boolean paid) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("paid"), paid));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("Пользователь с id=" + userId + " не найден"));
    }

    private Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new EntityNotFoundException("Категория с id=" + categoryId + " не найдена"));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new EntityNotFoundException("Событие с id=" + eventId + " не найдено"));
    }
}