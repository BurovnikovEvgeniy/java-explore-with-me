package ru.practicum.ewm.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String msg) {
        super(msg);
    }
}
