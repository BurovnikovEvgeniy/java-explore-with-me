package ru.practicum.ewm.request.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.user.model.User;

public abstract class RequestMapperDecorator implements RequestMapper {

    @Autowired
    private RequestMapper requestMapper;

    @Override
    public Request toRequest(RequestDto requestDto, Event event, User requester) {
        Request request = requestMapper.toRequest(requestDto, event, requester);
        request.setEvent(event);
        request.setRequester(requester);
        return request;
    }
}