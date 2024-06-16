package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.utils.RequestStatus;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    Boolean existsByEventIdAndRequesterId(Long eventId, Long userId);

    Long countByEventIdAndStatus(Long eventId, RequestStatus status);

    List<Request> findAllByIdIn(List<Long> ids);

    List<Request> findAllByRequesterId(Long userId);

    List<Request> findAllByEventIdInAndStatus(List<Long> ids, RequestStatus status);

    List<Request> findAllByEventId(Long eventId);
}