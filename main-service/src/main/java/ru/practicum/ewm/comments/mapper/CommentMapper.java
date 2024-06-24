package ru.practicum.ewm.comments.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.ewm.comments.dto.FullCommentDto;
import ru.practicum.ewm.comments.dto.ShortCommentDto;
import ru.practicum.ewm.comments.model.Comment;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {
    FullCommentDto toFullCommentDto(Comment comment);

    List<FullCommentDto> toFullCommentDto(List<Comment> comment);

    ShortCommentDto toShortCommentDto(Comment comment);

    List<ShortCommentDto> toShortCommentDto(List<Comment> comment);
}