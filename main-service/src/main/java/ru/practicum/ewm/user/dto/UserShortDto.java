package ru.practicum.ewm.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserShortDto {
    private int id;
    private String name;
}