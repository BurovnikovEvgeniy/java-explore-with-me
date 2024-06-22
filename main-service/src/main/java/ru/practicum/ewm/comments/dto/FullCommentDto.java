package ru.practicum.ewm.comments.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.event.dto.ShortEventDto;
import ru.practicum.ewm.user.dto.UserShortDto;

import static ru.practicum.ewm.utils.Constants.DATE_FORMAT;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class FullCommentDto {
    private long id;
    private String text;
    private UserShortDto author;
    private ShortEventDto event;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private String createdOn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private String updatedOn;
}