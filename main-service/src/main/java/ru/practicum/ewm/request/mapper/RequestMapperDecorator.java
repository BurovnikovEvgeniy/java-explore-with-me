package ru.practicum.ewm.request.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.dto.RequestDTO;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.user.model.User;

public abstract class RequestMapperDecorator implements RequestMapper {
    @Autowired
    private RequestMapper requestMapper;

    @Override
    public Request toRequest(RequestDTO requestDTO, Event event, User requester) {
        Request request = requestMapper.toRequest(requestDTO, event, requester);
        request.setEvent(event);
        request.setRequester(requester);
        return request;
    }
}