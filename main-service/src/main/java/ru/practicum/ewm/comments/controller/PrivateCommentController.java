package ru.practicum.ewm.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.comments.dto.FullCommentDto;
import ru.practicum.ewm.comments.dto.NewCommentDto;
import ru.practicum.ewm.comments.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
public class PrivateCommentController {
    private final CommentService commentService;

    @PostMapping("/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public FullCommentDto addComment(@RequestBody @Valid NewCommentDto newCommentDTO,
                                     @PathVariable @Positive Long userId,
                                     @PathVariable @Positive Long eventId) {
        return commentService.addComment(newCommentDTO, userId, eventId);
    }

    @PatchMapping("/{commId}")
    public FullCommentDto updateComment(@RequestBody @Valid NewCommentDto newCommentDTO,
                                        @PathVariable @Positive Long userId,
                                        @PathVariable @Positive Long commId) {
        return commentService.updateComment(newCommentDTO, userId, commId);
    }

    @DeleteMapping("/{commId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable @Positive Long userId,
                              @PathVariable @Positive Long commId) {
        commentService.deleteComment(userId, commId);
    }
}
