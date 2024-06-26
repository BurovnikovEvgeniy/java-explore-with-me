package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.ewm.utils.EventState;

import java.time.LocalDateTime;

import static ru.practicum.ewm.utils.Constants.DATE_FORMAT;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FullEventDto extends ShortEventDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDateTime createdOn;
    private Integer participantLimit;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private EventState state;
}