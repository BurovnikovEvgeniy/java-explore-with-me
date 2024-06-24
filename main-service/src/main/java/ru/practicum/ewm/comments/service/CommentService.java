package ru.practicum.ewm.comments.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.comments.dto.FullCommentDto;
import ru.practicum.ewm.comments.dto.NewCommentDto;
import ru.practicum.ewm.comments.dto.ShortCommentDto;

import java.util.List;

public interface CommentService {
    FullCommentDto addComment(NewCommentDto newCommentDTO, Long userId, Long eventId);

    FullCommentDto updateComment(NewCommentDto newCommentDTO, Long userId, Long commId);

    void deleteComment(Long userId, Long commId);

    List<FullCommentDto> getCommentsByAuthorId(Long userId, PageRequest pageRequest);

    void deleteCommentByAdmin(Long commId);

    FullCommentDto getComment(Long commId);

    List<ShortCommentDto> getCommentsByEventId(Long eventId, PageRequest pageRequest);
}