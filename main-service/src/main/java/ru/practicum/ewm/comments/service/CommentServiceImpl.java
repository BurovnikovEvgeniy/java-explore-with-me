package ru.practicum.ewm.comments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comments.dto.FullCommentDto;
import ru.practicum.ewm.comments.dto.NewCommentDto;
import ru.practicum.ewm.comments.dto.ShortCommentDto;
import ru.practicum.ewm.comments.mapper.CommentMapper;
import ru.practicum.ewm.comments.model.Comment;
import ru.practicum.ewm.comments.repository.CommentRepository;
import ru.practicum.ewm.error.ConflictException;
import ru.practicum.ewm.error.NotFoundException;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.utils.EventState.PUBLISHED;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public FullCommentDto addComment(NewCommentDto newCommentDTO, Long userId, Long eventId) {
        Event event = getEventIfExist(eventId);
        if (!PUBLISHED.equals(event.getState())){
            throw new ConflictException("Нельзя добавить комментарий к неопубликованному событию");
        }
        User user = getUserIfExist(userId);
        Comment comment = Comment.builder()
                .text(newCommentDTO.getText())
                .author(user)
                .event(event)
                .createdOn(LocalDateTime.now())
                .updatedOn(null)
                .build();
        return commentMapper.toFullCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public FullCommentDto updateComment(NewCommentDto newCommentDTO, Long userId, Long commId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
        Comment comment = getCommentIfExist(commId);
        if (!userId.equals(comment.getAuthor().getId())) {
            throw new ConflictException("Только автор может редактировать комментарий");
        }
        comment.setText(newCommentDTO.getText());
        comment.setUpdatedOn(LocalDateTime.now());
        return commentMapper.toFullCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteComment(Long userId, Long commId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
        Comment comment = getCommentIfExist(commId);
        if (!userId.equals(comment.getAuthor().getId())) {
            throw new ConflictException("Only author can delete the comment");
        }
        commentRepository.deleteById(commId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FullCommentDto> getCommentsByAuthorId(Long userId, PageRequest pageRequest) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
        List<Comment> comments = commentRepository.findAllByAuthorId(userId, pageRequest);
        return commentMapper.toFullCommentDto(comments);
    }

    @Override
    @Transactional
    public void deleteCommentByAdmin(Long commId) {
        if (!commentRepository.existsById(commId)) {
            throw new NotFoundException("Комментарий с id=" + commId + " не найден");
        }
        commentRepository.deleteById(commId);
    }

    @Override
    @Transactional(readOnly = true)
    public FullCommentDto getComment(Long commId) {
        Comment comment = getCommentIfExist(commId);
        return commentMapper.toFullCommentDto(comment);
    }

    @Override
    @Transactional
    public List<ShortCommentDto> getCommentsByEventId(Long eventId, PageRequest pageRequest) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Событие с id=" + eventId + " не найдено");
        }
        List<Comment> comments = commentRepository.findAllByEventId(eventId, pageRequest);
        return commentMapper.toShortCommentDto(comments);
    }

    private User getUserIfExist(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id=" + userId + " не найден"));
    }

    private Event getEventIfExist(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с id=" + eventId + " не найдено"));
    }

    private Comment getCommentIfExist(Long commId) {
        return commentRepository.findById(commId).orElseThrow(() ->
                new NotFoundException("Комментарий с id=" + commId + " не найден"));

    }
}
