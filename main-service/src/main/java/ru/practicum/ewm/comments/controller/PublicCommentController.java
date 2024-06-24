package ru.practicum.ewm.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.comments.dto.FullCommentDto;
import ru.practicum.ewm.comments.dto.ShortCommentDto;
import ru.practicum.ewm.comments.service.CommentService;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class PublicCommentController {
    private final CommentService commentService;

    @GetMapping("/comments/{commId}")
    public FullCommentDto getComment(@PathVariable @Positive Long commId) {
        return commentService.getComment(commId);
    }

    @GetMapping("/events/{eventId}/comments")
    public List<ShortCommentDto> getCommentsByEventId(@PathVariable @Positive Long eventId,
                                                      @RequestParam(defaultValue = "0") int from,
                                                      @RequestParam(defaultValue = "10") int size) {
        return commentService.getCommentsByEventId(eventId, PageRequest.of(from / size, size));
    }
}