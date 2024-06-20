package ru.practicum.ewm.utils;

import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.error.ConflictException;

import java.time.LocalDateTime;

public class Utilities {
    public static PageRequest fromSizePage(int from, int size) {
        return PageRequest.of(from / size, size);
    }

    public static void checkEventStart(LocalDateTime start) {
        if (start != null && start.isBefore(LocalDateTime.now().plusHours(2)))
            throw new ConflictException("The event can only start in two hours from now");
    }
}