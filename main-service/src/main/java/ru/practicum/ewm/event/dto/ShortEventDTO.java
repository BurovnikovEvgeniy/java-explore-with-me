package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.ewm.category.dto.CategoryDTO;
import ru.practicum.ewm.location.dto.LocationDTO;
import ru.practicum.ewm.user.dto.UserShortDTO;

import java.time.LocalDateTime;

import static ru.practicum.ewm.utils.Constants.DATE_FORMAT;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ShortEventDTO {
    private String annotation;
    private CategoryDTO category;
    private Long confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDateTime eventDate;
    private Long id;
    private UserShortDTO initiator;
    private LocationDTO location;
    private String title;
    private Long views;
    private Boolean paid;
}